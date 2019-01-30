package frc.robot;
import edu.wpi.first.wpilibj.Joystick;


 public class MagicInput {
  static Joystick driveStick = new Joystick(0);
  static Joystick turnStick = new Joystick(1);
  static Joystick copilotStick = new Joystick(2);
  
  /**
   * Doesnt yet account for toggleing buttons: Maybe handle that in Joystick?
   * Anyway, this is a snarky one-liner which gets the joystick from type, then gets the right button
   * I think it works.  If it doesnt, I had nothing to do with it.
   */
  static boolean isButtonOn(ButtonEnum type) {
    return getJoystick(type.getJoystickNum()).getRawButton(type.getButtonNum());
  }
  /**
   * Gets how far forward or back the drive stick is.  Hopefully.
   */
  static double getDrive(){
    return driveStick.getRawAxis(1);
  }
  /**
   * Gets how far left or right the turn stick is.  "Left is positive"--Jack Wertz 2019
   */
  static double getTurn(){
    return turnStick.getRawAxis(0);
  }
  /**
   * Uppdates TogglingButtons in ButtonEnum
   */
  static void updates(){
    for (ButtonEnum bob : ButtonEnum.values()){
      if (null != bob.getToggledButton()) {//We dont want to call a null variables methods
        bob.getToggledButton().update(isButtonOn(bob));
      }
    }
  }
  /**
   * Returns the joystick at the given port, or the joystick with a given name.
   */
  static Joystick getJoystick(int port)  {
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
  static Joystick getJoystick(String type){
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