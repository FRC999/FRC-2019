package frc.robot;

/**
 * This class has an unkown purpose: it is intended to hold values to help with configuration,
 * but most of that seems to happen in the Magic classes.
 *
 */
public class MagicConstants {

	/**
	 * Which PID slot to pull gains from. Starting 2018, you can choose from
	 * 0,1,2 or 3. Only the first two (0,1) are visible in web-based
	 * configuration.
	 */
	public static final int kSlotIdx = 0;

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

	/**
	 * Gains used in Motion Magic, to be adjusted accordingly
   * Gains(kp, ki, kd, kf, izone, peak output);
   */
  public static final double kP = 0.2;
  public static final double kI = 0.0;
  public static final double kD = 0.2;
  public static final double kF = 0.2;
  public static final int iZone = 0;
  public static final double kPeakOutput = 1.0;

}