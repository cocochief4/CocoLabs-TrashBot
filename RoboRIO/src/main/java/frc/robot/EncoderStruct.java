package frc.robot;

public class EncoderStruct {
    Double rVelocity;
    Double lVelocity;
    Double time; // Time in seconds

    public EncoderStruct(double rightVelocity, double leftVelocity, double timeStamp) {
        rVelocity = rightVelocity;
        lVelocity = leftVelocity;
        time = timeStamp;
    }

    public String toString() {
        String string = new String();
        string = "Velocity (left right): " + lVelocity.toString() + ", " + rVelocity.toString() + " Time: " + time.toString();

        return string;
    }
}
