#include <Wire.h>
int ch1Pin;
int ch2Pin;
int ch1Val;
int ch2Val;

byte channelData[4];
  
void setup() {
  // put your setup code here, to run once:
  ch1Pin = 5;
  ch2Pin = 6;
  pinMode(ch1Pin, INPUT);
  pinMode(ch2Pin, INPUT);
  
  Wire.begin(1);        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
  Wire.onRequest(receiveRequest);
}

void loop() {
  // put your main code here, to run repeatedly:
  ch1Val = pulseIn(ch1Pin, HIGH);
  ch2Val = pulseIn(ch2Pin, HIGH);
  Serial.print(ch1Val);
  Serial.print(", ");
  Serial.println(ch2Val);
  
}

void receiveRequest() {
  Wire.beginTransmission(4);
}
