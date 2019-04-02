package frc.robot;

/**
 * This code generates a (kinda) table of the buttons, their names, and their assignments
 * DO NOT USE in competition code: It is almost certainly calculation and memory intesive
 */
public class ButtonListMaker {
  StringBuilder[] joystick0List = new StringBuilder[13];
  StringBuilder[] joystick1List = new StringBuilder[13];
  StringBuilder[] joystick2List = new StringBuilder[13];
  String joystick0String;
  String joystick1String;
  String joystick2String;

  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();
  /**
   * Only call once, during initalization.  This is probably the most intensive function in this years robot.
   */
  public void buildStrings(){
    joystick0List = new StringBuilder[13]; //Clear button lists
    joystick1List = new StringBuilder[13];
    joystick2List = new StringBuilder[13];

    for (ButtonEnum bob : ButtonEnum.values()){
      int curJoy = bob.getJoystickNum();
      int curBut = bob.getButtonNum();
      StringBuilder[] curList = (curJoy == 0) ? joystick0List : ((curJoy == 1) ? joystick1List : ((curJoy == 2) ? joystick2List : null));
      StringBuilder curSb = curList[curBut];
      if (0 > curJoy || 2 < curJoy || 1 > curBut || 12 < curBut) {
        System.out.println("Warning: Invalid button!" + bob.name());
        continue;
      }
      if (curSb != null){
        curSb = new StringBuilder("Oh shoot! Two entries bound to this button!");
        continue;
      }
      curSb = new StringBuilder(50);
      curSb.append("Button ");
      curSb.append(String.format("%-2d", curBut));
      curSb.append("Named ");
      curSb.append(String.format("%-20s", bob.name()));
      curSb.append("Toggles ");
      curSb.append((bob.getToggledButton() != null) ? "Yah" : "Nah");
      curSb.append("Elevator Height ");
      if (bob.getElevatorPreset() == null) {
      curSb.append("Nah");}
      else{
      curSb.append(String.format("%-3d", bob.getElevatorPreset().getHeightCM()));}

    }
    joystick0List[0] = new StringBuilder("Joystick 0 (drive/left stick): \n");
    joystick1List[0] = new StringBuilder("Joystick 1 (turn/right stick): \n");
    joystick2List[0] = new StringBuilder("Joystick 2 (co-pilot's stick): \n");

    StringBuilder sb = new StringBuilder(500);
    for(StringBuilder joy : joystick0List){
      sb.append(joy);
      sb.append("\n");
    }
    joystick0String = sb.toString();
    sb.setLength(0);

    for(StringBuilder joy : joystick1List){
      sb.append(joy);
      sb.append("\n");
    }
    joystick1String = sb.toString();
    sb.setLength(0);

    for(StringBuilder joy : joystick2List){
      sb.append(joy);
      sb.append("\n");
    }
    joystick2String = sb.toString();
  }
}