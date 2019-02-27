package frc.robot;

import edu.wpi.first.wpilibj.Compressor;
import edu.wpi.first.wpilibj.DoubleSolenoid;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

/** The purpose of this class is to have methods to do anything we might want to do with pneumatics 
 *  This includes: 
 *      Starting a comp,
 *      stopping a comp, even in the middle of a match, 
 *      switching a double solenoid on and off,
 *      switching two double solenoids on or off in sync,
 *      switching two double solenoids on or off with a delay in between
 *      
*/
public class MagicPneumatics {
    private Compressor comp;
    private DoubleSolenoid leftCyl;
    private DoubleSolenoid rightCyl;
    public MagicPneumatics(Compressor compressor, DoubleSolenoid testLeft, DoubleSolenoid testRight) {
        comp = compressor;
        leftCyl = testLeft;
        rightCyl = testRight;
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
            } else if (ID == 1) {
                rightCyl.set(Value.kReverse);
            }
            break;
        }
    }
}

    /** create a comp object at a specified port. returns the object and makes it this class's private comp
     *  @param cLoop specifies whether closed loop control should be set 
     *  @param port the port number of the comp
     */
    public Compressor createCompressor(int port, boolean cLoop) {
      
            Compressor c = new Compressor(port);
            comp = c;
            c.setClosedLoopControl(cLoop);
            return c;
    }
    public Compressor createCompressor(int port) {
            return createCompressor(port, true);
    }
    public Compressor createCompressor() {
            return createCompressor(0, true);
    }

    // a series of methods to query this class comp
    /** This method checks whether this class's private comp is on.
     *  If this method doesn't work, you didn't create a comp!
     *  @see createCompressor
     */
        boolean isCompressorOn() {return comp.enabled();} 
    /** This method checks the state of the pressure switch on this class's private comp. If this method doesn't work, you didn't create a comp!
     *  @return true if the pressure is too low
     *  @see createCompressor
     */
    boolean getWhetherPressureIsLow() {return comp.getPressureSwitchValue();}

    /** This method checks the current on this class's private comp. If this method doesn't work, you didn't create a comp!
     *  @return current consumed by the comp in amps
     *  @see createCompressor
     */
    double getCompressorCurrent() {return comp.getCompressorCurrent();}

    boolean getCompressorClosedLoopControl() {return comp.getClosedLoopControl();}

    /**set a single given double solenoid to go forward. */
    public void setForward(DoubleSolenoid ds) {
        ds.set(DoubleSolenoid.Value.kForward);
    }

    /**set two given double solenoids to go forward. */
    public void setForward(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kForward);
        ds2.set(DoubleSolenoid.Value.kForward);
    }

    /**set a single given double solenoid to go reverse. */
    public  void setReverse(DoubleSolenoid ds) {
        ds.set(DoubleSolenoid.Value.kReverse);
    }

    /**set two given double solenoids to go reverse. */
    public void setReverse(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kReverse);
        ds2.set(DoubleSolenoid.Value.kReverse);
    }

    /**set a single given double solenoid to turn off. */
    public void setOff(DoubleSolenoid ds) {
        ds.set(DoubleSolenoid.Value.kOff);
    }

    /**set two given double solenoids to turn off. */
    public void setOff(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kOff);
        ds2.set(DoubleSolenoid.Value.kOff);
    }

    /**sets two given double solenoids to go in opposite directions.
     * @param ds the DoubleSolenoid to set forward
     * @param ds2 the DoubleSolenoid to set reverse
     */
    public void setOpposite(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kForward);
        ds2.set(DoubleSolenoid.Value.kReverse);
    }

    public void setForwardWithDelay(DoubleSolenoid ds, DoubleSolenoid ds2, double delay) {
        ds.set(DoubleSolenoid.Value.kForward);
    }
}
