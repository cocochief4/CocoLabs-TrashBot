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
    protected EuclideanCoord ScaleSquare(double x, double y) {
        double Xscale = x/500 - 3.0;
        double Yscale = y/500 - 3.0;

        EuclideanCoord euclideanCoord = new EuclideanCoord(Xscale, Yscale);

        return euclideanCoord;
    }

    // Scale from a square to a unit circle
    protected EuclideanCoord ScaleDown (double x, double y) {
        double slope = 0;
        double xUnitSquare = 1;
        double yUnitSquare = 1;
        double hyp;
        double scaleFactor;
        if (x == 0) {
            slope = 0;
            scaleFactor = 1;
        } else {
            slope = y/x;
            if (Math.abs(y/x) <= 1) {
                yUnitSquare = 1 * slope;
                xUnitSquare = 1;
            } else if (Math.abs(y/x) >= 1) {
                yUnitSquare = 1;
                xUnitSquare = 1/slope;
            }
            hyp = Math.sqrt(Math.pow(xUnitSquare, 2) + Math.pow(yUnitSquare, 2));
            scaleFactor = 1/hyp;
        }

        xUnitSquare = x * scaleFactor;
        yUnitSquare = y * scaleFactor;

        EuclideanCoord euclideanCoord = new EuclideanCoord(xUnitSquare, yUnitSquare);
        return euclideanCoord;
    }

    // Convert to polar coordinates (r and theta); theta is in deg
    protected PolarCoord CartToPolar(double x, double y) {
        double r = Math.sqrt(Math.pow(x, 2.0) + Math.pow(y, 2.0));
        double theta = Math.atan((y/x));
        theta = theta * (180/Math.PI);
        
        
        if (x == 0 && y == 0) {
            r = 0;
            theta = 0;
        }
        if (y == 0 && x < 0) {
            theta = 180;
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

    // Rotate by -45 deg
    protected double driveConversion(double theta) {
        double thetaLocal = theta;
        if (thetaLocal < 0) {
            thetaLocal += 360;
        }
        thetaLocal -= 45;
        if (thetaLocal > 180) {
            thetaLocal -= 360;
        }

        return thetaLocal;
    }

    // Scale Up The code
    protected EuclideanCoord ScaleUp(double x, double y) {
        double slope = 0;
        double xUnitSquare = 1;
        double yUnitSquare = 1;
        double hyp;
        double scaleFactor;
        if (x == 0) {
            slope = 0;
            scaleFactor = 1;
        } else {
            slope = y/x;
            if (Math.abs(y/x) <= 1) {
                yUnitSquare = 1 * slope;
                xUnitSquare = 1;
            } else if (Math.abs(y/x) >= 1) {
                yUnitSquare = 1;
                xUnitSquare = 1/slope;
            }
            hyp = Math.sqrt(Math.pow(xUnitSquare, 2) + Math.pow(yUnitSquare, 2));
            scaleFactor = hyp/1;
        }

        xUnitSquare = x * scaleFactor;
        yUnitSquare = y * scaleFactor;

        EuclideanCoord euclideanCoord = new EuclideanCoord(xUnitSquare, yUnitSquare);
        return euclideanCoord;
    }
}

