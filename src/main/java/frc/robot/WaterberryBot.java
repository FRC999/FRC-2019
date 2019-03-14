package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.TimedRobot;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
  

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;
//import com.kauailabs.navx.frc.AHRS;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.cameraserver.CameraServer;

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

  boolean climbRetractCyl;
  boolean cylButton;
  boolean retractClimbCylinderButton;
  boolean camSwapButton;
  
  double tilt;
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

  boolean lastCamPress;
  UsbCamera frontCam;
  UsbCamera backCam;
  VideoSink camServer;
  
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

    backCam = CameraServer.getInstance().startAutomaticCapture(0);
    frontCam = CameraServer.getInstance().startAutomaticCapture(1);
    camServer = CameraServer.getInstance().getServer();

    MOAC.set(DoubleSolenoid.Value.kReverse);
    hatchHolder.set(DoubleSolenoid.Value.kReverse);
    cargoHolder.set(DoubleSolenoid.Value.kReverse);   
  }

  @Override
  public void robotPeriodic() {
    forward = (leftStick.getRawAxis(1))*-1 ;
    turn = rightStick.getRawAxis(0);
    camSwapButton = leftStick.getRawButton(6);
    cylButton = leftStick.getRawButton(7);
    retractClimbCylinderButton = leftStick.getRawButton(8);

    
    if (camSwapButton && !lastCamPress){
      System.out.println("Swapping Cams");
      camServer.setSource(frontCam);
    }
    else if (!camSwapButton && lastCamPress){
      System.out.println("Swapping Cams");
      camServer.setSource(backCam);
    }
    lastCamPress = camSwapButton;
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
    else {MOAC.set(DoubleSolenoid.Value.kOff);}// potental fix: it may cause the cylendar to plummet
  }
}