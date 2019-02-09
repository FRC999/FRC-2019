package frc.robot;

import edu.wpi.first.wpilibj.SerialPort;

public class Vision {
    private int bRate;
    private int counting;
    private int xVal;
    private int yVal;
    private int wVal;
    private int hVal;
    private int distVal;
    private int confVal;
    private int stopDistance = 200;
    private int confidenceThreshold = 300;
    private int delayCount = 1;
    private int arduinoCounter; // loop counter passed from arduino for timing checks
    private int startOfDataStream;
    private int endOfDataStream;
    private int blocksSeen;
    
    public Vision(int baud, int count, int stop, int conf, int delay) {
    counting = count;
    bRate = baud;
    xVal = 0;
    yVal = 0;
    wVal = 0;
    hVal = 0;
    distVal = 0;
    confVal = 0;
    blocksSeen = 0;
    arduinoCounter = 0;
    stopDistance = stop;
    confidenceThreshold = conf;
    delayCount = delay;
    }
    public SerialPort getArduino() {
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
            SerialPort  arduino = new SerialPort(bRate, SerialPort.Port.kMXP);
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
} public int parseX(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          xVal = Integer.parseInt(positionNums[2]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return xVal;
} public int parseY(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          yVal = Integer.parseInt(positionNums[3]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return yVal;
} public int parseW(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          wVal = Integer.parseInt(positionNums[4]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return wVal;
} public int parseH(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          hVal = Integer.parseInt(positionNums[5]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return hVal;
} public int parseDist(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          distVal = Integer.parseInt(positionNums[6]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return distVal;
} public int parseConf(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          confVal = Integer.parseInt(positionNums[7]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return confVal;
} public int parseBlocks(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          blocksSeen = Integer.parseInt(positionNums[1]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return blocksSeen;
} public int parseCounter(int delayCount, SerialPort arduino) {
    counting = (counting + 1);
    if (counting == delayCount) {
      counting = 0;
      String targetPosition = arduino.readString();
      int startOfDataStream = targetPosition.indexOf("B");
      int endOfDataStream = targetPosition.indexOf("\r");// looking for the first carriage return
      // The indexOf method returns -1 if it can't find the char in the string
      if (startOfDataStream != -1 && endOfDataStream != -1 && (endOfDataStream - startOfDataStream) > 12) {
        targetPosition = (targetPosition.substring(startOfDataStream, endOfDataStream));
        System.out.println(targetPosition);
        if (targetPosition.startsWith("Block")) {
          String[] positionNums = targetPosition.split(":");
          //positionNums[0] would be "Block
          arduinoCounter = Integer.parseInt(positionNums[8]);
        } else {
          System.out.println("Bad String from Arduino: Doesn't start with Block");
        }
      } else {
        System.out.println("Bad String from Arduino: no carriage return character or too short");
      }
    }
    return arduinoCounter;
}
}