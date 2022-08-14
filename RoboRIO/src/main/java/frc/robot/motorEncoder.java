package frc.robot;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;

public class Encoder {
    CANSparkMax motor;
    RelativeEncoder encoder;

    protected Encoder(CANSparkMax Motor) {
        motor = Motor;
        encoder = motor.getEncoder();
    }
}
