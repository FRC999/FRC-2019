package frc.robot;
import edu.wpi.first.wpilibj.Joystick;


 public class MagicInput {
  Joystick driveStick = new Joystick(0);
  Joystick turnStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);
  
  /**
   * Doesnt yet account for toggleing buttons: Maybe handle that in Joystick?
   * Anyway, this is a snarky one-liner which gets the joystick from type, then gets the right button
   * I think it works.  If it doesnt, I had nothing to do with it.
   */
  boolean isButtonOn(ButtonEnum type) {
    if(null != type.getToggledButton()){
      return type.getToggledButton().toggleState;
    }
    return isButtonPressed(type);
  }
  boolean isButtonPressed(ButtonEnum type) {
    return getJoystick(type.getJoystickNum()).getRawButton(type.getButtonNum());

  }
  /**
   * Gets how far forward or back the drive stick is.  Hopefully.
   */
  double getDrive(){
    return driveStick.getRawAxis(1);
  }
  /**
   * Gets how far left or right the turn stick is.  "Left is positive"--Jack Wertz 2019
   */
  double getTurn(){
    return turnStick.getRawAxis(0);
  }
  /**
   * Uppdates TogglingButtons in ButtonEnum
   */
  void updates(){
    for (ButtonEnum bob : ButtonEnum.values()){
      if (null != bob.getToggledButton()) {//We dont want to call a null variables methods
        bob.getToggledButton().update(isButtonPressed(bob));
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

}