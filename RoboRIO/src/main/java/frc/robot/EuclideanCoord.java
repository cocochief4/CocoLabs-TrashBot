package frc.robot;

public class EuclideanCoord {
    Double x;
    Double y;

    public EuclideanCoord(double Xeuclid, double Yeuclid) {
        x = Xeuclid;
        y = Yeuclid;
    }

    public String toString() {
        String string = new String();
        string = x.toString() + " " + y.toString();

        return string;
    }
}
