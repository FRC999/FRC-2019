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
    static int pneumaticInButton = 1;//BUTTON
    static final int intakeIn = 5;//BUTTON Id
    static final int intakeOut = 5;//BUTTON
    static final int elevatorUp = 6; //BUTTON
    static final int elevatorDown = 6;//BUTTON
    
    static final double intakeVal = .5;  //rate at which the intake will spin
    static final double elevatorVal = .25;  //rate at which the eleator will spin
    static final double elevatorNeutral = .1; //value at which elevator will turn to get it to hld in place

    static final int compressorPort = 0;
}