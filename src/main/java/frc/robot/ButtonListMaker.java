package frc.robot;

public class ButtonListMaker {
  StringBuilder[] joystick0List = new StringBuilder[13];
  StringBuilder[] joystick1List = new StringBuilder[13];
  StringBuilder[] joystick2List = new StringBuilder[13];
  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();
  StringBuilder[] curList;
  int curJoy;
  int curBut;
  public StringBuilder[] doMagic(){
    StringBuilder[] Joystick1List = new StringBuilder[13];
    StringBuilder[] Joystick2List = new StringBuilder[13];
    for (ButtonEnum bob : ButtonEnum.values()){
      curJoy = bob.getJoystickNum();
      curBut = bob.getButtonNum();
      curList = (curJoy == 0) ? joystick0List : ((curJoy == 1) ? joystick1List : ((curJoy == 2) ? joystick2List : null));
      if (curList[curBut] == null){
        curList[curBut] = new StringBuilder("Oh shoot! Two entries bound to this button!");
      } else if(curList == joystick0List) {

      }  
    }
    joystick0List[0] = new StringBuilder("Joystick 0 (drive/left stick): \n");
    joystick1List[1] = new StringBuilder("Joystick 1 (turn/right stick): \n");
    joystick2List[2] = new StringBuilder("Joystick 2 (co-pilot's stick): \n");
    return null;
  }
}