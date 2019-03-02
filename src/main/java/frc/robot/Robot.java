/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                                */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.TimedRobot;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the build.gradle file in the
 * project.
 */
public class Robot extends TimedRobot {
  private static final String kDefaultAuto = "Default";
  private static final String kCustomAuto = "My Auto";
  private String m_autoSelected;
  private final SendableChooser<String> m_chooser = new SendableChooser<>();

  MagicElevator ELEVATOR;
  MagicInput INPUT;  
  MagicVision VISION = new MagicVision(115200, 200, 1, 300);
  MagicOutput OUTPUT;
  MagicPneumatics PNEUMATICS;

  long cycles = 0;
  double forward;
  double turn;
  
  WPI_TalonSRX driveFL = new WPI_TalonSRX(1); //Forward left tank drive motor
  WPI_TalonSRX driveRL = new WPI_TalonSRX(2); //Rear left tank drive motor
  WPI_TalonSRX driveFR = new WPI_TalonSRX(3); //Forward Right tank drive motor
  WPI_TalonSRX driveRR = new WPI_TalonSRX(4); //Rear Right left tank drive motor
  // WPI_TalonSRX testLeft = new WPI_TalonSRX(10);
  // WPI_TalonSRX testRight = new WPI_TalonSRX(11);


  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFL, driveRL);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFR, driveRR);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);

 static int counter = 0;  //this is a counter for how many periodic enabled cycles the robot has been in, and increases by one every cycle

  /**
   * This function is run when the robot is first started up and should be used
   * for any initialization code.
   */
  @Override
  public void robotInit() {
    testElevator.setNeutralMode(NeutralMode.Brake);
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    INPUT = new MagicInput();
    //OUTPUT = new MagicOutput(INPUT);
    ELEVATOR = new MagicElevator(9, INPUT);
    PNEUMATICS = new MagicPneumatics(SolenoidEnum.leftThing.getSolenoid(), SolenoidEnum.rightThing.getSolenoid());
    driveFL.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative);
    
  }

  /**
   * This function is called every robot packet, no matter the mode. Use this for
   * items like diagnostics that you want ran during disabled, autonomous,
   * teleoperated and test.
   *
   * <p>
   * This runs after the mode specific periodic functions, but before LiveWindow
   * and SmartDashboard integrated updating.
   */
  @Override
  public void robotPeriodic() {
    cycles++;
   // System.out.println(driveFL.getSelectedSensorVelocity(0));
  }
  /**
   * This autonomous (along with the chooser code above) shows how to select
   * between different autonomous modes using the dashboard. The sendable chooser
   * code works with the Java SmartDashboard. If you prefer the LabVIEW Dashboard,
   * remove all of the chooser code and uncomment the getString line to get the
   * auto name from the text box below the Gyro
   *
   * <p>
   * You can add additional auto modes by adding additional comparisons to the
   * switch structure below with additional strings. If using the SendableChooser
   * make sure to add them to the chooser code above as well.
   */

  @Override
  public void autonomousInit() {
    System.out.println("Starting autonomousInit - " + m_autoSelected);
    chassisDrive.arcadeDrive(forward, turn);
    m_autoSelected = m_chooser.getSelected();
     //autoSelected = SmartDashboard.getString("Auto Selector", defaultAuto);
    System.out.println("Auto selected: " + m_autoSelected);
    VISION.getArduino();
    counter = 0;
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    counter += 1;
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      // System.out.println("Starting autonomousPeriodic - Custom auto.");
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      // System.out.println("Starting autonomousPeriodic - Default auto.");
      break;
    } // switch (m_autoSelected)
    if (INPUT.isButtonPressed(ButtonEnum.testBool) == true) {
      VISION.parseVal(1, 1, VISION.getArduino());
      VISION.parseVal(2, 1, VISION.getArduino());
      VISION.parseVal(5, 1, VISION.getArduino());
      VISION.parseVal(6, 1, VISION.getArduino());
    }

  }
 
  @Override
  public void teleopInit() {
     counter = 0;
  }

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
    INPUT.updates(); //Update the toggling booleen
    OUTPUT.checkCamSwap();
    counter += 1;
    //Drive code: Jack says that's all I need

    if (INPUT.isButtonPressed(ButtonEnum.elevatorUp) == true && INPUT.isButtonPressed(ButtonEnum.elevatorDown) == false) {
      testElevator.set(.5);
    } else if (INPUT.isButtonPressed(ButtonEnum.elevatorDown) == true && INPUT.isButtonPressed(ButtonEnum.elevatorUp) == false)  {
      testElevator.set(-.5);
    } else {
      testElevator.set(0);
    }
    chassisDrive.arcadeDrive(INPUT.getDrive(), INPUT.getTurn());
    if (INPUT.isButtonPressed(ButtonEnum.IntakeIn)) {
      testElevator.set(.2);
      //    PNEUMATICS.setCyl(1, 1);
   //   PNEUMATICS.setCyl(0, -1);
    } else if (INPUT.isButtonPressed(ButtonEnum.IntakeOut)) {
      testElevator.set(-.2);
      //  PNEUMATICS.setCyl(1,-1);
    //  PNEUMATICS.setCyl(0, 1);
    } else {
      testElevator.set(0);
    //  PNEUMATICS.setCyl(1,0);
    //  PNEUMATICS.setCyl(0, 0);
    }
  }
  



  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    
    INPUT.updates();
    /*
    System.out.println(INPUT.getElevatorTarget());
    ELEVATOR.elevatorPeriodic();
    System.out.println(ELEVATOR.getElevatorPos());
    */
  }
public static int getCycleCount() {
  return counter;
  }
}
