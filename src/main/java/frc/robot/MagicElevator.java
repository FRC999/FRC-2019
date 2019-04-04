package frc.robot;

/**
 * This class is to enable PID control of the elevator.
 * It is untested, because *someone* needs to give us a working elevator.
 * WARNING: GEARBOX ON COMP-BOT IS DIFFERENT THAN SISTER-BOT
 */
public class MagicElevator extends MagicPIDMotor{
  int eCurrent;
  EncoderPresets eTargetPreset;
  MagicIntake INTAKE;

  MagicJoystickInput INPUT;
  static final int  eOffsetHeight = 2; //How high the zero point of the native units is off the floor in NU's

  static final int eMin = 0; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 90000;//In NativeUnits: Test value: please let us test!
  //circumference is pi * 2.54 * 2; In centimeters:  Spool was measured at 1 inch radius
  
  /**
   * @param port The number of the elevator talon
   * @param the The intake to be updated when targets change
   */
  public MagicElevator(int port, MagicIntake in) {
    super( 2.54*2*Math.PI,   1,   .2, .0, .2, .2,   0.1,       0,     port,      eOffsetHeight, -4096, 4096); // numbers are made up
    //      circum.       gearRat  P   D   I   F  peakOutput  slot#   talon#       start pt.     min    max
    INTAKE = in;
  }

  /**
   * Check if the elevator button is pressed: if yes, update
   * Includes min/max validation
   * Does not initiate motion
   *
   */
  public void updateElevatorTarget () {
    for (ButtonEnum bob : ButtonEnum.values()){ //Propperly magical iterator OF DOOM
      if (null != bob.getElevatorPreset()) {//We dont want to call a null variable's methods
        if (INPUT.isButtonPressed(bob)){
          setTarget(bob.getElevatorPreset().getHeightNU());
          eTargetPreset = bob.getElevatorPreset();
          //INTAKE.gotoPreset(eTargetPreset); Implement whe MagicIntake is ready
        }
      }
    }
    increaseTarget((int) INPUT.getElevatorAdjuster() * 1);
    if (INPUT.isButtonOn(ButtonEnum.elevatorUp)){
      increaseTarget(1);
    }
    if (INPUT.isButtonOn(ButtonEnum.elevatorDown)){
      increaseTarget(-1);
    }

  }


  /**
   * Updates elevator position, then moves elevator to that position.
   */
  public void elevatorPeriodic() {
    updateElevatorTarget();
    startMotion();
  }
}