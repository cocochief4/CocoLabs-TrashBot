package frc.robot;

import java.lang.Math;

public class TeleopMath {
    private double Xcontroller;
    private double Ycontroller;

    // When mapped onto a coordinate grid, X is steering and Y is throttle
    // pos X is turn right, neg X is turn left
    // pos Y is forward, neg Y is backward

    //Create a constructor
    protected TeleopMath(Double steering, double throttle) {
        Xcontroller = steering;
        Ycontroller = throttle;
    }

    // Create a method to scale down the value
    // From 1000 - 2000 to -1 - 1
    private EuclideanCoord ScaleToUnitSquare (EuclideanCoord coord) {
        double Xscale = (coord.xEuclid/500.0 - 3.0);
        double Yscale = (coord.yEuclid/500.0 - 3.0);

        EuclideanCoord euclideanCoord = new EuclideanCoord(Xscale, Yscale);

        return euclideanCoord;
    }

    // Scale from a square to a unit circle
    private EuclideanCoord ScaleSquareToUnitCircle (EuclideanCoord coord) {
        double slope = 0;
        double xUnitSquare = 1;
        double yUnitSquare = 1;
        double hyp;
        double scaleFactor;
        double xOutput;
        double yOutput;
        if (coord.xEuclid == 0) {
            slope = 0;
            scaleFactor = 1;
        } else {
            slope = coord.yEuclid/coord.xEuclid;
            if (Math.abs(coord.yEuclid/coord.xEuclid) <= 1) {
                yUnitSquare = 1 * slope;
                xUnitSquare = 1;
            } else if (Math.abs(coord.yEuclid/coord.xEuclid) >= 1) {
                yUnitSquare = 1;
                xUnitSquare = 1/slope;
            }
            hyp = Math.sqrt(Math.pow(xUnitSquare, 2) + Math.pow(yUnitSquare, 2));
            scaleFactor = 1/hyp;
        }
        xOutput = coord.xEuclid * scaleFactor;
        yOutput = coord.yEuclid * scaleFactor;

        EuclideanCoord euclideanCoord = new EuclideanCoord(xOutput, yOutput);
        return euclideanCoord;
    }

    // Convert to polar coordinates (r and theta); theta is in deg
    private PolarCoord CartToPolar(EuclideanCoord coord) {
        double r = Math.sqrt(Math.pow(coord.xEuclid, 2.0) + Math.pow(coord.yEuclid, 2.0));
        double theta = Math.atan((coord.yEuclid/coord.xEuclid));
        theta = theta * (180/Math.PI);
        
        
        if (coord.xEuclid == 0 && coord.yEuclid == 0) {
            r = 0;
            theta = 0;
        }
        if (coord.yEuclid == 0 && coord.xEuclid < 0) {
            theta = 180;
        }
        if (coord.xEuclid < 0) {
            if (coord.yEuclid > 0) {
                theta = theta + 180;
            } else if (coord.yEuclid < 0) {
                theta = theta - 180;
            }
        }

        PolarCoord polarCoord = new PolarCoord(r, theta);
        return polarCoord;
    }

    // Convert to euclidean coordinates
    private EuclideanCoord PolarToCart(PolarCoord coord) {
        double x = coord.r * Math.cos(coord.theta * Math.PI/180);
        double y = coord.r * Math.sin(coord.theta * Math.PI/180);
        if (coord.r == 0) {
            x = 0;
            y = 0;
        }

        EuclideanCoord euclideanCoord = new EuclideanCoord(x, y);
        return euclideanCoord;
    }

    // Rotate by -45 deg
    private double driveConversion(double theta) {
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
    private EuclideanCoord ScaleUp(EuclideanCoord coord) {
        double slope = 0;
        double xUnitSquare = 1;
        double yUnitSquare = 1;
        double hyp;
        double scaleFactor;
        if (coord.xEuclid == 0) {
            slope = 0;
            scaleFactor = 1;
        } else {
            slope = coord.yEuclid/coord.xEuclid;
            if (Math.abs(coord.yEuclid/coord.xEuclid) <= 1) {
                yUnitSquare = 1 * slope;
                xUnitSquare = 1;
            } else if (Math.abs(coord.yEuclid/coord.xEuclid) >= 1) {
                yUnitSquare = 1;
                xUnitSquare = 1/slope;
            }
            hyp = Math.sqrt(Math.pow(xUnitSquare, 2) + Math.pow(yUnitSquare, 2));
            scaleFactor = hyp/1;
        }

        xUnitSquare = coord.xEuclid * scaleFactor;
        yUnitSquare = coord.yEuclid * scaleFactor;

        EuclideanCoord euclideanCoord = new EuclideanCoord(xUnitSquare, yUnitSquare);
        return euclideanCoord;
    }

    protected EuclideanCoord CalcRamp(EuclideanCoord currentSpeed, EuclideanCoord inputSpeed, double rampMax) {
        EuclideanCoord outputSpeed = new EuclideanCoord(0, 0);
        // Do ramp rate
        // The teleopPeriodic() updates every 20 ms (50 times per second)
        if (Math.abs(currentSpeed.xEuclid - inputSpeed.xEuclid) > rampMax) {
            if (inputSpeed.xEuclid > currentSpeed.xEuclid) {
            outputSpeed.xEuclid = currentSpeed.xEuclid + rampMax;
            } else if (inputSpeed.xEuclid < currentSpeed.xEuclid) {
            outputSpeed.xEuclid = currentSpeed.xEuclid - rampMax;
            }
        } else {
            outputSpeed.xEuclid = inputSpeed.xEuclid;

        }
    
        if (Math.abs(currentSpeed.yEuclid - inputSpeed.yEuclid) > rampMax) {
            if (inputSpeed.yEuclid > currentSpeed.yEuclid) {
            outputSpeed.yEuclid = currentSpeed.yEuclid + rampMax;
            } else if (inputSpeed.yEuclid < currentSpeed.yEuclid) {
            outputSpeed.yEuclid = currentSpeed.yEuclid - rampMax;
            }
        } else {
            outputSpeed.yEuclid = inputSpeed.yEuclid;

        }

        return outputSpeed;
    }

    protected EuclideanCoord RcToDifferential() {
        EuclideanCoord RcCoord = new EuclideanCoord(Xcontroller, Ycontroller);
        RcCoord = ScaleToUnitSquare(RcCoord);
        RcCoord = ScaleSquareToUnitCircle(RcCoord);
        PolarCoord RcPolar = CartToPolar(RcCoord);
        RcPolar.theta = driveConversion(RcPolar.theta);
        RcCoord = PolarToCart(RcPolar);
        RcCoord = ScaleUp(RcCoord);
        
        return RcCoord;
    }
}
