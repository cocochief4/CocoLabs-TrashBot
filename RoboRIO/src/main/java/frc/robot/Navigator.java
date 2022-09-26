package frc.robot;

import java.lang.Math;

public class Navigator {
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 
    private static final double FOOT_TO_DD = 2.7495495495495496E-6;

    protected static final double RADIANS_MULTIPLIER = Math.PI/180f;
    protected static final double DEGREES_MULTIPLIER = 180f/Math.PI;

    protected static final double DD_LONG_TO_DOUBLE = 10E-7;

    private static NavigatorData location;

    protected static boolean haveTurned = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;
    protected static GPSLatLongData calibStartPos = new GPSLatLongData(0, 0, (short) 0, System.currentTimeMillis());
    protected static GPSLatLongData calibEndPos = new GPSLatLongData(0, 0, (short) 0, System.currentTimeMillis());

    protected static void init() {
        GPSLatLongData gps = null;
        gps = ArduinoManager.getGPS();

        location = new NavigatorData(gps.latitude * DD_LONG_TO_DOUBLE, gps.longitude * DD_LONG_TO_DOUBLE, 0d, 0d, System.currentTimeMillis()); // Need calibrate function
                                 // Direction is how many degrees from North (positive is west of, negative is east of)
        System.out.println("Navigator Init Finished!");
    }

    // private static NavigatorData LatLongToNav(GPSLatLongData latLongFixStruct) {
    //     NavigatorData navigatorStruct = new NavigatorData(latLongFixStruct.latitude, 
    //                                                         latLongFixStruct.longitude, location.yawFromNorth, (double) 0, System.currentTimeMillis());
    //     return navigatorStruct;
    // }
    
    protected static void calibrate() { // WORK ON CALIBRATE
        if (haveTurned == false) {
            GPSLatLongData gpsLatLongData = ArduinoManager.getGPS();
            System.out.println("calibStart: " + gpsLatLongData + "real: " + calibStartPos);
            if (System.currentTimeMillis() - gpsLatLongData.timeStamp < 50) {
                haveStartedCalib = true;
                calibStartPos = gpsLatLongData;
            }
        } else {
            haveStartedCalib = false;
        }

        if (haveStartedCalib == false) {
            GPSLatLongData gpsLatLongData = ArduinoManager.getGPS();
            if (gpsLatLongData.timeStamp != calibStartPos.timeStamp) {
                calibEndPos = gpsLatLongData;
                System.out.println("end pos gps: " + gpsLatLongData.toString() + ", real: " + calibEndPos);
                double deltaLat = calibStartPos.latitude - calibEndPos.latitude;
                double deltaLong = calibStartPos.longitude - calibEndPos.longitude;
                double tan = Math.toDegrees(Math.atan(deltaLong/deltaLat));
                double yawTan = NavXManager.getData().yawFromNorth;
                NavXManager.yawDeltaFromNorth = yawTan-tan;
            }
        }

    }


    protected static NavigatorData getLocation() {
        System.out.println(location.toString());

        double localYawFromNorth = NavXManager.getData().yawFromNorth;

        GPSLatLongData gps = ArduinoManager.getGPS();
        EncoderStruct encoderStruct = MotorEncoder.getVelocity();
        double gpsDistance = 0;
        if (gps.timeStamp > location.timeStamp) { // GPS Reading
            location.timeStamp = gps.timeStamp;
            gpsDistance = Math.sqrt(Math.pow(Math.abs(location.latitude - gps.latitude), 2) + 
                                                        Math.pow(Math.abs(location.longitude - gps.longitude), 2));
            location.latitude = gps.latitude * DD_LONG_TO_DOUBLE;
            location.longitude = gps.longitude * DD_LONG_TO_DOUBLE;
        }
        if (Math.abs(encoderStruct.lVelocity - encoderStruct.rVelocity) < 0.015) { // Moving forward
            double timeBetweenPolls = Math.abs(encoderStruct.time - location.timeStamp);
            double avgVelocity = (encoderStruct.lVelocity + encoderStruct.rVelocity)/2;
            double distanceTraveled = avgVelocity * timeBetweenPolls;
            double latChange = Math.cos(localYawFromNorth * RADIANS_MULTIPLIER) * distanceTraveled * FOOT_TO_DD;
            double lonChange = Math.sin(localYawFromNorth * RADIANS_MULTIPLIER) * distanceTraveled * FOOT_TO_DD;

            location.latitude += latChange;
            location.longitude += lonChange;
            location.distanceFromLastReading = distanceTraveled + gpsDistance;
        
        } else {
            location.distanceFromLastReading = 0d;
        }
        location.timeStamp = System.currentTimeMillis();
        location.yawFromNorth = (double) localYawFromNorth;
        return location;
    }
}
