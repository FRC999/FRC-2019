/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  Joystick driveStick = new Joystick(0);
  Joystick turnStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);
  boolean LEDOn;
  boolean intakePush;
  boolean intakePull;
  boolean smallClimberUp;
  boolean smallClimberDown;
  boolean MOACUp;
  boolean MOACDown;
  boolean syringePull;
  boolean syringePush;
  boolean visionButton;

  int xVal;
  int yVal;
  int hVal;
  int wVal;
  int distVal;
  int confVal;
  int blocksSeen;
  int pixyMountX;
  int pixyMountY;
  int pixyMountAngle;
  int arduinoCounter;
  int bRate = 115200;
  SerialPort ard;
  String targetPosition;
  int startOfDataStream;
  int endOfDataStream;// looking for the first carriage return
  // The indexOf method returns -1 if it can't find the char in the string
  

/*
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_VictorSPX driveMiddleLeft = new WPI_VictorSPX(5);
  WPI_VictorSPX driveBackLeft = new WPI_VictorSPX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_VictorSPX driveMiddleRight = new WPI_VictorSPX(2);
  WPI_VictorSPX driveBackRight = new WPI_VictorSPX(3);
*/
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_TalonSRX driveMiddleLeft = new WPI_TalonSRX(5);
  WPI_TalonSRX driveBackLeft = new WPI_TalonSRX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_TalonSRX driveMiddleRight = new WPI_TalonSRX(2);
  WPI_TalonSRX driveBackRight = new WPI_TalonSRX(3);

  WPI_TalonSRX elevatorDriver = new WPI_TalonSRX(9);

  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFrontLeft, driveMiddleLeft, driveBackLeft);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFrontRight, driveMiddleRight, driveBackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);

  DoubleSolenoid MOAC = new DoubleSolenoid(0, 7);
  DoubleSolenoid lowClimber = new DoubleSolenoid(1, 6);
  DoubleSolenoid intake = new DoubleSolenoid(2, 5);
  DoubleSolenoid syringe = new DoubleSolenoid(3, 4);

  MagicVision V = new MagicVision(115200);

  Compressor comp = new Compressor(0);
  double forward;
  double turn;
  @Override
  public void robotInit() {
    ard = V.startArduino();
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kOff);
    lowClimber.set(Value.kOff);
    intake.set(Value.kOff);
    syringe.set(Value.kOff); 
  } //robotInit()

  @Override
  public void robotPeriodic() {
    intakePull = driveStick.getRawButton(3);
    intakePush = driveStick.getRawButton(5);
    syringePull = driveStick.getRawButton(4);
    syringePush = driveStick.getRawButton(6);
    smallClimberUp = driveStick.getRawButton(9);
    smallClimberDown = driveStick.getRawButton(10);
    visionButton = driveStick.getRawButton(2);
    MOACUp = driveStick.getRawButton(11);
    MOACDown = driveStick.getRawButton(12);
    forward = (driveStick.getRawAxis(1));
    turn = turnStick.getRawAxis(0);
  }
  @Override
  public void autonomousInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
  }
  @Override
  public void autonomousPeriodic() {
    if (visionButton) {
      V.getArray(ard);
      if (V.getDist() >= V.getStopDist()) {
     if (V.isOnLeft(V.parseVal(2,0,ard))) {
      System.out.println("left");
       leftSide.set(0);
       rightSide.set(.2);
      } else if (V.isInMiddle(V.parseVal(2,0,ard))) {
        System.out.println("middle");
        leftSide.set(-.2);
        rightSide.set(.2);
      } else if (V.isOnRight(V.parseVal(2,0,ard))) {
        System.out.println("right");
        leftSide.set(.2);
        rightSide.set(0);
      } else {
        leftSide.set(0);
        rightSide.set(0);
      }
    } //DISTANCE CHECKER END
    } else {
      chassisDrive.arcadeDrive(forward, turn);
      if (intakePull && !intakePush) {
        intake.set(Value.kReverse);
      } else if (intakePush && !intakePull) {
        intake.set(Value.kForward);
      } else {
        intake.set(Value.kOff);
      }
      if (syringePull && !syringePush) {
        syringe.set(Value.kReverse);
      } else if (syringePush && !syringePull) {
        syringe.set(Value.kForward);
      } else {
        syringe.set(Value.kOff);
      }
      if (MOACUp && !MOACDown) {
        MOAC.set(Value.kReverse);
      } else if (MOACDown && !MOACUp) {
        MOAC.set(Value.kForward);
      } else {
        MOAC.set(Value.kOff);
      }
      if (smallClimberUp && !smallClimberDown) {
        lowClimber.set(Value.kForward);
      } else if (smallClimberDown && !smallClimberUp) {
        lowClimber.set(Value.kReverse);
      } else {
        lowClimber.set(Value.kOff);
      }
    }
  }
  @Override
  public void teleopInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
  }
  @Override
  public void teleopPeriodic() {
    if (visionButton) {
      System.out.println(V.parseVal(2,0,ard));
      if (V.getBlocksSeen() > -1) {
      //if (V.getDist() >= V.getStopDist()) {
     if (V.isOnLeft(V.parseVal(2,0,ard))) {
       leftSide.set(0);
       rightSide.set(.2);
      } else if (V.isInMiddle(V.parseVal(2,0,ard))) {
        leftSide.set(-.2);
        rightSide.set(.2);
      } else if (V.isOnRight(V.parseVal(2,0,ard))) {
        leftSide.set(.2);
        rightSide.set(0);
      } else {
        leftSide.set(0);
        rightSide.set(0);
      }
    }
    //} //DISTANCE CHECKER END
    } else {
      chassisDrive.arcadeDrive(forward, turn);
      if (intakePull && !intakePush) {
        intake.set(Value.kReverse);
      } else if (intakePush && !intakePull) {
        intake.set(Value.kForward);
      } else {
        intake.set(Value.kOff);
      }
      if (syringePull && !syringePush) {
        syringe.set(Value.kReverse);
      } else if (syringePush && !syringePull) {
        syringe.set(Value.kForward);
      } else {
        syringe.set(Value.kOff);
      }
      if (MOACUp && !MOACDown) {
        MOAC.set(Value.kReverse);
      } else if (MOACDown && !MOACUp) {
        MOAC.set(Value.kForward);
      } else {
        MOAC.set(Value.kOff);
      }
      if (smallClimberUp && !smallClimberDown) {
        lowClimber.set(Value.kForward);
      } else if (smallClimberDown && !smallClimberUp) {
        lowClimber.set(Value.kReverse);
      } else {
        lowClimber.set(Value.kOff);
      } // lowClimber
    } // no vision
      } // teleopPeriodic
    } // Robot