package frc.robot;

import com.ctre.phoenix.motorcontrol.ControlMode;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;


/**
 * This class is to enable PID control of the elevator.  It is a WIP
 */
public class MagicElevator {
  private WPI_TalonSRX elevatorTalon;
  MagicInput INPUT;
  int eTarget;
  int eCurrent;
  static final int eMin = 200; //Test value: Annoy build team to get real value
  static final int eMax = 200000; //Test value: please let us test!
  
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    elevatorTalon = tal;
    INPUT = in; 
  }

  public int convertToNativeUnits(double centis){
    final double spoolCircumfrence = 1; //In centimeters
    final int stepsPerRotation = 4096;
    int nativity = (int) (stepsPerRotation * (centis/spoolCircumfrence));
    return nativity;
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