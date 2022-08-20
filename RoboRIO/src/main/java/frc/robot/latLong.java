package frc.robot;

public class latLong {
    double Lat;
    double Long;

    protected latLong (double latitude, double longitude) {
        Lat = latitude;
        Long = longitude;
    }

    protected String toString(latLong LatLong) {
        String string = "Latitude, Longitude: " + LatLong.Lat + ", " + LatLong.Long;

        return string;
    }
}
