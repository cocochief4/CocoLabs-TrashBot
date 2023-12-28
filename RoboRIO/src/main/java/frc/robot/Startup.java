package frc.robot;

public class Startup {

    public static  boolean NAVIGATION, PICKUP, VISION; // RC Drive is always true

    public static  int pickupStart, pickupEnd;

    /**
     * NAVIGATION: is Phase 2 (autonomous navigation) capability installed on robot?
     * PICKUP: is Phase 4 (trash pickup) capability installed on robot?
     * VISION: is Phase 3 (trash detection) capability installed on robot?
     */
    public static void init() {
        // Change startup options here
        NAVIGATION = false; // You need GPS attached.
        PICKUP = false; // You need pickup arduino attached
        VISION = false; // You need camera mounted

        pickupStart = 0;
        pickupEnd = 1;
    }

}