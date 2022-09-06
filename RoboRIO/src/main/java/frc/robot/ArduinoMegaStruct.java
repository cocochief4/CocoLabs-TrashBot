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
        
        string = "RC*GPS" + rc.toString() + "*" + gps.toString();

        return string;
    }

}