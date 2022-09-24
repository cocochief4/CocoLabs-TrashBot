package frc.robot;

import java.util.ArrayList;

public class PathHandler {
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

    private static void calibrate() { // WORK ON CALIBRATE
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

    public static boolean GoTo(latLong nextNode) {
        NavigatorData location = Navigator.getLocation();
        latLong relativeNodeLocation = new latLong(location.latitude, location.longitude);
        relativeNodeLocation.Lat = nextNode.Lat - location.latitude;
        relativeNodeLocation.Long = nextNode.Long - location.longitude;
        double nodeThetaFromNorth = Math.atan(Math.toRadians(relativeNodeLocation.Long/relativeNodeLocation.Lat));
        double nodeRelativeTheta = Math.toRadians(nodeThetaFromNorth - location.yawFromNorth);
        if (Math.abs(relativeNodeLocation.Lat) > 5E10-7 || 
            Math.abs(relativeNodeLocation.Long) > 5E10-7) { // If we have not arrived at target node...
            if (Math.abs(Math.toDegrees(nodeRelativeTheta)) < 1) { // Go forward
                haveTurned = false;
                AutonomousDrive.drive(1, 0);
            } else { // Turn
                haveTurned = true;
                distanceWithoutTurning = 0;
                if (nodeRelativeTheta < 0) {
                    AutonomousDrive.drive(0, -0.5);
                } else if (nodeRelativeTheta > 0) {
                    AutonomousDrive.drive(0, 0.5);
                }
            }

            calibrate();
            return false;
        } else {
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
