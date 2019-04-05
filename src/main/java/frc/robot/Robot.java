/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//CHECK SOLENOID ID's BEFORE USE!!!
package frc.robot;


import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.NeutralMode; // *** JW added ***

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
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
  double speed = .25;

  // Our own special magic
  MagicJoystickInput JOYSTICKINPUT = MagicJoystickInput.getInstance();
  MagicRobotCameras CAMERAS = new MagicRobotCameras();
  ExtraUtilities UTILITY = new ExtraUtilities();
  MagicDriverPrints PRINTER = MagicDriverPrints.getInstance();

  // Motor controllers for 2019 robot
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_VictorSPX driveMiddleLeft = new WPI_VictorSPX(5);
  WPI_VictorSPX driveBackLeft = new WPI_VictorSPX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_VictorSPX driveMiddleRight = new WPI_VictorSPX(2);
  WPI_VictorSPX driveBackRight = new WPI_VictorSPX(3);


  // Drivetrain
  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFrontLeft, driveMiddleLeft, driveBackLeft);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFrontRight, driveMiddleRight, driveBackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);
  double forward;
  double turn;

  // Elevator
  WPI_TalonSRX elevatorDriver = new WPI_TalonSRX(9);
  double elevatorSpeed = .25;

  // Cargo
  WPI_VictorSPX cargo = new WPI_VictorSPX(13);
  double cargoSpeed = .25;

  //Hatch
  WPI_VictorSPX hatch = new WPI_VictorSPX(14);
  double hatchSpeed = .25;

  //Pneumatics
  Compressor airCompressor = new Compressor(0);
  DoubleSolenoid rearClimber = new DoubleSolenoid(0, 7);
  DoubleSolenoid frontClimber = new DoubleSolenoid(1, 6);
  DoubleSolenoid hatchCylinders = new DoubleSolenoid(2, 5);


  @Override
  public void robotInit() {
    airCompressor.setClosedLoopControl(true);
    rearClimber.set(Value.kOff);
    frontClimber.set(Value.kOff);
    hatchCylinders.set(Value.kOff);
    elevatorDriver.setNeutralMode(NeutralMode.Brake);
    elevatorDriver.neutralOutput();
  } //robotInit()

  @Override
  public void robotPeriodic() {
    JOYSTICKINPUT.updates();
    PRINTER.printMagicLine();
    CAMERAS.checkCamSwap();

    forward = JOYSTICKINPUT.getDrive();
    turn = JOYSTICKINPUT.getTurn();

  }


  @Override
  public void autonomousInit() {
    airCompressor.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);

  }


  @Override
  public void autonomousPeriodic() {
    elevatorDriver.set(UTILITY.TwoButtonChecker(JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorUp), JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorDown))*elevatorSpeed);
    chassisDrive.arcadeDrive(forward, turn);
    hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchIntake), JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchOuttake), hatchCylinders)*hatchSpeed);
    cargo.set(UTILITY.TwoButtonChecker(JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoIntake), JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoOuttake)));
    frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(JOYSTICKINPUT.isButtonOn(ButtonEnum.climbFront)));
    rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(JOYSTICKINPUT.isButtonOn(ButtonEnum.climbBack)));
  } // teleopPeriodic


  @Override
  public void teleopInit() {
    airCompressor.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);
  }


  @Override
  public void teleopPeriodic(){
    chassisDrive.arcadeDrive(forward, turn);
    elevatorDriver.set(UTILITY.TwoButtonChecker(JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorUp), JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorDown))*elevatorSpeed);
    hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchIntake), JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchOuttake), hatchCylinders)*hatchSpeed);
    cargo.set(UTILITY.TwoButtonChecker(JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoIntake), JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoOuttake)));
    frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(JOYSTICKINPUT.isButtonOn(ButtonEnum.climbFront)));
    rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(JOYSTICKINPUT.isButtonOn(ButtonEnum.climbBack)));
  } // teleopPeriodic

  public void testInit(){
  }
  public void testPeriodic(){}
  public void disabledInit(){
    elevatorDriver.setNeutralMode(NeutralMode.Brake);
    elevatorDriver.neutralOutput();
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);
  }
} // Robot