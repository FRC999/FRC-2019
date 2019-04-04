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

  final int pixyXFieldOfViewDegrees = 60;
  final double pixyXFieldOfViewRadians = (60 * (2*Math.PI)/360);
  final int pixyYFieldOfViewDegrees = 40;
  final double pixyYFieldOfViewRadians = ( 40  * (2*Math.PI)/360);

  final int widthOfPixyInPixels = 316;
  final int heightOfPixyInPixels = 208;
  final double xRadiansPerPixel = pixyXFieldOfViewRadians/ widthOfPixyInPixels;
  final double distanceBetweenDistanceSensorsCm = 80-2.5;
  
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
      arduinoCounter = Integer.parseInt(positionNums[10]);
      distValRight = Integer.parseInt(positionNums[8]);
      confValRight = Integer.parseInt(positionNums[9]);
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
  }// end track()

 private int pathStages= 1;
 private double distanceToHatch;
 private double angleToHatch;
/** Objective: get the robot parallel to the hatch.
 * uses vision to locate the rightmost avaliable vision target, orient using it and two distance sensors, drive backwards, 
 * then follow a pathto get to the hatch straight.
 *  The path has two parts: a diagonal path that overshoots the goal so that the straight path can hit it straight.*/
  public void hatchPathExecutor(SerialPort arduino) {
    
    switch (pathStages) {
      case 1:
              if (parseVal(arduino, 1) == -1) // if it does not see a vision target
              { Robot.chassisDrive(0,0.2);}// rotate arbitrarily
        /* else if (parseVal(arduino, 2) < (widthOfPixyInPixels/2 - 10))
        Robot.chassisDrive(0,0.2);
        else if (parseVal(arduino, 2) < (widthOfPixyInPixels/2 + 10)
        Robot.chassisDrive(0, - 0.2);  // might want to swap minus signs with the other one
        */
              else {// if robot center is pointed at the center of the hatch
              
                if (parseVal(arduino, 7) > 800 && parseVal(arduino, 9 > 800)) {
                distanceToHatch = (parseVal(arduino, 6) +parseVal(arduino, 8))/2;
                angleToHatch = Math.arcTan(    (distanceBetweenDistanceSensorsCm * 10)/ 
                               (Math.abs(parseVal(arduino, 6) - parseVal(arduino, 8))) );
                Robot.zeroDriveEncoders();
                pathStages = 2;
                }// if confvals are greater than 800
                else 
              {System.out.println("get closer, within distance sensor range");}
              } // else if robot center is pointed at from the hatch
      break;
      case 2:
      // d = distanceToHatch + 
        //(2 * Math.PI * wheelRadiusMillimeters * Math.abs(Robot.driveFrontLeft.getSelectedSensorPosition() + Robot.driveFrontRight.getSelectedSensorPosition())/2);
      // if d > predeterminedArbitraryDistance
      //{Robot.chassisDrive.arcadeDrive(0,0); pathStages = 3;}
      // else {Robot.chassisDrive.arcadeDrive(-0.3, 0);}
      break;
      case 3:
      /* d = distanceToHatch + 
       (2 * Math.PI * wheelRadiusMillimeters * Math.abs(Robot.driveFrontLeft.getSelectedSensorPosition() + Robot.driveFrontRight.getSelectedSensorPosition())/2);
      if ((Angle2 + AngleToHatch + MinimumTurnableAngle) >= 2* Math.PI)
       Angle2 = 180 - angleToHatch - minAccAngle;
       
       A = d * Math.sin(angleToHatch) / Math.sin(angle2);
       B = */
      break;
      case 4:
      break;
      case 5:
      break;
      default:
      break; }
  }
}// end class
