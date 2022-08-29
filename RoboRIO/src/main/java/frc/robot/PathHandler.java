package frc.robot;

public class PathHandler {
    private static latLong[] nodeArr; // GAYWALA
    private static latLong nextNode = null;

    protected static boolean haveTurned = false;
    protected static boolean haveStartedCalib = false;
    protected static double distanceWithoutTurning = 0;

    protected static NavigatorStruct calibStartPos = new NavigatorStruct(0, 0, 0, 0);
    protected static NavigatorStruct calibEndPos = new NavigatorStruct(0, 0, 0, 0);

    private static void calibrate() { // WORK ON CALIBRATE
        if (haveTurned = false) {
            haveStartedCalib = true;
            calibStartPos = Navigator.getLocation();
        } else {
            haveStartedCalib = false;
        }

        if (haveStartedCalib == true) {
            NavigatorStruct location = Navigator.getLocation();
            distanceWithoutTurning += location.distance;
        } else {
            if (distanceWithoutTurning > 2) {
                calibEndPos = Navigator.getLocation();
                double absLat = calibStartPos.latitude - calibEndPos.latitude;
                double absLong = calibStartPos.longitude - calibEndPos.longitude;
                double tan = Math.atan(absLong/absLat) * Navigator.DEGREES_MULTIPLIER;
                double yawTan;
                if (calibStartPos.direction < 0) {
                    calibStartPos.direction += 360;
                }
            }
        }

    }

    public static void GoTo() {
        NavigatorStruct location = Navigator.getLocation();
        latLong relativeNodeLocation = new latLong(location.latitude, location.longitude);
        if (nextNode == null) {
            nextNode.Lat += ((Math.random()-0.5) * 2) * 10E-6;
            nextNode.Long += ((Math.random()-0.5) * 2) * 10E-6;
        }
        relativeNodeLocation.Lat = nextNode.Lat - location.latitude;
        relativeNodeLocation.Long = nextNode.Long - location.longitude;
        double nodeThetaFromNorth = Math.atan((relativeNodeLocation.Lat/relativeNodeLocation.Long) * Navigator.RADIANS_MULTIPLIER);
        double nodeRelativeTheta = nodeThetaFromNorth - location.direction * Navigator.RADIANS_MULTIPLIER;
        if (Math.abs(relativeNodeLocation.Lat) > 5E10-7 && 
            Math.abs(relativeNodeLocation.Long) > 5E10-7) { // If we have not arrived at target node...
            if (Math.abs(nodeRelativeTheta * Navigator.DEGREES_MULTIPLIER) < 1) { // Go forward
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
