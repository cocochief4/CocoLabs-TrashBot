package frc.robot;

public class EncoderStruct {
    Double rVelocity;
    Double lVelocity;
    Long time; // Time in millis

    public EncoderStruct(double rightVelocity, double leftVelocity, long timeStamp) {
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
