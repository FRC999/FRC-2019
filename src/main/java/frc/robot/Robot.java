/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

//CHECK SOLENOID ID's BEFORE USE!!!
package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
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
  boolean frontClimberUp;
  boolean frontClimberDown;
  boolean frontClimb;
  boolean rearClimb;
  boolean visionButton;
  boolean cargoIn;
  boolean cargoOut;
  boolean hatchIn;
  boolean hatchOut;

  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();
  MagicDriverPrints PRINTER = MagicDriverPrints.getInstance();
  MagicRobotCameras CAMERAS = new MagicRobotCameras();


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
  SerialPort arduino;
  String targetPosition;
  int startOfDataStream;
  int endOfDataStream;// looking for the first carriage return
  double speed = .25;
  boolean elevatorUp;
  boolean elevatorDown;
  boolean elevatorLowHatch;
  boolean elevatorMiddleHatch;
  boolean elevatorHighHatch;
  boolean frontClimberToggle;
  boolean backClimberToggle;
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

  WPI_VictorSPX cargo = new WPI_VictorSPX(13);
  WPI_VictorSPX hatch = new WPI_VictorSPX(14);
  int elevatorSetPoint = 5000;
  int elevatorMin = 100;
  int elevatorMax = 15000;
  double elevatorSpeed = .25;
  WPI_TalonSRX elevatorDriver = new WPI_TalonSRX(9);
  
  double cargoSpeed = .25;
  double hatchSpeed = .25;

  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFrontLeft, driveMiddleLeft, driveBackLeft);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFrontRight, driveMiddleRight, driveBackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);

  DoubleSolenoid frontClimber = new DoubleSolenoid(0, 7);
  DoubleSolenoid rearClimber = new DoubleSolenoid(1, 6);
  DoubleSolenoid intake = new DoubleSolenoid(2, 5);

  Compressor comp = new Compressor(0);
  double forward;
  double turn;
  MagicVision VISION = new MagicVision();
  ExtraUtilities UTILITY = new ExtraUtilities();
  @Override
  public void robotInit() {
    comp.setClosedLoopControl(true);
    rearClimber.set(Value.kOff);
    frontClimber.set(Value.kOff);
    intake.set(Value.kOff);
    try {
      arduino = new SerialPort(bRate, SerialPort.Port.kUSB);
      System.out.println("Connected to kUSB");
    } 
    catch (Exception e) {
	  System.out.println("Couldn't connect to kUSB, trying kUSB1");
      try {
        arduino = new SerialPort(bRate, SerialPort.Port.kUSB1);
        System.out.println("Connected to kUSB1");
      }
      catch (Exception e1){
        System.out.println("Couldn't Connect to kUSB1, trying kUSB2");
        try {
          arduino = new SerialPort(bRate, SerialPort.Port.kUSB2);
          System.out.println("Connected to kUSB2");
        }
        catch (Exception e2) {
          System.out.println("Not connected to any of the USB ports, trying MXP spot");
          try {
            arduino = new SerialPort(bRate, SerialPort.Port.kMXP);
            System.out.println("Connected to MXP port");
          }
          catch (Exception eMXP) {
            System.out.println("Not Connected to MXP port, trying Onboard");
            try {
              arduino = new SerialPort(bRate, SerialPort.Port.kOnboard);
              System.out.println("Connected to Onboard");  
            }
            catch (Exception eOnboard){
              System.out.println("Not connected to any ports on the RoboRIO");
  
            }  //catch (Exception eOnboard)
          } // catch (Exception eMXP)
        }  //catch (Exception e2)
      }  //catch (Exception e1)
    }  //catch (Exception e) 
  } //robotInit()

  @Override
  public void robotPeriodic() {
    INPUT.updates();
    PRINTER.printMagicLine();
    CAMERAS.checkCamSwap();

    forward = INPUT.getDrive();
    turn = INPUT.getTurn();
        
    intakePull = INPUT.isButtonOn(ButtonEnum.hatchIntake);
    intakePush = INPUT.isButtonOn(ButtonEnum.hatchOuttake);
    frontClimb = INPUT.isButtonOn(ButtonEnum.climbFront);
    visionButton = INPUT.isButtonOn(ButtonEnum.vision);
    
    elevatorUp = INPUT.isButtonOn(ButtonEnum.elevatorUp);
    elevatorDown = INPUT.isButtonOn(ButtonEnum.elevatorDown);
  }
  @Override
  public void autonomousInit() {
    comp.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
  }
  @Override
  public void autonomousPeriodic() {
    if (visionButton) {
      int x = VISION.parseVal(arduino, 2);
      VISION.track(leftSide, rightSide, x);
      } else { 
        chassisDrive.arcadeDrive(forward, turn);
      int elevatorPos = elevatorDriver.getSelectedSensorPosition();
      System.out.println(elevatorPos);
      /*if (elevatorUp) {
        elevatorDriver.set(ControlMode.MotionMagic, elevatorSetPoint);
      } else if(elevatorDown) {
        elevatorDriver.set(ControlMode.MotionMagic, 300);
      } else {
        elevatorDriver.set(0);
      }
      */
        elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
        hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(hatchIn, hatchOut, intake)*hatchSpeed);
        cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut)*cargoSpeed);
        frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
        rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));
        
        //rearClimber.set(U.TwoButtonCheckerPneumatics(rearClimberUp, rearClimberDown));
        //frontClimber.set((U.TwoButtonCheckerPneumatics(frontClimberUp, frontClimberDown)));
    } // no vision
      } // teleopPeriodic
  @Override
  public void teleopInit() {
    comp.setClosedLoopControl(true);
    rearClimber.set(Value.kReverse);
    chassisDrive.setSafetyEnabled(false);
  }
  @Override
  public void teleopPeriodic() {
    if (visionButton) {
      int x = VISION.parseVal(arduino, 2);
      VISION.track(leftSide, rightSide, x);
      } else { 
        chassisDrive.arcadeDrive(forward, turn);
      int elevatorPos = elevatorDriver.getSelectedSensorPosition();
      System.out.println(elevatorPos);
      /*if (elevatorUp) {
        elevatorDriver.set(ControlMode.MotionMagic, elevatorSetPoint);
      } else if(elevatorDown) {
        elevatorDriver.set(ControlMode.MotionMagic, 300);
      } else {
        elevatorDriver.set(0);
      }
      */
        elevatorDriver.set(UTILITY.TwoButtonChecker(elevatorUp, elevatorDown)*elevatorSpeed);
        hatch.set(UTILITY.twoButtonCheckerWithConstantSolenoid(hatchIn, hatchOut, intake)*hatchSpeed);
        cargo.set(UTILITY.TwoButtonChecker(cargoIn, cargoOut)*cargoSpeed);
        frontClimber.set(UTILITY.SingleButtonCheckerPneumatics(frontClimb));
        rearClimber.set(UTILITY.SingleButtonCheckerPneumatics(rearClimb));

        //rearClimber.set(U.TwoButtonCheckerPneumatics(rearClimberUp, rearClimberDown));
        //frontClimber.set((U.TwoButtonCheckerPneumatics(frontClimberUp, frontClimberDown)));
    } // no vision
      } // teleopPeriodic
    } // Robot