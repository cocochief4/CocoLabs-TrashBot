package frc.robot;

import java.lang.Math;

import javax.sound.sampled.AudioFileFormat;

import edu.wpi.first.wpilibj.DataLogManager;

public class Navigator {
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 
    private static final double FOOT_TO_DD = 2.7495495495495496E-6;

    protected static final double RADIANS_MULTIPLIER = Math.PI/180f;
    protected static final double DEGREES_MULTIPLIER = 180f/Math.PI;

    protected static final double DD_LONG_TO_DOUBLE = 1E-7;

    private static NavigatorData location;

    protected static boolean haveTurned = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;
    protected static GPSLatLongData calibStartPos = null;
    protected static GPSLatLongData calibEndPos = null;
    
    private static boolean GPSticked = false;
    private static long GPSlocalTimestamp = 0;
    

    protected static void init() {
        GPSticked = false;
        GPSLatLongData gps = null;
        gps = ArduinoManager.getGPS();
        calibStartPos = null;

        location = new NavigatorData(gps.latitude * DD_LONG_TO_DOUBLE, gps.longitude * DD_LONG_TO_DOUBLE, 0d, 0d, System.currentTimeMillis(), true); // Need calibrate function
                                 // Direction is how many degrees from North (positive is west of, negative is east of)
        // DataLogManager.log("Navigator Init Finished!");
    }

    // private static NavigatorData LatLongToNav(GPSLatLongData latLongFixStruct) {
    //     NavigatorData navigatorStruct = new NavigatorData(latLongFixStruct.latitude, 
    //                                                         latLongFixStruct.longitude, location.yawFromNorth, (double) 0, System.currentTimeMillis());
    //     return navigatorStruct;
    // }

    protected static void calibrateYaw() { // WORK ON CALIBRATE
        EncoderStruct encoderStruct = MotorEncoder.getVelocity();
        // DataLogManager.log("  rVelo: " + encoderStruct.rVelocity + "\n  lvelo: " + encoderStruct.lVelocity);
        if (Math.signum(encoderStruct.rVelocity) == Math.signum(encoderStruct.lVelocity)) {
            haveTurned = false;
        }
        else {
            // DataLogManager.log("calib turned");
            haveTurned = true;
            calibStartPos = null;
        }
        if (haveTurned == false) {
            // DataLogManager.log("calibrating");
            if (calibStartPos == null) {
                if (System.currentTimeMillis() - ArduinoManager.getGPS().timeStamp < 200) {
                    calibStartPos = ArduinoManager.getGPS();
                    // DataLogManager.log("startPos: " + calibStartPos);
                    DataLogManager.log("startPos," + calibStartPos);
                }
            }
            else {
                if (ArduinoManager.getGPS().timeStamp != calibStartPos.timeStamp) {
                    calibEndPos = ArduinoManager.getGPS();
                    long deltaLatitude = calibEndPos.latitude - calibStartPos.latitude;
                    long deltaLongitude = calibEndPos.longitude - calibStartPos.longitude;
                    double magnitude = Math.sqrt((double) (deltaLatitude*deltaLatitude + deltaLongitude*deltaLongitude));
                    if (GPSlocalTimestamp != ArduinoManager.getGPS().timeStamp) {
                        DataLogManager.log("magnitude," + magnitude);
                        GPSlocalTimestamp = ArduinoManager.getGPS().timeStamp;
                    }
                // if the distance traveled is greater that around 30 in for guaranteed accuracy. IMPLEMENTED
                    if (magnitude > 35d) {
                        double yawFromNorth = Math.toDegrees(Math.atan2(deltaLongitude, deltaLatitude));
                        // DataLogManager.log("yawfromNOrth latest: " + yawFromNorth
                        //                     + "\n Nav x get data: " + NavXManager.getData().toString());
                        double rawYaw = NavXManager.getData().rawYaw;
                        NavXManager.yawDeltaFromNorth = yawFromNorth - rawYaw;
                        // DataLogManager.log("yawfromNOrth latest: " + yawFromNorth
                        //                     + "\n Nav x get data: " + NavXManager.getData().toString());
                        // DataLogManager.log("calibStartPos: " + calibStartPos + 
                        //                     "\n calibEndPos: " + calibEndPos + 
                        //                     "\n yawFromNorth: " + yawFromNorth + 
                        //                     "\n navX Yaw: " + NavXManager.getData().yawFromNorth + 
                        //                     "\n deltaLat: " + deltaLatitude + 
                        //                     "\n deltaLon: " + deltaLongitude +
                        //                     "\n magnitude: " + magnitude);
                        DataLogManager.log("calibStartPos," + calibStartPos + 
                                            ",calibEndPos," + calibEndPos + 
                                            ",yawFromNorth," + yawFromNorth + 
                                            ",rawYaw," + NavXManager.getData().rawYaw + 
                                            ",YawDeltaFromNOrth," + NavXManager.yawDeltaFromNorth +
                                            ",deltaLat," + deltaLatitude + 
                                            ",deltaLon," + deltaLongitude +
                                            ",magnitude," + magnitude);
                        calibStartPos = null;
                        calibEndPos = null;
                    }
                }
            }
        }
    } 


    protected static NavigatorData getLocation() {
        // DataLogManager.log(location.toString());

        double localYawFromNorth = NavXManager.getData().yawFromNorth;

        GPSLatLongData gps = ArduinoManager.getGPS();
        EncoderStruct encoderStruct = MotorEncoder.getVelocity();
        double gpsDistance = 0;
        if (gps.timeStamp > location.timeStamp) { // GPS Reading
            location.timeStamp = gps.timeStamp;
            // gpsDistance = Math.sqrt(Math.pow(Math.abs(location.latitude - gps.latitude), 2) + 
                                                        // Math.pow(Math.abs(location.longitude - gps.longitude), 2));
            location.latitude = gps.latitude * DD_LONG_TO_DOUBLE;
            location.longitude = gps.longitude * DD_LONG_TO_DOUBLE;

            location.isGpsReading = true;
        } else {
            location.isGpsReading = false;
        }
        
        double timeBetweenPolls = Math.abs(encoderStruct.time - location.timeStamp);
        double avgVelocity = (encoderStruct.lVelocity + encoderStruct.rVelocity)/2;
        double distanceTraveled = avgVelocity * timeBetweenPolls;
        double latChange = Math.cos(localYawFromNorth * RADIANS_MULTIPLIER) * distanceTraveled * FOOT_TO_DD;
        double lonChange = Math.sin(localYawFromNorth * RADIANS_MULTIPLIER) * distanceTraveled * FOOT_TO_DD;

        location.latitude += latChange;
        location.longitude += lonChange;
        location.distanceFromLastReading = distanceTraveled + gpsDistance;

        location.timeStamp = System.currentTimeMillis();
        location.yawFromNorth = (double) localYawFromNorth;
        DataLogManager.log(location.toLogString());
        return location;
    }
}
