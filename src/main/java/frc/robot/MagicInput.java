package frc.robot;
import edu.wpi.first.wpilibj.Joystick;


public class MagicInput {
  Joystick leftStick = new Joystick(0);
  Joystick rightStick = new Joystick(1);
  
  boolean isButtonOn(buttonEnum type){ //Doesnt yet account for toggleing buttons: Maybe handle that in MagicJoystick?
    return getJoystick(type.joystickNum).getRawButton(type.buttonNum); //Snarky one-liner which gets the joystick from type, then gets the right button
    
  }

  Joystick getJoystick(int  port)  {
    switch (port){
      case 0:
        return leftStick;
        break;
      case 1:
        return rightStick;
        break;
      case 2:
        return copilotStick; 
        break;
      default:
        System.out.println("joystick port number passed to getJoystick() is not 0, 1, or 2!");
        return null;}
    }
  Joystick getJoystick(String type){
      switch (type){
        case "left":
          return leftStick;
          break;
        case "right":
          return rightStick;
          break;
        case "copilot":
          return copilotStick;
          break;
        default:
          System.out.println("Name of joystick given to getJoystick is invalid");
      }
          
  }

}
