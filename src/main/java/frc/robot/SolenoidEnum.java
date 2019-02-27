package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;

public enum SolenoidEnum {
  leftThing(1,2), rightThing(3,4);
  final private DoubleSolenoid hanSolenoid;

  private SolenoidEnum(int porta, int portb){
    hanSolenoid = new DoubleSolenoid(porta, portb);
  }
  public DoubleSolenoid getSolenoid(){return hanSolenoid;}
} 