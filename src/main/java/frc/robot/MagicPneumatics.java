package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class MagicPneumatics {
    private Compressor compressor;
    private DoubleSolenoid leftCyl;
    private DoubleSolenoid rightCyl;
    public MagicPneumatics(Compressor comp, DoubleSolenoid testLeft, DoubleSolenoid testRight) {
        compressor = comp;
        leftCyl = testLeft;
        rightCyl = testRight;
        compressor.setClosedLoopControl(true);
    }
    public void setCyl(int ID, int state) { // -1 for back, 0 for off, 1 for forward
    switch (state) {
        case 1 : {
            if (ID == 1) {
                leftCyl.set(Value.kForward);
            } else if (ID == 0) {
                rightCyl.set(Value.kForward);
            }
            break;
        }
    case 0 : {
        if (ID == 1) {
            leftCyl.set(Value.kOff);
        } else if (ID == 0) {
            rightCyl.set(Value.kOff);
        }
        break;
    }
    case -1 : {
        if (ID == 1) {
            leftCyl.set(Value.kReverse);
        } else {
            rightCyl.set(Value.kReverse);
        }
        break;
    }
    }
}
}