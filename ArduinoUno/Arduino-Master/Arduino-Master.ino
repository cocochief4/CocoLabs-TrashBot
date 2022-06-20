#include <Wire.h>

void setup() {
  // put your setup code here, to run once:
  Wire.begin(1); //join I2C with ID of 1
  Serial.begin(115200); //begin Serial Monitor with baud of 9600
  Serial.println("Start");
}

void loop() {
  // put your main code here, to run repeatedly:
  Wire.requestFrom(2, 6);    // request 6 bytes from peripheral device #8

  while (Wire.available()) { // peripheral may send less than requested
    char c = Wire.read(); // receive a byte as character
    Serial.print(c);         // print the character
  }

  delay(500);
  Serial.println("loop"); 
}
