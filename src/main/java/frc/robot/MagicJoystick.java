package frc.robot ;
import edu.wpi.first.wpilibj.Joystick;
/**
This class is made for cleaning up how the joystick methods are called.
so that we can make buttons have toggle behavior; check a button, 
then wait for some other variable to have chaned state, then go back t reading the button;
*/
public class MagicJoystick extends Joystick {
  
  private enum joystickID {left, right, copilotLeft, copilotRight, copilot, broken}
  
  public enum buttonTypeEnum{
    IntakeIn(1), IntakeOut(1), Three(3);
    int num;
    private buttonTypeEnum(int numberOfButton) {num = numberOfButton;}
    public int getNum() {return num;}    
  }

  private joystickID kJoystickID;
   
    public MagicJoystick(int port) {        
      super(port); 
      switch (port) {
      case (0) :
        kJoystickID = joystickID.left;
        break;
      case (1):
        kJoystickID = joystickID.right;
        break;
      case (2) :
        kJoystickID = joystickID.copilot;
        break;
      default :
        kJoystickID = joystickID.broken;
        System.out.println("Error identifying joystick, try another port or try other hardware. /n Joystick is not in ports 1 - 3");
        break;
      }
    }
  public boolean buttonInUse(buttonTypeEnum x) {
    return this.getRawButton(x.getNum());    
  }
    
}
