package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;

public class MagicPID {
    private WPI_TalonSRX ElevatorTalon;
    MagicInput INPUT;
    int P,I,D = 1;
    public MagicPID(WPI_TalonSRX tal, MagicInput in) {
        ElevatorTalon = tal;
        INPUT = in; 
    }    

}