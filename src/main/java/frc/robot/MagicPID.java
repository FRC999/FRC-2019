package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 * Intended to provide a common ground for all PID systems to make things easier (ish)
 * Inherited by class MagicElevator
 */
public abstract class MagicPID {
  protected WPI_TalonSRX talon;
  MagicInput INPUT;

  static final int stepsPerRotation = 4096;
  final double circumference;
  final double gearRatio;

/**
   * @param _tal The talon
   * @param IN The MagicInput instance (till we make it a singleton)
   * @param cir The circumference of the thing (2.54*Math.PI*2 for the elevator)
   * @param gearRat The ratio of the connected gearbox (imput rotations/output rotations)
   */
  MagicPID(WPI_TalonSRX _tal, MagicInput IN, double cir, double gearRat) {
    talon = _tal;
    INPUT = IN;
    circumference = cir;
    gearRatio = gearRat;
  }

  /**
   * Converts centimeters to the native Talon units for elevator PID use
   * @param imput Centimeter height of the elevator (off the ground? not? more testing!)
   * @return the desired motor value
   */
  public int convertToNativeUnits(double imput){
    return (int) (stepsPerRotation * (imput/circumference) * gearRatio);
  }

    /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input*circumference) / stepsPerRotation / gearRatio); //Yes, I checked my math
  } 

}