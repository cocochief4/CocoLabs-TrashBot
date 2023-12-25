public class Phase4SignalTesting {

    public static String str = "";

    public static void main(String[] args) {


        Chip rio = new Chip();
        Arduino arduino = new Arduino();

        while(true) {
            str = "";

            
            arduino.doArduino(rio);
            rio.rioDo(arduino);

            if (!str.equals("")) {
                System.out.println(str);
            }
        }
        
    }
}

class Chip {
    protected boolean out;
    protected boolean in;

    private boolean isPickingUp;
    private boolean broke;

    public Chip() {
        out = false;
        in = false;

        isPickingUp = false;
        broke = false;
    }

    /**
     * Checks if you are receiving from another chip (checks if their out is true)
     * @param receivingFrom The chip who we need to check their <out> pin
     * @return true if they are sending, false if not.
     */
    public boolean isReceiving(Chip receivingFrom) {
        return receivingFrom.isSending();
    }

    /**
     * Checks if the chip is sending
     * @return true if it is, false if not
     */
    private boolean isSending() {
        return out;
    }

    public void rioDo(Chip comms) {
        if (!isPickingUp) { // If not currently picking up...
            if (!broke/*isThereTrash()*/) { // and there is trash then start the pickup process
                Phase4SignalTesting.str += "Rio: Start picking up (should only be called once)  ";
                isPickingUp = true; // Start the pickup
                // pickupStart.setSpeed(1);
                out = true;
            }
        } else { // If it is currently picking up...
            if (isReceiving(comms)) { // And it finished picking up,
                Phase4SignalTesting.str += "Rio: Finished picking up, stopping pickup (should only be called once)  ";
                // pickupStart.setSpeed(0); // stop the pickup
                out = false;
                isPickingUp = false;
                broke = true;
            }
        }
    }
}

class Arduino extends Chip {
    private int pickup; // Counter for the pickup
    private boolean previousRioInState;
    private boolean pickingUp;

    public Arduino() {
        super();
        previousRioInState = false;
        pickup = 0;
        pickingUp = false;
    }

    public void doArduino(Chip comms) {
        if (pickingUp) {
            Phase4SignalTesting.str += "picking up";
            pickup--;
        }

        checkRio(comms);
    }

    public void checkRio(Chip comms) {
        if (isReceiving(comms) && previousRioInState == false) {
            Phase4SignalTesting.str += "Arduino: pinged to start picking up, start picking up (should only be called once)  ";
            pickup = 5;
            pickingUp = true;
        } else if (isReceiving(comms) && previousRioInState == true && pickup == 0) { // Done picking up
            Phase4SignalTesting.str += "Arduino: Done picking up, sending signal back to rio (should only be called once)   ";
            out = true;
            pickingUp = false;
        } else if (!isReceiving(comms)) { // If they stop sending, we stop sending.
            out = false;
        }

        previousRioInState = isReceiving(comms);
    }
}
