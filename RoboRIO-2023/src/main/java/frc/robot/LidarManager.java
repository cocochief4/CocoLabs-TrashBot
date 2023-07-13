// package frc.robot;

// import edu.wpi.first.wpilibj.I2C;
// import edu.wpi.first.wpilibj.I2C.Port;

// public class LidarManager {
//     private final static I2C LidarArduino = new I2C(Port.kOnboard, 2);
//     private static final int sendSize = 16;
//     private static final float MAX_FLOAT = (float) 3.4028235E38;

//     protected static String GetDataFromLidar() {
//         //reading from the arduino to the roborio (i2c)
//         byte[] byteArr = new byte[sendSize]; //THE LAST BYTE DOES NOT READ
//         LidarArduino.read(2, sendSize, byteArr);
//         //converting the byte array into the two values of throttle and stee
//         String arduinoReceive = new String(byteArr);

//         return arduinoReceive;

//     }

//     protected static float ConvertToFloat(String num) {
//         try {
//             float number = Float.parseFloat(num);

//             return number;
//         } catch (Exception NumberFormatException) {
//             return MAX_FLOAT;
//         }
//     }

//     protected static LidarStruct ParseLidar() {
//         LidarStruct lidarStruct;
        
//         String lidarData = LidarManager.GetDataFromLidar();
//         lidarData = lidarData.substring(0, sendSize-1);

//         // System.out.print("lidarData:");
//         // System.out.println(lidarData.toString());

//         String[] lidarBuf = lidarData.split(",");

//         System.out.print("lidarBuf[0]:");
//         System.out.println(lidarBuf[0]);
//         System.out.print("lidarBuf[1]:");
//         System.out.println(lidarBuf[1]);

//         lidarBuf[0] = lidarBuf[0].trim();
//         lidarBuf[1] = lidarBuf[1].trim();

//         float distanceInt = LidarManager.ConvertToFloat(lidarBuf[0]);
//         float angleInt = LidarManager.ConvertToFloat(lidarBuf[1]);
//         if (distanceInt != MAX_FLOAT && angleInt != MAX_FLOAT) {
//             distanceInt /= 100;
//             angleInt /= 100;

//             // System.out.print("lidarBuf[2]:");
//             lidarBuf[2] = lidarBuf[2].trim();
//             // System.out.println(lidarBuf[2]);
//             byte quality = Byte.parseByte(lidarBuf[2]);

//             lidarStruct = new LidarStruct(distanceInt, angleInt, quality);
//             // System.out.println(lidarStruct.toString());

//             return lidarStruct;
//         }
//         return null;
//     }   
// }
