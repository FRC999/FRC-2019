package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;


/**
 * This class is to enable PID control of the elevator.  
 * It is untested, because *someone* needs to give us a working elevator.
 * WARNING: GEARBOX ON COMP-BOT IS DIFFERENT THAN SISTER-BOT
 */
public class MagicElevator extends MagicPID{
  int eTarget = eMin;
  int eCurrent; //Must be implemented
  int ePrevTarg;

	/** How much smoothing [0,8] to use during MotionMagic */
  int _smoothing = 0;
  
  static final int eMin = 20; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 2000;//In NativeUnits: Test value: please let us test!
  //circumference is pi * 2.54 * 2; In centimeters:  Spool was measured at 1 inch radius
  /**
   * @param tal The elevator talon
   * @param in The MagicInput instance (till we make it a singleton)
   */
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    super(tal, in, 2.54*2*Math.PI, 1); //FIXXXX!!!

    /* Factory default hardware to prevent unexpected behavior */
    talon.configFactoryDefault();
    
  	/**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		talon.setSensorPhase(true);
		talon.setInverted(false);

		/* Configure Sensor Source for Pirmary PID */
    talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    MagicConstants.kPIDLoopIdx, 
    MagicConstants.kTimeoutMs);

    /* Set relevant frame periods to be at least as fast as periodic rate */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, MagicConstants.kTimeoutMs);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, MagicConstants.kTimeoutMs);

		/* Set the peak and nominal outputs */
		talon.configNominalOutputForward(0, MagicConstants.kTimeoutMs);
		talon.configNominalOutputReverse(0, MagicConstants.kTimeoutMs);
		talon.configPeakOutputForward(1, MagicConstants.kTimeoutMs);
		talon.configPeakOutputReverse(-1, MagicConstants.kTimeoutMs);

    /* Set Motion Magic gains in slot0 - see documentation */
    talon.selectProfileSlot(MagicConstants.kSlotIdx, MagicConstants.kPIDLoopIdx);
    talon.config_kF(MagicConstants.kSlotIdx, MagicConstants.kF, MagicConstants.kTimeoutMs);
		talon.config_kP(MagicConstants.kSlotIdx, MagicConstants.kP, MagicConstants.kTimeoutMs);
		talon.config_kI(MagicConstants.kSlotIdx, MagicConstants.kI, MagicConstants.kTimeoutMs);
    talon.config_kD(MagicConstants.kSlotIdx, MagicConstants.kD, MagicConstants.kTimeoutMs);
    
  	/* Set acceleration and vcruise velocity - see documentation */
    talon.configMotionCruiseVelocity(15000, MagicConstants.kTimeoutMs);
    talon.configMotionAcceleration(6000, MagicConstants.kTimeoutMs);
    
  	/* Zero the sensor */
    talon.setSelectedSensorPosition(0, MagicConstants.kPIDLoopIdx, MagicConstants.kTimeoutMs);

    // Use magical S- Curve magic
    talon.configMotionSCurveStrength(_smoothing);
  }

  /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input/stepsPerRotation)*circumference);
  } 

  //Check if the elevator button is pressed: if yes, do stuff
  public void updateElevatorTarget () {
    eTarget = convertToNativeUnits(INPUT.getElevatorTarget());
    
    //if (eTarget > eMax) {eTarget = eMax; System.out.println("over max");} //No going above the height limit
    //if (eTarget < eMin) {eTarget = eMin; System.out.println("under min");} //No going below it, either
    INPUT.setElevatorTarget(convertFromNativeUnits(eTarget)); //Update the validated target in MagicInput
  }

  public double setElevatorTargetNU(int targ){
    ePrevTarg = eTarget;
    eTarget = targ;
    return INPUT.setElevatorTarget(convertToNativeUnits(targ));
  }
  

  /**
   * Gets the elevator position, you numbskull
   * @return elevator position (In native units)
   */
  public int getElevatorPos() {
    return talon.getSelectedSensorPosition(0);
  }

  public void elevatorPeriodic() {
    eCurrent = getElevatorPos();
    updateElevatorTarget();
    if (eCurrent == eTarget) {talon.setNeutralMode(NeutralMode.Brake);}
    else {moveElevator();}
  }
  /**
   * Moves the elevator to the current target.  Or it should.
   */
  public void moveElevator() { 
    if (ePrevTarg != eTarget){
      talon.set(ControlMode.MotionMagic, eTarget);
      System.out.println("Elevator in motion");
    }
  }
}