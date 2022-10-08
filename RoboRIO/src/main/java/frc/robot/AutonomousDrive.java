package frc.robot;

public class AutonomousDrive {
    static EuclideanCoord previousSpeed = new EuclideanCoord(0, 0);

    public static void drive(double throttle, double steering) {
        EuclideanCoord throttleSteering = new EuclideanCoord(steering, throttle);
        TeleopMath teleopMath = new TeleopMath(steering, throttle);
        throttleSteering = teleopMath.ScaleSquareToUnitCircle(throttleSteering);
        PolarCoord polar = teleopMath.CartToPolar(throttleSteering);
        polar.theta = teleopMath.driveConversion(polar.theta);
        throttleSteering = teleopMath.PolarToCart(polar);
        throttleSteering = teleopMath.ScaleUp(throttleSteering);
        // throttleSteering = teleopMath.CalcRamp(previousSpeed, throttleSteering, Robot.RAMP_MAX);
        previousSpeed = throttleSteering;
        System.out.println("throttle, steering: " + throttle + ", " + steering);
        Robot.m_myRobot.tankDrive(throttleSteering.yEuclid * -1, throttleSteering.xEuclid);
    }
    
}
