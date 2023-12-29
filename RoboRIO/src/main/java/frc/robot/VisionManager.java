package frc.robot;

import edu.wpi.first.wpilibj.DigitalInput;

public class VisionManager {
    private static DigitalInput port;
    protected static void init(int portNumber) {
        port = new DigitalInput(portNumber);
    }

    protected static boolean trashDetected() {
        boolean trashDetected = port.get();
        return trashDetected;
    }
}

