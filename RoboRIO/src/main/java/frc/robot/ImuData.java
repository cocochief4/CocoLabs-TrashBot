package frc.robot;

public class ImuData {
    public float yawFromNorth;
    public float xVelocity;
    public float yVelocity;
    public float zVelocity;

    public ImuData(float Yaw, float VelocityX, float VelocityY, float VelocityZ) {
        yawFromNorth = Yaw;
        xVelocity = VelocityX;
        yVelocity = VelocityY;
        zVelocity = VelocityZ;
    }

    public String toString() {
        String string = "IMU Data (yaw from North (deg), x, y, z): " + yawFromNorth + ", " + xVelocity + ", " + yVelocity + ", " + zVelocity;
        
        return string;
    }
}
