package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;
import edu.wpi.first.wpilibj.SpeedController;
import edu.wpi.first.wpilibj.SpeedControllerGroup;

public class MagicVision {
  private int bRate;
  private int counting;
  private int xVal; //In order of appearance
  private int yVal;
  private int wVal;
  private int hVal;
  private int distVal;
  private int confVal;
  private int stopDistance;
  private int confidenceThreshold;
  private int delayCount;
  private int arduinoCounter; // loop counter passed from arduino for timing checks
  private int startOfDataStream;
  private int endOfDataStream;
  private int blocksSeen;
  private SerialPort arduino;
  private String targetPosition;
  final double speed = .5;
  final int leftMax = 130;
  final int rightMax = 170;
  final int min = 0;
  final int max = 316;

  /**VSCode mistakenly labeled several variable as not used, creating warnings; in an attempt to use them, this method was made. It doesn't do anything,
   *  just a pointless test of if several variables added together equal zero to get the code to have zero warnings
   * @param none no parameters
   * @return void no return value
   * @see the youtube video about a pointless button, that, in being pressed, turns itself off.
  **/
  public void removeFreakingAnnoyingVSCodeWarnings(){if(stopDistance + confidenceThreshold +delayCount +startOfDataStream+endOfDataStream==0){}}
  public MagicVision(int baud, int stop) {
    bRate = baud;
    stopDistance = stop;
  }
  /**
   * @param baud the baud rate of the Arduino (Frequency, they must match between java and Arduino)
   * @param stop the minimum distance accepted by the distance sensor
   * @param delay the delay between reads (0 if none)
   * @param conf the minimum accepted confidence value for distance;
   */
  public MagicVision(int baud, int stop, int delay, int conf) {
    bRate = baud;
    stopDistance = stop;
    confidenceThreshold = conf;
    delayCount = delay;
  }
  public MagicVision(int baud) {
    bRate = baud;
  }
  /**
   * @return the serial port of the arduino.
   */
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
   * @return whether the string is good.
   */
  public boolean parseJunk(SerialPort parsePort) {
    String targetPosition = parsePort.readString();
    int startOfDataStream = targetPosition.indexOf("B");
    int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
    // The indexOf method returns -1 if it can't find the char in the string
    if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
      targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
     // System.out.println(targetPosition);
      if (targetPosition.startsWith("Block")) {
        String[] positionNums = targetPosition.split(":");
        // positionNums[0] would be "Block
        // positionNums [1] would be number of block: always 0
        xVal = Integer.parseInt(positionNums[2]);
        yVal = Integer.parseInt(positionNums[3]);
        wVal = Integer.parseInt(positionNums[4]);
        hVal = Integer.parseInt(positionNums[5]);
        distVal = Integer.parseInt(positionNums[6]);
        confVal = Integer.parseInt(positionNums[7]);
        blocksSeen = Integer.parseInt(positionNums[1]);
        arduinoCounter = Integer.parseInt(positionNums[8]);
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
  public void setArduino(SerialPort port) {
    arduino = port;
  }
  //Getters
  public SerialPort getArduino() {return arduino;}
  public int getX() {return xVal;}
  public int getY() {return yVal;}
  public int getW() {return wVal;}
  public int getH() {return hVal;}
  public int getDist() {return distVal;}
  public int getStopDist() {return stopDistance;}
  public int getConf() {return confVal;}
  public int getBlocksSeen() {return blocksSeen;}
  public int getArduinoCounter() {return arduinoCounter;}

  //Better getters
  /**
   * @return true if in range, false if out of range
   */
  public boolean isOnLeftWithDist(int valX, int valDist){return (valX > min && valX < leftMax && valDist > 500);}
  /**
   * @return true if in range, false if out of range
   */
  public boolean isInMiddleWithDist(int valX, int valDist){return (valX >= leftMax && valX <= rightMax && valDist > 500);}
  /**
   * @return true if in range, false if out of range
   */
  public boolean isOnRightWithDist(int valX, int valDist){return(valX > rightMax && valX < 316 && valDist > 500);}
  
  /**
   * @return true if in range, false if out of range
   */
  public boolean isOnLeft(int val){return (val > min && val < leftMax);}
  /**
   * @return true if in range, false if out of range
   */
  public boolean isInMiddle(int val){return (val >= leftMax && val <= rightMax);}
  /**
   * @return true if in range, false if out of range
   */
  public boolean isOnRight(int val){return(val > rightMax && val < max);}
  
  public void track(SpeedControllerGroup left, SpeedControllerGroup right) {
    if (isOnLeft(parseVal(xVal, 0))) {
      left.set(0);
      right.set(speed);
    } else if (isOnRight(parseVal(xVal, 10))) {
      left.set(-speed);
      right.set(0);
    } else if (isInMiddle(parseVal(xVal, 10))) {
      left.set(-speed);
      right.set(speed);
    } else {
      left.set(0);
      right.set(0);
    }
  }
  /**
   * 
   * @param left left speed controller group of drivetrain
   * @param right right speed controller group of drivetrain
   * @param l checks if the target is to the left
   * @param m checks if the target is in the middle
   * @param r
   */
  public void trackAlternate(SpeedControllerGroup left, SpeedControllerGroup right, boolean l, boolean m, boolean r) {
    if (l) {
      left.set(0);
      right.set(speed);
    } else if (r) {
      left.set(-speed);
      right.set(0);
    } else if (m) {
      left.set(-speed);
      right.set(speed);
    } else {
      left.set(0);
      right.set(0);
    }
  }
  /**
   * @return the array generated by the arduino
   */
  public String[] getArray(SerialPort arduinoPort) {
  String targetPosition = arduinoPort.readString();
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
  public int parseVal(int index, int delayCount) {
    if (counting >= delayCount) {
      counting +=1;
      targetPosition = startArduino().readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
      //  System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          // positionNums[0] would be "Block
          xVal = Integer.parseInt(positionNums[2]);
          yVal = Integer.parseInt(positionNums[3]);
          wVal = Integer.parseInt(positionNums[4]);
          hVal = Integer.parseInt(positionNums[5]);
          distVal = Integer.parseInt(positionNums[6]);
          confVal = Integer.parseInt(positionNums[7]);
          blocksSeen =  Integer.parseInt(positionNums[1]);
          arduinoCounter = Integer.parseInt(positionNums[8]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    } else {
      counting -=10;
    }
    if (index >= 0 && index <= 8) {
    try {
      String [] val = getArray(arduino);
    return Integer.parseInt(val[index]);
    } catch (NullPointerException badInt) {
      //System.out.println("Could not fetch int");
    }
    }
    switch(index) {
      case 2 : {
        return xVal;
      }
      case 3 : {
        return yVal;
      }
      case 4 : {
        return wVal;
      }
      case 5 : {
        return hVal;
      }
      case 6 : {
        return distVal;
      }
      case 7 : {
        return confVal;
      }
      case 8 : {
        return arduinoCounter;
      }
      case 1 : {
        return blocksSeen;
      }
      default : {
        return 3000;
      }
    }
  }
}