package frc.robot;

public class NavigatorData {
    Double latitude;
    Double longitude;
    Double yawFromNorth;
    Double distanceFromLastReading;
    Long timeStamp; // In millis

    public NavigatorData(double Latitutude, double Longitude, double Direction, double Distance, long TimestampMillis) {
        latitude = Latitutude;
        longitude = Longitude;
        yawFromNorth = Direction;
        distanceFromLastReading = Distance;
        timeStamp = TimestampMillis;

    }

    public String toString() {
        String string = new String();
        string = "Lat,Long,Distance,Direction " + latitude.toString() + " " 
        + longitude.toString() + " " + distanceFromLastReading.toString() + " " + yawFromNorth.toString();

        return string;
    }
}
