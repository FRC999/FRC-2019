package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MagicVision {
  String [] positionNums;
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
  final double speed = .25;
  final int leftMax = 130;
  final int rightMax = 170;
  final int min = 0;
  final int max = 316;
  
  public MagicVision() {
  }
  public String[] parse (SerialPort arduino) {
    targetPosition = arduino.readString();
    startOfDataStream = targetPosition.indexOf("B");
    endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
  //indexOf returns -1 if it cannot find either char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
    targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
    System.out.println(targetPosition);
    if (targetPosition.startsWith("Block")) {
       positionNums = targetPosition.split(":");
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
   //   distValRight = Integer.parseInt(positionNums[9]);
   //   confValRight = Integer.parseInt(positionNums[10]);
    } else {
      //System.out.println("Bad String from Arduino: Doesn't start with Block");
    }
  } else {
    //System.out.println("Bad String from Arduino: no carriage return character or too short");
  }
  return positionNums;
  }
  public int parseVal (SerialPort arduino, int val) {
    targetPosition = arduino.readString();
    startOfDataStream = targetPosition.indexOf("B");
    endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
  //indexOf returns -1 if it cannot find either char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
    targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
    if (targetPosition.startsWith("Block")) {
       positionNums = targetPosition.split(":");
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
   //   distValRight = Integer.parseInt(positionNums[9]);
   //   confValRight = Integer.parseInt(positionNums[10]);
    } else {
      //System.out.println("Bad String from Arduino: Doesn't start with Block");
    }
  } else {
    //System.out.println("Bad String from Arduino: no carriage return character or too short");
  }
  System.out.println(Integer.parseInt(positionNums[val]));
  return Integer.parseInt(positionNums[val]);
  }
  public void track (SpeedControllerGroup l, SpeedControllerGroup r, int val) {
  //    System.out.println("targetPosition = null");
    if (val < 130 && val > 0/* && distVal > 500 */) {
      l.set(0);
      r.set(speed);
 //     System.out.println("xVal < (316/2) && distVal > 500");
    } else if (val > 130 && val < 170 /*&& distVal > 500 */) {
     l.set(-speed);
     r.set(speed);
     //System.out.println("xVal == (316/2) && distVal > 500");
    } else if (val > 170 /*&& distVal > 500 */) {
     l.set(-speed);
     r.set(0);
     //System.out.println("xVal > (316/2) && distVal > 500");
    } else {
     System.out.println("none of the if statements in auto periodic applied, distval probably <500");
     l.set(0);
     r.set(0);
    }
  }
}