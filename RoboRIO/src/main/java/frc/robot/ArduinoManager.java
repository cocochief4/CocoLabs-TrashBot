package frc.robot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;

public class ArduinoManager {
    private static SerialPort arduino = new SerialPort(115200, Port.kMXP, 8, Parity.kOdd, StopBits.kOne);

    protected static String getData() {
        String read = arduino.readString();
        System.out.print("read: ");
        System.out.println(read);
        return read;
    }

    static ArduinoMegaStruct get() {
        RcStruct rcStruct;
        LatLongFixStruct gpsStruct;

        String data = getData();
        String dataArr[] = data.split("L");
        String rc = dataArr[0];
        String gps = dataArr[1];
        if (rc.toString() == "N") {
            rcStruct = null;
        } else {
            String rcArr[] = rc.split(",");
            rcStruct = new RcStruct(Long.parseLong(rcArr[1]), Long.parseLong(rcArr[0]), 
                Byte.parseByte(rcArr[2]));
        }

        if (gps.toString() == "N") {
            gpsStruct = null;
        } else {
            String gpsArr[] = gps.split(",");
            gpsStruct = new LatLongFixStruct(Long.parseLong(gpsArr[0]), 
                Long.parseLong(gpsArr[1]), Byte.parseByte(gpsArr[2]));
        }

        ArduinoMegaStruct arduinoMegaStruct = new ArduinoMegaStruct(rcStruct, gpsStruct);

        return arduinoMegaStruct;
    }

    static LatLongFixStruct getGPS() {
        ArduinoMegaStruct arduinoMegaStruct = get();
        
        return arduinoMegaStruct.gps;
    }

    static RcStruct getRC() {
        ArduinoMegaStruct arduinoMegaStruct = get();
        
        return arduinoMegaStruct.rc;
    }
}
