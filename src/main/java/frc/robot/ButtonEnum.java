package frc.robot;
public enum ButtonEnum {
    intakeIn(0, 1), intakeOut(0, 2), 
    Three(0, 3, false), Cog(0,4,false), testBool(0,5,false), 
    elevatorUp(2,1), elevatorDown(2,2),

    hatchClose(0,4), hatchOpen(0,3), cameraChange(0,6), retractCargoIntake(0,5),
    toggleIntake(1,2), intakeCargo(1,1), outtakeCargo(1,3), endgame(1,7),
    elevatorLowHatch(2,12,20), elevatorMidHatch(2,10,48), elevatorHighHatch(2,8,77),
    elevatorLowBall(2,11,24), elevatorMidBall(2,9,55), elevatorHighBall(2,7,83),
    elevatorCargoShipBall(2,5,43), elevatorFloor(2,3,0); //According to Mohawk Measurments
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