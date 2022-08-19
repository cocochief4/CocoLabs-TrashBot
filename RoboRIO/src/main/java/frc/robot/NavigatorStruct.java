package frc.robot;

public class NavigatorStruct {
    Double latitude;
    Double longitude;
    Double direction;

    public NavigatorStruct(double Latitutude, double Longitude, double dDirection) {
        latitude = Latitutude;
        longitude = Longitude;
        direction = dDirection;

    }

    public String toString() {
        String string = new String();
        string = latitude.toString() + " " + longitude.toString() + " " + direction.toString();

        return string;
    }
}
