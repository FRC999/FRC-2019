package frc.robot;

import com.ctre.phoenix.motorcontrol.FeedbackDevice;
import com.ctre.phoenix.motorcontrol.can.WPI_TalonSRX;
import com.kauailabs.navx.frc.AHRS;

public class MagicPID {
    private WPI_TalonSRX ElevatorTalon;
    MagicInput INPUT;
    public MagicPID(WPI_TalonSRX tal, MagicInput in) {
        ElevatorTalon = tal;
        INPUT = in; 

    }    

}