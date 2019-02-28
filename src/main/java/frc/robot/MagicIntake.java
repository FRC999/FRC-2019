package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.ctre.phoenix.motorcontrol.can.WPI_VictorSPX;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public class MagicIntake  {
    private WPI_TalonSRX headTalon;
    private WPI_TalonSRX armTalon;
    private WPI_VictorSPX leftIntake;
    private WPI_VictorSPX rightIntake;
    private DoubleSolenoid cableSolenoid;
    MagicPneumatics PNEUMATICS;
    public MagicIntake(WPI_TalonSRX head, WPI_TalonSRX arm, WPI_VictorSPX left, WPI_VictorSPX right) {
        headTalon = head;
        armTalon = arm;
        leftIntake = left;
        rightIntake = right;
    }
    public void Rotate(WPI_TalonSRX appendage, int setpoint) {
        appendage.set(ControlMode.MotionMagic, setpoint);
    }
    public void Intake(double speed) {       
      leftIntake.set(speed);
      rightIntake.set(-speed);
    }
    public void Outtake(double speed) {
      leftIntake.set(-speed);
      rightIntake.set(speed);
    }
    public void OpenJaws() {
      PNEUMATICS.setForward(cableSolenoid);
    }
    public void CloseJaws() {
        PNEUMATICS.setReverse(cableSolenoid);
    }

}