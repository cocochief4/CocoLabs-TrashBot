package frc.robot;

public class ImuData {
    public double yawFromNorth;
    public double xVelocity;
    public double yVelocity;
    public double zVelocity;
    public double rawYaw;

    public ImuData(double yawFromNorth, double xVelocity, double yVelocity, double zVelocity, double rawYaw) {
        this.yawFromNorth = yawFromNorth;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
        this.rawYaw = rawYaw;
    }

    public String toString() {
        String string = "IMU Data (yaw from North (deg)- rawYaw- x- y- z): " + yawFromNorth + ", " + rawYaw + ", " + xVelocity + ", " + yVelocity + ", " + zVelocity;
        
        return string;
    }
}
