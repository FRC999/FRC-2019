package frc.robot;

import com.kauailabs.navx.frc.AHRS;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
/**
 * Intended to provide a common ground for all PID systems to make things easier
 * Inherited by classes
 */
public abstract class MagicPID {
  protected WPI_TalonSRX talon;
  MagicInput INPUT;

  static final int stepsPerRotation = 4096;
  final double circumference;

/**
   * @param tal The talon
   * @param in The MagicInput instance (till we make it a singleton)
   * @param circ The circumference of the thing (2.54*Math.PI*2 for the elevator)
   */
  MagicPID(WPI_TalonSRX _tal, MagicInput IN, double cir) {
    talon = _tal;
    INPUT = IN;
    circumference = cir;
  }

  /**
   * Converts centimeters to the native Talon units for elevator PID use
   * @param centis Centimeter height of the elevator (off the ground? not? more testing!)
   * @return the desired motor value
   */
  public int convertToNativeUnits(double centis){
    return (int) (stepsPerRotation * (centis/circumference));
  }

    /**
   * Converts native units to centimeters, for logging and puting back in MagicOutput.
   * @param input native talon units for conversion
   * @return centimer height of elevator (off the ground?), iff I got my math right
   */
  public double convertFromNativeUnits(int input){
    return (double) ((input/stepsPerRotation)*circumference);
  } 

}