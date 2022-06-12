#include <Wire.h>
int ch1Pin;
int ch2Pin;
  
void setup() {
  // put your setup code here, to run once:
  ch1Pin = 5;
  ch2Pin = 6;
  pinMode(ch1Pin, INPUT);
  pinMode(ch2Pin, INPUT);
  
  Wire.begin(1);        // join i2c bus (address optional for master)
  Serial.begin(9600);  // start serial for output
  Wire.onReceive(receiveEvent);
}

void loop() {
  // put your main code here, to run repeatedly:
  int ch1Val = pulseIn(ch1Pin, HIGH);
  int ch2Val = pulseIn(ch2Pin, HIGH);
  Serial.print(ch1Val);
  Serial.print(", ");
  Serial.println(ch2Val);
  
}

void receiveEvent(int howMany) {
  
}
