package frc.robot;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;

public class ArduinoManager {
    private static SerialPort arduino = new SerialPort(115200, Port.kMXP, 8, Parity.kOdd, StopBits.kOne);
    protected static String queueBuf = "";

    protected static String getData() {
        String read = arduino.readString();
        queueBuf += read;
        // System.out.println("origin queubuf" + queueBuf);;
        // System.out.println("queue:" + queueBuf.toString());
        if (queueBuf.indexOf("|") != -1) {
            String realRead = queueBuf.substring(0, queueBuf.indexOf("|"));
            queueBuf = queueBuf.substring(queueBuf.indexOf("|") + 1);
            System.out.println(realRead);
            // System.out.print("midbuf");
            // System.out.println(queueBuf);
            if (queueBuf.length() > 20) {
                queueBuf = queueBuf.substring(queueBuf.substring(0, queueBuf.lastIndexOf("|")).lastIndexOf("|")+1);
            }
            // System.out.print("endbuf");
            // System.out.println(queueBuf);
            return realRead;
        } else {
            read = "";
            // System.out.println("data ded");
            return read;
        }
    }

    static ArduinoMegaData get() {
        RcStruct rcStruct;
        LatLongFixStruct gpsStruct;
        String data = "";
        while (data.equals("")) {
            data = getData();
        }
        System.out.println("data:" + data.toString());
        String dataArr[] = data.split("_");
        System.out.println("dataArr:" + dataArr.length);
        String rc = dataArr[0];
        String gps = dataArr[1];
        if (rc.equals("N")) {
            rcStruct = null;
        } else {
            String rcArr[] = rc.split(",");
            rcStruct = new RcStruct(Long.parseLong(rcArr[1]), Long.parseLong(rcArr[0]), 
                Byte.parseByte(rcArr[2]));
        }

        if (gps.equals("N")) {
            gpsStruct = null;
        } else {
            String gpsArr[] = gps.split(",");
            gpsStruct = new LatLongFixStruct(Long.parseLong(gpsArr[0]), 
                Long.parseLong(gpsArr[1]), Byte.parseByte(gpsArr[2]));
        }

        ArduinoMegaData arduinoMegaData = new ArduinoMegaData(rcStruct, gpsStruct);

        return arduinoMegaData;
    }

    static LatLongFixStruct getGPS() {
        ArduinoMegaData arduinoMegaData = get();
        
        return arduinoMegaData.gps;
    }

    static RcStruct getRC() {
        ArduinoMegaData arduinoMegaData = get();
        
        return arduinoMegaData.rc;
    }
}
