package frc.robot;
import edu.wpi.first.wpilibj.Joystick;


public class MagicInput {
  Joystick leftStick = new Joystick(0);
  Joystick rightStick = new Joystick(1);
  Joystick copilotStick = new Joystick(2);
  
  /**
   * Doesnt yet account for toggleing buttons: Maybe handle that in Joystick?
   * Anyway, this is a snarky one-liner which gets the joystick from type, then gets the right button
   * I think it works.  If it doesnt, I had nothing to do with it.
   */
  boolean isButtonOn(ButtonEnum type) {return getJoystick(type.getJoystickNum()).getRawButton(type.getButtonNum());}

  Joystick getJoystick(int  port)  {
    switch (port){
      case 0:
        return leftStick;
      case 1:
        return rightStick;
      case 2:
        return copilotStick; 
      default:
        System.out.println("joystick port number passed to getJoystick() is not 0, 1, or 2!");
        return null;
      }
  }
  Joystick getJoystick(String type){
      switch (type){
        case "left":
          return leftStick;
        case "right":
          return rightStick;
        case "copilot":
          return copilotStick;
        default:
          System.out.println("Name of joystick given to getJoystick is invalid");
          return null;
      }
  }

}