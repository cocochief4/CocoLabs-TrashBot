package frc.robot;

import javax.swing.plaf.synth.SynthSeparatorUI;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SerialPort.Parity;
import edu.wpi.first.wpilibj.SerialPort.Port;
import edu.wpi.first.wpilibj.SerialPort.StopBits;

public class ArduinoManager {
    static ArduinoMegaData arduinoMegaData;
    

    private static SerialPort arduino = new SerialPort(115200, Port.kMXP, 8, Parity.kOdd, StopBits.kOne);
    protected static String queueBuf = "";

    protected static String readRaw() {
        return arduino.readString();
    }

    protected static String readUARTData() {
        String read = arduino.readString();
        queueBuf += read;
        // System.out.println("origin queubuf" + queueBuf);;
        // System.out.println("queue:" + queueBuf.toString());
        if (queueBuf.indexOf("|") != -1) {
            String realRead = queueBuf.substring(0, queueBuf.indexOf("|"));
            queueBuf = queueBuf.substring(queueBuf.indexOf("|") + 1);

            if (queueBuf.length() > 20) {
                queueBuf = queueBuf.substring(queueBuf.substring(0, queueBuf.lastIndexOf("|")).lastIndexOf("|")+1);
            }
            return realRead;
        } else {
            read = "";
            return read;
        }
    }

    /**
     * Checks all comms with Arduino Mega Initiative as well as waits for a fix on gps if wanted. Loopable function if it fails the first time.
     * @param gpsRequired If true, then it waits until a fix (2). If not, proceeds once comms with Arduino is established
     * @return true if check succeeded, false if it did not
     */
    static boolean init(boolean gpsRequired) {
        RcData rcStruct;
        GPSLatLongData gpsStruct;
        String data = readUARTData(); // 1500,1500,0_N
        if (!data.equals("")) {
            String dataArr[] = data.split("_");
            String rc = dataArr[0];
            String gps;
            if (dataArr.length == 2) {
                gps = dataArr[1];
            } else {
                gps = "N";
            }

            if (rc.equals("N")) {
                rcStruct = null;
            } else {
                String rcArr[] = rc.split(",");
                rcStruct = new RcData(Integer.parseInt(rcArr[1]), Integer.parseInt(rcArr[0]), 
                    Byte.parseByte(rcArr[2]));
            }

            if (gps.equals("N")) {
                gpsStruct = null;
            } else {
                if(Startup.NAVIGATION) {
                    if (Byte.parseByte(gps.split(",")[2]) == 2) {
                        String gpsArr[] = gps.split(",");
                        gpsStruct = new GPSLatLongData(Long.parseLong(gpsArr[0]), 
                            Long.parseLong(gpsArr[1]), Byte.parseByte(gpsArr[2]), System.currentTimeMillis());
                    } else {
                        gpsStruct = null;
                    }
                } else {
                    String gpsArr[] = gps.split(",");
                        gpsStruct = new GPSLatLongData(Long.parseLong(gpsArr[0]), 
                            Long.parseLong(gpsArr[1]), Byte.parseByte(gpsArr[2]), System.currentTimeMillis());
                }
            }

            ArduinoMegaData localArduinoMegaData = new ArduinoMegaData(rcStruct, gpsStruct, System.currentTimeMillis());
            arduinoMegaData = localArduinoMegaData;
            if (gpsRequired) {
                if (gpsStruct != null) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        } else {
            return false;
        }
    }

    static boolean getArduinoMegaData() {
        RcData rcStruct;
        GPSLatLongData gpsStruct = arduinoMegaData.gps;
        String data = readUARTData();
        if (data.equals("") != true) {
            // System.out.println("data:" + data.toString());
            String dataArr[] = data.split("_");
            // System.out.println("dataArr:" + dataArr.length);
            String rc = dataArr[0];
            String gps = dataArr[1];
            if (rc.equals("N")) {
                rcStruct = null;
            } else {
                String rcArr[] = rc.split(",");
                rcStruct = new RcData(Integer.parseInt(rcArr[1]), Integer.parseInt(rcArr[0]), 
                    Byte.parseByte(rcArr[2]));
            }

            if (gps.equals("N")) {
            } else {
                String gpsArr[] = gps.split(",");
                if (Byte.parseByte(gpsArr[2]) == 2) {
                    gpsStruct = new GPSLatLongData(Long.parseLong(gpsArr[0]), 
                                                   Long.parseLong(gpsArr[1]),
                                                   Byte.parseByte(gpsArr[2]),
                                                   System.currentTimeMillis());
                }
            }

            ArduinoMegaData localArduinoMegaData = new ArduinoMegaData(rcStruct, gpsStruct, System.currentTimeMillis());
            arduinoMegaData = localArduinoMegaData;
            return true;
        } else {
            return false;
        }
    }

    static GPSLatLongData getGPS() {
        return arduinoMegaData.gps;
    }

    static RcData getRC() {
        return arduinoMegaData.rc;
    }
}
