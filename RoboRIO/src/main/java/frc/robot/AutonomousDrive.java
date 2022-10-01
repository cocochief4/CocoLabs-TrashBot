package frc.robot;

public class AutonomousDrive {
    static EuclideanCoord previousSpeed = new EuclideanCoord(0, 0);

    public static void drive(double throttle, double steering) {
        // System.out.println("input:" + throttle + "," + steering);
        EuclideanCoord throttleSteering = new EuclideanCoord(steering, throttle);
        TeleopMath teleopMath = new TeleopMath(steering, throttle);
        throttleSteering = teleopMath.ScaleSquareToUnitCircle(throttleSteering);
        // System.out.println("scalesquaretounitcircle" + throttleSteering.toString());
        PolarCoord polar = teleopMath.CartToPolar(throttleSteering);
        polar.theta = teleopMath.driveConversion(polar.theta);
        throttleSteering = teleopMath.PolarToCart(polar);
        // System.out.println("polartocart" + throttleSteering.toString());
        throttleSteering = teleopMath.ScaleUp(throttleSteering);
        // System.out.println("scaleup" + throttleSteering.toString());

        throttleSteering = teleopMath.CalcRamp(previousSpeed, throttleSteering, Robot.RAMP_MAX);
        previousSpeed = throttleSteering;
        // System.out.println("calcramp" + throttleSteering.toString());

        System.out.println("Tank Drive" + throttleSteering.toString());

        Robot.m_myRobot.tankDrive(throttleSteering.yEuclid * -1, throttleSteering.xEuclid);
    }
    
}
