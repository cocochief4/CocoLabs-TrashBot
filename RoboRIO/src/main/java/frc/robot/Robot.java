/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import frc.robot.*;

import java.util.*;

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

  private static final int leftUpDeviceID = 1; 
  private static final int leftDownDeviceID = 2;
  private CANSparkMax leftUpMotor;
  private CANSparkMax leftDownMotor;
  private MotorControllerGroup leftGroup;

  private static final int rightUpDeviceID = 3;
  private static final int rightDownDeviceID = 4;
  private CANSparkMax rightUpMotor;
  private CANSparkMax rightDownMotor;
  private MotorControllerGroup rightGroup;

  private final I2C arduino = new I2C(Port.kOnboard, 4);


  private DigitalInput testInput = new DigitalInput(0);

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
  public void teleopInit() {
    
  }

  public void teleopPeriodic() {
    //reading from the arduino to the roborio (i2c)
    byte[] byteArr = new byte[9]; //THE LAST BYTE DOES NOT READ
    arduino.read(4, 9, byteArr);
    //converting the byte array into the two values of throttle and steering
    String bytes = new String(byteArr);
    String throttleS = bytes.substring(0, 4);
    String steeringS = bytes.substring(4, 8);
    //System.out.println(steeringS + ", " + throttleS);
    double throttle =  Double.parseDouble(throttleS);
    double steering =  Double.parseDouble(steeringS);

    TeleopMath control =  new TeleopMath(throttle, steering);
    System.out.println(control.RcToDifferential().toString());
  
    //Run the Motors
    m_myRobot.tankDrive(control.RcToDifferential().xEuclid, control.RcToDifferential().yEuclid);
  }
}
