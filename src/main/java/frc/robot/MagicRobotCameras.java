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
public class MagicRobotCameras{
  UsbCamera backCam;
  UsbCamera frontCam;
  MagicJoystickInput INPUT;
  boolean lastCamChoice;
  VideoSink camServer;
  static final int CAMPORT1 = 0;
  static final int CAMPORT2 = 1;
  CameraServer CAMERASERVER;

  MagicRobotCameras(){
    INPUT= MagicJoystickInput.getInstance();    
    CAMERASERVER = CameraServer.getInstance();

    startCameras();
  }


public void startCameras() {
    backCam = CAMERASERVER.startAutomaticCapture("BackCam", CAMPORT1);
    frontCam = CAMERASERVER.startAutomaticCapture("FrontCam", CAMPORT2);
    camServer = CAMERASERVER.getServer();
    setCameraSettings();
    checkCamSwap();
}
public void setCameraSettings(){
  backCam.setResolution(640, 480);
  frontCam.setResolution(640, 480);
  
}
/**
 * Check if cameras should be swapped: if so, swap cameras.
 * If not, do nothing
 * Uses input from INPUT provided at construction
 */
  public void checkCamSwap(){
    if (INPUT.isButtonOn(ButtonEnum.cameraChange) && !lastCamChoice){
      System.out.println("Swapping Cams");
      camServer.setSource(frontCam);
    }
    else if (!INPUT.isButtonOn(ButtonEnum.cameraChange) && lastCamChoice){
      System.out.println("Swapping Cams");
      camServer.setSource(backCam);
    }
    lastCamChoice = INPUT.isButtonOn(ButtonEnum.cameraChange);
  }
}