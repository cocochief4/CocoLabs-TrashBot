package frc.robot;

import edu.wpi.first.wpilibj.DigitalOutput;
import edu.wpi.first.wpilibj.PWM;

public class Actuator {

    public DigitalOutput forward;
    public DigitalOutput backward;
    public PWM speed;

    public Actuator(int forwardPort, int backwardPort, int speedPort) {
        forward = new DigitalOutput(forwardPort);
        backward = new DigitalOutput(backwardPort);
        speed = new PWM(speedPort);
    }
}
