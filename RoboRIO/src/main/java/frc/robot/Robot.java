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
import edu.wpi.first.util.datalog.DataLog;
import edu.wpi.first.util.datalog.StringLogEntry;
import edu.wpi.first.wpilibj.DataLogManager;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PWM;
import edu.wpi.first.wpilibj.DigitalOutput;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

import javax.xml.crypto.Data;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Robot extends TimedRobot {

  /* The following PID Controller coefficients will need to be tuned */
  /* to match the dynamics of your drive system. Note that the */
  /* SmartDashboard in Test mode has support for helping you tune */
  /* controllers by displaying a form where you can enter new P, I, */
  /* and D constants and test the mechanism. */

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

  protected static DifferentialDrive m_myRobot;

  protected static EuclideanCoord robotSpeed = new EuclideanCoord(0.0, 0.0);
  protected static EuclideanCoord currentSpeed = new EuclideanCoord(0, 0);
  protected static final double RAMP_MAX = 0.015;

  StringLogEntry strLog;

  public static final boolean IS_AUTO = false;

  protected static int visionPort = 0;

  private DigitalOutput pickupOut;
  private DigitalInput pickupIn;

  private ArrayList<Boolean> visionDetected = new ArrayList<>();

  @Override
  public void robotInit() {

    // motor = new PWM(0);

    // // basic input output stuff
    
    // // the numbers in the constructor are the port numbers
    // forward = new DigitalOutput(1);
    // backward = new DigitalOutput(2);

    // forward.get(); // this returns true or false for the input
    // backward.set(true); // this sets it to true or false for the output 



    // VisionManager.init(visionPort); // Comment this out if no camera
    DataLogManager.start();
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

      DataLog log = DataLogManager.getLog();
      strLog = new StringLogEntry(log, "/my/string");

  }
  int startCooldown = 100; // Timer to force the all of the motors to 0,
                          // as there is a jump for no reason at all

  /** 
   * If you only want driving capibility, comment out:
   * 
   * while (arduinoData == false) {
   *   arduinoData = ArduinoManager.init();
   * }
   * 
   * NavXManager.resetYaw();
   * 
   * Navigator.init();
   * 
   * NavXManager.resetYaw();
   */
  public void teleopInit() {

    pickupIn = new DigitalInput(0); // placeholder
    pickupOut = new DigitalOutput(0); // placeholder

    for (int i = 0; i<10; i++) {
      visionDetected.add(false);
    }

    NavXManager.RInit();
    boolean arduinoData = false;
    // Waits for a rtk gps fix before continuing
    /*
    while (arduinoData == false) {
      arduinoData = ArduinoManager.init();
      // System.out.println("init"); // Must be before Navigator Init
    }
    */
    // resetYaw MUST BE DELAYED FROM RInit as RInit Calibration overrides resetYaw request.
    // ArduinoManager.init() has a init time of around 3 sec, varies though
    Timer.delay(1.5);
    /* NavXManager.resetYaw(); */// Must be before Nav init
    MotorEncoder.init(); // Must be before Nav init
    currentSpeed = new EuclideanCoord(0, 0);
    DataLogManager.log("Start!");
    System.out.print("Start!");
    startCooldown = 50;
    /*Navigator.init();*/
    PathHandler.init();
    /*NavXManager.resetYaw();*/
    arrived = false;

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

  boolean arrived = false;
  public void teleopPeriodic() {

    pickupOut.set(false);

    double sum = 0;
    boolean average = false;
    
    visionDetected.remove(0);
    visionDetected.add(VisionManager.trashDetected());
    for (int i = 0; i<10; i++) {
      if (visionDetected.get(i)) {
        sum+=1;
      }
    }
    
    sum/=10.0;
    average = sum > 0.8 ? true : false;
    if (average) {
      pickupOut.set(true);
      while (!pickupIn.get()) {}
      pickupOut.set(false);
    }

    ArduinoManager.getArduinoMegaData();
    if (ArduinoManager.getRC() == null) {
      if (!arrived) {
      arrived = PathHandler.autonomousMainLoop();
      Navigator.calibrateYaw();
      //  // Point 3
      // System.out.println("Yaw From North" + NavXManager.getData().yawFromNorth);
      } else {
        System.out.println("arrived");
        DataLogManager.log("arrived");
        m_myRobot.tankDrive(0, 0);
      }
      RcData rcData = ArduinoManager.getRC();
      EuclideanCoord steeringThrottle = new EuclideanCoord(rcData.steering, rcData.throttle);
      steeringThrottle = new TeleopMath(0d, 0d).ScaleToUnitSquare(steeringThrottle);
      AutonomousDrive.drive(steeringThrottle.yEuclid, steeringThrottle.xEuclid);
      // DataLogManager.log("calcYaw" + NavXManager.getData().yawFromNorth);
      // DataLogManager.log("rawYaw" + NavXManager.getData().rawYaw);
    }
  } 
  
  // End of TeleopPeriodic()

  // private DigitalOutput forward;
  // private DigitalOutput backward;

  // private PWM a1A;
  // private PWM a1B;

  public PickupMechanism pickupMechanism;

  public void autonomousInit() {
    
    // pickupMechanism = new PickupMechanism();

    // pickupMechanism.startReading();

    // a1A = new PWM(0);
    // a1B = new PWM(1);

    // // basic input output stuff
    
    // // the numbers in the constructor are the port numbers
    // forward = new DigitalOutput(1);
    // backward = new DigitalOutput(2);
  }

  public void autonomousPeriodic() {
    
    // int currentLimitSwitchHit = pickupMechanism.isTriggered();

    

    // if (pickupMechanism.verticalMaxTriggered()) {
    //   pickupMechanism.driveHorizontalBackward();
    // }


    // forward.set(false);
    // backward.set(true);

    // a1A.setRaw(4095);
  }
}
