package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class MagicIntake  {
    private MagicPID wristTalon;
    private MagicPID elbowTalon;
    private WPI_VictorSPX leftIntake;
    private WPI_VictorSPX rightIntake;
    private DoubleSolenoid cableSolenoid;
    MagicPneumatics PNEUMATICS;
    private ElevatorPresets currentPreset;
    public MagicIntake(int wrist, int elbow, int left, int right) {
        wristTalon = new MagicPID (0,1,0,0,0,0,.5,1,1,wrist, 0);
        elbowTalon = new MagicPID (0,1,0,0,0,0,.5,1,1,elbow, 0);
        leftIntake = new WPI_VictorSPX(left);
        rightIntake = new WPI_VictorSPX(right);
    }
    /**
     * Moves the elbow, *without* changing the position of the wrist.
     * @param goal the target, in Native Units.  If you want to input in degrees, too bad.
     */
    public void moveElbow (int goal){
        elbowTalon.moveTo(goal);
        currentPreset = null;
    }
    public void moveWrist (int goal){
        wristTalon.moveTo(goal);
        currentPreset = null;
    }
    public void gotoPreset(ElevatorPresets preset) {
        if (currentPreset != preset){
            wristTalon.moveTo(preset.getWristAngleNU());
            elbowTalon.moveTo(preset.getElbowAngleNU());
            currentPreset = preset;
        }
        
    }
    public void intakeCargo(double speed) {       
      leftIntake.set(speed);
      rightIntake.set(-speed);
    }
    public void fireCargo(double speed) {
      leftIntake.set(-speed);
      rightIntake.set(speed);
    }
    public void openJaws() {
      PNEUMATICS.setForward(cableSolenoid);
    }
    public void closeJaws() {
      PNEUMATICS.setReverse(cableSolenoid);
    }

}