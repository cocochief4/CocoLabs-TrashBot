package frc.robot;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.Port;

public class GPSManager {
    // Static variable reference of single_instance
    // of type Singleton
    private final static I2C GPSArduino = new I2C(Port.kOnboard, 8);
    private static final int sendSize = 27;

    private static int Previousflag = 1; // Detect any change in the flag that comes in from the Arduino

    protected static String GetDataFromGPS() {
        //reading from the arduino to the roborio (i2c)
        byte[] byteArr = new byte[sendSize]; //THE LAST BYTE DOES NOT READ
        GPSArduino.read(4, sendSize, byteArr);
        //converting the byte array into the two values of throttle and stee
        String arduinoReceive = new String(byteArr);

        return arduinoReceive;
    }

    protected static long ConvertToLong(String longString) {
        try {
            return Long.parseLong(longString);

        } catch (NumberFormatException e) {
            return 9223372036854775807L;
        }
    }

    protected static short ConvertToShort(String shortString) {
        try {
            return Short.parseShort(shortString);

        } catch (NumberFormatException e) {
            return (short) 32767;
        }
    }

}
