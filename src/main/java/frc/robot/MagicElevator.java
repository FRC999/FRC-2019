package frc.robot;

/**
 * This class is to enable PID control of the elevator.  
 * It is untested, because *someone* needs to give us a working elevator.
 * WARNING: GEARBOX ON COMP-BOT IS DIFFERENT THAN SISTER-BOT
 */
public class MagicElevator extends MagicPID{
  int eTarget = eMin;
  int eCurrent; //Must be implemented
  int ePrevTarg;

	/** How much smoothing [0,8] to use during MotionMagic */
  static final int _smoothing = 0;
  
  static final int eMin = 20; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 90000;//In NativeUnits: Test value: please let us test!
  //circumference is pi * 2.54 * 2; In centimeters:  Spool was measured at 1 inch radius
  /**
   * @param port The number of the elevator talon
   */
  public MagicElevator(int port) {
    super( 2.54*2*Math.PI, 1,     .2, .0, .2, .2, 1.0,    0,     _smoothing,      port); // numbers are made up
    //      circum.       gearRat  S   P   D   I    F   slot#   s-curve smoothing, port #
  }

  /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimeter height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input/stepsPerRotation)*circumference);
  } 

  /**
   * Check if the elevator button is pressed: if yes, do stuff
   * Includes min/max validation
   *  
   */
  
  public void updateElevatorTarget () {
    ePrevTarg = eTarget;
    eTarget = convertToNativeUnits(INPUT.getElevatorTarget());
    eTarget = validateTarget(eTarget);
    
  }

  public int validateTarget(int targ){
    if (targ > eMax) {
      targ = eMax-1;
      System.out.println("over max");
      setElevatorTargetNU(targ);

      return eMax;
    } 
    else if (targ < eMin) {
      targ = eMin+1; 
      System.out.println("under min");
      setElevatorTargetNU(targ);

      return eMin;
    }
    else{
      return targ;
    }
  }

  /**
   * Changes the elevator target to whatever it is given
   * Including the target stored in MagicInput
   * Has validation
   */
  public double setElevatorTargetNU(int targ){
    targ = validateTarget(targ);
    ePrevTarg = eTarget;
    eTarget = targ;
    return INPUT.setElevatorTarget(convertFromNativeUnits(targ));
  }
  

  /**
   * Gets the elevator position, you numbskull
   * @return elevator position (In native units)
   */
  public int getElevatorPos() {
    return talon.getSelectedSensorPosition(0);
  }

  /**
   * Updates elevator position, then moves elevator to that position.
   */
  public void elevatorPeriodic() {
    eCurrent = getElevatorPos();
    updateElevatorTarget();
    if (eCurrent == eTarget) {freeze();}
    else {moveElevator();}
  }
  /**
   * Moves the elevator to the current target.  Or it should.
   */
  public void moveElevator() { 
    if (ePrevTarg != eTarget){
      moveTo(eTarget);
      System.out.println("Elevator in motion");
    }
  }
}