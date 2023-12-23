#include <Arduino.h>
#include <Servo.h>

// Actuator Pins and Friends
#define a 2
#define f1 3
#define b1 4
#define f2 5
#define b2 6
#define b 7

#define aSwitchLow 12
#define aSwitchHigh 13
#define bSwitchLow 10
#define bSwitchHigh 11

#define A_SPEED 150
#define B_SPEED 130

// Rack and Pinion Pins and Friends
#define rpPin 8
#define rpSwitchBack 22
#define rpSwitchForward 24

enum command {
  s, // Stop for everything
  ru, // right actuator up
  rd, // right actuator down
  lu, // Left up
  ld, // Left down
  au, // auto actuator up (with limit switch ends)
  ad, // auto actuator down (with limit switch ends)
  af, // auto rack and pinion forward (with limit switch ends)
  ab, // auto rack and pinion backward (with limit switch ends)
  m, // main loop
};

const static struct {
    command val;
    const char *str;
} conversion [] = {
  {s, "s"},
  {ru, "ru"},
  {rd, "rd"},
  {lu, "lu"},
  {ld, "ld"},
  {au, "au"},
  {ad, "ad"},
  {af, "af"},
  {ab, "ab"},
  {m, "m"}
};

command str2enum (const char *str)
{
     int j;
     for (j = 0;  j < sizeof (conversion) / sizeof (conversion[0]);  ++j)
         if (!strcmp (str, conversion[j].str))
             return conversion[j].val;
}

static struct {
    boolean isTripped;
    int pinNumber;
} switches [] = {
  {false, aSwitchHigh},
  {false, aSwitchLow},
  {false, bSwitchHigh},
  {false, bSwitchLow},
  {false, rpSwitchBack},
  {false, rpSwitchForward},
};

boolean isSwitchTripped(int pinNumber) {
  for (int index = 0; index < sizeof (switches); index++) {
    if (switches[index].pinNumber == pinNumber) {
      return switches[index].isTripped;
    }
  }
  Serial.println("no such switch");
  return false;
}

/**
 * check all the limit switches if they have been triggered
*/
void switchMaster();

/**
 * move "A" actuator
 * speed = 0-255, direction = 0 for up, 1 for down
*/
void moveA(int speed, int direction);

/**
 * move "B" actuator
 * speed = 0-255, direction = 0 for up, 1 for down
*/
void moveB(int speed, int direction);

/**
 * init actuator pins
 * init actuator limit switches
*/
void actuatorInit();

/**
 * actuators go up until hit limit switch
 * 
 * @return true if limit switch was hit, false if not
*/
boolean autoUp();

/**
 * actuators go down until hit limit switch
 * 
 * @return true if limit switch was hit, false if not
*/
boolean autoDown();

void setup() {
  Serial.begin(115200);
}

String str = "";
void loop() {
  str = "";
  // put your main code here, to run repeatedly:
}

void switchMaster() {
  for (int switchIndex = 0; switchIndex < sizeof (switches); switchIndex++) {
    int isTripped = digitalRead(switches[switchIndex].pinNumber);
    if (isTripped == 1) {
      switches[switchIndex].isTripped = false;
    } else {
      switches[switchIndex].isTripped = true;
    }
  }
}

boolean autoUp() {
  if (!isSwitchTripped(bSwitchHigh)) {
    moveB(B_SPEED, 0);
  } else {
    moveB(0, 0);
  }
  if (!isSwitchTripped(aSwitchHigh)) {
    moveA(A_SPEED, 0);
  } else {
    moveA(0,0);
  }
  if (isSwitchTripped(aSwitchHigh) && isSwitchTripped(bSwitchHigh)) {
    return true;
  } else {
    return false;
  }
}

void moveA(int speed, int direction) {
  if (speed == 0) { // coast
    digitalWrite(f1, LOW);
    digitalWrite(b1, LOW);
    analogWrite(a, 0);
    return;
  }
  if (direction == 0 /*&& digitalRead(aSwitchLow) == HIGH*/) { // down
    digitalWrite(f1, HIGH);
    digitalWrite(b1, LOW);
  }
  else if (direction == 1 /*&& digitalRead(aSwitchHigh) == HIGH*/) { // up
    digitalWrite(f1, LOW);
    digitalWrite(b1, HIGH);
  }
  analogWrite(a, speed);
  
}

void moveB(int speed, int direction) {
  if (speed == 0) { // coast
    digitalWrite(f2, LOW);
    digitalWrite(b2, LOW);
    analogWrite(b, 0);
    return;
  }
  if (direction == 1 /*&& digitalRead(bSwitchLow) == HIGH*/) { // down
    digitalWrite(f2, HIGH);
    digitalWrite(b2, LOW);
  }
  else if (direction == 0 /*&& digitalRead(bSwitchHigh) == HIGH*/) { // up
    digitalWrite(f2, LOW);
    digitalWrite(b2, HIGH);
  }
  analogWrite(b, speed);
}

void actuatorInit() {
  pinMode(bSwitchHigh, INPUT_PULLUP);
  pinMode(bSwitchLow, INPUT_PULLUP);
  pinMode(aSwitchLow, INPUT_PULLUP);
  pinMode(aSwitchHigh, INPUT_PULLUP);

  pinMode(f1, OUTPUT);
  pinMode(b1, OUTPUT);
  pinMode(f2, OUTPUT);
  pinMode(b2, OUTPUT);
  pinMode(a, OUTPUT);
  pinMode(b, OUTPUT);

}