package frc.robot;

import java.util.ArrayList;

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
        System.out.println("Node Relative Location:" + relativeNodeLocation.toString(relativeNodeLocation));
        double nodeThetaFromNorth = Math.toDegrees(Math.atan2(relativeNodeLocation.Long, relativeNodeLocation.Lat));
        double nodeRelativeTheta = nodeThetaFromNorth - location.yawFromNorth;
        if (Math.abs(relativeNodeLocation.Lat) > 5E-7 || 
            Math.abs(relativeNodeLocation.Long) > 5E-7) { // If we have not arrived at target node...
            if (haveTurned) {
                if (Math.abs(nodeRelativeTheta) > 0.5) {
                    turning = true;
                    haveTurned = true;
                    if (nodeRelativeTheta < 0) {
                        System.out.println("Turning");
                        if (Math.abs(autoDrive.xEuclid) < MAX_TURN_SPEED) {
                            autoDrive.xEuclid -= 0.1;
                            if (autoDrive.yEuclid > 0d) {
                                autoDrive.yEuclid -= 0.1;
                            } else if (autoDrive.yEuclid < 0d) {
                                autoDrive.yEuclid = 0d;
                            }
                        }
                        AutonomousDrive.drive(autoDrive.yEuclid, autoDrive.xEuclid);
                    } else if (nodeRelativeTheta > 0) {
                        if (Math.abs(autoDrive.xEuclid) < MAX_TURN_SPEED) {
                            autoDrive.xEuclid += 0.1;
                            if (autoDrive.yEuclid > 0d) {
                                autoDrive.yEuclid += 0.1;
                            } else if (autoDrive.yEuclid < 0d) {
                                autoDrive.yEuclid = 0d;
                            }
                        }
                        System.out.println("Turning");
                        AutonomousDrive.drive(autoDrive.yEuclid, autoDrive.xEuclid);
                    }
                } else {
                    haveTurned = false;
                    turning = false;
                }
            }
            if (Math.abs(nodeRelativeTheta) < 5 && turning == false) { // Go forward
                System.out.println("Driving");
                if (autoDrive.yEuclid < MAX_DRIVE_SPEED) {
                    autoDrive.yEuclid += 0.1;
                } else if (autoDrive.yEuclid > MAX_DRIVE_SPEED) {
                    autoDrive.yEuclid = MAX_DRIVE_SPEED;
                }
                if (Math.abs(autoDrive.xEuclid) > 0.06) {
                    if (autoDrive.xEuclid < 0) {
                        autoDrive.xEuclid += 0.1;
                    } else if (autoDrive.xEuclid > 0) {
                        autoDrive.xEuclid -= 0.1;
                    } else {
                        autoDrive.xEuclid = 0d;
                    }
                }
                AutonomousDrive.drive(autoDrive.yEuclid, autoDrive.xEuclid);
            } else { // Turn
                haveTurned = true;
                distanceWithoutTurning = 0;
                turning = true;
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
