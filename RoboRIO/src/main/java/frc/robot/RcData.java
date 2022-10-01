package frc.robot;

public class RcData {
    Integer throttle;
    Integer steering;
    Byte modeSwitch;

    public RcData(Integer Throttle, Integer Steering, Byte Mode) {
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
