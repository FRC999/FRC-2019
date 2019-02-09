package frc.robot;

public enum ButtonEnum {
  IntakeIn(0, 1), IntakeOut(0, 2), Three(0, 3), Cog(0,4,false), testBool(0,5,false), cameraChange(0,6,false);
  final private int buttonNum;
  final private int joystickNum;
  final private TogglingButton toggledButton;
  private ButtonEnum(int numberOfJoystick, int numberOfButton) {
    buttonNum = numberOfButton; 
    joystickNum = numberOfJoystick;
    toggledButton = null;
  }
  private ButtonEnum(int numberOfJoystick, int numberOfButton, boolean toggledButtonState){
    buttonNum = numberOfButton; 
    joystickNum = numberOfJoystick;
    toggledButton = new TogglingButton(toggledButtonState);
  }
  
  public int getButtonNum() {return buttonNum;}   
  public int getJoystickNum() {return joystickNum;}   
  public TogglingButton getToggledButton(){return toggledButton;}

}