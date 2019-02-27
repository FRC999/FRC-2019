package frc.robot;

import edu.wpi.first.wpilibj.DoubleSolenoid;
/**
 * SolenoidEnum holds references to the various solenoids of the robot
 * This is for the same purposes as ButtonEnum
 * Get the solenoid like SolenoidEnum.thing.getSolenoid()
 */
public enum SolenoidEnum {
    leftThing(4,5), rightThing(6,7);
  final private DoubleSolenoid hanSolenoid;

  /**
   * Constructor. Duh.  As each solenoid requires two ports, it has 2 arguments
   * @param portA the solenoids first port
   * @param portB the solenoids seccond port
   */
  private SolenoidEnum(int portA, int portB){
    hanSolenoid = new DoubleSolenoid(portA, portB);
  }
  /**
   * Gets the solenoid
   * @return the stored solenoid
   */
  public DoubleSolenoid getSolenoid(){return hanSolenoid;}
} 