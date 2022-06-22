/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

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

      m_leftStick = new Joystick(1);
      m_rightStick = new Joystick(5);
  }
  (
  public void teleopInit() {
    
  }

  public void teleopPeriodic() {
    double throttle = (double) 0;
    double steering = (double) 0;
    //Converting from the original values Arduino sends over to -1 to 1 scale
    if (throttle <= 1460 || throttle >= 1540) {
      throttle /= 500;
      throttle -= 3;
    }
    else {
      throttle = 0;
    }
    if (steering <= 1460 || throttle >= 1540) {
      steering /= 500;
      steering -= 3;
    }
    else {
      steering = 0;
    }
    //Converting to polar using (r, theta) | theta is in radians
    double hypoteneuse = Math.sqrt((throttle*throttle) + (steering*steering));
    double theta = Math.atan(steering/throttle);
    //Finding the max length r on the unit circle for a specific angle
    double rMax;
    if (theta <= 45) {
      rMax = (1 / Math.cos(theta));
    }
    else if (theta <= 90 && theta > 45) {
      rMax = (1 / Math.sin(theta));
    }
    //you can combine these two if statements into 1 as sin will be positive in both of these regions | rememeber to do it later 
    else if (theta >= 90 && theta < 135) {
      rMax = (1 / Math.sin(theta));
    }
    else if (theta >= 135 && theta < 180 ) {
      rMax = (-1 / Math.cos(theta));
    }
    else if (theta >= 180 && theta < 225) {
      rMax = (-1 / Math.cos(theta));
    }
    else if (theta >= 225 && theta < 270) {
      rMax = (-1 / Math.sin(theta));
    }
    else if (theta >= 270 && theta < 315) {
      rMax = (-1 / Math.sin(theta));
    }
    else { //case where theta <=360 yet >=315
      rMax = (1 / Math.cos(theta))
    }
    //now scaling down when we scale to unit circle 
    r *= (1/rMax);
    //rotating 45 degrees (bruh)
    theta = Math.ToDegrees(theta);
    theta -= 45; //talk with chris about this i'm hella confused on how we rotate (i just converted to radians and then back)
    theta = Math.ToRadians(theta);
    //recalculation rMax
    double rMax;
    if (theta <= 45) {
      rMax = (1 / Math.cos(theta));
    }
    else if (theta <= 90 && theta > 45) {
      rMax = (1 / Math.sin(theta));
    }
    //you can combine these two if statements into 1 as sin will be positive in both of these regions | rememeber to do it later 
    else if (theta >= 90 && theta < 135) {
      rMax = (1 / Math.sin(theta));
    }
    else if (theta >= 135 && theta < 180 ) {
      rMax = (-1 / Math.cos(theta));
    }
    else if (theta >= 180 && theta < 225) {
      rMax = (-1 / Math.cos(theta));
    }
    else if (theta >= 225 && theta < 270) {
      rMax = (-1 / Math.sin(theta));
    }
    else if (theta >= 270 && theta < 315) {
      rMax = (-1 / Math.sin(theta));
    }
    else { //case where theta <=360 yet >=315
      rMax = (1 / Math.cos(theta))
    }
    //scaling up again
    r *= rMax;
    double left;
    double right;
    left = r * Math.cos(theta);
    right = r * Math.sin(theta);
    // m_myRobot.tankDrive(.5, .5);
    // System.out.println(testInput.get());
    // System.out.println(leftUpMotor.get());
  }
}