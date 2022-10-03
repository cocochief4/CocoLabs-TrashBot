package frc.robot;

import java.util.ArrayList;

import pabeles.concurrency.IntOperatorTask.Max;
import stcpack.stc.node;

public class PathHandler {
    private static final double MAX_DRIVE_SPEED = 0.5d;
    private static final double MAX_TURN_SPEED = 0.5d;
    protected static EuclideanCoord autoDrive = new EuclideanCoord(0d, 0d);

    private static ArrayList<latLong> nodeArr;

    protected static boolean haveTurned = false;
    protected static boolean turning = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;

    protected static NavigatorData calibStartPos = new NavigatorData(0d, 0d, 0d, 0d, System.currentTimeMillis());
    protected static NavigatorData calibEndPos = new NavigatorData(0, 0, 0, 0, System.currentTimeMillis());

    private static int index = 0;

    protected static void init() {
        latLong initPos = new latLong(ArduinoManager.getGPS().latitude, ArduinoManager.getGPS().longitude);
        stcpack.stc.spanningTreeCoverageAlgorithm(initPos);
        nodeArr = stcpack.stc.finalNavigate;
        autoDrive.xEuclid = 0d;
        autoDrive.yEuclid = 0d;
    }
/*
    protected static void calibrate() { 
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
        double nodeThetaFromNorth = Math.toDegrees(Math.atan2(relativeNodeLocation.Long, relativeNodeLocation.Lat));
        double nodeRelativeTheta = nodeThetaFromNorth - location.yawFromNorth;
        if (Math.abs(relativeNodeLocation.Lat) > 5E-7 || 
            Math.abs(relativeNodeLocation.Long) > 5E-7) { // If we have not arrived at target node...
            if (Math.abs(nodeRelativeTheta) > 60) {
                autoDrive.yEuclid = 0d;
            } else {
                autoDrive.yEuclid = MAX_DRIVE_SPEED;
            }
            autoDrive.xEuclid = nodeRelativeTheta/60 * MAX_TURN_SPEED;
            if (Math.abs(autoDrive.xEuclid) > MAX_TURN_SPEED) {
                autoDrive.xEuclid = Math.signum(autoDrive.xEuclid) * MAX_TURN_SPEED;
            }
            if (Math.abs(autoDrive.xEuclid) < 0.1) {
                autoDrive.xEuclid = Math.signum(autoDrive.xEuclid) * 0.1;
            }
            System.out.println("Node Relative Location:" + relativeNodeLocation.toString(relativeNodeLocation) + ", Node relavtive Yaw: " + nodeRelativeTheta);
            System.out.println("Autodrive: " + autoDrive.toString());
            AutonomousDrive.drive(autoDrive.yEuclid, autoDrive.xEuclid);
            return false;
        } else {
            // System.out.println("location" + location.toString());
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
