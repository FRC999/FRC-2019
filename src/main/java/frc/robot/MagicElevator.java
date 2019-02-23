package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/**
 * This class is to enable PID control of the elevator.  
 * It is untested, because *someone* needs to give us a working elevator.
 */
public class MagicElevator {
  private WPI_TalonSRX elevatorTalon;
  MagicInput INPUT;
  int eTarget;
  int eCurrent;

  static final int eMin = 200; //In NativeUnits: Test value: Annoy build team to get real value
  static final int eMax = 200000; //In NativeUnits: Test value: please let us test!
  static final double spoolCircumfrence = 1; //In centimeters:  Require a spool to measure
  static final int stepsPerRotation = 4096;
  
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    elevatorTalon = tal;
    INPUT = in; 
  }

  public int convertToNativeUnits(double centis){
    return (int) (stepsPerRotation * (centis/spoolCircumfrence));
  }
  public double convertFromNativeUnits(int imput){
    return (double) ((imput/stepsPerRotation)*spoolCircumfrence);
  } 

  //Check if the elevator button is pressed: if yes, do stuff
  public void updateElevatorTarget () {
    eTarget = convertToNativeUnits(INPUT.getElevatorTarget());
    if (INPUT.isButtonOn(ButtonEnum.elevatorUp)) {
      if (eMax - eTarget > 20) {
        eTarget +=20;
      } else {
        eTarget = eMax;
      }
    }
    if (INPUT.isButtonOn(ButtonEnum.elevatorDown)) {
      if (INPUT.isButtonOn(ButtonEnum.elevatorUp)) {
        if (eMin - eTarget < 20) {
          eTarget -=20;
        } else {
          eTarget = eMin;
        }
      }
    }
  }
  public void elevatorPeriodic() {
    updateElevatorTarget();
    moveElevator();
  }
  public void moveElevator() { 
    elevatorTalon.set(ControlMode.MotionMagic, eTarget);
  }
}