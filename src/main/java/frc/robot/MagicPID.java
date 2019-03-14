package frc.robot;

import com.ctre.phoenix.motorcontrol.NeutralMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.StatusFrameEnhanced;
import com.ctre.phoenix.motorcontrol.ControlMode;


/**
 * Intended to provide a common ground for all PID systems to make things easier (ish).
 * Each instance builds and operates one WPI_TalonSRX for PID control.
 * Inherited by classes MagicElevator, MagicClimber, .
 * heavily based on CTRE's example PID code,
 * @see https://github.com/CrossTheRoadElec/Phoenix-Examples-Languages/blob/92520fe425e63520f4a8e73ab3edac9890eeaeff/Java/MotionMagic/src/main/java/frc/robot/Robot.java
 */
public class MagicPID {
  protected WPI_TalonSRX talon;
  MagicInput INPUT;

  static final int stepsPerRotation = 4096;
  final double circumference;
  final double gearRatio;
  private int curTargetNU;


  /**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public final int kSlotIdx;

	/**
	 * Talon SRX/ Victor SPX will supported multiple (cascaded) PID loops. For
	 * now we just want the primary one.
	 */
	public static final int kPIDLoopIdx = 0;

	/**
	 * set to zero to skip waiting for confirmation, set to nonzero to wait and
	 * report to DS if action fails.
	 */
  public static final int kTimeoutMs = 0;

  public double kP;
  public double kI;
  public double kD;
  public double kF;
  public int iZone = 0;
  public double kPeakOutput;
  public int _smoothing;
  public final int min;
  public final int max;
  public final int startPos;

  protected int currentPos;


/**
   *
   * @param cir The circumference of the spool/wheel (2.54*Math.PI*2 for the elevator)
   * @param gearRat The ratio of the connected gearbox (imput rotations/output rotations)
   * @param peakOutput
   * @param slot "Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based configuration." NOT the port number.
   * @param smoothee how much s-curve smoothing to apply
   * @param port the port number of the talon that this class operates
   * @param startPoint the starting point of the system.  Ie, if the system starts at 90 ticks positive of
   * it's intended zero, set to 90.
   * @param mi the minimum point (for validation)
   * @param ma the maximum point (for validation)
   */
  MagicPID(double cir, double gearRat, double P, double I, double D, double F, double peakOutput, int slot, int smoothee, int port, int startPoint, int mi, int ma) {
    talon = new WPI_TalonSRX(port);
    INPUT = MagicInput.getInstance();
    circumference = cir;
    gearRatio = gearRat;
    kP = P;
    kI = I;
    kD = D;
    kF = F;
    kPeakOutput = peakOutput;
    kSlotIdx = slot;
    _smoothing = smoothee;
    min = mi;
    max = ma;
    startPos = startPoint;





    /* Factory default hardware to prevent unexpected behavior */
    talon.configFactoryDefault();

  	/**
		 * Configure Talon SRX Output and Sesnor direction accordingly
		 * Invert Motor to have green LEDs when driving Talon Forward / Requesting Postiive Output
		 * Phase sensor to have positive increment when driving Talon Forward (Green LED)
		 */
		talon.setSensorPhase(true);
		talon.setInverted(false);

		/* Configure Sensor Source for Primary PID */
    talon.configSelectedFeedbackSensor(FeedbackDevice.CTRE_MagEncoder_Relative,
    kPIDLoopIdx,
    kTimeoutMs);

    /* Set relevant frame periods to be at least as fast as periodic rate */
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_13_Base_PIDF0, 10, kTimeoutMs);
		talon.setStatusFramePeriod(StatusFrameEnhanced.Status_10_MotionMagic, 10, kTimeoutMs);

		/* Set the peak and nominal outputs */
		talon.configNominalOutputForward(0, kTimeoutMs);
		talon.configNominalOutputReverse(0, kTimeoutMs);
		talon.configPeakOutputForward(1, kTimeoutMs);
		talon.configPeakOutputReverse(-1, kTimeoutMs);

    /* Set Motion Magic gains in slot0 - see documentation */
    talon.selectProfileSlot(kSlotIdx, kPIDLoopIdx);
    talon.config_kF(kSlotIdx, kF, kTimeoutMs);
		talon.config_kP(kSlotIdx, kP, kTimeoutMs);
		talon.config_kI(kSlotIdx, kI, kTimeoutMs);
    talon.config_kD(kSlotIdx, kD, kTimeoutMs);

  	/* Set acceleration and vcruise velocity - see documentation */
    talon.configMotionCruiseVelocity(15000, kTimeoutMs);
    talon.configMotionAcceleration(6000, kTimeoutMs);

  	/* Zero the sensor */
    talon.setSelectedSensorPosition(startPoint, kPIDLoopIdx, kTimeoutMs);

    // Use magical S- Curve magic
    talon.configMotionSCurveStrength(_smoothing);
  }

  /**
   * Zero sensor
   */
  public void zeroSensor(){
    talon.setSelectedSensorPosition(0, kPIDLoopIdx, kTimeoutMs);
  }

  /**
   * Re-set sensor to another value
   */
  public void setSensor (int set){
    talon.setSelectedSensorPosition(set, kPIDLoopIdx, kTimeoutMs);
  }
  /**
   * Converts centimeters to the native Talon units for elevator PID use
   * @param input Centimeter height of the elevator (off the ground? not? more testing!)
   * @return the desired motor value
   */
  public int convertToNativeUnits(double input){
    return (int) (stepsPerRotation * (input/circumference) * gearRatio);
  }

    /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input*circumference) / stepsPerRotation / gearRatio); //Yes, I checked my math
  }
/**sets the talon's neutral mode to brake, and starts braking */
  public void freeze(){
    talon.setNeutralMode(NeutralMode.Brake);
    talon.set(0);
  }
  /**sets the talon's neut. mode to coast, and starts to sliiide */
  public void slide(){
    talon.setNeutralMode(NeutralMode.Coast);
    talon.set(0);
  }
/** sets the talon's motionmagic to move to a new position.
 *
 * @param newPos the setpoint "in encoder ticks or an analog value, depending on the sensor", according to CTRE's javadocs on the WPI_TalonSRX
*/
  public void setTarget(int newPos){
    if (curTargetNU != newPos){
      validateTarget(newPos);
    }
  }
  /**
   * Moves talon to the selected position
   */
  public void startMotion(){
    if(curTargetNU != getCurrentPos()){
      talon.set(ControlMode.MotionMagic, curTargetNU);
    }
    else {
      freeze();
    }
  }

  public WPI_TalonSRX getTalon(){return talon;}
  public int getCurrentPos() {
    currentPos = talon.getSelectedSensorPosition(0);
    return currentPos;
  }

  /**
   * validates the target, then sets it
   */
  public int validateTarget(int targ){
    if (targ > max) {
      setTarget(max-1);
      System.out.println("over max");

      return max-1;
    }
    else if (targ < min) {
      setTarget(min+1);
      System.out.println("under min");

      return min+1;
    }
    else{
      curTargetNU = targ;
      return targ;
    }
  }
  /**
   *  Validates existing target, updating it if nessessary
   */
  public void validateTarget(){
    validateTarget(curTargetNU);

  }
  /**
   * Sets target to be the starting position
   */
  public void retract(){
    setTarget(startPos);

  }
  public int getTarget (){
    return curTargetNU;
  }
  public int increaseTarget(int num){
    setTarget(curTargetNU+=num);
    return getTarget();
  }
}