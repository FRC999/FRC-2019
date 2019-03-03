package frc.robot;
import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.VictorSPX;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;
import edu.wpi.first.wpilibj.DoubleSolenoid;
 /** arms turn, then after the robot reaches a certain angle, the cylinder activate.
     *  One method to begin climb,
     * one method to unclimb
     */
public class MagicClimber extends MagicPID {

    WPI_VictorSPX climbFollower;
/**@param port the port number of the main climber talon
 * @param portVictor the port number of the victor that should follow this 
 */
   public MagicClimber (int port, int portVictor) { // this season, the port is 12, and the portVictor is 
       super( 2.54*2*Math.PI, 1, .2, .0, .2, .2, 1.0, 0,  0, port);
      WPI_VictorSPX climbFollower =new WPI_VictorSPX(portVictor);
climbFollower.set(ControlMode.Follower, port);
} // edit these numbers to tune
   // 12 is the port number of the climber


}