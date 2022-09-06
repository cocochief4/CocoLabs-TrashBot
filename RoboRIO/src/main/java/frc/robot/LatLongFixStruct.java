package frc.robot;

public class LatLongFixStruct {
    Long latitude;
    Long longitude;
    Short fix; //0 = No fix, 1 = Floating fix, 2 = Fixed fix

    public LatLongFixStruct(long lat, long lon, short Fix) {
        latitude = lat;
        longitude = lon;
        fix = Fix;

    }

    public String toString() {
        String string = new String();
        string = "LatLong: " + latitude.toString() + " " + longitude.toString() + " " + fix.toString();

        return string;
    }
}
