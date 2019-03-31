package frc.robot;
public enum ButtonEnum {
    intakeIn(0, 1), intakeOut(0, 2),
    Three(0, 3, false), Cog(0,4,true), testBool(0,5,false),

    hatchIntake(1,3), hatchOuttake(1,5), cameraChange(0, 6, false), 
    intakeCargo(1,4), outtakeCargo(1,6), endgame(1,7), vision(0,0),

    climbFront(1, 8, false), climbBack(1, 7, false), 

    elevatorUp(2,1), elevatorDown(2,2),
    elevatorLowHatch(2,12, EncoderPresets.lowHatch), elevatorMidHatch(2,10, EncoderPresets.midHatch), elevatorHighHatch(2,8, EncoderPresets.highHatch),
    elevatorLowBall(2,11, EncoderPresets.lowBall), elevatorMidBall(2,9, EncoderPresets.midBall), elevatorHighBall(2,7, EncoderPresets.highBall),
    elevatorCargoShipBall(2,5, EncoderPresets.cargoShipBall), elevatorRetract(2,3, EncoderPresets.retract);
  
  final private int buttonNum;
  final private int joystickNum;
  final private TogglingBoolean toggledButton;
  final private EncoderPresets ePresets;
  /**
   * If there is no boolean nor third double, it is a boring old button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton) {
    buttonNum = numberOfButton;
    joystickNum = numberOfJoystick;
    toggledButton = null; //Code should avoid playing with TogglingButton properties on non-toggling buttons
    ePresets = null;
  }
  /**
   * If it contains a boolean, it is a toggling button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   * @param toggledButtonState the initial state of the TogglingButton
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton, boolean toggledButtonState){
    buttonNum = numberOfButton;
    joystickNum = numberOfJoystick;
    toggledButton = new TogglingBoolean(toggledButtonState);
    ePresets = null;
  }
  /**
   * If its final value is an elevator button, it is an elevator height selection button
   * @param numberOfJoystick the port number of the buttons joystick
   * @param numberOfButton the button number on the joystick of the button
   * @param heightOfElevator the height (in centimeters) that the elevator should go to
   */
  private ButtonEnum(int numberOfJoystick, int numberOfButton, EncoderPresets target) {
    buttonNum = numberOfButton;
    joystickNum = numberOfJoystick;
    toggledButton = null; //Code should avoid playing with TogglingButton properties on non-toggling buttons
    ePresets = target;
  }
  public int getButtonNum() {return buttonNum;}
  public int getJoystickNum() {return joystickNum;}
  public TogglingBoolean getToggledButton(){return toggledButton;}
  public EncoderPresets getElevatorPreset() {return ePresets;}
}