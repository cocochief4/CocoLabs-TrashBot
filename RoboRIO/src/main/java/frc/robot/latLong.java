package frc.robot;

public class latLong {
    public double Lat;
    public double Long;

    public latLong (double latitude, double longitude) {
        Lat = latitude;
        Long = longitude;
    }

    public String toString() {
        String string = "Latitude, Longitude: " + Lat + ", " + Long;

        return string;
    }
}
