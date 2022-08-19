package frc.robot;

import edu.wpi.first.wpilibj.Timer;
import java.lang.Math;

public class Navigator {
    //Use 1 ft = 0.0000027495 decimal degrees; 495 is the repetend but it shouldn't matter that much because we are getting down to 
    private static final double FOOT_TO_DD = 2.7495495495495496E-6;

    private static Timer timer;

    private static final double TIRE_MULTIPLIER = Math.PI * 13;
    private static final double RADIANS_MULTIPLIER = Math.PI/180;

    private static NavigatorStruct location;
    private static double previousTimePollEncoder = 0; // In seconds
    private static double previousVelocity = 0;

    protected static void init() {
        LatLongFixStruct gps = null;
        while (gps == null) {
            gps = GPSManager.ParseGPSData((byte) 1);
        }

        location.latitude = gps.latitude * 10E-7;
        location.longitude = gps.longitude * 10E-7;
        location.direction = 0d; // Need calibrate function
                                 // Direction is how many degrees from North (positive is west of, negative is east of)

        timer = new Timer();
        timer.reset();
        previousTimePollEncoder = timer.get();
    }

    private static NavigatorStruct LatLongToNav(LatLongFixStruct latLongFixStruct) {
        NavigatorStruct navigatorStruct = new NavigatorStruct(latLongFixStruct.latitude, latLongFixStruct.longitude, location.direction);
        return navigatorStruct;
    }
    
    protected static NavigatorStruct getLocation() {
        NavigatorStruct navigatorStruct = location;

        LatLongFixStruct gps = GPSManager.ParseGPSData((byte) 1);
        if (gps == null) { // No GPS Reading
                EncoderStruct encoderStruct = MotorEncoder.getVelocity();
                if (Math.abs(encoderStruct.lVelocity - encoderStruct.rVelocity) < 0.015) { // Moving forward
                    double timeBetweenPolls = Math.abs(encoderStruct.time - previousTimePollEncoder);
                    double avgVelocity = (previousVelocity + ((encoderStruct.lVelocity + encoderStruct.rVelocity)/2))/2;
                    double magnitude = avgVelocity * timeBetweenPolls;
                    magnitude *= TIRE_MULTIPLIER;
                    double latChange = Math.cos(NavXManager.getData().yaw * RADIANS_MULTIPLIER) * magnitude;
                    double lonChange = Math.sin(NavXManager.getData().yaw * RADIANS_MULTIPLIER) * magnitude;

                    latChange *= FOOT_TO_DD;
                    lonChange *= FOOT_TO_DD;

                    navigatorStruct.latitude += latChange;
                    navigatorStruct.longitude += lonChange;
                    navigatorStruct.direction = (double) NavXManager.getData().yaw;

                    return navigatorStruct;
                    
                } else { // Stay in same place, most likely turning
                    navigatorStruct.direction = (double) NavXManager.getData().yaw * -1;
                    return navigatorStruct;
                }

        } else {
                navigatorStruct = LatLongToNav(gps);
                navigatorStruct.direction = (double) NavXManager.getData().yaw * -1;
                return navigatorStruct;
        }
    }
}
