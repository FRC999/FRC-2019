package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/**
 * This class is to enable PID control of the elevator.  
 * It is untested, because *someone* needs to give us a working elevator.
 * WARNING: GEARBOX ON COMP-BOT IS DIFFERENT THAN SISTER-BOT
 */
public class MagicElevator extends MagicPID{
  int eTarget;
  int eCurrent; //Must be implemented

  static final int eMin = 20; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 2000;//In NativeUnits: Test value: please let us test!
  //circumference is pi * 2.54 * 2; In centimeters:  Spool was measured at 1 inch radius
  /**
   * @param tal The elevator talon
   * @param in The MagicInput instance (till we make it a singleton)
   * @param circ The circumference of the thing (2.54*Math.PI*2 for the elevator)
   */
  public MagicElevator(WPI_TalonSRX tal, MagicInput in, double circ) {
    super(tal, in, circ);
  }

  /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input/stepsPerRotation)*circumference);
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

  /**
   * Gets the elevator position, you numbskull
   * @return elevator position (In native units)
   */
  public int getElevatorPos() {
    return talon.getSelectedSensorPosition(0);
  }

  public void elevatorPeriodic() {
    eCurrent = getElevatorPos();
    updateElevatorTarget();
    if (eCurrent == eTarget) {talon.setNeutralMode(NeutralMode.Brake);}
    else {moveElevator();}
  }
  /**
   * Moves the elevator to the current target.  Or it should.
   */
  public void moveElevator() { 
    talon.set(ControlMode.MotionMagic, eTarget);
  }
}