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

  boolean intakePush;
  boolean intakePull;
  boolean smallClimberUp;
  boolean smallClimberDown;
  boolean MOACUp;
  boolean MOACDown;
  boolean syringePull;
  boolean syringePush;

  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(4);
  WPI_VictorSPX driveMiddleLeft = new WPI_VictorSPX(5);
  WPI_VictorSPX driveBackLeft = new WPI_VictorSPX(6);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(1);
  WPI_VictorSPX driveMiddleRight = new WPI_VictorSPX(2);
  WPI_VictorSPX driveBackRight = new WPI_VictorSPX(3);

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
  @Override
  public void robotInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kOff);
    lowClimber.set(Value.kOff);
    intake.set(Value.kOff);
    syringe.set(Value.kOff);
    
    int bRate = 115200;
    SerialPort arduino;
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
    MOACUp = driveStick.getRawButton(11);
    MOACDown = driveStick.getRawButton(12);
    forward = (driveStick.getRawAxis(1))*-1;
    turn = driveStick.getRawAxis(0);
  }
  @Override
  public void autonomousInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
  }
  @Override
  public void autonomousPeriodic() {
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
  @Override
  public void teleopInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
  }
  @Override
  public void teleopPeriodic() {
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
  @Override
  public void testInit() {
    comp.setClosedLoopControl(true);
    MOAC.set(Value.kReverse);
    lowClimber.set(Value.kReverse);
    intake.set(Value.kReverse);
    syringe.set(Value.kReverse);
  }
  @Override
  public void testPeriodic() {
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