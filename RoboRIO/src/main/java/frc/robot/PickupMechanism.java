package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.PWM;

public class PickupMechanism {
    
    public int currentLimitSwitchHit = 0;

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
        horizontalMid = new DigitalInput(7);
        horizontalMax = new DigitalInput(8);


    }

    // writes 0 for no limit switch is triggered
    // 1 for vertical min
    // 2 for vertical max
    // 3 for horizontal min
    // 4 for horizontal mid
    // 5 for horizontal max
    public int isTriggered() {

        if (verticalMin.get()) {
            return 1;
        }
        if (verticalMax.get()) {
            return 2;
        }
        if (horizontalMin.get()) {
            return 3;
        }
        if (horizontalMid.get()) {
            return 4;
        }
        if (horizontalMax.get()) {
            return 5;
        }
        return 0;
        
    }

    public void driveHorizontalPause() {

        vertical.speed.setRaw(0);

    }

    public void driveVerticalPause() {

        horizontal.speed.setRaw(0);

    }

    public void driveHorizontalForward() {

        vertical.forward.set(true);
        vertical.backward.set(false);
        vertical.speed.setRaw(4095);

    }

    public void driveHorizontalBackward() {

        vertical.forward.set(false);
        vertical.backward.set(true);
        vertical.speed.setRaw(4095);

    }

    public void driveVerticalForward() {

        horizontal.forward.set(true);
        horizontal.backward.set(false);
        horizontal.speed.setRaw(4095);

    }

    public void driveVerticalBackward() {

        horizontal.forward.set(false);
        horizontal.backward.set(true);
        horizontal.speed.setRaw(4095);

    }
    
}
