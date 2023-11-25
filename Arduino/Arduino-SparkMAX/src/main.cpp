#include <Arduino.h>
#include <Servo.h>

#define f 9
#define b 10

Servo forward;
Servo backward;

// put function declarations here:
int myFunction(int, int);

void setup() {

  // Servo Lib
  forward.attach(f, 1000, 2000);
  backward.attach(b, 1000, 2000);

  pinMode(f, OUTPUT);
}

void loop() {
  // put your main code here, to run repeatedly:

  forward.write(100); // 11% forward
  backward.write(80);
}

// put function definitions here:
int myFunction(int x, int y) {
  return x + y;
}