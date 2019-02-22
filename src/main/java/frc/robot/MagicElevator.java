package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;


/**
 * This class is to enable PID control of the elevator.  It is a WIP
 */
public class MagicElevator {
  private WPI_TalonSRX ElevatorTalon;
  MagicInput INPUT;
  int eTarget;
  public MagicElevator(WPI_TalonSRX tal, MagicInput in) {
    ElevatorTalon = tal;
    INPUT = in; 
  }    

  //Check if the elevator button is pressed: if yes, do stuff
  public void updateElevatorTarget () {
    eTarget = INPUT.getElevatorTarget();
  }
}