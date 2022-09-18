package frc.robot;

public class ArduinoMegaData {
    RcData rc;
    LatLongFixData gps;
    Long timestamp = 0L;

    public ArduinoMegaData(RcData RC, LatLongFixData GPS, long Timestamp) {
        rc = RC;
        gps = GPS;
        timestamp = Timestamp;
    }

    public String toString() {
        String string = new String();
        String rcStr;
        String gpsStr;
        if (rc == null) {
            rcStr = "null";
        } else {
            rcStr = rc.toString();
        }
        if (gps == null) {
            gpsStr = "null";
        } else {
            gpsStr = gps.toString();
        }
        
        string = "RC*GPS*Timestamp:" + rcStr + "*" + gpsStr + "*" + timestamp.toString();

        return string;
    }

}