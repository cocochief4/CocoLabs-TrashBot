// Wire Peripheral Sender
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Sends data as an I2C/TWI peripheral device
// Refer to the "Wire Master Reader" example for use with this

// Created 29 March 2006

// This example code is in the public domain.
int Ch1Pin = 5;
int Ch2Pin = 6;

int Ch1Val;
int Ch2Val;

String finalSend;

int requestCount = 0;
#include <Wire.h>

void setup() {
  Wire.begin(4);                // join i2c bus with address #8
  Wire.onRequest(requestEvent); // register event
  Serial.begin(9600);

  pinMode(Ch1Pin, INPUT);
  pinMode(Ch2Pin, INPUT);
}

void loop() {
  delay(10);
  //Serial.println(requestCount);
  Ch1Val = pulseIn(Ch1Pin, HIGH);
  Ch2Val = pulseIn(Ch2Pin, HIGH);

  if (Ch1Val < 1000) {
    Ch1Val = 1000;
  } else if (Ch1Val > 2000) {
    Ch1Val = 2000;
  } else if (Ch1Val > 1460 && Ch1Val < 1540) {
    Ch1Val = 1500;
  }
  
  if (Ch2Val < 1000) {
    Ch2Val = 1000;
  } else if (Ch2Val > 2000) {
    Ch2Val = 2000;
  } else if (Ch2Val > 1460 && Ch2Val < 1540) {
    Ch2Val = 1500;
  }

  finalSend = String(Ch1Val) + String(Ch2Val);
  Serial.println(finalSend);
}

// function that executes whenever data is requested by master
// this function is registered as an event, see setup()
void requestEvent() {
  char buffer[32];
  finalSend.toCharArray(buffer, 32);
  Wire.write(buffer); // 
  //
  //requestCount++;
}
