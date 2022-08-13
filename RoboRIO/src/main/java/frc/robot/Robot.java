/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;

import java.math.*;

public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;

  private static final int leftUpDeviceID = 3; 
  private static final int leftDownDeviceID = 1;
  private CANSparkMax leftUpMotor;
  private CANSparkMax leftDownMotor;
  private MotorControllerGroup leftGroup;

  private static final int rightUpDeviceID = 4;
  private static final int rightDownDeviceID = 2;
  private CANSparkMax rightUpMotor;
  private CANSparkMax rightDownMotor;
  private MotorControllerGroup rightGroup;

  private EuclideanCoord robotSpeed = new EuclideanCoord(0.0, 0.0);
  private EuclideanCoord currentSpeed = new EuclideanCoord(0, 0);
  private final double RAMP_MAX = 0.02;

  private final I2C RCArduino = new I2C(Port.kOnboard, 4);

  private int driveType = 0; // 0 is Teleop, 1 is Autonomous

  @Override
  public void robotInit() {
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
  int startCooldown = 50; // Timer to force the all of the motors to 0,
                          // as there is a jump for no reason at all

  public void teleopInit() {
    currentSpeed = new EuclideanCoord(0, 0);
    System.out.print("Start!");
    startCooldown = 50;
    GPSManager.ParseGPSData((byte) 0);

    //do we just wait for the gps to work? possibly need to implement a wait function here
    Area map = new Area(); //This should have the default preset values defined in Area.java
  }

  private void TeleopDrive(double throttle, double steering) {
    if (throttle != 9223372036854775807L && steering != 9223372036854775807L) {
      TeleopMath control =  new TeleopMath(steering, throttle);
    

      robotSpeed = new EuclideanCoord(control.RcToDifferential().xEuclid, control.RcToDifferential().yEuclid);
      System.out.println(robotSpeed.toString());
      System.out.println("Teleop mode ON");

      // Ramp rate
      currentSpeed = control.CalcRamp(currentSpeed, robotSpeed, RAMP_MAX);

    }

      //Run the Motors
      m_myRobot.tankDrive(-1 * currentSpeed.yEuclid, currentSpeed.xEuclid);
  }

  public void teleopPeriodic() {
    LatLongFixStruct latLongFixStruct = GPSManager.ParseGPSData((byte) 0);
    if (latLongFixStruct != null) {
      System.out.println(latLongFixStruct.toString());
    }

    //reading from the arduino to the roborio (i2c)
    byte[] byteArr = new byte[13]; //THE LAST BYTE DOES NOT READ
    RCArduino.read(4, 13, byteArr);

    //converting the byte array into the two values of throttle and steering
    String bytes = new String(byteArr);

    // Parse I2C data from the PWM Arduino
    String steeringS = bytes.substring(0, 4);
    String throttleS = bytes.substring(4, 8);
    String killSwitchS = bytes.substring(8, 12);
    System.out.println(killSwitchS);

    double throttle =  GPSManager.ConvertToLong(throttleS);
    double steering =  GPSManager.ConvertToLong(steeringS);
    double killSwitch = GPSManager.ConvertToLong(killSwitchS);

    // Set a cooldown before starting the motors
    
    if (startCooldown < 0) {
      if (Math.abs((int) killSwitch - driveType) > 500) {
        while (currentSpeed.xEuclid != 0 && currentSpeed.yEuclid != 0) {
          TeleopDrive(1500, 1500); // 1000 - 2000, 1500 is "0"
        }
      }

      if (killSwitch > 1500) {  // Teleoperated code
        TeleopDrive(throttle, steering);
        driveType = (int) killSwitch;
      } else {  // Autonomous code
        driveType = (int) killSwitch;
        System.out.println("Autonomous mode");
      }
    }

    startCooldown -= 1;
  }

  public void autonomousInit() {

    
  }

  public void autonomousPeriodic() {

  }
}
