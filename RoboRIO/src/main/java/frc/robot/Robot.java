/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends TimedRobot {

  /* The following PID Controller coefficients will need to be tuned */
  /* to match the dynamics of your drive system. Note that the */
  /* SmartDashboard in Test mode has support for helping you tune */
  /* controllers by displaying a form where you can enter new P, I, */
  /* and D constants and test the mechanism. */

  protected static DifferentialDrive m_myRobot;

  private static final int leftUpDeviceID = 3; 
  private static final int leftDownDeviceID = 1;
  protected static CANSparkMax leftUpMotor;
  protected static CANSparkMax leftDownMotor;
  protected static MotorControllerGroup leftGroup;

  private static final int rightUpDeviceID = 4;
  private static final int rightDownDeviceID = 2;
  protected static CANSparkMax rightUpMotor;
  protected static CANSparkMax rightDownMotor;
  protected static MotorControllerGroup rightGroup;

  protected static EuclideanCoord robotSpeed = new EuclideanCoord(0.0, 0.0);
  protected static EuclideanCoord currentSpeed = new EuclideanCoord(0, 0);
  protected static final double RAMP_MAX = 0.015;

  @Override
  public void robotInit() {
    // NavXManager.RInit();

    /* Note that the PIDController GUI should be added automatically to */
    /* the Test-mode dashboard, allowing manual tuning of the Turn */
    /* Controller's P, I and D coefficients. */
    /* Typically, only the P value needs to be modified. */
    
    /**
     * SPARK MAX controllers are intialized over CAN by constructing a CANSparkMax object
     * 
     * The CAN ID, which can be configured using the SPARK MAX Client, is passed as the
     * first parameter
     * 
     * The motor type is passed as the second parameter. Motor type can either be:
     *  com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushless
     *  com.revrobotics.CANSparkMaxLowLevel.MotorType.kBrushed
     * 
     * The example below initializes four brushless motors with CAN IDs 1 and 2. Change
     * these parameters to match your setup
     */
      leftUpMotor = new CANSparkMax(leftUpDeviceID, MotorType.kBrushless);
      leftDownMotor = new CANSparkMax(leftDownDeviceID, MotorType.kBrushless);
      
      rightUpMotor = new CANSparkMax(rightUpDeviceID, MotorType.kBrushless);
      rightDownMotor = new CANSparkMax(rightDownDeviceID, MotorType.kBrushless);

      rightGroup = new MotorControllerGroup(rightUpMotor, rightDownMotor);
      leftGroup = new MotorControllerGroup(leftUpMotor, leftDownMotor);

    
      
      /*
       * The RestoreFactoryDefaults method can be used to reset the configuration parameters
       * in the SPARK MAX to their factory default state. If no argument is passed, these
       * parameters will not persist between power cycles
       */
      leftUpMotor.restoreFactoryDefaults();
      leftDownMotor.restoreFactoryDefaults();
      rightUpMotor.restoreFactoryDefaults();
      rightDownMotor.restoreFactoryDefaults();
      m_myRobot = new DifferentialDrive(leftGroup, rightGroup);

  }
  int startCooldown = 100; // Timer to force the all of the motors to 0,
                          // as there is a jump for no reason at all

  public void teleopInit() {
    NavXManager.RInit();
    boolean arduinoData = false;
    while (arduinoData == false) {
      arduinoData = ArduinoManager.init();
      // System.out.println("init"); // Must be before Navigator Init
    }
    // resetYaw MUST BE DELAYED FROM RInit as RInit Calibration overrides resetYaw request.
    // ArduinoManager.init() has a init time of around 3 sec, varies though
    Timer.delay(1.5);
    NavXManager.resetYaw(); // Must be before Nav init
    MotorEncoder.init(); // Must be before Nav init
    currentSpeed = new EuclideanCoord(0, 0);
    System.out.print("Start!");
    startCooldown = 50;
    Navigator.init();
    PathHandler.init();
    NavXManager.resetYaw();

  }

  // private void TeleopDrive(double throttle, double steering) {
  //   i = 0;
  //   //System.out.println(NavXManager.getData().toString());

  //   if (throttle != 9223372036854775807L && steering != 9223372036854775807L) {
  //     TeleopMath control =  new TeleopMath(steering, throttle);

  //     robotSpeed = new EuclideanCoord(control.RcToDifferential().xEuclid, control.RcToDifferential().yEuclid);
  //     // System.out.println(robotSpeed.toString());
  //     // System.out.println("Teleop mode ON");
  //     // System.out.println(MotorEncoder.getVelocity().toString());
  //     // System.out.println(NavXManager.getData().toString());

  //     // Ramp rate
  //     currentSpeed = control.CalcRamp(currentSpeed, robotSpeed, RAMP_MAX);

  //   }

      
  // }
  boolean previousState = true; // true is auto, false is teleop
  
  public void teleopPeriodic() {
    ArduinoManager.getArduinoMegaData();
    if (ArduinoManager.getRC() == null) {
      // boolean arrived = PathHandler.GoTo(new latLong(373453108E-7, -1220160366E-7)); // Center of Driveway
      boolean arrived = PathHandler.GoTo(new latLong(373453102E-7,-1220160612E-7)); // Closer to Garage
      // System.out.println("Yaw From North" + NavXManager.getData().yawFromNorth);
      if (arrived) {
        m_myRobot.tankDrive(0, 0);
        System.out.println("arrived");
      }
    } else {
      if (previousState) {
        previousState = false;
      }
      RcData rcData = ArduinoManager.getRC();
      EuclideanCoord steeringThrottle = new EuclideanCoord(rcData.steering, rcData.throttle);
      steeringThrottle = new TeleopMath(0d, 0d).ScaleToUnitSquare(steeringThrottle);
      AutonomousDrive.drive(steeringThrottle.yEuclid, steeringThrottle.xEuclid);
    }
  } // End of TeleopPeriodic()

  public void autonomousInit() {
    
  }

  public void autonomousPeriodic() {

  }
}
