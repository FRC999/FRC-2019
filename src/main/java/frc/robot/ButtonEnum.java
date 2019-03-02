package frc.robot;
public enum ButtonEnum {
    intakeIn(0, 1), intakeOut(0, 2), 
    Three(0, 3), Cog(0,4,false), testBool(0,5,false), 
    cameraChange(0,6,false), 
    elevatorUp(2,1), elevatorDown(2,2),
    elevatorLowHatch(2,7,10), elevatorMidHatch(2,8,20), elevatorHighHatch(2,9,30),
    elevatorLowBall(2,10,40), elevatorMidBall(2,11,50), elevatorHighBall(2,12,60),
    elevatorCargoShip(2,6,0);
    final private int buttonNum;
  final private int joystickNum;
  final private TogglingButton toggledButton;
  final private double elevatorHeight; //In cm: Converted at MagicElevator
  /**
   * If there is no boolean nor third double, it is a boring old button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton) {
    buttonNum = numberOfButton; 
    joystickNum = numberOfJoystick;
    toggledButton = null; //Code should avoid playing with TogglingButton properties on non-toggling buttons
    elevatorHeight = -1; //0 is a valid value: -1 is not
  }
  /**
   * If it contains a boolean, it is a toggling button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   * @param toggledButtonState the inital state of the TogglingButton
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton, boolean toggledButtonState){
    buttonNum = numberOfButton; 
    joystickNum = numberOfJoystick;
    toggledButton = new TogglingButton(toggledButtonState);
    elevatorHeight = -1; //0 is a valid value: -1 is not
  }
  /**
   * If its final value is an double, it is an elevator height selection button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   * @param heightOfElevator the height (in centimeters) that the elevator should go to
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton, double heightOfElevator){
    buttonNum = numberOfButton; 
    joystickNum = numberOfJoystick;
    toggledButton = null;
    elevatorHeight = heightOfElevator;
  }
  public int getButtonNum() {return buttonNum;}   
  public int getJoystickNum() {return joystickNum;}   
  public TogglingButton getToggledButton(){return toggledButton;}
  public double getElevatorHeight(){return elevatorHeight;}
}