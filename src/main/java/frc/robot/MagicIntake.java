package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class MagicIntake  {
    private WPI_TalonSRX wristTalon;
    private WPI_TalonSRX elbowTalon;
    private WPI_VictorSPX leftIntake;
    private WPI_VictorSPX rightIntake;
    private DoubleSolenoid cableSolenoid;
    MagicPneumatics PNEUMATICS;
    private ElevatorPresets currentPreset;
    public MagicIntake(WPI_TalonSRX wrist, WPI_TalonSRX elbow, WPI_VictorSPX left, WPI_VictorSPX right) {
        wristTalon = wrist;
        elbowTalon = elbow;
        leftIntake = left;
        rightIntake = right;
    }
    /**
     * Moves the elbow, *without* changing the position of the wrist.
     * @param goal the target, in Native Units.  If you want to input in degrees, too bad.
     */
    public void moveElbow (int goal){
        elbowTalon.set(ControlMode.MotionMagic, goal);
        currentPreset = null;
    }
    public void moveWrist (int goal){
        wristTalon.set(ControlMode.MotionMagic, goal);
        currentPreset = null;
    }
    public void gotoPreset(ElevatorPresets preset) {
        if (currentPreset != preset){
            wristTalon.set(ControlMode.MotionMagic, preset.getWristAngleNU());
            elbowTalon.set(ControlMode.MotionMagic, preset.getElbowAngleNU());
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