/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//CHECK SOLENOID ID's BEFORE USE!!!
package frc.robot;

import java.util.Arrays;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice; // *** JW added ***
import com.ctre.phoenix.motorcontrol.NeutralMode; // *** JW added ***

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.Watchdog; // *** Check This ***
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
  boolean LEDOn;
  boolean intakePush;
  boolean intakePull;
  boolean frontClimberUp;
  boolean frontClimberDown;
  boolean frontClimb;
  boolean rearClimb;
  boolean visionButton;
  boolean cargoIn;
  boolean cargoOut;
  boolean hatchIn;
  boolean hatchOut;

  int [] test;
  int delayCounter = 0;
  int timingDelay = 5;
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
 // int bRate = 9600;
  SerialPort arduino;
  String targetPosition;
  int startOfDataStream;
  int endOfDataStream;// looking for the first carriage return
  boolean elevatorUp;
  boolean elevatorDown;
  boolean elevatorLowHatch;
  boolean elevatorMiddleHatch;
  boolean elevatorHighHatch;
  boolean frontClimberToggle;
  boolean backClimberToggle;

  int x;
  int lDist;
  int lConf;
  int rDist;
  int rConf;
  double speed = .25;
  int minDist = 60; // in mm
  int minConf = 50;

  // Joysticks
  Joystick driveStick = new Joystick(0);
  Joystick turnStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);

  // Our own special magic
  MagicJoystickInput JOYSTICKINPUT = MagicJoystickInput.getInstance();
  MagicRobotCameras CAMERAS = new MagicRobotCameras();
  MagicVision VISION = new MagicVision(bRate); 
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
  int elevatorSetPoint = 5000;
  int elevatorMin = 100;
  int elevatorMax = 15000;
  double elevator_kP = .001; // Start at .001, guessing it will be around .1 - .15
  double elevator_kI = 0;
  double elevator_kD = 0;
  double elevator_kF = 0;

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
    arduino = VISION.startArduino(bRate);
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
    elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
    chassisDrive.arcadeDrive(forward, turn);
    hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(hatchIn, hatchOut, hatchCylinders)*hatchSpeed);
    cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut));
    frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
    rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));
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
    chassisDrive.feed(); // *** Check This ***
    chassisDrive.arcadeDrive(forward, turn);
    elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
    hatch.set(UTILITY.TwoButtonChecker(hatchIn, hatchOut)*hatchSpeed);
    cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut));
    frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
    rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));
  } // teleopPeriodic

  public void testInit(){
    ButtonListMaker but = new ButtonListMaker();
    but.buildStrings();

    System.out.println("Buttons:");
    System.out.println(but.joystick0String);
    System.out.println(but.joystick1String);
    System.out.println(but.joystick1String);

    elevatorDriver.setNeutralMode(NeutralMode.Coast);
  }
  public void testPeriodic(){
    if (JOYSTICKINPUT.isButtonOn(ButtonEnum.tunePidValUp)){
      elevator_kP += .001;
      elevatorDriver.config_kP(0,elevator_kP);
      System.out.println(elevator_kP);
    }
    if (JOYSTICKINPUT.isButtonOn(ButtonEnum.tunePidValDown)){
      elevator_kP += .001;
      elevatorDriver.config_kP(0,elevator_kP);
      System.out.println(elevator_kP);
    }
    if (JOYSTICKINPUT.isButtonOn(ButtonEnum.moveElevator)){
      elevatorDriver.set(ControlMode.MotionMagic, 4096*10 );
    }
  }
  public void disabledInit(){
    elevatorDriver.setNeutralMode(NeutralMode.Brake);
    elevatorDriver.neutralOutput();
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);
  }
} // Robot