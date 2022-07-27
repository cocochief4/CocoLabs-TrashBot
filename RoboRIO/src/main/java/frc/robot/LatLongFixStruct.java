package frc.robot;

public class LatLongFixStruct {
    Long latitude;
    Long longitude;
    Short fix; //0 = No fix, 1 = Floating fix, 2 = Fixed fix
    Short flag;

    public LatLongFixStruct(long lat, long lon, short Fix, short Flag) {
        latitude = lat;
        longitude = lon;
        fix = Fix;
        flag = Flag;

    }

    public String toString() {
        String string = new String();
        string = latitude.toString() + " " + longitude.toString() + " " + fix.toString();

        return string;
    }
}
