package frc.robot;

import java.lang.Math;

public class Navigator {
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 
    private static final double FOOT_TO_DD = 2.7495495495495496E-6;

    protected static final double RADIANS_MULTIPLIER = Math.PI/180f;
    protected static final double DEGREES_MULTIPLIER = 180f/Math.PI;

    private static NavigatorData location;
    private static long previousTimePollEncoder = 0l; // In millis
    private static double previousVelocity = 0;

    protected static void init() {
        GPSLatLongData gps = null;
        gps = ArduinoManager.getGPS();

        location = new NavigatorData(gps.latitude * 10E-7, gps.longitude * 10E-7, 0d, 0d); // Need calibrate function
                                 // Direction is how many degrees from North (positive is west of, negative is east of)

        previousTimePollEncoder = System.currentTimeMillis();
        System.out.println("Navigator Init Finished!");
    }

    private static NavigatorData LatLongToNav(GPSLatLongData latLongFixStruct) {
        NavigatorData navigatorStruct = new NavigatorData(latLongFixStruct.latitude, 
                                                            latLongFixStruct.longitude, location.direction, (double) 0);
        return navigatorStruct;
    }
    
    protected static NavigatorData getLocation() {
        NavigatorData navigatorStruct = location;
        System.out.println(location.toString());

        GPSLatLongData gps = ArduinoManager.getGPS();
        if (gps == null) { // No GPS Reading
                EncoderStruct encoderStruct = MotorEncoder.getVelocity();
                if (Math.abs(encoderStruct.lVelocity - encoderStruct.rVelocity) < 0.015) { // Moving forward
                    double timeBetweenPolls = Math.abs(encoderStruct.time - previousTimePollEncoder);
                    double avgVelocity = (previousVelocity + ((encoderStruct.lVelocity + encoderStruct.rVelocity)/2))/2;
                    double magnitude = avgVelocity * timeBetweenPolls;
                    double latChange = Math.cos(NavXManager.getData().yawFromNorth * RADIANS_MULTIPLIER) * magnitude;
                    double lonChange = Math.sin(NavXManager.getData().yawFromNorth * RADIANS_MULTIPLIER) * magnitude;

                    latChange *= FOOT_TO_DD;
                    lonChange *= FOOT_TO_DD;
                    // Figure out which unit we are in

                    navigatorStruct.latitude += latChange;
                    navigatorStruct.longitude += lonChange;
                    navigatorStruct.direction = (double) NavXManager.getData().yawFromNorth;
                    navigatorStruct.distance = magnitude;

                    return navigatorStruct;
                    
                } else { // Stay in same place, most likely turning
                    navigatorStruct.direction = (double) NavXManager.getData().yawFromNorth * -1;
                    return navigatorStruct;
                }

        } else {
            EncoderStruct encoderStruct = MotorEncoder.getVelocity();
            double timeBetweenPolls = Math.abs(encoderStruct.time - previousTimePollEncoder);
            double avgVelocity = (previousVelocity + ((encoderStruct.lVelocity + encoderStruct.rVelocity)/2))/2;
            double magnitude = avgVelocity * timeBetweenPolls; // In feet

            navigatorStruct = LatLongToNav(gps);
            navigatorStruct.direction = (double) NavXManager.getData().yawFromNorth * -1;
            navigatorStruct.direction = magnitude;
            return navigatorStruct;
        }
    }
}
