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
#define rpSwitchForward 26

int speed = 10; // range is (0, 90), speed for rack and pinion

Servo rev550; // rack and pinion motor

enum CommandState {
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
  mo, // move rack and pinion out (main loop)
  mu, // move teeth actuator up (main loop)
  mi, // move rack and pinion in (main loop)
  md, // move teeth actuator down (main loop)
};

CommandState cmd = s; // keeps the cmd what to do.

const static struct {
    CommandState val;
    String str;
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
  {m, "m"},
  {mo, "mo"},
  {mu, "mu"},
  {mi, "mi"},
  {md, "md"},
};

CommandState str2enum (String str)
{
     int j;
     for (j = 0;  j < sizeof (conversion) / sizeof (conversion[0]);  j++)
         if (str == conversion[j].str)
             return conversion[j].val;
      Serial.println("no such cmd");
      return s;
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
  for (int index = 0; index < (sizeof (switches) / sizeof (switches[0])); index++) {
    if (switches[index].pinNumber == pinNumber) {
      return switches[index].isTripped;
    }
  }
  Serial.println("no such switch");
  return false;
}

/**
 * does all cmd switch conditions
*/
void switchMaster();

/**
 * cmd = 1. Moves the rack back until it hits limit switch
 * 
 * @return true if limit switch is hit, false if not.
*/
boolean rpAutoBack();

/**
 * cmd = 2. Moves the rack forward until it hits limit switch
 * 
 * @return true if limit switch is hit, false if not.
*/
boolean rpAutoForward();

/**
 * Load pins and init Servo object for rack and pinion, including limit switches
*/
void rackAndPinionInit();

/**
 * check all the limit switches if they have been triggered
*/
void limitSwitchMaster();

/**
 * check all cmd cases
*/

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
  cmd = s;
  Serial.begin(115200);

  actuatorInit();
  rackAndPinionInit();
}

String str = "";
void loop() {
  str = "";
  Serial.println("start");
  Serial.println(cmd);

  limitSwitchMaster();
  Serial.println(cmd);

  if (Serial.available()) {
    cmd = str2enum(Serial.readStringUntil('\n'));
  }
  Serial.println(cmd);

  switchMaster();
  Serial.println(cmd);

  if (!str.equals("")) {
    Serial.println(str);
  }
  Serial.println(cmd);
}

void switchMaster() {
  switch(cmd) {
    case s:
      moveA(0, 0);
      moveB(0, 0);
      rev550.write(90);
      cmd = s;
      break;
    case ru:
      if (!isSwitchTripped(aSwitchHigh)) {
        moveA(A_SPEED, 0);
      } else {
        cmd = s;
      }
      break;
    case rd:
      if (!isSwitchTripped(aSwitchLow)) {
        moveA(A_SPEED, 1);
      } else {
        cmd = s;
      }
      break;
    case lu:
      if (!isSwitchTripped(bSwitchHigh)) {
        moveB(B_SPEED, 0);
      } else {
        cmd = s;
      }
      break;
    case ld:
      if (!isSwitchTripped(bSwitchLow)) {
        moveB(B_SPEED, 1);
      } else {
        cmd = s;
      }
      break;
    case au:
      if (autoUp()) {
        cmd = s;
      }
      break;
    case ad:
      if (autoDown()) {
        cmd = s;
      }
      break;
    case af:
      if (rpAutoForward()) {
        cmd = s;
      }
      break;
    case ab:
      if (rpAutoBack()) {
        cmd = s;
      }
      break;
    case m: // Start the main loop
      cmd = mo;
      break;
    case mo:
      if (rpAutoForward()) {
        moveA(0, 0);
        moveB(0, 0);
        rev550.write(90);
        cmd = mu;
      }
      break;
    case mu:
      if (autoUp()) {
        moveA(0, 0);
        moveB(0, 0);
        rev550.write(90);
        cmd = mi;
      }
      break;
    case mi:
      if (rpAutoBack()) {
        moveA(0, 0);
        moveB(0, 0);
        rev550.write(90);
        cmd = md;
      }
      break;
    case md:
      if (autoDown()) {
        moveA(0, 0);
        moveB(0, 0);
        rev550.write(90);
        cmd = s;
      }
      break;
  }
}

boolean rpAutoForward() {
  if (!isSwitchTripped(rpSwitchForward)) {
    int writeSpeed = 90 + speed;
    rev550.write(writeSpeed);
    return false;
  } else {
    return true;
  }
}

boolean rpAutoBack() {
  if (!isSwitchTripped(rpSwitchBack)) {
    int writeSpeed = 90 - speed;
    rev550.write(writeSpeed);
    return false;
  } else {
    return true;
  }
}

void rackAndPinionInit() {
  // Servo Lib
  rev550.attach(rpPin, 1000, 2000);

  pinMode(rpPin, OUTPUT);
  pinMode(rpSwitchBack, INPUT_PULLUP);
  pinMode(rpSwitchForward, INPUT_PULLUP);
}

void limitSwitchMaster() {
  for (int switchIndex = 0; switchIndex < (sizeof (switches) / sizeof (switches[0])); switchIndex++) {
    int isTripped = digitalRead(switches[switchIndex].pinNumber);
    if (switches[switchIndex].pinNumber != rpSwitchForward) {
      if (isTripped == 1) {
        switches[switchIndex].isTripped = false;
      } else {
        switches[switchIndex].isTripped = true;
      }
    } else { // rpSwitchForward has opposite conditions because of freaking Akhil and his hardware.
      if (isTripped == 1) {
        switches[switchIndex].isTripped = true;
      } else {
        switches[switchIndex].isTripped = false;
      }
    }
    // str = str + switches[switchIndex].pinNumber + ": " + switches[switchIndex].isTripped + "  ";
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

boolean autoDown() {
  if (!isSwitchTripped(bSwitchLow)) {
    moveB(B_SPEED, 1);
  } else {
    moveB(0, 0);
  }
  if (!isSwitchTripped(aSwitchLow)) {
    moveA(A_SPEED, 1);
  } else {
    moveA(0,0);
  }
  if (isSwitchTripped(aSwitchLow) && isSwitchTripped(bSwitchLow)) {
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
  if (direction == 0) { // down
    digitalWrite(f1, HIGH);
    digitalWrite(b1, LOW);
  }
  else if (direction == 1) { // up
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
  if (direction == 1) { // down
    digitalWrite(f2, HIGH);
    digitalWrite(b2, LOW);
  }
  else if (direction == 0) { // up
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