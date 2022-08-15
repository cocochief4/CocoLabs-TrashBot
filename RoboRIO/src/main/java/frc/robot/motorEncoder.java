package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class MotorEncoder {
    protected final int SPIN_PER_REVOLUTION = 125;

    CANSparkMax motor;
    RelativeEncoder encoder;
    RelativeEncoder altEncoder;

    protected MotorEncoder(CANSparkMax Motor) {
        motor = Motor;
        encoder = motor.getEncoder();
        altEncoder = motor.getAlternateEncoder(SPIN_PER_REVOLUTION);
        altEncoder.setVelocityConversionFactor(SPIN_PER_REVOLUTION);
    }

    protected void getDistance() {
        System.out.println(altEncoder.getVelocity());
    }
}
