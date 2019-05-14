package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.MjpegServer;
import edu.wpi.cscore.UsbCamera;

/**
 * This class is designed to be the sole handler of the driving cameras on the robot
 * Please note that if done improperly, Cameras *will* eat up CPU like nobodys buisness.
 * This problem may have cost us an event: DO NOT change some settings.
 */
public class MagicRobotCameras {
  UsbCamera backCam;
  UsbCamera frontCam;
  MagicJoystickInput INPUT;
  boolean lastCamChoice;
  MjpegServer camServer;
  static final int CAMPORT1 = 0;
  static final int CAMPORT2 = 1;

  
  MagicRobotCameras() {
    INPUT = MagicJoystickInput.getInstance();
    startCameras();
  }

  /**
   * This method is called in the constructor: calling it again results in broken cameras and
   * a steady stream of angry errors.
   */
  protected void startCameras() {
    backCam = CameraServer.getInstance().startAutomaticCapture(CAMPORT1);
    frontCam = CameraServer.getInstance().startAutomaticCapture(CAMPORT2);
    camServer = CameraServer.getInstance().addSwitchedCamera("The One True Source");
    setCameraSettings();
    camServer.setSource(frontCam);
  }

  /**
   * Camera settings MUST be set as follows to keep the robot from dying.
   * 
   * These settings are designed to lower CPU usage by channelling the camera output straight
   * into the driver's station.  It seems that any code run upon the video is immensly
   * CPU-intensive.  
   * 
   * If you wish to modify these settings to conserve bandwidth, then please note
   * that compression MUST be kept at -1, as that is the most expensive setting to change.
   * 
   * It may be OK to lower resolution to conserve latency: however, it will significantly 
   * increase CPU usage
   */
  public void setCameraSettings() {
    backCam.setResolution(640, 480);
    frontCam.setResolution(640, 480);
    camServer.setCompression(-1); //DO NOT TOUCH
    camServer.setResolution(640, 480);
  }

  /**
   * Check if cameras should be swapped: if so, swap cameras. If not, do nothing
   * Uses input from INPUT.
   */
  public void checkCamSwap() {
    if (INPUT.isButtonOn(Buttons.cameraChange) && !lastCamChoice) {
      //System.out.println("Swapping Cams");
      camServer.setSource(frontCam);
    } else if (!INPUT.isButtonOn(Buttons.cameraChange) && lastCamChoice) {
      //System.out.println("Swapping Cams");
      camServer.setSource(backCam);
    }
    lastCamChoice = INPUT.isButtonOn(Buttons.cameraChange);
  }
}