package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class LidarManager {
    private final static I2C LidarArduino = new I2C(Port.kOnboard, 2);
    private static final int sendSize = 16;

    protected static String GetDataFromLidar() {
        //reading from the arduino to the roborio (i2c)
        byte[] byteArr = new byte[sendSize]; //THE LAST BYTE DOES NOT READ
        LidarArduino.read(2, sendSize, byteArr);
        //converting the byte array into the two values of throttle and stee
        String arduinoReceive = new String(byteArr);

        return arduinoReceive;

    }

    protected static LidarStruct ParseLidar() {
        LidarStruct lidarStruct;
        
        String lidarData = LidarManager.GetDataFromLidar();
        lidarData = lidarData.substring(0, sendSize-1);

        // System.out.print("lidarData:");
        // System.out.println(lidarData.toString());

        String[] lidarBuf = lidarData.split(",");

        // System.out.print("lidarBuf[0]:");
        // System.out.println(lidarBuf[0]);
        // System.out.print("lidarBuf[1]");
        // System.out.println(lidarBuf[1]);

        lidarBuf[0] = lidarBuf[0].trim();
        lidarBuf[1] = lidarBuf[1].trim();

        float distanceInt = Float.parseFloat(lidarBuf[0]);
        Float angleInt = Float.parseFloat(lidarBuf[1]);
        distanceInt /= 100;
        angleInt /= 100;

        // System.out.print("lidarBuf[2]:");
        lidarBuf[2] = lidarBuf[2].trim();
        // System.out.println(lidarBuf[2]);
        byte quality = Byte.parseByte(lidarBuf[2]);

        lidarStruct = new LidarStruct(distanceInt, angleInt, quality);
        System.out.println(lidarStruct.toString());

        return lidarStruct;
    }
}
