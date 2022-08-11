// Wire Peripheral Sender
// by Nicholas Zambetti <http://www.zambetti.com>

// Demonstrates use of the Wire library
// Sends data as an I2C/TWI peripheral device
// Refer to the "Wire Master Reader" example for use with this

// Created 29 March 2006

// This example code is in the public domain.
int ch1Pin = 10;
int ch2Pin = 11;
int ch9Pin = 9;

int ch1Val;
int ch2Val;
int ch9Val;

String finalSend;

int requestCount = 0;
#include <Wire.h>

void setup() {
  Wire.begin(4);                // join i2c bus with address #8
  Wire.onRequest(requestEvent); // register event
  Serial.begin(9600);

  pinMode(ch1Pin, INPUT);
  pinMode(ch2Pin, INPUT);
  pinMode(ch9Pin, INPUT);
}

void loop() {
  delay(10);
  //Serial.println(requestCount);
  ch1Val = pulseIn(ch1Pin, HIGH);
  ch2Val = pulseIn(ch2Pin, HIGH);
  ch9Val = pulseIn(ch9Pin, HIGH);


  if (ch1Val < 1000) {
    ch1Val = 1000;
  } else if (ch1Val > 2000) {
    ch1Val = 2000;
  } else if (ch1Val > 1470 && ch1Val < 1530) {
    ch1Val = 1500;
  }

  if (ch2Val < 1000) {
    ch2Val = 1000;
  } else if (ch2Val > 2000) {
    ch2Val = 2000;
  } else if (ch2Val > 1470 && ch2Val < 1530) {
    ch2Val = 1500;
  }


  finalSend = String(ch1Val) + String(ch2Val) + String(ch9Val);
  Serial.println(finalSend + "*");
}

// function that executes whenever data is requested by master
// this function is registered as an event, see setup()
void requestEvent() {
  char buffer[13];
  finalSend.toCharArray(buffer, 13);
  Wire.write(buffer); //
  //
  //requestCount++;
}
