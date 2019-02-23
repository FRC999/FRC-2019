/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

/*
Drive Front Left Talon
Drive Back Left Talon
Drive Front Right Talon
Drive Back Right Talon

Intake

*/


package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.networktables.NetworkTableInstance;

import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Compressor;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends IterativeRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();
  MagicInput INPUT;  
  MagicOutput OUTPUT;

  int cycles = 0;
  double forward;
  double turn;
  static final int intakeIn = 5;//BUTTON Id
  static final int intakeOut = 5;//BUTTON
  static final double intakeVal = .5;  //rate at which the intake will spin
  static final int elevatorUp = 6; //BUTTON
  static final int elevatorDown = 6;//BUTTON
  static final double elevatorVal = .25;  //rate at which the eleator will spin
  static final double elevatorNeutral = .1; //value at which elevator will turn to get it to hld in place

  WPI_TalonSRX driveFL = new WPI_TalonSRX(1); //Forward left tank drive motor
  WPI_TalonSRX driveRL = new WPI_TalonSRX(2); //Rear left tank drive motor
  WPI_TalonSRX driveFR = new WPI_TalonSRX(3); //Forward Right tank drive motor
  WPI_TalonSRX driveRR = new WPI_TalonSRX(4); //Rear Right left tank drive motor
/*
  
  WPI_TalonSRX testLeft = new WPI_TalonSRX(10);
  WPI_TalonSRX testRight = new WPI_TalonSRX(11);

  WPI_TalonSRX testElevator = new WPI_TalonSRX(12);
*/
  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFL, driveRL);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFR, driveRR);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);
  
  int pneumaticInButton = 1;//BUTTON
  int compressorPort = 0;
  //Compressor testCompressor = new Compressor(compressorPort);
  //Solenoid solenoid1 = new Solenoid(0);
  //Solenoid solenoid2 = new Solenoid(1);

   

  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);

    INPUT = new MagicInput();
    OUTPUT = new MagicOutput(INPUT);
  }

  /**
   * This function is called every robot packet, no matter the mode. Use
   * this for items like diagnostics that you want ran during disabled,
   * autonomous, teleoperated and test.
   *
   * <p>This runs after the mode specific periodic functions, but before
   * LiveWindow and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    INPUT.updates(); //Update the toggling 
    
    OUTPUT.checkCamSwap();
  }
  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable
   * chooser code works with the Java SmartDashboard. If you prefer the
   * LabVIEW Dashboard, remove all of the chooser code and uncomment the
   * getString line to get the auto name from the text box below the Gyro
   *
   * <p>You can add additional auto modes by adding additional comparisons to
   * the switch structure below with additional strings. If using the
   * SendableChooser make sure to add them to the chooser code above as well.
   */

  @Override
  public void autonomousInit() {
    m_autoSelected = m_chooser.getSelected();
     //autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
      case kCustomAuto:
        // Put custom auto code here
        break;
      case kDefaultAuto:
      default:
        // Put default auto code here
        break;
    }
  }
 
  @Override
  public void teleopInit() {
     

  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    //Drive code: Jack says that's all I need
    chassisDrive.arcadeDrive(INPUT.getDrive(), INPUT.getTurn());

    
  }


  int testItCh1;
  int testItCh2;
  int testItCh0;
  int testItCh3;
  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
//chassisDrive.arcadeDrive(0.4, 0, false);
leftSide.set(.4);
rightSide.set(-.4);
    //test current draw
 // System.out.println("Input voltage = " + PDPJNI.getPDPVoltage(m_handle));
  System.out.println("total current of all monitored PDP channels = " + PDPJNI.getPDPTotalCurrent(m_handle));
  //System.out.println("total energy in J of monitored channels = " + PDPJNI.getPDPTotalEnergy(m_handle));
  System.out.println(
    "current of: \nch.0 = " + PDPJNI.getPDPChannelCurrent((byte) 0,  m_handle)
   //+ ",\nch. 1 = " +PDPJNI.getPDPChannelCurrent((byte) 1,  m_handle)
   // + ",\nch. 2 = " + PDPJNI.getPDPChannelCurrent((byte) 2,  m_handle) + 
  +  ", \nch. 3 = " + PDPJNI.getPDPChannelCurrent((byte) 3,  m_handle) );
//System.out.println("total power(W):" + pdp.getTotalPower());
if (PDPJNI.getPDPChannelCurrent((byte) 0,  m_handle) != 0.0) {testItCh0++;}
if (PDPJNI.getPDPChannelCurrent((byte) 1,  m_handle) != 0.0) {testItCh1++;}
if (PDPJNI.getPDPChannelCurrent((byte) 2,  m_handle) != 0.0) {testItCh2++;}
if (PDPJNI.getPDPChannelCurrent((byte) 3,  m_handle) != 0.0) {testItCh3++;
System.out.println("channel 3 has run for " + testItCh3 + " iterations");}
}
@Override
public void disabledInit()
  {System.out.println(
"Pdp channel 0 ran " + testItCh0 + "iterations before zeroing\n" +
"Pdp channel 1 ran " + testItCh1 + "iterations before zeroing\n" +
"Pdp channel 2 ran " + testItCh2 + "iterations before zeroing\n" +
"Pdp channel 3 ran " + testItCh3 + "iterations before zeroing\n"
);
}
}