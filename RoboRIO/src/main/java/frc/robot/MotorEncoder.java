package frc.robot;

import com.revrobotics.RelativeEncoder;

public class MotorEncoder {
    private static final float GEAR_RATIO = 125f;

    private static final double MILLIS_PER_MINUTE = 60000;

    private static final double TIRE_MULTIPLIER = Math.PI * 13d/12d;

    public static double time; // Time in seconds

    private static RelativeEncoder rightUpEncoder;
    private static RelativeEncoder rightDownEncoder;
    private static RelativeEncoder leftUpEncoder;
    private static RelativeEncoder leftDownEncoder;

    protected static void init() {
        rightUpEncoder = Robot.rightUpMotor.getEncoder();
        rightDownEncoder = Robot.rightDownMotor.getEncoder();
        leftUpEncoder = Robot.leftUpMotor.getEncoder();
        leftDownEncoder = Robot.leftDownMotor.getEncoder();
    }

    protected static EncoderStruct getVelocity() {
        double rUpVelocity = rightUpEncoder.getVelocity();
        double rDownVelocity = rightDownEncoder.getVelocity();
        double lUpVelocity = leftUpEncoder.getVelocity();
        double lDownVelocity = leftDownEncoder.getVelocity();

        double rVelocity = (rUpVelocity + rDownVelocity)*0.5*TIRE_MULTIPLIER/GEAR_RATIO/MILLIS_PER_MINUTE; // Feet per millis
        double lVelocity = -1 * (lUpVelocity + lDownVelocity)*0.5*TIRE_MULTIPLIER/GEAR_RATIO/MILLIS_PER_MINUTE; // Feet per millis

        EncoderStruct encoderStruct = new EncoderStruct(rVelocity, lVelocity, System.currentTimeMillis()); // Feet per millis

        return encoderStruct;
    }
}
