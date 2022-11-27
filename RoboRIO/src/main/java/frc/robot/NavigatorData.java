package frc.robot;

public class NavigatorData {
    Double latitude;
    Double longitude;
    Double yawFromNorth;
    Double distanceFromLastReading;
    Long timeStamp; // In millis
    Boolean isGpsReading;

    public NavigatorData(double Latitutude, double Longitude, double Direction, double Distance, long TimestampMillis, boolean isGpsReading) {
        latitude = Latitutude;
        longitude = Longitude;
        yawFromNorth = Direction;
        distanceFromLastReading = Distance;
        timeStamp = TimestampMillis;
        this.isGpsReading = isGpsReading;

    }

    public String toString() {
        String string = new String();
        string = "Lat, Long: " + latitude.toString() + ", " + longitude.toString() +
        "distance: " + distanceFromLastReading.toString() +
        "direction: " + yawFromNorth.toString() +
        "is GPS Reading: " + this.isGpsReading;

        return string;
    }
}
