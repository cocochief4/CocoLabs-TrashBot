package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.I2C.Port;

public class NavXManager {
    public static double rotateToAngleRate;
    public static AHRS ahrs;
    
    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;

    static final double kToleranceDegrees = 2.0f;

    static double yawDeltaFromNorth = 0;

    static double previousRawYaw = 0;
    static double previousCorrectedYaw = 0;
    static final double YAW_SCALE_FACTOR = 360d/390d;

    public static void RInit() {
        previousCorrectedYaw = 0;
        previousRawYaw = 0;
        if (ahrs != null) {
            return;
        }

        try {
            /***********************************************************************
             * navX-MXP: - Communication via RoboRIO MXP (SPI, I2C) and USB. - See
             * http://navx-mxp.kauailabs.com/guidance/selecting-an-interface.
             * 
             * navX-Micro: - Communication via I2C (RoboRIO MXP or Onboard) and USB. - See
             * http://navx-micro.kauailabs.com/guidance/selecting-an-interface.
             * 
             * VMX-pi: - Communication via USB. - See
             * https://vmx-pi.kauailabs.com/installation/roborio-installation/
             * 
             * Multiple navX-model devices on a single robot are supported.
             ************************************************************************/
            ahrs = new AHRS(SerialPort.Port.kUSB1);
          } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
          }
          PIDController turnController = new PIDController(kP, kI, kD);
          turnController.enableContinuousInput(-180.0f, 180.0f);
          turnController.close();
        //   Calibrate();
        //   ahrs.zeroYaw();
    }

    public static void Calibrate() { // Is this called?
        ahrs.calibrate();
    }
    protected static void resetYaw() {
        ahrs.reset();
    }

    public static ImuData getData() {
        double rawYaw = ahrs.getYaw();
        double yawFromNorth = scaleYawDrift(rawYaw) + yawDeltaFromNorth;
        ImuData imuStruct = new ImuData(yawFromNorth, ahrs.getVelocityX(), ahrs.getVelocityY(), ahrs.getVelocityZ(), rawYaw);
        return imuStruct;
    }

    // Scale the delta as there is a linear drift in yaw, not random. (180 has 15 deg, 360 has 30, so forth)
    protected static double scaleYawDrift(Double yaw) {
        double calcYaw = yaw;
        if (Math.signum(yaw) != Math.signum(previousRawYaw) && Math.abs(yaw) > 100) {
            calcYaw += Math.signum(previousRawYaw)*360;
        }
        double yawDelta = (calcYaw - previousRawYaw)*YAW_SCALE_FACTOR;
        previousRawYaw = yaw;
        double correctedYaw = previousCorrectedYaw + yawDelta;
        if (Math.abs(correctedYaw) > 180) {
            correctedYaw += Math.signum(correctedYaw)*-360;
        }
        previousCorrectedYaw = correctedYaw;
        return correctedYaw;
    }
}
