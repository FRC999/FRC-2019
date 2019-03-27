package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MagicVision {
  
  private String targetPosition;
  private int startOfDataStream;
  private int endOfDataStream;
  private SerialPort ard;
  private int xVal;
  private int yVal;
  private int wVal;
  private int hVal;
  private int distValLeft;
  private int confValLeft;
  private int distValRight;
  private int confValRight;
  private int blocksSeen;
  private int arduinoCounter;
  final double speed = .5;
  final int leftMax = 130;
  final int rightMax = 170;
  final int min = 0;
  final int max = 316;
  
  public MagicVision() {
  }
  public void parse () {
    targetPosition = ard.readString();
    startOfDataStream = targetPosition.indexOf("B");
    endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
  //indexOf returns -1 if it cannot find either char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
    targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
    System.out.println(targetPosition);
    if (targetPosition.startsWith("Block")) {
      String[] positionNums = targetPosition.split(":");
      // positionNums[0] would be "Block
      // positionNums [1] would be number of block: always 0
      xVal = Integer.parseInt(positionNums[2]);
      yVal = Integer.parseInt(positionNums[3]);
      wVal = Integer.parseInt(positionNums[4]);
      hVal = Integer.parseInt(positionNums[5]);
      distValLeft = Integer.parseInt(positionNums[6]);
      confValLeft = Integer.parseInt(positionNums[7]);
      blocksSeen = Integer.parseInt(positionNums[1]);
      arduinoCounter = Integer.parseInt(positionNums[8]);
      distValRight = Integer.parseInt(positionNums[9]);
      confValRight = Integer.parseInt(positionNums[10]);
    } else {
      //System.out.println("Bad String from Arduino: Doesn't start with Block");
    }
  } else {
    //System.out.println("Bad String from Arduino: no carriage return character or too short");
  }
  }
  public int getX() {return xVal;}
  public int getY() {return yVal;}
  public int getW() {return wVal;}
  public int getH() {return hVal;}
  public int getDistLeft() {return distValLeft;}
  public int getDistRight() {return distValRight;}
  public int getConfLeft() {return confValLeft;}
  public int getConfRight() {return confValRight;}
  public int getBlocksSeen() {return blocksSeen;}
  public int getArduinoCounter() {return arduinoCounter;}
}