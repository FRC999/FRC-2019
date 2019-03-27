package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

class MagicRobotClimber {
  WPI_TalonSRX leftTalon;
  WPI_TalonSRX rightTalon;
  boolean leftIsClimbing;
  boolean rightIsClimbing;
  static final double climbForce = .1;
  static final int encoderStopPoint = 9001;

  static final int leftPort = 999;
  static final int rightPort = 999;

  public void init(){
    leftTalon  = new WPI_TalonSRX(leftPort);
    rightTalon = new WPI_TalonSRX(rightPort);
    freeze();
    leftTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    rightTalon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Absolute);
    leftTalon.setSelectedSensorPosition(0);
    rightTalon.setSelectedSensorPosition(0);
  }
  /**
   * Every body clap your your hands
   * (stops the motors, and brakes)
   */
  public void freeze(){
    leftTalon.set(0);
    rightTalon.set(0);
    leftTalon.setNeutralMode(NeutralMode.Brake);
    rightTalon.setNeutralMode(NeutralMode.Brake);
    leftIsClimbing = false;
    rightIsClimbing = false;
  }
  
  public void periodic(boolean button){
    if (button){
      if (leftTalon.getSelectedSensorPosition() < encoderStopPoint){
        leftTalon.set(climbForce);
        leftIsClimbing = true;
      } //if left talon is in bounds
      else {
        leftTalon.set(0);
        leftTalon.setNeutralMode(NeutralMode.Brake);
        leftIsClimbing = false;
      } //if left talon is out of bounds
      if (rightTalon.getSelectedSensorPosition() < encoderStopPoint){
        rightTalon.set(climbForce);
        rightIsClimbing = true;
      } //if right talon is in bounds
      else {
        rightTalon.set(0);
        rightTalon.setNeutralMode(NeutralMode.Brake);
        rightIsClimbing = false;
      } //if right talon is out of bounds
    }
    else {
      if (leftIsClimbing || rightIsClimbing){
        freeze();
      }
    }
  }
}