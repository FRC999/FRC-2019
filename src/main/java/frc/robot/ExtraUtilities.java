package frc.robot;
public class ExtraUtilities {
    public ExtraUtilities () {
        
    }
    public int TwoButtonChecker (boolean b1, boolean b2) {
        if (b1 && !b2) {
            return 1;
        } else if (!b1 && b2) {
            return 0;
        } else {
            return -1;
        }
    }
    public static <E extends java.lang.Number> E Checker(boolean b1, boolean b2, E val) {
    if (b1 && !b2) {
        return val;
    } else if (!b1 && b2) {
        if(java.lang.Math.floor(val.doubleValue()) == val.doubleValue()) {}
    } else {
        return val;
    }
    return val;
    }
    public void checkerChecker() {
        ExtraUtilities.<Integer>Checker(true, true, 10);
        ExtraUtilities.<Double>Checker(false,true, 0.5);
    }
}