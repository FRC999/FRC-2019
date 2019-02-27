package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/**
 * This class is to enable PID control of the elevator.  
 * It is untested, because *someone* needs to give us a working elevator.
 */
public class MagicElevator {
  private WPI_TalonSRX elevatorTalon;
  MagicInput INPUT;
  int eTarget;
  int eCurrent; //Must be implemented

  static final int eMin = 200; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 200000; //In NativeUnits: Test value: please let us test!
  static final double spoolCircumfrence = Math.PI * 2.54 * 2; //In centimeters:  Spool was measured at 1 inch radius
  static final int stepsPerRotation = 4096;
  
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    elevatorTalon = tal;
    INPUT = in; 
  }

  /**
   * Converts centimeters to the native Talon units for elevator PID use
   * @param centis Centimeter height of the elevator (off the ground? not? more testing!)
   * @return the desired motor value
   */
  public int convertToNativeUnits(double centis){
    return (int) (stepsPerRotation * (centis/spoolCircumfrence));
  }
  /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input/stepsPerRotation)*spoolCircumfrence);
  } 

  //Check if the elevator button is pressed: if yes, do stuff
  public void updateElevatorTarget () {
    eTarget = convertToNativeUnits(INPUT.getElevatorTarget());
    
    if (INPUT.isButtonOn(ButtonEnum.elevatorUp)) {eTarget += 20;}
    if (INPUT.isButtonOn(ButtonEnum.elevatorDown)) {eTarget -=20;}
    //If we also allow them to control the elevator axis via joystick, put code here

    if (eTarget > eMax) {eTarget = eMax;} //No going above the height limit
    if (eTarget < eMin) {eTarget = eMin;} //No going below it, either
    INPUT.setElevatorTarget(convertFromNativeUnits(eTarget)); //Update the validated target in MagicInput
  }
  public int elevatorPos() {
    return elevatorTalon.getSelectedSensorPosition(0);
  }
  public void elevatorPeriodic() {
    eCurrent = elevatorTalon.getSelectedSensorPosition();
    updateElevatorTarget();
    if (eTarget == eCurrent) {elevatorTalon.setNeutralMode(NeutralMode.Brake);}
    else {moveElevator();}
  }
  /**
   * Moves the elevator to the current target.  Or it should.
   */
  public void moveElevator() { 
    elevatorTalon.set(ControlMode.MotionMagic, eTarget);
  }
}