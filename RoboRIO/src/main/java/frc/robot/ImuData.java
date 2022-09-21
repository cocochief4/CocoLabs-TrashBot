package frc.robot;

public class ImuData {
    public float yaw;
    public float xVelocity;
    public float yVelocity;
    public float zVelocity;

    public ImuData(float Yaw, float VelocityX, float VelocityY, float VelocityZ) {
        yaw = Yaw;
        xVelocity = VelocityX;
        yVelocity = VelocityY;
        zVelocity = VelocityZ;
    }

    public String toString() {
        String string = "IMU Data (yaw (deg), x, y, z): " + yaw + ", " + xVelocity + ", " + yVelocity + ", " + zVelocity;
        
        return string;
    }
}
