package frc.robot;
public enum ButtonEnum {

    hatchIntake(0,3),
    hatchOuttake(0,5),
    cameraChange(1, 3, false),
    hatchCyl(0,1, false),
    cargoIntake(0,4),
    cargoOuttake(2,1),
    vision(1,1),

    climbFront(1, 7, false),
    climbBack(1, 8, false), 

    slowDrive(1,6),

    elevatorUp(2,2), 
    elevatorDown(2,3),
    elevatorLowHatch(2,12, EncoderPresets.lowHatch), 
    elevatorMidHatch(2,10, EncoderPresets.midHatch), 
    elevatorHighHatch(2,8, EncoderPresets.highHatch),
    elevatorLowBall(2,11, EncoderPresets.lowBall), 
    elevatorMidBall(2,4, EncoderPresets.midBall), 
    resetPorts(2,9),
    elevatorCargoShipBall(2,7, EncoderPresets.cargoShipBall), 
    elevatorRetract(2,6, EncoderPresets.retract);

   // moveElevator (0,1),
   // tunePidValUp(0,3),
   // tunePidValDown(0,5);
  
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