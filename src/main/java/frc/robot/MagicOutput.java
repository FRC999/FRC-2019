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
public class MagicOutput{
  UsbCamera backCam;
  UsbCamera frontCam;
  MagicInput INPUT;  
  boolean lastCamPress;
  VideoSink camServer;
  final int CAMPORT1 = 0;
  final int CAMPORT2 = 1;

  MagicOutput(MagicInput inputToUse){
    INPUT=inputToUse;
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
}