package frc.robot;

import java.util.ArrayList;

public class PathHandler {
    private static ArrayList<latLong> nodeArr;
    private static latLong nextNode = null;

    protected static boolean haveTurned = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;

    protected static NavigatorData calibStartPos = new NavigatorData(0, 0, 0, 0);
    protected static NavigatorData calibEndPos = new NavigatorData(0, 0, 0, 0);

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
            distanceWithoutTurning += location.distance;
        } else {
            if (distanceWithoutTurning > 2) {
                calibEndPos = Navigator.getLocation();
                double deltaLat = calibStartPos.latitude - calibEndPos.latitude;
                double deltaLong = calibStartPos.longitude - calibEndPos.longitude;
                double tan = Math.toDegrees(Math.atan(deltaLong/deltaLat));
                double yawTan = calibEndPos.direction;
                NavXManager.yawDeltaFromNorth = yawTan-tan;
            }
        }

    }

    public static void GoTo() {
        NavigatorData location = Navigator.getLocation();
        latLong relativeNodeLocation = new latLong(location.latitude, location.longitude);
        if (nextNode == null) {
            // TODO: Add case for if nodeArr is finished
            nextNode.Lat = nodeArr.get(index).Lat;
            nextNode.Long = nodeArr.get(index).Long;
        }
        relativeNodeLocation.Lat = nextNode.Lat - location.latitude;
        relativeNodeLocation.Long = nextNode.Long - location.longitude;
        double nodeThetaFromNorth = Math.atan((relativeNodeLocation.Lat/relativeNodeLocation.Long) * Navigator.RADIANS_MULTIPLIER);
        double nodeRelativeTheta = nodeThetaFromNorth - location.direction * Navigator.RADIANS_MULTIPLIER;
        if (Math.abs(relativeNodeLocation.Lat) > 5E10-7 && 
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
        } else {
            nextNode = null;
        }
    }
}
