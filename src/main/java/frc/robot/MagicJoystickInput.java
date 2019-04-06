package frc.robot;
import edu.wpi.first.wpilibj.Joystick;

/**
 * MagicInput is a class designed to handle all input from the drivers station.
 * It is primarily focused on the joysticks.  It provides this information to other
 * classes and methods, free of charge.  MagicInput, unlike other off-the-shelf input
 * brands, is designed for configuration, first and foremost.  Where is button is, and its
 * toggling behavior, is specified in ButtonEnum.  Do not use MagicInput
 * if you like writing long lines of code any time you wish to speak with the manager.
 *
 * Use getJoystick() to fetch joysticks if you want to throw together a button without
 * putting it in ButtonEnum.  Otherwise, keep buttons there.
 */
 public class MagicJoystickInput {

  private static MagicJoystickInput mInstance = new MagicJoystickInput();

  public static MagicJoystickInput getInstance() {return mInstance;}

  Joystick driveStick;
  Joystick turnStick;
  Joystick copilotStick;

  final static double deadZoneRadius = .025; //WARNING: Very high! Only for testing purposes!

  /**
   * Note: Code checks if joystick is null: however, this (should) never be the case
   */
  private MagicJoystickInput(){
    driveStick = new Joystick(0);
    turnStick = new Joystick(1);
    copilotStick = new Joystick(2);
  }

  /**
   * Checks whether a button is "on" ie, it is toggled on or pressed.
   * If it is a toggling button, checks whether it is toggled on, otherwise, checks if it is pressed
   * @param type is a ButtonEnum button.  Format it like ButtonEnum.selectedButton
   * @return true if on: false if not
   */
  boolean isButtonOn(ButtonEnum type) {
    if(null != getJoystick(type) && null != type.getToggledButton()){
      return type.getToggledButton().toggleState;
    }
    return isButtonPressed(type);
  }

  /**
   * Checks whether the physical button is currently being held down.
   * Does *not* account for TogglingButtons: only use if you are *certain* it will never be one
   * @param type is a ButtonEnum button.  Format it like ButtonEnum.selectedButton
   * @return true if on: false if not
   */
  boolean isButtonPressed(ButtonEnum type) {
    if(getJoystick(type) != null)
      return getJoystick(type).getRawButton(type.getButtonNum());
    return false;
  }

  double joystickDeadzoneTest(double toTest){
    if(Math.abs(toTest) > deadZoneRadius){
      return toTest;
    }
    else return 0;
  }

  /**
   * Gets how far forward or back the drive stick is.  Hopefully.
   * @return a double between -1 and 1, with one being all the way forward
   */

  double getDrive(){
    if (driveStick != null){
      return -joystickDeadzoneTest(driveStick.getRawAxis(1));
    }
    return 0;
  }
  /**
   * Gets how far left or right the turn stick is.  "Left is positive"--Jack Wertz 2019
   * @return a double between -1 and 1, with 1 being all the way left
   */
  double getTurn(){
    if (turnStick != null){
      return joystickDeadzoneTest(turnStick.getRawAxis(0));
    }
    return 0;
  }
  /**
   * Gets the elevator axis: we haven't decided how to ultimately treat this
   * We should probably annoy the drive team or something
   * @return a double between -1 and 1, with 1 being at the top.  Maybe?
   */
  double getElevatorAdjuster(){
    if (copilotStick != null){
      return joystickDeadzoneTest(copilotStick.getRawAxis(0));
    }
    return 0;
  }

  /**
   * Updates TogglingButtons in ButtonEnum
   * This *must* be called every cycle for TogglingButtons to function
   * It also updates elevatorHeight with the current selection
   * If more than one elevator height selector button is pressed,
   * it should go to the last one on the ButtonEnum list.  Should.
   */
  void updates(){
    for (ButtonEnum bob : ButtonEnum.values()){ //Properly magical iterator OF DOOM
      if (null != bob.getToggledButton()) {//We dont want to call a null variable's methods
        bob.getToggledButton().update(isButtonPressed(bob));
      }
    }
  }
  /**
   * Returns the joystick at the given port: not currently used
   * @param port must be 0,1, or 2, otherwise it will return a null
   * @return the selected joystick, or null if invalid input
   */
  Joystick getJoystick(int port)  {
    switch (port){
      case 0:
        return driveStick;
      case 1:
        return turnStick;
      case 2:
        return copilotStick;
      default:
        System.out.println("joystick port number passed to getJoystick() is not 0, 1, or 2!");
        return null;
      }
  }

  /**
   * Gets the joystick of the specified button.  Used in MagicInput
   * @param type is a ButtonEnum button.  Format it like ButtonEnum.selectedButton
   * @return Joystick that holds chosen button
   */
  Joystick getJoystick(ButtonEnum type){
    return getJoystick(type.getJoystickNum());
  }
}