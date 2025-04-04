package frc.robot;

public class PolarCoord {
    Double r;
    Double theta; // Theta is in degrees

    public PolarCoord(double Rpolar, double Tpolar) {
        r = Rpolar;
        theta = Tpolar;
    }

    public String toString() {
        String string = new String();
        string = r.toString() + " " + theta.toString();

        return string;
    }
}
