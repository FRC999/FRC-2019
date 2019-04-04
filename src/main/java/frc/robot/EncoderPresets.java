/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;

//NEEDS TO BE MORE GENERAL, WILL REVIEW AT EARLIEST CONVENIENCE
public enum EncoderPresets {
  lowHatch(50), midHatch(123), highHatch(196), lowBall(61), midBall(140), highBall(211), cargoShipBall(110), floor(0),
  retract(0);
  final private double heightCM;
  final private int heightNU;

  private EncoderPresets(double height) {
    heightCM = height;
    heightNU = (int) (4096 * (height / (2.54 * Math.PI))); // 2.54 is the diameter of the spool in CM
  }

  /**
   * @return the height in CM
   */
  public double getHeightCM() {
    return heightCM;
  }

  /**
   * @return the height in native units
   */
  public int getHeightNU() {
    return heightNU;
  }

}
