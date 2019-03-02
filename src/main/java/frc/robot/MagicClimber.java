package frc.robot;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
 /** arms turn, then after the robot reaches a certain angle, the cylinder activate.
     *  One method to begin climb,
     * one method to unclimb
     */
public class MagicClimber extends MagicPID {


   public MagicClimber () { super( 2.54*2*Math.PI, 1, .2, .0, .2, .2, 1.0, 0,  0, 12); }
   // 12 is the port number of the climber

}