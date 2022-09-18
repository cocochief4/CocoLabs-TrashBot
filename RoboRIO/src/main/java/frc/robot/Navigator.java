package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;

public class Navigator {
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 
    private static final double FOOT_TO_DD = 2.7495495495495496E-6;

    private static Timer timer;

    private static final double TIRE_MULTIPLIER = Math.PI * 13;
    protected static final double RADIANS_MULTIPLIER = Math.PI/180;
    protected static final double DEGREES_MULTIPLIER = 180/Math.PI;

    private static NavigatorStruct location;
    private static double previousTimePollEncoder = 0; // In seconds
    private static double previousVelocity = 0;

    protected static void init() {
        LatLongFixStruct gps = null;
        int i = 0;
        while (gps == null && i < 10000) {
            gps = ArduinoManager.getGPS();
            i++;
            // System.out.println(i);
        }

        location = new NavigatorStruct(gps.latitude * 10E-7, gps.longitude * 10E-7, 0d, 0d); // Need calibrate function
                                 // Direction is how many degrees from North (positive is west of, negative is east of)

        timer = new Timer();
        timer.reset();
        previousTimePollEncoder = timer.get();
        System.out.println("Navigator Init Finished!");
    }

    private static NavigatorStruct LatLongToNav(LatLongFixStruct latLongFixStruct) {
        NavigatorStruct navigatorStruct = new NavigatorStruct(latLongFixStruct.latitude, 
                                                            latLongFixStruct.longitude, location.direction, (double) 0);
        return navigatorStruct;
    }
    
    protected static NavigatorStruct getLocation() {
        NavigatorStruct navigatorStruct = location;
        System.out.println(location.toString());

        LatLongFixStruct gps = ArduinoManager.getGPS();
        if (gps == null) { // No GPS Reading
                EncoderStruct encoderStruct = MotorEncoder.getVelocity();
                if (Math.abs(encoderStruct.lVelocity - encoderStruct.rVelocity) < 0.015) { // Moving forward
                    double timeBetweenPolls = Math.abs(encoderStruct.time - previousTimePollEncoder);
                    double avgVelocity = (previousVelocity + ((encoderStruct.lVelocity + encoderStruct.rVelocity)/2))/2;
                    double magnitude = avgVelocity * timeBetweenPolls;
                    magnitude *= TIRE_MULTIPLIER; // Change back to Decimal degrees?
                    double latChange = Math.cos(NavXManager.getData().yaw * RADIANS_MULTIPLIER) * magnitude;
                    double lonChange = Math.sin(NavXManager.getData().yaw * RADIANS_MULTIPLIER) * magnitude;

                    latChange *= FOOT_TO_DD;
                    lonChange *= FOOT_TO_DD;
                    // Figure out which unit we are in

                    navigatorStruct.latitude += latChange;
                    navigatorStruct.longitude += lonChange;
                    navigatorStruct.direction = (double) NavXManager.getData().yaw;
                    navigatorStruct.distance = magnitude;

                    return navigatorStruct;
                    
                } else { // Stay in same place, most likely turning
                    navigatorStruct.direction = (double) NavXManager.getData().yaw * -1;
                    return navigatorStruct;
                }

        } else {
            EncoderStruct encoderStruct = MotorEncoder.getVelocity();
            double timeBetweenPolls = Math.abs(encoderStruct.time - previousTimePollEncoder);
            double avgVelocity = (previousVelocity + ((encoderStruct.lVelocity + encoderStruct.rVelocity)/2))/2;
            double magnitude = avgVelocity * timeBetweenPolls; // In feet
            magnitude *= TIRE_MULTIPLIER;

            navigatorStruct = LatLongToNav(gps);
            navigatorStruct.direction = (double) NavXManager.getData().yaw * -1;
            navigatorStruct.direction = magnitude;
            return navigatorStruct;
        }
    }
}
