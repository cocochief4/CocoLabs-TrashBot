package frc.robot;

import java.lang.Math;

public class TeleopMath {

    // When mapped onto a coordinate grid, X is steering and Y is throttle
    // pos X is turn right, neg X is turn left
    // pos Y is forward, neg Y is backward

    //Create a constructor
    protected TeleopMath(Double steering, double throttle) {
        double Xcontroller = steering;
        double Ycontroller = throttle;
    }

    // Create a method to scale down the value
    // From 1000 - 2000 to -1 - 1
    protected EuclideanCoord scaleSquare(double x, double y) {
        double Xscale = x/500 - 3.0;
        double Yscale = y/500 - 3.0;

        EuclideanCoord euclideanCoord = new EuclideanCoord(Xscale, Yscale);

        return euclideanCoord;
    }

    // Convert to polar coordinates (r and theta); theta is in radians

    protected PolarCoord CartToPolar(double x, double y) {
        double r = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
        double theta = Math.atan((y/x));
        theta = theta * (180/Math.PI);
        
        
        if (x == 0 && y == 0) {
            r = 0;
            theta = 0;
        }
        if (x < 0) {
            if (y > 0) {
                theta = theta + 180;
            } else if (y < 0) {
                theta = theta - 180;
            }
        }
        

        PolarCoord polarCoord = new PolarCoord(r, theta);
        
        return polarCoord;
    }
}

