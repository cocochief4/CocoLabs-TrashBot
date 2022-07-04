package frc.robot;

public class EuclideanCoord {
    Double Xeuclid;
    Double Yeuclid;

    public EuclideanCoord(double x, double y) {
        Xeuclid = x;
        Yeuclid = y;
    }

    public String toString() {
        String string = new String();
        string = Xeuclid.toString() + " " + Yeuclid.toString();

        return string;
    }
}
