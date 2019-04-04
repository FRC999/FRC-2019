package frc.robot;

public class MagicDriverPrints {
  private static MagicDriverPrints minstance = new MagicDriverPrints();

  public static MagicDriverPrints getInstance() {
    return minstance;
  }

  MagicJoystickInput INPUT = MagicJoystickInput.getInstance();
  StringBuilder sb = new StringBuilder(500);

  public MagicDriverPrints addToPrint(String toPrint) {
    sb.append(toPrint);
    return minstance;
  }

  public String printMagicLine() {
    System.out.print("\n");
    System.out.print(sb);
    sb.setLength(0);
    return sb.toString();
  }
}