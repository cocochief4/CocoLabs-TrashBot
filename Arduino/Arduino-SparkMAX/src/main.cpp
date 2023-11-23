#include <Arduino.h>
#include <Servo.h>

#define pwm 9

Servo sparkMax;

// put function declarations here:
int myFunction(int, int);

void setup() {
  // put your setup code here, to run once:
  int result = myFunction(2, 3);

  // Servo Lib
  sparkMax.attach(pwm, 1000, 2000);

  pinMode(pwm, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:

  sparkMax.write(90);
}

// put function definitions here:
int myFunction(int x, int y) {
  return x + y;
}