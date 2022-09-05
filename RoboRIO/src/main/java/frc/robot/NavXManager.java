package frc.robot;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj.I2C;

public class NavXManager {
    public static double rotateToAngleRate;
    public static AHRS ahrs;
    
    static final double kP = 0.03;
    static final double kI = 0.00;
    static final double kD = 0.00;
    static final double kF = 0.00;

    static final double kToleranceDegrees = 2.0f;

    static double yawDeltaFromNorth = 0;

    public static void RInit() {
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
            ahrs = new AHRS(I2C.Port.kMXP);
          } catch (RuntimeException ex) {
            DriverStation.reportError("Error instantiating navX MXP:  " + ex.getMessage(), true);
          }
          PIDController turnController = new PIDController(kP, kI, kD);
          turnController.enableContinuousInput(-180.0f, 180.0f);
          turnController.close();
    }

    public static void Calibrate() { // Is this called?
        ahrs.calibrate();
    }

    public static ImuStruct getData() {
        ImuStruct imuStruct = new ImuStruct(ahrs.getYaw(), ahrs.getVelocityX(), ahrs.getVelocityY(), ahrs.getVelocityZ());
        imuStruct.yaw += yawDeltaFromNorth;

        return imuStruct;
    }
}
