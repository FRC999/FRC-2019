/*----------------------------------------------------------------------------*/
/* Copyright (c) 2017-2018 FIRST. All Rights Reserved.                        */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                                */
/*----------------------------------------------------------------------------*/
package frc.robot;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.SerialPort;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
//import edu.wpi.first.wpilibj.I2C;
//import edu.wpi.first.wpilibj.Timer;
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
  private SerialPort arduino;
  //private Timer timer;
  int bRate = 115200;
  String targetPosition;
  int xVal;
  int yVal;
  int wVal;
  int hVal;
  int distVal;
  int confVal;
  int stopDistance = 200;
  int confidenceThreshold = 300;
  int counting = 0;
  int delayCount = 1;
  String targetPos;
  int arduinoCounter; // loop counter passed from arduino for timing checks
  int blocksSeen;
  WPI_TalonSRX driveFrontLeft = new WPI_TalonSRX(1);
  WPI_TalonSRX driveBackLeft = new WPI_TalonSRX(2);
  WPI_TalonSRX driveFrontRight = new WPI_TalonSRX(3);
  WPI_TalonSRX driveBackRight = new WPI_TalonSRX(4);
  SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFrontLeft, driveBackLeft);
  SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFrontRight, driveBackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);
  double forward;
  double turn;
  int cycles;
  /**
   * This function is run when the robot is first started up and should be
   * used for any initialization code.
   */
  @Override
  public void robotInit() {
    m_chooser.setDefaultOption("Default Auto", kDefaultAuto);
    m_chooser.addOption("My Auto", kCustomAuto);
    SmartDashboard.putData("Auto choices", m_chooser);
    System.out.println("Reached try-catch statement (OF DOOM - Calum)");

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
    // increment loop counter for syncronization timing with arduino
    cycles++;
        
      } 
      //aint findX = targetPosition.indexOf("x:");
      //int findY = targetPosition.indexOf("y");
      //try {
      //String xCoord = targetPosition.substring(findX, findY);
      //System.out.println(xCoord);
      //} catch (Exception badCoords) {
       // System.out.println("Bad Coordinate Indexes");
     // }
    // System.out.print(targetPosition);
    // SmartDashboard.putString(nameName, arduino.readString());
   //robotPeriodic()

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
    System.out.println("Starting autonomousInit - " + m_autoSelected);
    chassisDrive.arcadeDrive(forward, turn); 
    m_autoSelected = m_chooser.getSelected();
    // autoSelected = SmartDashboard.getString("Auto Selector",
    // defaultAuto);
  }

  /**
   * This function is called periodically during autonomous.
   */
  @Override
  public void autonomousPeriodic() {
    switch (m_autoSelected) {
    case kCustomAuto:
      // Put custom auto code here
      //System.out.println("Starting autonomousPeriodic - Custom auto.");
      break;
    case kDefaultAuto:
    default:
      // Put default auto code here
      //System.out.println("Starting autonomousPeriodic - Default auto.");
      break;
    } // switch (m_autoSelected)

    //System.out.println("auto periodic loop counter: " + counting);
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      // System.out.println("parsing...");
      targetPos = "begin" + arduino.readString().trim();
      targetPosition = targetPos.substring(targetPos.indexOf("Block")+5);
      System.out.println(targetPos);
      if (targetPosition != null && !targetPosition.trim().isEmpty()) {
        // System.out.println("String TargetPosition = " + targetPosition);
        // var positions = targetPosition.split(";");
        String[] positions = targetPosition.split(";");
        if (targetPosition.startsWith("ID")) {
          for (int i = 0; i < positions.length; i++) {
            // var positionNums = positions[i].split(":");
            String[] positionNums = positions[i].split(":");
            // System.out.println("positionNums Values " + positionNums[0] + " and " +
            // positionNums[1]);
            if (positionNums[0].equals("x")) {
              xVal = Integer.parseInt(positionNums[1]);
              //System.out.println("xval = " + xVal);
            } else if (positionNums[0].equals("y")) {
              yVal = Integer.parseInt(positionNums[1]);
              // System.out.println("yval = " + yVal);
            } else if (positionNums[0].equals("h")) {
              hVal = Integer.parseInt(positionNums[1]);
              // System.out.println("hval = " + hVal);
            } else if (positionNums[0].equals("w")) {
              wVal = Integer.parseInt(positionNums[1]);
              // System.out.println("wval = " + wVal);
            } else if (positionNums[0].equals("dist")) {
              distVal = Integer.parseInt(positionNums[1]);
              // System.out.println("distval = " + distVal);
            } else if (positionNums[0].equals("conf")) {
              confVal = Integer.parseInt(positionNums[1]);
              // System.out.println("confval = " + confVal);
            } else if (positionNums[0].equals("count")) {
              arduinoCounter = Integer.parseInt(positionNums[1]);
              // System.out.println("arduinoCounter = " + arduinoCounter);
            } else if (positionNums[0].equals("BlockID")) {
              blocksSeen = Integer.parseInt(positionNums[1]);
              // System.out.println("blockids = " + blocksSeen);
            } else {
              System.out.println("He's dead, Jim ("+ positionNums[0]+ ") Killed the parser");
            }
          }
        } else {System.out.println("Bad String from Arduino: ");}
      }
    }
    // checks confidence value of distance sensor and ignores low confidence readings
    if (confVal <= confidenceThreshold) {
      distVal = stopDistance + 1;
    }
    //System.out.println("xval = " + xVal + "arduinoCounter = " + arduinoCounter);
    if (targetPosition == "") {
      leftSide.set(0);
      rightSide.set(0);
      System.out.println("targetPosition is EMPTY! plz fill.");
    } else if (xVal < 158 && xVal > 0 && distVal > stopDistance) {
      leftSide.set(0);
      rightSide.set(.2);
      //System.out.println("Turning left xVal = "+ xVal+ " arduinoCounter = " + arduinoCounter);
      //System.out.println(targetPosition);
    } else if (xVal == 158 && distVal > stopDistance) {
      leftSide.set(-.2);
      rightSide.set(.2);
      System.out.println("Straight ahead xVal = "+ xVal + " arduinoCounter = " + arduinoCounter);
    } else if (xVal > 158 && distVal > stopDistance) {
      leftSide.set(-.2);
      rightSide.set(0);
      ///System.out.println("Turning right xVal = "+ xVal + " arduinoCounter = " + arduinoCounter);
    } else {
      leftSide.set(0);
      rightSide.set(0);
      //System.out.println("Not moving xVal = "+ xVal +"distVal = "+ distVal + " arduinoCounter = " + arduinoCounter);
    }
  } // autonomousPeriodic()

  /**
   * This function is called periodically during operator control.
   */
  @Override
  public void teleopPeriodic() {
  }

  /**
   * This function is called periodically during test mode.
   */
  @Override
  public void testPeriodic() {
    leftSide.set(-.2);
    rightSide.set(.2);
  }
}
