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

    protected static LatLongFixStruct ParseGPSData(byte callOrigin) {
        LatLongFixStruct latLongFixStruct = new LatLongFixStruct(0L, 0L, (short)0, (short)0);

        String data = GetDataFromGPS();
        if (data.length() == sendSize) {
            data = data.substring(0, sendSize);
            data.trim();

            String[] strArr = new String[4];
            int numOfValues = 0;

            strArr = data.split(",");

            numOfValues = strArr.length;

            // System.out.println("size check");
            // System.out.println(numOfValues);
            // System.out.println(GPSManager.GetDataFromGPS().toString());

            if (numOfValues == 4) {
                long latitude = ConvertToLong(strArr[0].trim());
                // System.out.println("String arr 0:" + strArr[0]);
                long longitude = ConvertToLong(strArr[1]);
                short fix = ConvertToShort(strArr[2]);
                short flag = ConvertToShort(strArr[3]);
                // System.out.println("commas check");
                // System.out.println("Long: " + long1 + ", " + long2);

                if (Previousflag != flag) {
                    Previousflag = flag;
                    if (latitude != 9223372036854775807L && longitude != 9223372036854775807L
                        && fix != 32767 && flag != 32767) {
                        // System.out.println("no corruption");
                        if (callOrigin == 0) {  // If called from setup
                            latLongFixStruct.latitude = latitude;
                            latLongFixStruct.longitude = longitude;
                            latLongFixStruct.fix = fix;
                            latLongFixStruct.flag = flag;

                            return latLongFixStruct;

                        } else if (callOrigin == 1) {
                            if (Previousflag != Short.parseShort(strArr[3])) {
                                latLongFixStruct.latitude = latitude;
                                latLongFixStruct.longitude = longitude;
                                latLongFixStruct.fix = fix;
                                latLongFixStruct.flag = flag;

                                return latLongFixStruct;
                            }
                            return null;
                        }
                        return null;
                    }
                    return null;
                }
                return null;
            }
            return null;
        }
        return null;
    }

}
