package frc.robot;

public class MagicDriverPrints{
  
  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();
  StringBuilder sb = new StringBuilder(500);
  
  public StringBuilder addToPrint(String toPrint){
    sb.append(toPrint);
    return sb;
  }
  public String printMagicLine(){
    System.out.print("\n");
    System.out.print(sb);
    sb.delete(0,sb.length());
    return sb.toString();
  }
}