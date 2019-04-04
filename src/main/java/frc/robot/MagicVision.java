package frc.robot;

import java.util.concurrent.TimeUnit;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MagicVision {
  private int bRate;
  private int counting = 0;
  private int xVal; //In order of appearance
  private int yVal;
  private int wVal;
  private int hVal;
  private int lDistVal;
  private int lConfVal;
  private int rDistVal;
  private int rConfVal;
  private int stopDistance;
  private int confidenceThreshold;
  private int delayCount;
  private int arduinoCounter; // loop counter passed from arduino for timing checks
  private int startOfDataStream;
  private int endOfDataStream;
  private int blocksSeen;
  private SerialPort arduinoPort;
  final int leftMax = 154;
  final int rightMax = 162;
  boolean left;
  boolean right;
  boolean middle;
  boolean backwards;
  public void removeFreakingAnnoyingVSCodeWarnings(){if(stopDistance + confidenceThreshold +delayCount +startOfDataStream+endOfDataStream==0){}}
  public MagicVision(int baud, int stop) {
    counting = 0;
    bRate = baud;
    xVal = 0;
    yVal = 0;
    wVal = 0;
    hVal = 0;
    lDistVal = 0;
    lConfVal = 0;
    rDistVal = 0;
    rConfVal = 0;
    blocksSeen = 0;
    arduinoCounter = 0;
    stopDistance = stop;
    confidenceThreshold = 500;
    delayCount = 1;
  }

  public MagicVision(int baud, int stop, int delay, int conf) {
    counting = 0;
    bRate = baud;
    xVal = 0;
    yVal = 0;
    wVal = 0;
    hVal = 0;
    lDistVal = 0;
    lConfVal = 0;
    rDistVal = 0;
    rConfVal = 0;
    blocksSeen = 0;
    arduinoCounter = 0;
    stopDistance = stop;
    confidenceThreshold = conf;
    delayCount = delay;
  }

  public MagicVision(int baud) {
    counting = 0;
    bRate = baud;
    xVal = 0;
    yVal = 0;
    wVal = 0;
    hVal = 0;
    lDistVal = 0;
    lConfVal = 0;
    rDistVal = 0;
    rConfVal = 0;
    blocksSeen = 0;
    arduinoCounter = 0;
    stopDistance = 200;
    confidenceThreshold = 500;
    delayCount = 1;

  }
  public SerialPort startArduino() {
    try {
      SerialPort arduino = new SerialPort(bRate, SerialPort.Port.kUSB);
      System.out.println("Connected to kUSB");
      return arduino;
    } catch (Exception e) {
      System.out.println("Couldn't connect to kUSB, trying kUSB1");
      try {
        SerialPort arduino = new SerialPort(bRate, SerialPort.Port.kUSB2);
        System.out.println("Connected to kUSB2");
        return arduino;
      } catch (Exception e2) {
        System.out.println("Not connected to any of the USB ports, trying MXP spot");
        try {
          SerialPort arduino = new SerialPort(bRate, SerialPort.Port.kMXP);
          System.out.println("Connected to MXP port");
          return arduino;
        } catch (Exception eMXP) {
          System.out.println("Not Connected to MXP port, trying Onboard");
          try {
            SerialPort arduino = new SerialPort(bRate, SerialPort.Port.kOnboard);
            System.out.println("Connected to Onboard");
            return arduino;
          } catch (Exception eOnboard) {
            System.out.println("Not connected to any ports on the RoboRIO");
            return null;
          } // catch (Exception eOnboard)
        }
      }
    }
  }
  /**
   * Primary parser: Other methods are for legacy reasons and for edge cases
   */
  public boolean parseJunk(SerialPort arduino) {
    //System.out.println("PARSEJUNK");
    String targetPosition = arduino.readString();
    int startOfDataStream = targetPosition.indexOf("B");
    int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
    // The indexOf method returns -1 if it can't find the char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
      targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
    //  System.out.println(targetPosition);
      if (targetPosition.startsWith("Block")) {
        String[] positionNums = targetPosition.split(":");
        // positionNums[0] would be "Block
        // positionNums [1] would be number of block: always 0
        xVal = Integer.parseInt(positionNums[2]);
        yVal = Integer.parseInt(positionNums[3]);
        wVal = Integer.parseInt(positionNums[4]);
        hVal = Integer.parseInt(positionNums[5]);
        lDistVal = Integer.parseInt(positionNums[6]);
        lConfVal = Integer.parseInt(positionNums[7]);
        rDistVal = Integer.parseInt(positionNums[8]);
        rConfVal = Integer.parseInt(positionNums[9]);
        blocksSeen = Integer.parseInt(positionNums[1]);
        arduinoCounter = Integer.parseInt(positionNums[10]);
      } else {
        //System.out.println("Bad String from Arduino: Doesn't start with Block");
        return false;
      }
    } else {
      //System.out.println("Bad String from Arduino: no carriage return character or too short");
      return false;
    }
    return true;
  }
  //Getters
  public int getX() {return xVal;}
  public int getY() {return yVal;}
  public int getW() {return wVal;}
  public int getH() {return hVal;}
  public int getLeftDist() {return lDistVal;}
  public int getLeftConf() {return lConfVal;}
  public int getRightDist() {return rDistVal;}
  public int getRightConf() {return rConfVal;}
  public int getBlocksSeen() {return blocksSeen;}
  public int getArduinoCounter() {return arduinoCounter;}

  //Better getters
  public boolean isOnLeft(){return (xVal > 0 && xVal < leftMax);}
  public boolean isInMiddle(){return (xVal >= leftMax && xVal <= rightMax);}
  public boolean isOnRight(){return(xVal > rightMax && xVal < 316);}
  public boolean isConf() {
    if (lConfVal >= 500 && rConfVal >= 500) {
      return true;
    } else {
      return false;
    }
  }
  public String[] getArray(SerialPort a) {
  String targetPosition = a.readString();
    int startOfDataStream = targetPosition.indexOf("B");
    int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
    // The indexOf method returns -1 if it can't find the char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
      targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
    //  System.out.println(targetPosition);
      if (targetPosition.startsWith("Block")) {
        String[] positionNums = targetPosition.split(":");
        return positionNums;
      }
    }
    return null;
  }
  /**
   * Legacy parsers, kept in case we want to update one value without messing with the others
   * NVM, killing it with fire because that is the magic of GIT
   */
  public int[] parseVal(SerialPort a, int val, int dist1, int conf1, int dist2, int conf2) {
    //  System.out.println("GOT TO PARSEVAL");
    String targetPosition = a.readString();
   // for (int i = 0; i < 100000; i++) {}
    if (targetPosition.length() < 12) {
    } else {
  //  System.out.println(targetPosition);
    int startOfDataStream = targetPosition.indexOf("B");
    int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
    // The indexOf method returns -1 if it can't find the char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
      targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
   //   System.out.println(targetPosition);
      if (targetPosition.startsWith("Block")) {
        String[] positionNums = targetPosition.split(":");
        // positionNums[0] would be "Block
        xVal = Integer.parseInt(positionNums[2]);
        yVal = Integer.parseInt(positionNums[3]);
        wVal = Integer.parseInt(positionNums[4]);
        hVal = Integer.parseInt(positionNums[5]);
        lDistVal = Integer.parseInt(positionNums[6]);
        lConfVal = Integer.parseInt(positionNums[7]);
        rDistVal = Integer.parseInt(positionNums[8]);
        rConfVal = Integer.parseInt(positionNums[9]);
        blocksSeen = Integer.parseInt(positionNums[1]);
        arduinoCounter = Integer.parseInt(positionNums[10]);
        int [] ans = new int [] {Integer.parseInt(positionNums[val]),Integer.parseInt(positionNums[dist1]),Integer.parseInt(positionNums[conf1]), Integer.parseInt(positionNums[dist2]),Integer.parseInt(positionNums[conf2])};
        return ans;
      } else {
        System.out.println("Bad String from Arduino: Doesn't start with Block");
      }
    } else {
      System.out.println(startOfDataStream +" "+ endOfDataStream);
      System.out.println("Bad String from Arduino: no carriage return character or too short");
    }
  }
  int [] ans = new int [] {-1,-1,-1,-1,-1};
  return ans;
  }
  public void trackWithVision (SerialPort a, SpeedControllerGroup l, SpeedControllerGroup r, int val, int distLeft, int confLeft, int distRight, int confRight, int minDist, int minConf, double speed) {
    if(confLeft > minConf && confRight > minConf) {
      if (distLeft > minDist && distLeft > minDist) {
      if (val < leftMax && val > 0) {
        left = true;
        middle = false;
        right = false;
        backwards = false;
      } else if (val > leftMax && val < rightMax) {
        left = false;
        middle = true;
        right = false;
        backwards = false;
      } else if (val > rightMax && val < 316) {
        left = false;
        middle = false;
        right = true;
        backwards = false;
      } else {
        left = false;
        middle = false;
        right = false;
        backwards = false;
      }
    } else {
      if (val < leftMax && val > 0) {
        left = false;
        middle = false;
        right = true;
        backwards = false;
      } else if (val > leftMax && val < rightMax) {
        left = false;
        middle = false;
        right = false;
        backwards = true;
      } else if (val > rightMax && val < 316) {
        left = false;
        middle = false;
        right = true;
        backwards = false;
      } else {
        left = false;
        middle = false;
        right = false;
        backwards = false;
      }
    }
  }
  if (left) {
    l.set(0);
    r.set(speed);
  } else if (middle) {
    l.set(-speed);
    r.set(speed);
  } else if (right) {
    l.set(-speed);
    r.set(0);
  } else if (backwards) {
    l.set(speed);
    r.set(-speed);
  } else {
    l.set(0);
    r.set(0);
  }
}
}