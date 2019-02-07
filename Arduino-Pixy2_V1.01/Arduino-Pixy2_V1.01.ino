#include <SparkFun_RFD77402_Arduino_Library.h>
#include <SPI.h>
#include <Pixy2.h>
#include <Pixy2CCC.h>

RFD77402 myDistance;
Pixy2 pixy;

  // The getBlocks() method returns the number of objects Pixy2 has detected in the  pixy.ccc.blocks[] array.(one array member for each detected object.)
  //pixy.ccc.blocks[i].m_signature The signature number of the detected object (1-7 for normal signatures)
  //pixy.ccc.blocks[i].m_x The x location of the center of the detected object (0 to 316)
  //pixy.ccc.blocks[i].m_y The y location of the center of the detected object (0 to 208)
  //pixy.ccc.blocks[i].m_width The width of the detected object (1 to 316)
  //pixy.ccc.blocks[i].m_height The height of the detected object (1 to 208)
  //pixy.ccc.blocks[i].m_angle The angle of the object detected object if the detected object is a color code (-180 to 180).
  //pixy.ccc.blocks[i].m_index The tracking index of the block
  //pixy.ccc.blocks[i].m_age The number of frames the block has been tracked.
  //pixy.ccc.blocks[i].print() A member function that prints the detected object information to the serial port

 // Putting in a flag for automatically removing code when deployed.
 bool deployed = false; // *** Change to true when ready to deploy ***
 int loopCounter = 0; // Initializing loop counter for testing timing betweein Ardueno and RoboRio 

void setup() {
  // put your setup code here, to run once:
 Serial.begin(115200);
 Serial.print("Starting...\n");
 pixy.init();
 
  if (myDistance.begin() == false)
  {
    Serial.println("Distance sensor failed to initialize. Check wiring.");
    //while (1); //Freeze!
  }
  Serial.println("Distance sensor online!");
}

void loop() {
  // put your main code here, to run repeatedly:
  loopCounter = loopCounter +1;
 
  //Distance Measurement
  myDistance.takeMeasurement(); //Tell sensor to take measurement
  
//  **** You can remove this when you understand why I commented it out ****
//  pixy.ccc.getBlocks();
//  unsigned int distance = myDistance.getDistance(); //Retrieve the distance value
//  unsigned int confidence = myDistance.getConfidenceValue(); //Retrieve the confidence value
// **** Removing the unsigned int because... [this is left as an exercise for the student] ****
  
  int distance = myDistance.getDistance(); //Retrieve the distance value in mm
  int confidence = myDistance.getConfidenceValue(); //Retrieve the confidence value between 0 and 2047, with 2047 being the “most confident”
  int numberOfTargets = 0;
  int i = 0;
  char targetBuffer[150];
  
  numberOfTargets = pixy.ccc.getBlocks(false); // sets the wait parameter to false 

//  Setting wait to false causes getBlocks() to return immediately if no new data is available (polling mode).
//  Setting wait to true (default) causes getBlocks() to block (wait) until the next frame of block data is available. 
//  Note, there may be no block data if no objects have been detected.

  if (numberOfTargets > 0){ 
    for (i=0; i < numberOfTargets; i++){
      sprintf(targetBuffer, "Block ID:%d;x:%d;y:%d;w:%d;h:%d;dist:%d;conf:%d;count:%d", i, pixy.ccc.blocks[i].m_x, pixy.ccc.blocks[i].m_y, pixy.ccc.blocks[i].m_width, pixy.ccc.blocks[i].m_height, distance, confidence, loopCounter);
      Serial.println(targetBuffer); 
    } 
  } else {
// No object was detected.  Set Block ID to -1, zero out other info, and just send distance data.
    sprintf(targetBuffer, "Block ID:-1;x:0;y:0;w:0;h:0;dist:%d;conf:%d;count:%d", distance, confidence, loopCounter);      
    Serial.println(targetBuffer); 
  }
}
