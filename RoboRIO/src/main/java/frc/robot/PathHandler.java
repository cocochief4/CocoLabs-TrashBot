package frc.robot;

import java.util.ArrayList;

public class PathHandler {
    private static final float MAX_DRIVE_SPEED = 0.5f;
    private static final float MAX_TURN_SPEED = 0.25f;

    private static ArrayList<latLong> nodeArr;

    protected static boolean haveTurned = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;

    protected static NavigatorData calibStartPos = new NavigatorData(0d, 0d, 0d, 0d, System.currentTimeMillis());
    protected static NavigatorData calibEndPos = new NavigatorData(0, 0, 0, 0, System.currentTimeMillis());

    private static int index = 0;

    protected static void init() {
        latLong initPos = new latLong(ArduinoManager.getGPS().latitude, ArduinoManager.getGPS().longitude);
        stcpack.stc.spanningTreeCoverageAlgorithm(initPos);
        nodeArr = stcpack.stc.finalNavigate;
    }
/*
    protected static void calibrate() { // WORK ON CALIBRATE
        if (haveTurned = false) {
            haveStartedCalib = true;
            calibStartPos = Navigator.getLocation();
        } else {
            haveStartedCalib = false;
        }

        if (haveStartedCalib == true) {
            NavigatorData location = Navigator.getLocation();
            distanceWithoutTurning += location.distanceFromLastReading;
        } else {
            if (distanceWithoutTurning > 2) {
                calibEndPos = Navigator.getLocation();
                double deltaLat = calibStartPos.latitude - calibEndPos.latitude;
                double deltaLong = calibStartPos.longitude - calibEndPos.longitude;
                double tan = Math.toDegrees(Math.atan(deltaLong/deltaLat));
                double yawTan = calibEndPos.yawFromNorth;
                NavXManager.yawDeltaFromNorth = yawTan-tan;
            }
        }

    }
*/
    public static boolean GoTo(latLong nextNode) {
        NavigatorData location = Navigator.getLocation();
        latLong relativeNodeLocation = new latLong(nextNode.Lat - location.latitude, nextNode.Long - location.longitude);
        System.out.println("Node Relative Location:" + relativeNodeLocation.toString(relativeNodeLocation));
        double nodeThetaFromNorth = Math.toDegrees(Math.atan2(relativeNodeLocation.Long, relativeNodeLocation.Lat));
        double nodeRelativeTheta = nodeThetaFromNorth - location.yawFromNorth;
        if (Math.abs(relativeNodeLocation.Lat) > 5E-7 || 
            Math.abs(relativeNodeLocation.Long) > 5E-7) { // If we have not arrived at target node...
            if (Math.abs(nodeRelativeTheta) < 1) { // Go forward
                haveTurned = false;
                System.out.println("Driving");
                AutonomousDrive.drive(MAX_DRIVE_SPEED, 0);
            } else { // Turn
                haveTurned = true;
                distanceWithoutTurning = 0;
                if (nodeRelativeTheta < 0) {
                    System.out.println("Turning");
                    AutonomousDrive.drive(0, -MAX_TURN_SPEED);
                } else if (nodeRelativeTheta > 0) {
                    System.out.println("Turning");
                    AutonomousDrive.drive(0, MAX_TURN_SPEED);
                }
            }

            return false;
        } else {
            System.out.println("location" + location.toString());
            return true;
        }
    }

    public static void autonomousMainLoop() {
        boolean targetAchieved = GoTo(nodeArr.get(index));
        if (targetAchieved) {
            index++;
        }
    }
}
