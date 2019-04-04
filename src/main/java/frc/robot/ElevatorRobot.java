/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the TimedRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class ElevatorRobot extends TimedRobot {
  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  long cycles = 0;
  double forward;
  double turn;
  WPI_TalonSRX driveFL = new WPI_TalonSRX(1); // Forward left tank drive motor
  WPI_TalonSRX driveML = new WPI_TalonSRX(2); // middle left tank drive motor
  WPI_TalonSRX driveRL = new WPI_TalonSRX(3); // rear left tank drive motor
  WPI_TalonSRX driveFR = new WPI_TalonSRX(4); // Front Right tank drive motor
  WPI_TalonSRX driveMR = new WPI_TalonSRX(5); // middle right tank drive motor
  WPI_TalonSRX driveRR = new WPI_TalonSRX(6); // rear right tank drive motor
  WPI_TalonSRX elevator = new WPI_TalonSRX(8);// elevator
  // WPI_TalonSRX rotator = new WPI_TalonSRX(11);
  // WPI_TalonSRX intakeL = new WPI_TalonSRX(10);
  // WPI_TalonSRX intakeR = new WPI_TalonSRX(12);
  Joystick leftStick = new Joystick(0);
  Joystick rightStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);
  boolean elevatorUp;
  boolean elevatorDown;
  // boolean runRotator;
  // boolean runRotatorMax;
  // boolean runElevator;
  // boolean runBoth;
  // boolean intake;
  // boolean outtake;
  boolean rotateManual;
  double intakeSpeed = .5;

  // double elevatorSpeed = -0.25;
  // double rotatorSpeed = .25;
  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFL, driveML, driveRL);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFR, driveMR, driveRR);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);
  boolean zeroRotator;
  boolean zeroElevator;
  int elevatorMin = 100;
  int elevatorMax = 15000;
  int rotatorMin = 0;
  int rotatorMax = 1700;
  int rotatorSetPoint = 1300;
  int elevatorSetPoint = 15000;
  Compressor compress = new Compressor(0);
  DoubleSolenoid intakeSolenoid = new DoubleSolenoid(0, 1);

  @Override
  public void robotInit() {
    // rotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
  }

  @Override
  public void autonomousInit() {

  }

  @Override
  public void autonomousPeriodic() {
    forward = (leftStick.getRawAxis(1)) * -1;
    turn = rightStick.getRawAxis(0);
    chassisDrive.arcadeDrive(forward, turn);
  }

  @Override
  public void teleopInit() {
    intakeSolenoid.set(DoubleSolenoid.Value.kReverse); // want it to stay closed
    elevator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    elevator.setNeutralMode(NeutralMode.Coast);
    double elevator_kP = .15; // previously, .75; we needed some dampening, and we didn't want to take more
                              // time tuning the other values
    double elevator_kI = 0;
    double elevator_kD = 0;
    double elevator_kF = 0;
    elevator.config_kP(0, elevator_kP);
    elevator.config_kI(0, elevator_kI);
    elevator.config_kD(0, elevator_kD);
    elevator.config_kF(0, elevator_kF);
    elevator.setInverted(true);
    elevator.setSensorPhase(true);
    // rotator.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    elevator.setNeutralMode(NeutralMode.Coast);
    /*
     * double rotator_kP = 8; double rotator_kI = 0; double rotator_kD = 0; double
     * rotator_kF = 0; rotator.config_kP(0, rotator_kP); rotator.config_kI(0,
     * rotator_kI); rotator.config_kD(0, rotator_kD); rotator.config_kF(0,
     * rotator_kF); rotator.setInverted(true); rotator.setSensorPhase(true);
     */
  }

  @Override
  public void teleopPeriodic() {
    forward = (leftStick.getRawAxis(1)) * -1;
    turn = rightStick.getRawAxis(0);
    chassisDrive.arcadeDrive(forward, turn);
    compress.setClosedLoopControl(true);
    intakeSolenoid.set(DoubleSolenoid.Value.kReverse);
    // runRotator = leftStick.getRawButton(1);
    rotateManual = leftStick.getRawButton(4);
    elevatorUp = leftStick.getRawButton(5);
    // runRotatorMax = leftStick.getRawButton(3);
    zeroRotator = leftStick.getRawButton(11);
    zeroElevator = leftStick.getRawButton(12);
    // runBoth = leftStick.getRawButton(1);
    // intake = rightStick.getRawButton(1);
    // outtake = rightStick.getRawButton(2);
    elevatorDown = leftStick.getRawButton(3);

    // int rotatorPos = rotator.getSelectedSensorPosition();
    int elevatorPos = elevator.getSelectedSensorPosition();
    // System.out.println("Rotator: " + rotatorPos);
    // System.out.println("Elevator: " + elevatorPos);
    /*
     * if (runRotator && !runElevator) { rotator.set(ControlMode.MotionMagic,
     * rotatorSetPoint); elevator.set(0); } else if (runElevator && !runRotator) {
     * rotator.set(0); elevator.set(ControlMode.MotionMagic, elevatorSetPoint); }
     * else if (runBoth){ rotator.set(ControlMode.MotionMagic, rotatorSetPoint);
     * elevator.set(ControlMode.MotionMagic, elevatorSetPoint); } else {
     * rotator.set(0); elevator.set(0); }
     */
    if (elevatorUp) {
      elevator.set(ControlMode.MotionMagic, elevatorSetPoint);
    } else if (elevatorDown) {
      elevator.set(ControlMode.MotionMagic, 300);
    } else {
      elevator.set(0);
    }
    /*
     * if (runRotator) { rotator.set(ControlMode.MotionMagic, rotatorSetPoint); }
     * else if (rotateManual) { if (rotator.getSelectedSensorPosition() <= 1200) {
     * rotator.set(.25); } } else { rotator.set(0); } if (zeroRotator) {
     * rotator.setSelectedSensorPosition(0); }
     */
    if (zeroElevator) {
      elevator.setSelectedSensorPosition(0);
    } /*
       * if (intake && !outtake) { intakeL.set(intakeSpeed);
       * intakeR.set(-intakeSpeed); } else if (outtake && !intake) {
       * intakeL.set(-intakeSpeed); intakeR.set(intakeSpeed); } else { intakeL.set(0);
       * intakeR.set(0); }
       */
  }

  @Override
  public void testInit() { // 11.25" ground to center of rotator motor
  }

  @Override
  public void testPeriodic() {

  }
  /*
   * @Override public void testPeriodic() { //elevator position values: 0 to 15000
   * elevatorUp = leftStick.getRawButton(5); elevatorDown=
   * leftStick.getRawButton(3); zeroEnc = leftStick.getRawButton(11);
   * System.out.println(elevator.getSelectedSensorPosition()); int elevatorPos =
   * elevator.getSelectedSensorPosition(); if (elevatorPos >= elevatorMin &&
   * elevatorPos <= elevatorMax) {
   * 
   * 
   * if(elevatorUp && !elevatorDown) { elevator.set(elevatorSpeed); } else
   * if(elevatorDown && !elevatorUp) { elevator.set(-elevatorSpeed); } else {
   * elevator.set(0); } if (zeroEnc) { elevator.setSelectedSensorPosition(0); } }
   * else if (elevatorPos > elevatorMax && (elevatorUp || elevatorDown)){
   * System.out.println("ABOVE RANGE"); elevator.set(-elevatorSpeed); } else if
   * (elevatorPos < elevatorMin && (elevatorUp || elevatorDown)) {
   * System.out.println("BELOW RANGE"); elevator.set(elevatorSpeed); } else {
   * elevator.set(0); } }
   */
}