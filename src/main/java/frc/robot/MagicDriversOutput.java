package frc.robot;

import edu.wpi.first.cameraserver.CameraServer;
import edu.wpi.cscore.UsbCamera;
import edu.wpi.cscore.VideoSink;
import edu.wpi.cscore.VideoSource.ConnectionStrategy;
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
  boolean lastCamChoice;
  VideoSink camServer;
  static final int CAMPORT1 = 0;
  static final int CAMPORT2 = 1;
  StringBuilder oneLinePrint = new StringBuilder(500);

  MagicDriversOutput(){
    INPUT= MagicJoystickInput.getInstance();
    backCam = CameraServer.getInstance().startAutomaticCapture(CAMPORT1);
    backCam.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    backCam.setResolution(640, 480);

    frontCam = CameraServer.getInstance().startAutomaticCapture(CAMPORT2);
    frontCam.setConnectionStrategy(ConnectionStrategy.kKeepOpen);
    frontCam.setResolution(640, 480);
    
    camServer = CameraServer.getInstance().getServer();
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
  public StringBuilder addToPrint(String toPrint){
    oneLinePrint.append(toPrint);
    return oneLinePrint;
  }
  public String printMagicLine(){
    System.out.print("\n");
    System.out.print(oneLinePrint);
    oneLinePrint.delete(0,oneLinePrint.length());
    return oneLinePrint.toString();
  }
}