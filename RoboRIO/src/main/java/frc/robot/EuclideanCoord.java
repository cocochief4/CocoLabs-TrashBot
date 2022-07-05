package frc.robot;

public class EuclideanCoord {
    Double xEuclid;
    Double yEuclid;

    public EuclideanCoord(double x, double y) {
        xEuclid = x;
        yEuclid = y;
    }

    public String toString() {
        String string = new String();
        string = xEuclid.toString() + " " + yEuclid.toString();

        return string;
    }
}
