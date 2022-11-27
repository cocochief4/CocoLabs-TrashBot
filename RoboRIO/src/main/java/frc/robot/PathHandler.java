package frc.robot;

import java.util.ArrayList;

import edu.wpi.first.wpilibj.DataLogManager;

import stcpack.stc.node;

public class PathHandler {
    private static final double MAX_DRIVE_SPEED = 0.5f;
    private static final double MAX_TURN_SPEED = 0.25f;

    private static final double ARRIVED_MARGIN = 7.5E-7; // About 15 cm margin (Most likely much smaller bc we not near the equator)

    private static EuclideanCoord autoDrive = new EuclideanCoord(0d, 0d); // x is turning, y is throttle

    private static ArrayList<latLong> nodeArr;

    protected static boolean haveTurned = false;
    protected static boolean turning = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;

    protected static NavigatorData calibStartPos = new NavigatorData(0d, 0d, 0d, 0d, System.currentTimeMillis(), false);
    protected static NavigatorData calibEndPos = new NavigatorData(0, 0, 0, 0, System.currentTimeMillis(), false);

    private static int index = 0;

    protected static void init() {
        // DataLogManager.log(ArduinoManager.getGPS());
        latLong initPos = new latLong(ArduinoManager.getGPS().latitude * 1E-7, ArduinoManager.getGPS().longitude * 1E-7);
        // DataLogManager.log("latlongstruct: " + initPos
        //                     + "\norigin struct: " + ArduinoManager.getGPS().latitude + ", " + ArduinoManager.getGPS().longitude);
        // DataLogManager.log(initPos);
        stcpack.stc.spanningTreeCoverageAlgorithm(initPos);
        nodeArr = stcpack.stc.finalPath;
        // DataLogManager.log("Final Path");
        DataLogManager.log("Final Path");
        for (int i = 0; i<stcpack.stc.finalPath.size(); i++) {
            // DataLogManager.log("(" + stcpack.stc.finalPath.get(i).Long + ", " + stcpack.stc.finalPath.get(i).Lat + ")");
            DataLogManager.log("**(" + stcpack.stc.finalPath.get(i).Long + ", " + stcpack.stc.finalPath.get(i).Lat + ")");
        }
        // DataLogManager.log("FinalNavigate");
        // for (int i = 0; i<stcpack.stc.finalNavigate.size(); i++) {
        //     DataLogManager.log("(" + stcpack.stc.finalNavigate.get(i).Long + ", " + stcpack.stc.finalNavigate.get(i).Lat + ")");
        // }
        // DataLogManager.log("Initial Position");
        DataLogManager.log("Initial Position");
        // DataLogManager.log("(" + initPos.Long + ", " + initPos.Lat + ")"); 
        DataLogManager.log("**Init(" + initPos.Long + ", " + initPos.Lat + ")"); 
        // DataLogManager.log("( " + stcpack.stc.startNode.Long + ", " + stcpack.stc.startNode.Lat + ")");
        // nodeArr = new ArrayList<latLong>();
        // nodeArr.add(new latLong(373453108E-7, -1220160366E-7)); // Center
        // nodeArr.add(new latLong(373453102E-7,-1220160612E-7)); // Near Garage
        // nodeArr.add(new latLong(373452958E-7,-1220160489E-7)); // Point 3
        index = 0;
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
        // DataLogManager.log("Yaw Calib Delta: " + NavXManager.yawDeltaFromNorth);
        NavigatorData location = Navigator.getLocation();
        latLong relativeNodeLocation = new latLong(nextNode.Lat - location.latitude, nextNode.Long - location.longitude);
        // DataLogManager.log("Node Relative Location:" + relativeNodeLocation.toString(relativeNodeLocation));
        double nodeThetaFromNorth = Math.toDegrees(Math.atan2(relativeNodeLocation.Long, relativeNodeLocation.Lat));
        double nodeRelativeTheta = nodeThetaFromNorth - location.yawFromNorth;
        nodeRelativeTheta = degreesTo180(nodeRelativeTheta);
        // DataLogManager.log("node relative theta, yaw from north, nodeThetaFromNorth: " + nodeRelativeTheta + ", " + location.yawFromNorth + ", " + nodeThetaFromNorth);
        if (Math.abs(relativeNodeLocation.Lat) > ARRIVED_MARGIN || 
            Math.abs(relativeNodeLocation.Long) > ARRIVED_MARGIN) { // If we have not arrived at target node...
            if (haveTurned) {
                if (Math.abs(nodeRelativeTheta) < 0.5) {
                    haveTurned = false;
                    goForward();
                } else {
                    // DataLogManager.log("Turning");
                    if (Math.abs(autoDrive.yEuclid) < 0.05) {
                        autoDrive.yEuclid = 0d;
                    } else {
                        autoDrive.yEuclid -= Math.signum(autoDrive.yEuclid) * 0.05;
                    }
                    AutonomousDrive.drive(autoDrive.yEuclid, Math.signum(nodeRelativeTheta) * MAX_TURN_SPEED);
                }
            } else {
                if (Math.abs(nodeRelativeTheta) > 5) {
                    haveTurned = true;
                } else {
                    goForward();
                }
            }

            // DataLogManager.log("yawDeltaFromNorth: " + NavXManager.yawDeltaFromNorth + 
            //                     "\n nodeRelativeLocation: " + relativeNodeLocation.toString() + 
            //                     "\n nodeRelativeTheta: " + nodeRelativeTheta + "\nyawFromNorth: " + location.yawFromNorth + "\n nodeThetaFromNorth: " + 
            //                     nodeRelativeTheta + "\n location: " + location.toString());
            return false;
        } else {
            // DataLogManager.log("location" + location.toString());
            return true;
        }
    }

    public static boolean autonomousMainLoop() {
        boolean targetAchieved = GoTo(nodeArr.get(index));
        if (targetAchieved) {
            // DataLogManager.log("Target Node: " + nodeArr.get(index).Lat + ", " + nodeArr.get(index).Long + 
                                // "\n Number: " + index + "out of total nodes of " + (nodeArr.size()-1));
            DataLogManager.log("Target Node: " + nodeArr.get(index).Lat + ", " + nodeArr.get(index).Long + 
                                "\n Number: " + index + "out of total nodes of " + (nodeArr.size()-1));
            index++;
            if (nodeArr.size() < index + 1) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private static void goForward() {
        haveTurned = false;
        // DataLogManager.log("Go Forward");
        autoDrive.yEuclid += Math.signum(autoDrive.yEuclid + 0.0001) * 0.05;
        if (Math.abs(autoDrive.yEuclid) > MAX_DRIVE_SPEED) {
            autoDrive.yEuclid  = Math.signum(autoDrive.yEuclid) * MAX_DRIVE_SPEED;
        }
        AutonomousDrive.drive(autoDrive.yEuclid, 0);
    }

    protected static double degreesTo180(double degree) {
        // DataLogManager.log("pre-math: " + degree);
        double newDegree = degree;
        while (Math.abs(newDegree) > 360) {
            newDegree -= Math.signum(newDegree) * 360;
        }
        if (newDegree > 180) {
            newDegree -= 360;
        } else if (newDegree < -180) {
            newDegree += 360;
        }
        // DataLogManager.log("post-math: " + newDegree);
        return newDegree;
    }
}