package frc.robot;

public enum ButtonEnum {
    IntakeIn(1, 1), IntakeOut(2, 1), Three(3, 3);
    public int buttonNum;
    public int joystickNum;

    private ButtonEnum(int numberOfJoystick, int numberOfButton) {
      buttonNum = numberOfButton; 
      joystickNum = numberOfJoystick;
	}
    public int getButtonNum() {return buttonNum;}   
    public int getJoystickNum() {return joystickNum;}   

  }