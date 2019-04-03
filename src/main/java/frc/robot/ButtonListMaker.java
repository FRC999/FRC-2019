package frc.robot;

/**
 * This code generates a (kinda) table of the buttons, their names, and their
 * assignments DO NOT USE in competition code: It is almost certainly
 * calculation and memory intesive
 */
public class ButtonListMaker {

  // These are what the code uses to build the stings of buttons as it goes along
  // They can be accsess individually. The index # is the button number, with
  // index zero
  // labelling the joystick. They do not end with newlines.
  StringBuilder[] joystick0List = new StringBuilder[13];
  StringBuilder[] joystick1List = new StringBuilder[13];
  StringBuilder[] joystick2List = new StringBuilder[13];

  // These contain the final string to print.
  String joystick0String;
  String joystick1String;
  String joystick2String;

  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();

  /**
   * Only call once, during initalization. This is probably the most intensive
   * function in this years robot. It invovles looping through every button
   * --twice
   */
  public void buildStrings() {
    joystick0List = new StringBuilder[13]; // Clear button lists
    joystick1List = new StringBuilder[13];
    joystick2List = new StringBuilder[13];

    for (ButtonEnum bob : ButtonEnum.values()) {
      int curJoy = bob.getJoystickNum();
      int curBut = bob.getButtonNum();

      // Abuse of the ternary operator is necessary in this case (I think)
      // This sets curList to whatever the current joysticks list is.
      StringBuilder[] curList = (curJoy == 0) ? joystick0List
          : ((curJoy == 1) ? joystick1List : ((curJoy == 2) ? joystick2List : null));

      StringBuilder curSb = curList[curBut];// Set the current stringbuilder to use

      if (0 > curJoy || 2 < curJoy || 1 > curBut || 12 < curBut) { // If button out-of-bounds
        System.out.println("Warning: Invalid button!" + bob.name());
        continue; // Exit this iteration, but continue the loop
      }
      if (curSb != null) { // If the buttons destination entry
        curSb = new StringBuilder("Button " + Integer.toString(curBut) + "Oh shoot! Two entries bound to this button!");
        continue;// Exit this iteration, but continue the loop
      }
      curSb = new StringBuilder(50); //TODO: Adjust up to whatever the final length of the stringbuilder is
      curSb.append("Button ");
      curSb.append(String.format("%2d", curBut)); //Button number padded to two zeroes
      curSb.append("Named ");
      curSb.append(String.format("%-20s", bob.name())); //Name padded to twenty charicters: left aligned
      curSb.append("Toggles ");
      curSb.append((bob.getToggledButton() != null) ? "Yah" : "Nah"); //Whether it is a toggling button
      curSb.append("Elevator Height ");
      if (bob.getElevatorPreset() == null) { 
        curSb.append("Nah"); 
      } else {//If it is an elevator controller...
        curSb.append(String.format("%3d", bob.getElevatorPreset().getHeightCM())); //state the height in CM
      }
    }
    //Set the titles on each joystick
    //The additional newline ensures an empty space between the title and the main body of the table
    joystick0List[0] = new StringBuilder("Joystick 0 (drive/left stick): \n");
    joystick1List[0] = new StringBuilder("Joystick 1 (turn/right stick): \n");
    joystick2List[0] = new StringBuilder("Joystick 2 (co-pilot's stick): \n");

    StringBuilder sb = new StringBuilder(500);
    for (StringBuilder joy : joystick0List) {
      sb.append(joy);
      sb.append("\n");
    }
    sb.append("\n \n"); //Two additional newlines ensure that there are three spaces between the end of one string and the start of the next
    joystick0String = sb.toString();
    sb.setLength(0);

    for (StringBuilder joy : joystick1List) {
      sb.append(joy);
      sb.append("\n");
    }
    sb.append("\n \n");
    joystick1String = sb.toString();
    sb.setLength(0);

    for (StringBuilder joy : joystick2List) {
      sb.append(joy);
      sb.append("\n");
    }
    sb.append("\n \n");
    joystick2String = sb.toString();
  }
}