package frc.robot;

public class PathHandler {
    private static latLong[] nodeArr; // GAYWALA
    private static latLong nextNode = null;

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
        if (Math.abs(relativeNodeLocation.Lat) > 5E10-7 && Math.abs(relativeNodeLocation.Long) > 5E10-7) {
            if (Math.abs(nodeRelativeTheta * Navigator.DEGREES_MULTIPLIER) < 1) {
                AutonomousDrive.drive(1, 0);
            } else {
                if (nodeRelativeTheta < 0) {
                    AutonomousDrive.drive(0, -0.5);
                } else if (nodeRelativeTheta > 0) {
                    AutonomousDrive.drive(0, 0.5);
                }
            }
        } else {
            nextNode = null;
        }
    }
}
