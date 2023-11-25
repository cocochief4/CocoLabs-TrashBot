package frc.robot;

public class ImuData {
    public double yawFromNorth;
    public double xVelocity;
    public double yVelocity;
    public double zVelocity;
    public double adjustedYaw;
    public double rawYaw;

    public ImuData(double yawFromNorth, double xVelocity, double yVelocity, double zVelocity, double rawYaw, double adjustedYaw) {
        this.yawFromNorth = yawFromNorth;
        this.xVelocity = xVelocity;
        this.yVelocity = yVelocity;
        this.zVelocity = zVelocity;
        this.rawYaw = rawYaw;
        this.adjustedYaw = adjustedYaw;
    }

    public String toString() {
        String string = "IMU Data (yaw from North (deg)- rawYaw- adjustedYaw- x- y- z): " + yawFromNorth + ", " + rawYaw + ", " + adjustedYaw + ", " + xVelocity + ", " + yVelocity + ", " + zVelocity;
        
        return string;
    }
}
