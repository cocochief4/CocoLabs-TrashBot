package frc.robot;

import com.revrobotics.RelativeEncoder;

public class MotorEncoder {
    private static final int GEAR_RATIO = 125;

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

        double rVelocity = (rUpVelocity + rDownVelocity)*0.5/GEAR_RATIO;
        double lVelocity = (lUpVelocity + lDownVelocity)*0.5/GEAR_RATIO;

        EncoderStruct encoderStruct = new EncoderStruct(rVelocity, lVelocity, System.currentTimeMillis());
        System.out.println(encoderStruct.toString());

        return encoderStruct;
    }
}
