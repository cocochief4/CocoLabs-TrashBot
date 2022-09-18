package frc.robot;

public class RcData {
    Long throttle;
    Long steering;
    Byte modeSwitch;

    public RcData(Long Throttle, Long Steering, Byte Mode) {
        throttle = Throttle;
        steering = Steering;
        modeSwitch = Mode;
    }

    public String toString() {
        String string = new String();
        
        string = "Throttle,Steering,modeSwitch: " + throttle.toString() + "," + steering.toString() + "," + modeSwitch.toString();

        return string;
    }
    
}
