package frc.robot;

public class ArduinoMegaStruct {
    RcStruct rc;
    LatLongFixStruct gps;

    public ArduinoMegaStruct(RcStruct RC, LatLongFixStruct GPS) {
        rc = RC;
        gps = GPS;
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
        
        string = "RC*GPS" + rcStr + "*" + gpsStr;

        return string;
    }

}