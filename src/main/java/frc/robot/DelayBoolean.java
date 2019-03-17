package frc.robot;
/**Inspired by Team 254's utility class of the same name, objects of this class have a delay
 *  (in number of cycles of perodic modes). before this delay elapses, test() returns false.
 *  After, test() returns true. */
public class DelayBoolean {
  private int delayVal;
  private int initCount;
  private int endCount;
  /**sets the delay, in number of cycles of periodic modes, that must elepse before test() can return true.  */
  public DelayBoolean(int delay) {
    delayVal = delay;
    initCount = Robot.getCycleCount();
    endCount = initCount + delayVal;
  }
  /** tests whether the current cycle number is greater than or equal to the sum of the
   *  delay number of cycles and the initial number of cycles. If it is, return true, else
   *  return false.
   */
  public boolean test() {
    return (Robot.getCycleCount() >= endCount) ? true : false;
  }
  public int getDelay() {return delayVal;}

  /**
   * get the number of cycles remaining until the delay elapses
   */
  public int getRemainingCycles() {return endCount - Robot.getCycleCount();}
  } // end class DelayBoolean