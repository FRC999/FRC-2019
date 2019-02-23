package frc.robot;
import edu.wpi.first.wpilibj.Joystick;


 public class MagicInput {
  Joystick driveStick;
  Joystick turnStick;
  Joystick copilotStick;
  int elevatorTarget;
  /**
   * Note: Code checks if joystick is null: however, this (should) never be the case 
   */
  MagicInput(){
    driveStick = new Joystick(0);
    turnStick = new Joystick(1);
    copilotStick = new Joystick(2);
  }
  int getElevatorTarget() {return elevatorTarget;}

  boolean isButtonOn(ButtonEnum type) {
    if(null != getJoystick(type) && null != type.getToggledButton()){
      return type.getToggledButton().toggleState;
    }
    return isButtonPressed(type);
  }
  boolean isButtonPressed(ButtonEnum type) {
    if(getJoystick(type) != null)
      return getJoystick(type.getJoystickNum()).getRawButton(type.getButtonNum());
    return false;
  }
  /**
   * Gets how far forward or back the drive stick is.  Hopefully.
   */
  double getDrive(){
    if (driveStick != null){
      return driveStick.getRawAxis(1);
    }
    return 0;
  }
  /**
   * Gets how far left or right the turn stick is.  "Left is positive"--Jack Wertz 2019
   */
  double getTurn(){
    if (turnStick != null){
      return turnStick.getRawAxis(0);
    }
    return 0;
  }
  /**
   * Range of outputs is -1 to 1
   */
  double getElevatorAdjuster(){
    if (copilotStick != null){
      return copilotStick.getRawAxis(0);
    }
    return 0;
  }
  /**
   * Uppdates TogglingButtons in ButtonEnum
   */
  void updates(){
    for (ButtonEnum bob : ButtonEnum.values()){
      if (null != bob.getToggledButton()) {//We dont want to call a null variable's methods
        bob.getToggledButton().update(isButtonPressed(bob));
      if (bob.getElevatorHeight() != -1){
        if(isButtonPressed(bob)){ //Elevator Buttons dont toggle
          elevatorTarget = bob.getElevatorHeight();
        }
      }
      }
    }
  }
  /**
   * Returns the joystick at the given port, or the joystick with a given name.
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
  Joystick getJoystick(String type){
      switch (type){
        case "turn":
        case "turnStick":
          return driveStick;
        case "drive":
        case "driveStick":
          return turnStick;
        case "copilot":
        case "copilotStick":
          return copilotStick;
        default:
          System.out.println("Name of joystick given to getJoystick is invalid");
          return null;
      }
  
  }
  Joystick getJoystick(ButtonEnum type){
    return getJoystick(type.getJoystickNum());
  }

}