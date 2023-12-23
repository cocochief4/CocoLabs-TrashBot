#include <Arduino.h>
#include <Servo.h>

#define signalPin 9

Servo rev550;

// put function declarations here:
int myFunction(int, int);

void setup() {

  // Servo Lib
  rev550.attach(signalPin, 1000, 2000);

  pinMode(signalPin, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:

  rev550.write(100); // 11% forward
}

// put function definitions here:
int myFunction(int x, int y) {
  return x + y;
}