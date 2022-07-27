/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.*;

import java.util.*;
import java.time.*;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.I2C.Port;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.motorcontrol.MotorControllerGroup;

import java.nio.ByteBuffer;

import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.wpilibj.I2C;
import edu.wpi.first.wpilibj.I2C.*;

public class Robot extends TimedRobot {
  private DifferentialDrive m_myRobot;

  private Joystick m_leftStick;
  private Joystick m_rightStick;

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
  private EuclideanCoord oldDrive = new EuclideanCoord(0.0, 0.0);
  private double RAMP_MAX = 0.01;

  private final I2C arduino = new I2C(Port.kOnboard, 4);

  private Clock clock = Clock.systemDefaultZone();

  private DigitalInput testInput = new DigitalInput(0);

  private LatLongFixStruct gpsCoords = new LatLongFixStruct(0L, 0L, (short)0);

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
  EuclideanCoord currentSpeed = new EuclideanCoord(0, 0);
  int startCooldown = 50; // Timer to force the all of the motors to 0,
                          // as there is a jump for no reason at all

  public void teleopInit() {
    currentSpeed = new EuclideanCoord(0, 0);
    System.out.print("Start!");
    startCooldown = 50;
    GPSManager.getInstance();
    gpsCoords = GPSManager.GetDataFromGPS();

  }

  public void teleopPeriodic() {
    if (clock.millis() % 1000 == 0) {
      GPSManager.getInstance();
      gpsCoords = GPSManager.GetDataFromGPS();
    }

    //reading from the arduino to the roborio (i2c)
    byte[] byteArr = new byte[9]; //THE LAST BYTE DOES NOT READ
    arduino.read(4, 9, byteArr);
    //converting the byte array into the two values of throttle and steering
    String bytes = new String(byteArr);
    String steeringS = bytes.substring(0, 4);
    String throttleS = bytes.substring(4, 8);
    //System.out.println(steeringS + ", " + throttleS);
    double throttle =  Double.parseDouble(throttleS);
    double steering =  Double.parseDouble(steeringS);

      if (startCooldown < 0) {
      TeleopMath control =  new TeleopMath(steering, throttle);

      robotSpeed = new EuclideanCoord(control.RcToDifferential().xEuclid, control.RcToDifferential().yEuclid);
      System.out.println(robotSpeed.toString());

      currentSpeed = control.CalcRamp(currentSpeed, robotSpeed, RAMP_MAX);
    
      //System.out.println(currentSpeed.toString());
      //Run the Motors
      m_myRobot.tankDrive(-1 * currentSpeed.yEuclid, currentSpeed.xEuclid);
    }
    

    startCooldown -= 1;
  }

  public void autonomousInit() {
    //do we just wait for the gps to work? possibly need to implement a wait function here
    Area map = new Area(); //This should have the default preset values defined in Area.java
    
  }

  public void autonomousPeriodic() {

  }
}
