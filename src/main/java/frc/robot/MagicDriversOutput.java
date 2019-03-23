package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
/**
 * This class is designed to be the counterpart of MagicInput, to handle things like cameras and swapping.
 * It is currently only handling CameraSwap, however, in future it may handle all
 * communication to the driver station
 * Or not.
 */
public class MagicDriversOutput{
  UsbCamera backCam;
  UsbCamera frontCam;
  MagicJoystickInput INPUT;
  boolean lastCamPress;
  VideoSink camServer;
  static final int CAMPORT1 = 0;
  static final int CAMPORT2 = 1;
  StringBuilder oneLinePrint;

  MagicDriversOutput(){
    INPUT= MagicJoystickInput.getInstance();
    backCam = CameraServer.getInstance().startAutomaticCapture(0);
    frontCam = CameraServer.getInstance().startAutomaticCapture(1);
    camServer = CameraServer.getInstance().getServer();
  }

/**
 * Check if cameras should be swapped: if so, swap cameras.
 * If not, do nothing
 * Uses input from INPUT provided at construction
 */
  public void checkCamSwap(){
    if (INPUT.isButtonOn(ButtonEnum.cameraChange) && !lastCamPress){
      System.out.println("Swapping Cams");
      camServer.setSource(frontCam);
    }
    else if (!INPUT.isButtonOn(ButtonEnum.cameraChange) && lastCamPress){
      System.out.println("Swapping Cams");
      camServer.setSource(backCam);
    }
    lastCamPress = INPUT.isButtonOn(ButtonEnum.cameraChange);
  }
  public StringBuilder addToPrint(String toPrint){
    oneLinePrint.append(toPrint);
    return oneLinePrint;
  }
  public String printMagicLine(){
    System.out.print("/r");
    System.out.print(oneLinePrint);
    oneLinePrint.delete(0,oneLinePrint.length());
    return oneLinePrint.toString();
  }
}