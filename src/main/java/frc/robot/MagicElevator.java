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
  int eTarget = eMin;
  int eCurrent; //Must be implemented
  int ePrevTarg;


  static final int eMin = 20; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 2000;//In NativeUnits: Test value: please let us test!
  //circumference is pi * 2.54 * 2; In centimeters:  Spool was measured at 1 inch radius
  /**
   * @param tal The elevator talon
   * @param in The MagicInput instance (till we make it a singleton)
   */
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    super(tal, in, 2.54*2*Math.PI, 1);
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
    
    //if (eTarget > eMax) {eTarget = eMax; System.out.println("over max");} //No going above the height limit
    //if (eTarget < eMin) {eTarget = eMin; System.out.println("under min");} //No going below it, either
    INPUT.setElevatorTarget(convertFromNativeUnits(eTarget)); //Update the validated target in MagicInput
  }

  public double setElevatorTargetNU(int targ){
    ePrevTarg = eTarget;
    eTarget = targ;
    return INPUT.setElevatorTarget(convertToNativeUnits(targ));
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
    if (ePrevTarg != eTarget){
      talon.set(ControlMode.MotionMagic, eTarget);
      System.out.println("Elevator in motion");
    }
  }
}