/*----------------------------------------------------------------------------*/
/* Copyright (c) 2018 FIRST. All Rights Reserved.                             */
/* Open Source Software - may be modified and shared by FRC teams. The code   */
/* must be accompanied by the FIRST BSD license file in the root directory of */
/* the project.                                                               */
/*----------------------------------------------------------------------------*/

package frc.robot;
//NEEDS TO BE MORE GENERAL, WILL REVIEW AT EARLIEST CONVIENIENCE
public enum EncoderPresets { //*** MUST CONVERT FROM IN. TO CM! ***
    lowHatch(20,0), midHatch(48,0), highHatch(77,0),
    lowBall(24,0), midBall(55,0), highBall(83,0),
    cargoShipBall(43,0), floor(0,0), retract(0,0);
    final private double heightCM;
    final private int heightNU;
    final private double elbowAngleDE;
    final private int elbowAngleNU;
    final private double wristAngleDE;
    final private int wristAngleNU;
    private EncoderPresets(double height, double angle) {
        heightCM = height*2.54;
        heightNU = (int) (4096 * (height / (2.54 * Math.PI))); //CLARIFY
        elbowAngleDE = angle;
        elbowAngleNU = (int) (4096 / 360 * angle);
        wristAngleDE = -angle; 
        wristAngleNU = -elbowAngleNU;
    }
    private EncoderPresets(double height, double angleE, double angleW){
        heightCM = height*2.54;        
        heightNU = (int) (4096 * (height / (2.54 * Math.PI)));
        elbowAngleDE = angleE;
        elbowAngleNU = (int) (4096 / 360 * angleE);
        wristAngleDE = angleW;
        wristAngleNU = (int) (4096 / 360 * angleW);
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
    /**
     * @return the angle of the elbow
     */
    public double getElbowAngleDE() {
        return elbowAngleDE;
    }
    /**
     * @return the angle of the wrist
     */
    public double getWristAngleDE() {
        return wristAngleDE;
    }
    /**
     * @return the angle of the elbow in NU's
     */
    public int getElbowAngleNU() {
        return elbowAngleNU;
    }
    /**
     * @return the angle of the wrist in NU's
     */
    public int getWristAngleNU() {
        return wristAngleNU;
    }

}
