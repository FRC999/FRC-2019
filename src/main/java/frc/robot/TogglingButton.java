package frc.robot;

class TogglingButton {
  //inspired by Cheesy Poof's class LatchedBoolean
  //https://github.com/Team254/FRC-2018-Public/blob/master/src/main/java/com/team254/lib/util/LatchedBoolean.java
  //boolean currentButtonState;
  boolean lastButtonState;
  boolean toggleState = false; 
  
  //constructors
  public TogglingButton (boolean ToggleState) {this.toggleState = ToggleState;}
  public TogglingButton () {toggleState = false;}
  
  public boolean update (boolean currentButtonState)  {
    //first, test whether the button was only just activated by using  the test
    if (currentButtonState && !lastButtonState) {
      //If this button was only just pressed, then  change the state of the toggle using 
      toggleState = !toggleState; }
  //last,  update the value of lastButtonState so that the toggle is not activated every 20 //milliseconds during the interval in which the  button is pressed
    lastButtonState = currentButtonState;
    return toggleState;
    }
  } 