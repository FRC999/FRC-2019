/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

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
  SerialPort arduino;
  String targetPosition;
  int startOfDataStream;
  int endOfDataStream;// looking for the first carriage return
  double speed = .25;
  boolean elevatorUp;
  boolean elevatorDown;
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

  int elevatorSetPoint = 5000;
  int elevatorMin = 100;
  int elevatorMax = 15000;

  WPI_TalonSRX elevatorDriver = new WPI_TalonSRX(9);

  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFrontLeft, driveMiddleLeft, driveBackLeft);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFrontRight, driveMiddleRight, driveBackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);

  DoubleSolenoid MOAC = new DoubleSolenoid(0, 7);
  DoubleSolenoid lowClimber = new DoubleSolenoid(1, 6);
  DoubleSolenoid intake = new DoubleSolenoid(2, 5);
  DoubleSolenoid syringe = new DoubleSolenoid(3, 4);

  Compressor comp = new Compressor(0);
  double forward;
  double turn;
  MagicVision V = new MagicVision();
  ExtraUtilities U = new ExtraUtilities();
  @Override
  public void robotInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kOff);
    lowClimber.set(Value.kOff);
    intake.set(Value.kOff);
    syringe.set(Value.kOff);
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
    elevatorUp = driveStick.getRawButton(8);
    elevatorDown = driveStick.getRawButton(7);
  }
  @Override
  public void autonomousInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
  }
  @Override
  public void autonomousPeriodic() {
    
    //*** VISION ***
    targetPosition = arduino.readString();
    System.out.println(arduino.readString()); 
      System.out.println("String TargetPosition = " + targetPosition);
      var positions = targetPosition.split(";");
       for (int i = 0; i < positions.length; i++) {
      //  System.out.println("String TargetPosition = " + targetPosition);
         var positionNums = positions[i].split(":");
         if (positionNums[0] == "x") {
           xVal = Integer.parseInt(positionNums[1]);
           System.out.println("xval = " + xVal);
         } else if (positionNums[0] == "y") {
           yVal = Integer.parseInt(positionNums[1]);
           System.out.println("yval =" + yVal);
         } else if (positionNums[0] == "h") {
           hVal = Integer.parseInt(positionNums[1]);
           System.out.println("hval =" + hVal);
         } else if (positionNums[0] == "w") {
           wVal = Integer.parseInt(positionNums[1]);
           System.out.println("wval =" + wVal);
         } else if (positionNums[0] == "dist") {
           distVal = Integer.parseInt(positionNums[1]);
           System.out.println("distval =" + distVal);
          } else if (positionNums[0] == "conf") {
            distVal = Integer.parseInt(positionNums[1]);
            System.out.println("confval =" + confVal);
         } else {
           System.out.println("Parsing sensor data failed.");
        //   System.out.println("positionNums[0] = " + positionNums[0]);
        //   System.out.println("positionNums[1] = " + positionNums[1]);
         }
        } 
if (visionButton) {
    System.out.println(targetPosition);
  if (targetPosition == null) {
      leftSide.set(0);
      rightSide.set(0);
  //    System.out.println("targetPosition = null");
    } else if (xVal < 158 /* && distVal > 500 */) {
      leftSide.set(0);
      rightSide.set(speed);
 //     System.out.println("xVal < (316/2) && distVal > 500");
    } else if (xVal == 158 /*&& distVal > 500 */) {
     leftSide.set(0);
     rightSide.set(speed);
     //System.out.println("xVal == (316/2) && distVal > 500");
    } else if (xVal > 158 /*&& distVal > 500 */) {
     leftSide.set(0);
     rightSide.set(.2);
     //System.out.println("xVal > (316/2) && distVal > 500");
    } else {
     System.out.println("none of the if statements in auto periodic applied, distval probably <500");
     leftSide.set(0);
     rightSide.set(0);
    }
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
    chassisDrive.setSafetyEnabled(false);
  }
  @Override
  public void teleopPeriodic() {
    if (visionButton) {
      int x = V.parseVal(arduino, 2);
      V.track(leftSide, rightSide, x);
      } else { 
        chassisDrive.arcadeDrive(forward, turn);
      int elevatorPos = elevatorDriver.getSelectedSensorPosition();
      System.out.println(elevatorPos);
      if (elevatorUp) {
        elevatorDriver.set(ControlMode.MotionMagic, elevatorSetPoint);
      } else if(elevatorDown) {
        elevatorDriver.set(ControlMode.MotionMagic, 300);
      } else {
        elevatorDriver.set(0);
      }
        intake.set(U.TwoButtonCheckerPneumatics(intakePush, intakePull));
        syringe.set(U.TwoButtonCheckerPneumatics(syringePush, syringePull));
        MOAC.set(U.TwoButtonCheckerPneumatics(MOACUp, MOACDown));
        lowClimber.set((U.TwoButtonCheckerPneumatics(smallClimberUp, smallClimberDown)));
    } // no vision
      } // teleopPeriodic

      
    } // Robot
