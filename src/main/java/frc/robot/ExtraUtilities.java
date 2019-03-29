package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid.Value;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class ExtraUtilities {
    public ExtraUtilities () {
        
    }
    /** Checks two buttons, which should each be intended to do the opposite of the other-
     *  say, setting a motor to go forward or reverse.
     *@return the values it returns are intended to be multiplied by the set speed of the motor in the parameter list of WPI_TalonSRX.set() 
     * in order to simply make the motor go forward at a speed, in reverse at that speed, or stop.
     * If b1 is pressed and not b2, then it returns 1. 
     * if b2 is pressed and not b1, then it returns -1.
     * if both or neither are pressed, then it returns 0.
     * @param b1 the boolean of the button that sets the motor to go forward
     * @param b2 the boolean value of the button that sets the motor to go in reverse
     */
    public int TwoButtonChecker (boolean b1, boolean b2) {
        if (b1 && !b2) {
            return 1;
        } else if (!b1 && b2) {
            return -1;
        } else {
            return 0;
        }
    }
    public Value TwoButtonCheckerPneumatics(boolean b1, boolean b2) {
        if (b1 && !b2) {
            return Value.kForward;
        } else if (!b1 && b2) {
            return Value.kReverse;
        } else {
            return Value.kOff;
        }
    }
   /* public static <E extends java.lang.Number> E Checker(boolean b1, boolean b2, E val) {
    if (b1 && !b2) {
        return val;
    } else if (!b1 && b2) {
        
    } else {
        return (E);
    }
    return val;
    }
    public void checkerChecker() {
       int e = (int) ExtraUtilities.<Integer>Checker(true, true, 10);
        ExtraUtilities.<Double>Checker(false,true, 0.5);
    }
    */
}