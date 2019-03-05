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
    /**
     * @param left the left double solenoid, or the main one
     * @param right the right double solenoid, or null
     */
    public MagicPneumatics(DoubleSolenoid left, DoubleSolenoid right) {
        comp = createCompressor();
        leftCyl = left;
        rightCyl = right;

    }
    /**
     * Sets a designated cylinder.  Has unclear purpose in the cutthroat modern world
     * @param ID The "id" number of the cylinder.  1 for left, 0 for right.
     * @param state -1 for reverse, 0 for off, 1 for forward
     */

    
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
            try {
                Compressor c = new Compressor(port);
                comp = c;
                c.setClosedLoopControl(cLoop);
                return c;

            } catch (Exception e) {
                System.out.println("Well shoot, compresser startup failed");
                return null;
            }
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
    public void setForward(){setForward(leftCyl);}
    public void setRightForward(){setForward(rightCyl);}

    /**set two given double solenoids to go forward. */
    public void setForward(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kForward);
        ds2.set(DoubleSolenoid.Value.kForward);
    }

    /**set a single given double solenoid to go reverse. */
    public  void setReverse(DoubleSolenoid ds) {
        ds.set(DoubleSolenoid.Value.kReverse);
    }
    public void setReverse(){setReverse(leftCyl);}
    public void setRightReverse(){setReverse(rightCyl);}

    /**set two given double solenoids to go reverse. */
    public void setReverse(DoubleSolenoid ds, DoubleSolenoid ds2) {
        ds.set(DoubleSolenoid.Value.kReverse);
        ds2.set(DoubleSolenoid.Value.kReverse);
    }

    /**set a single given double solenoid to turn off. */
    public void setOff(DoubleSolenoid ds) {
        ds.set(DoubleSolenoid.Value.kOff);
    }
    public void setOff() {setOff(leftCyl);}
    public void setRightOff() {setOff(rightCyl);}

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
/** Sets one DoubleSolenoid to one state, waits for a certain number of cycles, then sets another DoubleSolenoid to another state.
 * 
 * @param ds the DoubleSolenoid to be activated first.
 * @param stateOfDoubleSolenoid1 the state to set the first DoubleSolenoid.
 * @param stateOfDoubleSolenoid2 the state to set the second DoubleSolenoid.
 * @param ds2 the DoubleSolenoid to be activated second.
 * @param delay the deay between the activations in numbers of cycles of periodic modes
 * @param ID an arbitrary naming String to set this particular delay operation to distinguish it from any other delays happening at the time.
 * @return true if the delay is over and the second DoubleSolenoid has been activated, false otherwise.
*/
public boolean setWithDelay(DoubleSolenoid ds, DoubleSolenoid.Value stateOfDoubleSolenoid1, DoubleSolenoid.Value stateOfDoubleSolenoid2, DoubleSolenoid ds2, int delay, String ID) {
    
    ds.set(stateOfDoubleSolenoid1);
    if (MagicDelay.getInstance().startDelay(delay, ID).test()) {
        ds2.set(stateOfDoubleSolenoid2);
        MagicDelay.getInstance().clearMemory();
        return true;
    }
    else return false;
}
}