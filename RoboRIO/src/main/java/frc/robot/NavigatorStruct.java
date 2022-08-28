package frc.robot;

public class NavigatorStruct {
    Double latitude;
    Double longitude;
    Double direction;
    Double distance;

    public NavigatorStruct(double Latitutude, double Longitude, double Direction, double Distance) {
        latitude = Latitutude;
        longitude = Longitude;
        direction = Direction;
        distance = Distance;

    }

    public String toString() {
        String string = new String();
        string = "Lat,Long,Distance,Direction " + latitude.toString() + " " 
                + longitude.toString() + " " + distance.toString() + " " + direction.toString();

        return string;
    }
}
