package frc.robot;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.SpeedControllerGroup;
import edu.wpi.first.wpilibj.drive.DifferentialDrive;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

import edu.wpi.first.wpilibj.Compressor;

public class Constants {
    
    static double forward;
    static double turn;
    static int pneumaticInButton = 1;//BUTTON
    static final int intakeIn = 5;//BUTTON Id
    static final int intakeOut = 5;//BUTTON
    static final int elevatorUp = 6; //BUTTON
    static final int elevatorDown = 6;//BUTTON
    
    static final double intakeVal = .5;  //rate at which the intake will spin
    static final double elevatorVal = .25;  //rate at which the eleator will spin
    static final double elevatorNeutral = .1; //value at which elevator will turn to get it to hld in place
  
    static WPI_TalonSRX driveFL = new WPI_TalonSRX(1); //Forward left tank drive motor
    static WPI_TalonSRX driveRL = new WPI_TalonSRX(2); //Rear left tank drive motor
    static WPI_TalonSRX driveFR = new WPI_TalonSRX(3); //Forward Right tank drive motor
    static WPI_TalonSRX driveRR = new WPI_TalonSRX(4); //Rear Right left tank drive motor
    
    static WPI_TalonSRX testLeft = new WPI_TalonSRX(10);
    static WPI_TalonSRX testRight = new WPI_TalonSRX(11);
  
    static WPI_TalonSRX testElevator = new WPI_TalonSRX(12);
  
    static SpeedControllerGroup leftSide = new SpeedControllerGroup(driveFL, driveRL);
    static SpeedControllerGroup rightSide = new SpeedControllerGroup(driveFR, driveRR);
    static DifferentialDrive chassisDrive = new DifferentialDrive(leftSide, rightSide);
    
    
    static final int compressorPort = 0;
    static Compressor testCompressor = new Compressor(compressorPort);
    static Solenoid solenoid1 = new Solenoid(0);
    static Solenoid solenoid2 = new Solenoid(1);

}