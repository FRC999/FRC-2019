package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
  

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;

public class WaterberryBot extends TimedRobot {

  WPI_TalonSRX motor;
  WPI_TalonSRX elevatorTalon = new WPI_TalonSRX(9);
  WPI_TalonSRX elbowTalon = new WPI_TalonSRX(10);
  WPI_TalonSRX wristTalon = new WPI_TalonSRX(7);
  //@@@ JW need intake victor assignments
  WPI_VictorSPX leftIntakeRoller = new WPI_VictorSPX(22);
  WPI_VictorSPX rightIntakeRoller = new WPI_VictorSPX(23);
  Joystick leftStick = new Joystick(0);
  Joystick rightStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);
  double forward;
  double turn;
  double intakeSpeed = .75;
  int testSetpoint =  -607;
  int testMin = 0;
  int testMax = -800;
  double testSpeed = .25;
  double testHold = .15;
  // *** Buttons ***
  boolean testUp;
  boolean testDown;
  boolean testHome;
  boolean fullClimb;
  boolean reverseClimb;
  boolean overrideButton;
  boolean climbRetractCyl;
  boolean climbRetractArm;    
  boolean cylButton;
  boolean cargoToggle;
  boolean groundLev;
  boolean cargoLev1;
  boolean cargoLev2;
  boolean cargoLev3;
  boolean hatchLev1;
  boolean hatchLev2;
  boolean hatchLev3;  
  boolean cargoShipLev;
  boolean outtakeButton;
  boolean rotateIntakeOutButton;
  boolean intakeButton;
  boolean climbButton;
  boolean hatch;
  boolean retractClimbCylinderButton;
  boolean visionTrackingButton;
  boolean camSwapButton;
  boolean retractIntake;
  boolean intakeBool;
  boolean elevatorUp;
  boolean elevatorDown;
  boolean elbowUp;
  boolean elbowDown;
  double tilt;
  boolean hatchGrabberZero;
  boolean zeroEnc;
  int testTolerence;
  DoubleSolenoid MOAC = new DoubleSolenoid (0,7); // mother of all cylinders
  WPI_TalonSRX testMotor;
  int _loops = 0;
  

  WPI_TalonSRX climbTalon = new WPI_TalonSRX(12);
  WPI_VictorSPX climbFollower = new WPI_VictorSPX(11);

  double climbSpeed = 1; // value to set the climb talons to
  double targAngle = 40.0;
  Compressor compress;  

  DoubleSolenoid hatchHolder = new DoubleSolenoid (2,5); // NEED A LEFT AND RIGHT

  DoubleSolenoid cargoHolder = new DoubleSolenoid (1,6);

  UsbCamera camera1;
  UsbCamera camera2;
  
  WPI_TalonSRX FrontRight = new WPI_TalonSRX(1);
  WPI_TalonSRX FrontLeft = new WPI_TalonSRX(4);
  WPI_VictorSPX MiddleLeft = new WPI_VictorSPX(5);
  WPI_VictorSPX BackLeft = new WPI_VictorSPX(6);
  WPI_VictorSPX MiddleRight = new WPI_VictorSPX(2);
  WPI_VictorSPX BackRight = new WPI_VictorSPX(3);
  SpeedControllerGroup leftSide = new SpeedControllerGroup(FrontLeft,MiddleLeft,BackLeft); 
  SpeedControllerGroup rightSide = new SpeedControllerGroup(FrontRight,MiddleRight,BackRight);
  DifferentialDrive chassisDrive = new DifferentialDrive(leftSide,rightSide);

  public void robotInit() {
  
  FrontLeft.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  FrontRight.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  elevatorTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  elbowTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  wristTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,0,0);
  elbowTalon.setNeutralMode(NeutralMode.Brake);
  wristTalon.setNeutralMode(NeutralMode.Brake);
  elevatorTalon.setSelectedSensorPosition(0);
  wristTalon.setSelectedSensorPosition(0);
  elbowTalon.setSelectedSensorPosition(-1024);
  compress = new Compressor(0);
  compress.setClosedLoopControl(true);

  camera1 = CameraServer.getInstance().startAutomaticCapture(0);
  camera2 = CameraServer.getInstance().startAutomaticCapture(1);

  MOAC.set(DoubleSolenoid.Value.kReverse);
  hatchHolder.set(DoubleSolenoid.Value.kReverse);
  cargoHolder.set(DoubleSolenoid.Value.kReverse);   
  }
  @Override
  public void robotPeriodic() {
    
    forward = (leftStick.getRawAxis(1))*-1 ;
    turn = rightStick.getRawAxis(0);
    overrideButton = leftStick.getRawButton(1);
    hatch = leftStick.getRawButtonPressed(4);
    camSwapButton = leftStick.getRawButton(6);      
    retractIntake = leftStick.getRawButton(5);
    intakeBool = rightStick.getRawButton(2); //Toggle intake open / closed
    intakeButton = rightStick.getRawButton(1); //Intake cargo
    outtakeButton = rightStick.getRawButton(3); //fire cargo
    cylButton = leftStick.getRawButton(7);
    climbButton = leftStick.getRawButton(8);
    climbRetractArm = leftStick.getRawButton(10);                                      //should this be "climbButton" or "endgameButton"
    retractClimbCylinderButton = leftStick.getRawButton(8);
    fullClimb = leftStick.getRawButton(11);
    reverseClimb = leftStick.getRawButton(12);
    cargoLev1 = copilotStick.getRawButton(11);
    cargoLev2 = copilotStick.getRawButton(9);
    cargoLev3 = copilotStick.getRawButton(7);
    hatchLev1 = copilotStick.getRawButton(12);
    hatchLev2 = copilotStick.getRawButton(10);
    hatchLev3 = copilotStick.getRawButton(8);
    cargoShipLev = copilotStick.getRawButton(5);
    groundLev = copilotStick.getRawButton(3);
    hatchGrabberZero = copilotStick.getRawButton(1);
  }
 public void autonomousInit() {
    climbTalon.setNeutralMode(NeutralMode.Brake);
    compress.setClosedLoopControl(true);
  }
@Override
  public void autonomousPeriodic() {
    chassisDrive.arcadeDrive(forward, turn);
  if (cylButton) // climbAngle is 45 degreesright now, tune
    {MOAC.set(DoubleSolenoid.Value.kForward);}
  else if (retractClimbCylinderButton) { //retract MOAC
    MOAC.set(DoubleSolenoid.Value.kReverse); }
  else {MOAC.set(DoubleSolenoid.Value.kOff);}
  
  }
  @Override
  public void teleopInit() {
     climbTalon.setNeutralMode(NeutralMode.Brake);
     compress.setClosedLoopControl(true); 
     }
  @Override
  public void teleopPeriodic() {
    chassisDrive.arcadeDrive(forward, turn);
    if (cylButton) // climbAngle is 45 degreesright now, tune
      {MOAC.set(DoubleSolenoid.Value.kForward);}
    else if (retractClimbCylinderButton) { //retract MOAC
      MOAC.set(DoubleSolenoid.Value.kReverse); }
    else {MOAC.set(DoubleSolenoid.Value.kOff);}
  }
}