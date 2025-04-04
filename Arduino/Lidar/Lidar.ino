/*
 * RoboPeak RPLIDAR Arduino Example
 * This example shows the easy and common way to fetch data from an RPLIDAR
 * 
 * You may freely add your application code based on this template
 *
 * USAGE:
 * ---------------------------------
 * 1. Download this sketch code to your Arduino board
 * 2. Connect the RPLIDAR's serial port (RX/TX/GND) to your Arduino board (Pin 0 and Pin1)
 * 3. Connect the RPLIDAR's motor ctrl pin to the Arduino board pin 3 
 */
 
/* 
 * Copyright (c) 2014, RoboPeak 
 * All rights reserved.
 * RoboPeak.com
 *
 * Redistribution and use in source and binary forms, with or without modification, 
 * are permitted provided that the following conditions are met:
 *
 * 1. Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY 
 * EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES 
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 * SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, 
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT 
 * OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 * HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR 
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, 
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */
 
// This sketch code is based on the RPLIDAR driver library provided by RoboPeak
#include <RPLidar.h>
#include <Wire.h>

// You need to create an driver instance 
RPLidar lidar;

#define RPLIDAR_MOTOR 3 // The PWM pin for control the speed of RPLIDAR's motor.
                        // This pin should connected with the RPLIDAR's MOTOCTRL signal 
#define sendSize 16

String i2cSend;
char i2cBuf[sendSize];
char buf[sendSize];
                        
void setup() {
  Serial.begin(115200);
  Wire.begin(2); //begin with Wire address #2
  Wire.onRequest(requestEvent);
  
  // bind the RPLIDAR driver to the arduino hardware serial
  lidar.begin(Serial1);
  
  // set pin modes
  pinMode(RPLIDAR_MOTOR, OUTPUT);
  Serial.println("Serial Start");
}

void loop() {
  if (IS_OK(lidar.waitPoint())) {
    float distance = lidar.getCurrentPoint().distance; //distance value in mm unit; 2 floating points
    float angle    = lidar.getCurrentPoint().angle; //anglue value in degree; 2 floating points
    bool  startBit = lidar.getCurrentPoint().startBit; //whether this point is belong to a new scan
    byte  quality  = lidar.getCurrentPoint().quality; //quality of the current measurement
    
    //perform data processing here...
    int distanceInt = distance * 100;
    String distanceS = String(distanceInt);
    while (distanceS.length() < 6) {
      distanceS = distanceS + " ";
    }

    int angleInt = angle * 100;
    String angleS = String(angleInt);
    while (angleS.length() < 5) {
      angleS = angleS + " ";
    }

    String qualityS = String(quality);
    while (qualityS.length() < 2) {
      qualityS = qualityS + " ";
    }
    
    i2cSend = distanceS + "," + angleS + "," + qualityS + "*";
    i2cSend.toCharArray(buf, sendSize);

    Serial.println(i2cSend);
    
  } else {
    Serial.println("Lidar ded");
    analogWrite(RPLIDAR_MOTOR, 250); //stop the rplidar motor
    
    // try to detect RPLIDAR... 
    rplidar_response_device_info_t info;
    if (IS_OK(lidar.getDeviceInfo(info, 100))) {
       // detected...
       lidar.startScan();
       
       // start motor rotating at max allowed speed
       Serial.println("Speed");
       analogWrite(RPLIDAR_MOTOR, 250);
       delay(1000);
    }
  }
}

void requestEvent() {
  for (int i = 0; i < sendSize; i++) {
    i2cBuf[i] = buf[i];
  }
  Wire.write(i2cBuf);
}
