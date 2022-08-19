package frc.robot;

import com.revrobotics.RelativeEncoder;
import edu.wpi.first.wpilibj.Timer;

public class MotorEncoder {
    private static Timer timer;

    private static final int SPIN_PER_REVOLUTION = 125;

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

        timer = new Timer();
        timer.reset();
    }

    protected static EncoderStruct getVelocity() {
        double rUpVelocity = rightUpEncoder.getVelocity();
        double rDownVelocity = rightDownEncoder.getVelocity();
        double lUpVelocity = leftUpEncoder.getVelocity();
        double lDownVelocity = leftDownEncoder.getVelocity();

        double rVelocity = rUpVelocity + rDownVelocity;
        double lVelocity = lUpVelocity + lDownVelocity;

        rVelocity /= 2;
        rVelocity /= SPIN_PER_REVOLUTION;

        lVelocity /= 2;
        lVelocity /= SPIN_PER_REVOLUTION;

        EncoderStruct encoderStruct = new EncoderStruct(rVelocity, lVelocity, timer.get());

        return encoderStruct;
    }
}
