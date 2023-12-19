package frc.robot;

public class Startup {

    public static boolean NAVIGATION, PICKUP, VISION, GPS; // RC Drive is always true

    /**
     * NAVIGATION: is Phase 2 (autonomous navigation) capability installed on robot?
     * PICKUP: is Phase 4 (trash pickup) capability installed on robot?
     * VISION: is Phase 3 (trash detection) capability installed on robot?
     */
    public static void init() {
        // Change startup options here
        NAVIGATION = false;
        PICKUP = true;
        VISION = false;
    }

}