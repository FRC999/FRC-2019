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
  int minDist = 1000; // in mm
  int minConf = 50;

  // Joysticks
  Joystick driveStick = new Joystick(0);
  Joystick turnStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);

  // Our own special magic
  MagicJoystickInput JOYSTICKINPUT = MagicJoystickInput.getInstance();
  //MagicRobotCameras CAMERAS = new MagicRobotCameras();
  MagicVision VISION = new MagicVision(bRate); 
  ExtraUtilities UTILITY = new ExtraUtilities();
  MagicDriverPrints PRINTER = MagicDriverPrints.getInstance();

/*
  // Motor controllers for 2019 robot
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_VictorSPX driveMiddleLeft = new WPI_VictorSPX(5);
  WPI_VictorSPX driveBackLeft = new WPI_VictorSPX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_VictorSPX driveMiddleRight = new WPI_VictorSPX(2);
  WPI_VictorSPX driveBackRight = new WPI_VictorSPX(3);
  // end Motor controllers for 2019 robot
*/

  //Motor controllors for 2018 robot
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_TalonSRX driveMiddleLeft = new WPI_TalonSRX(5);
  WPI_TalonSRX driveBackLeft = new WPI_TalonSRX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_TalonSRX driveMiddleRight = new WPI_TalonSRX(2);
  WPI_TalonSRX driveBackRight = new WPI_TalonSRX(3);
  // end Motor controllers for 2018 robot

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
  } //robotInit()


  @Override
  public void robotPeriodic() {
    JOYSTICKINPUT.updates();
    //PRINTER.printMagicLine();
    //CAMERAS.checkCamSwap();

    forward = JOYSTICKINPUT.getDrive();
    turn = JOYSTICKINPUT.getTurn();
        
    intakePull = JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchIntake);
    intakePush = JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchOuttake);

    frontClimb = JOYSTICKINPUT.isButtonOn(ButtonEnum.climbFront);
    rearClimb = JOYSTICKINPUT.isButtonOn(ButtonEnum.climbBack);
    //frontClimb = turnStick.getRawButton(8);
    //rearClimb = turnStick.getRawButton(7);
    visionButton = JOYSTICKINPUT.isButtonOn(ButtonEnum.vision);
    
    elevatorUp = JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorUp);
    elevatorDown = JOYSTICKINPUT.isButtonOn(ButtonEnum.elevatorDown);
    hatchIn = JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchIntake);
    hatchOut = JOYSTICKINPUT.isButtonOn(ButtonEnum.hatchOuttake);
    cargoIn = JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoIntake);
    cargoOut = JOYSTICKINPUT.isButtonOn(ButtonEnum.cargoOuttake);
    // *** elevator presets? ***
  }


  @Override
  public void autonomousInit() {
    airCompressor.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);
    
    // *** JW inserted code for elevator PID ***
    elevatorDriver.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);  
    elevatorDriver.setNeutralMode(NeutralMode.Coast);
    elevatorDriver.config_kP(0, elevator_kP);
    elevatorDriver.config_kI(0, elevator_kI);
    elevatorDriver.config_kD(0, elevator_kD);
    elevatorDriver.config_kF(0, elevator_kF);
    //elevatorDriver.setInverted(true);
    //elevatorDriver.setSensorPhase(true);
    //elevatorDriver.setNeutralMode(NeutralMode.Coast);
    elevatorDriver.setNeutralMode(NeutralMode.Brake);

  }


  @Override
  public void autonomousPeriodic() {
    if (visionButton) {
      if (delayCounter == 0) {

        /*        
        Parameters for parseVal 
        blocksSeen = parameter 1
        xVal = parameter 2
        yVal = parameter 3
        wVal = parameter 4
        hVal = parameter 5
        lDistVal = parameter 6
        lConfVal = parameter 7
        rDistVal = parameter 8
        rConfVal = parameter 9
        arduinoCounter = parameter 10
        
        If parseVal fails it returns -1 for all parameters
        */
        test = VISION.parseVal(arduino, 2, 6, 7, 8, 9);
      }  
      if (delayCounter == 1) {
        VISION.trackWithVision(arduino, leftSide, rightSide, test[0], test[1], test[2], test[3], test[4], minDist, minConf, speed);
        //System.out.println(Arrays.toString(test));
      }
      delayCounter++;
      if (delayCounter >= timingDelay) {delayCounter = 0;}
    } else { 
        chassisDrive.arcadeDrive(forward, turn);
      int elevatorPos = elevatorDriver.getSelectedSensorPosition();
      /*
      if (elevatorUp) {
        elevatorDriver.set(ControlMode.MotionMagic, elevatorSetPoint);
      } else if (elevatorDown) {
          elevatorDriver.set(ControlMode.MotionMagic, 300);
        } else {
            elevatorDriver.set(0);
          }
      */ 
        elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
        hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(hatchIn, hatchOut, hatchCylinders)*hatchSpeed);
        cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut));
        frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
        rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));
        
    } // no vision
  } // teleopPeriodic


  @Override
  public void teleopInit() {
    airCompressor.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
    frontClimber.set(Value.kReverse);
    hatchCylinders.set(Value.kReverse);

    chassisDrive.setSafetyEnabled(false);// *** Check this ***


    // *** JW inserted code for elevator PID ***
    elevatorDriver.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);  
    elevatorDriver.setNeutralMode(NeutralMode.Coast);
    elevatorDriver.config_kP(0, elevator_kP);
    elevatorDriver.config_kI(0, elevator_kI);
    elevatorDriver.config_kD(0, elevator_kD);
    elevatorDriver.config_kF(0, elevator_kF);
    //elevatorDriver.setInverted(true);
    //elevatorDriver.setSensorPhase(true);
    //elevatorDriver.setNeutralMode(NeutralMode.Coast);
    elevatorDriver.setNeutralMode(NeutralMode.Brake);
  }


  @Override
  public void teleopPeriodic(){
    chassisDrive.feed(); // *** Check This ***
    if (visionButton) {
      if (delayCounter == 0) {

        /*        
        Parameters for parseVal 
        blocksSeen = parameter 1
        xVal = parameter 2
        yVal = parameter 3
        wVal = parameter 4
        hVal = parameter 5
        lDistVal = parameter 6
        lConfVal = parameter 7
        rDistVal = parameter 8
        rConfVal = parameter 9
        arduinoCounter = parameter 10
        
        If parseVal fails it returns -1 for all parameters
        */
        test = VISION.parseVal(arduino, 2, 6, 7, 8, 9);
      }  
      if (delayCounter == 1) {
        VISION.trackWithVision(arduino, leftSide, rightSide, test[0], test[1], test[2], test[3], test[4], minDist, minConf, speed);
        //System.out.println(Arrays.toString(test));
      }
      delayCounter++;
      if (delayCounter > timingDelay) {delayCounter = 0;}
    } else { 
        chassisDrive.arcadeDrive(forward, turn);
        int elevatorPos = elevatorDriver.getSelectedSensorPosition();
      /*
      if (elevatorUp) {
        elevatorDriver.set(ControlMode.MotionMagic, elevatorSetPoint);
      } else if (elevatorDown) {
          elevatorDriver.set(ControlMode.MotionMagic, 300);
        } else {
            elevatorDriver.set(0);
          }
      */
        elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
        hatch.set(UTILITY.TwoButtonChecker(hatchIn, hatchOut)*hatchSpeed);
        cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut));
        frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
        rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));
        
    } // no vision
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
} // Robot