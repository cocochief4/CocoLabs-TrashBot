package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;

public class PickupMechanism {
    
    // linear actuator
    public Actuator vertical;
    public Actuator horizontal;

    public DigitalInput verticalMin;
    public DigitalInput verticalMax;
    public DigitalInput horizontalMin;
    public DigitalInput horizontalMax;
    public DigitalInput horizontalMid;

    public PickupMechanism() {

        // fill these in with akhil

        vertical = new Actuator(2, 4, 0);
        horizontal = new Actuator(1, 3, 1);

        verticalMin = new DigitalInput(5);
        verticalMax = new DigitalInput(9);
        horizontalMin = new DigitalInput(6);
        horizontalMax = new DigitalInput(8);
        horizontalMid = new DigitalInput(7);


    }

    public void driveXForward() {

        vertical.forward.set(true);
        vertical.backward.set(false);
        vertical.speed.setRaw(4095);

    }

    public void driveXBackward() {

        vertical.forward.set(false);
        vertical.backward.set(true);
        vertical.speed.setRaw(4095);

    }

    public void driveYForward() {

        horizontal.forward.set(true);
        horizontal.backward.set(false);
        horizontal.speed.setRaw(4095);

    }

    public void driveYBackward() {

        horizontal.forward.set(false);
        horizontal.backward.set(true);
        horizontal.speed.setRaw(4095);

    }

    
}
