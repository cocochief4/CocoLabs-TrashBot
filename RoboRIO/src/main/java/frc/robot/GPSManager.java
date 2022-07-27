package frc.robot;

import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.SPI.Port;;

public class GPSManager {
    // Static variable reference of single_instance
    // of type Singleton
    private static GPSManager single_instance = null;

    private static Port port = Port.kOnboardCS0;
    private static SPI gps = new SPI(port);

    private static final int sendSize = 28;

    private static int Previousflag = 1; // Detect any change in the flag that comes in from the Arduino
  
    // Constructor
    // Here we will be creating private constructor
    // restricted to this class itself
    private GPSManager()
    {
        gps.setMSBFirst();
		gps.setChipSelectActiveLow();
		gps.setClockRate(1000);
		gps.setClockActiveLow();
    }
  
    // Static method
    // Static method to create instance of Singleton class
    public static GPSManager getInstance()
    {
        if (single_instance == null) {
            single_instance = new GPSManager();
        }
        return single_instance;
    }

    protected static LatLongFixStruct GetDataFromGPS() {
        byte[] buffer = new byte[sendSize];
        byte[] sendBytes = new byte[28];

        gps.transaction(sendBytes, buffer, sendSize);

        String strBuf = new String(buffer);
        String[] strArr = strBuf.split(",", 0);
        String lat = strArr[0];
        String lon = strArr[1];
        String fix = strArr[2];
        String flag = strArr[3];
        lat.substring(1, lat.length());
        lat.trim();
        lon.trim();

        if (Previousflag != Integer.parseInt(flag)) {
            flag = "2";

        }

        LatLongFixStruct gpsCoords = new LatLongFixStruct(Long.parseLong(lat), Long.parseLong(lon),
            Short.parseShort(fix), Short.parseShort(flag));

        return gpsCoords;

    }

}
