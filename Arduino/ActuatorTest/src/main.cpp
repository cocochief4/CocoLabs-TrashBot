#include <Arduino.h>

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

boolean autoDownCmd = false;
boolean autoUpCmd = false;

/**
 * speed = 0-255, direction = 0 for up, 1 for down
*/
void moveA(int speed, int direction);
/**
 * speed = 0-255, direction = 0 for up, 1 for down
*/
void moveB(int speed, int direction);

// Manually trigger movement using limit switches, in use 12/3/2023
#define mp1 8
#define mp2 9

// put it all into one method
void manualMove();
void manualInit();
void switchInit();
void autoDown();
void autoUp();

void setup() {
  Serial.begin(115200);

  pinMode(f1, OUTPUT);
  pinMode(b1, OUTPUT);
  pinMode(f2, OUTPUT);
  pinMode(b2, OUTPUT);
  pinMode(a, OUTPUT);
  pinMode(b, OUTPUT);

  manualInit();
  // switchInit();
   pinMode(bSwitchHigh, INPUT_PULLUP);
  pinMode(bSwitchLow, INPUT_PULLUP);
  pinMode(aSwitchLow, INPUT_PULLUP);
  pinMode(aSwitchHigh, INPUT_PULLUP);
}

String str;

void loop() {
  str = "";
  str = str + "switcha: " + digitalRead(aSwitchLow) + "  ";
  str = str + "switchb: " + digitalRead(bSwitchLow) + "  ";
  if (Serial.available()) {
    String cmd = Serial.readStringUntil('\n');

    Serial.println(cmd);

    if (cmd == "ru") { // right up
      moveA(A_SPEED, 0);
    } else if (cmd == "rd") { // right down
      moveA(A_SPEED, 1);
    } else if (cmd == "lu") { // right up
      moveB(B_SPEED, 0);
    } else if (cmd == "ld") { // right down
      moveB(B_SPEED, 1);
    } else if (cmd == "u") {
      moveA(A_SPEED, 0);
      moveB(B_SPEED, 0);
    } else if (cmd == "d") {
      moveA(A_SPEED, 1);
      moveB(B_SPEED, 1);
    } else if (cmd == "s") {
      moveA(0, 0);
      moveB(0, 0);
    } else if (cmd == "autoDown") {
      str += "PING  ";
      autoDownCmd = true;
    } else if (cmd == "autoUp") {
      autoUpCmd = true;
    }

  }

  // Comment this out if you want console movement, put it back in for switch movement.
  // manualMove();
  autoUp();
  autoDown();
  if (!str.equals("")) {
    Serial.println(str);
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

void manualInit() {
  pinMode(mp1, INPUT_PULLUP);
  pinMode(mp2, INPUT_PULLUP);
}

void manualMove() {
  if (digitalRead(mp1) == LOW) {
    moveA(A_SPEED, 0);
    moveB(B_SPEED, 0);
  } else if (digitalRead(mp2) == LOW) {
    moveA(A_SPEED, 1);
    moveB(B_SPEED, 1);
  } else {
    moveA(0, 0);
    moveB(0, 0);
  }
}

// void switchInit() {
//   pinMode(bSwitchHigh, INPUT_PULLUP);
//   pinMode(bSwitchLow, INPUT_PULLUP);
//   pinMode(aSwitchLow, INPUT_PULLUP);
//   pinMode(aSwitchHigh, INPUT_PULLUP);
// }

void autoDown() {
  if (autoDownCmd) {
    str += "auto down running  ";
    // str += "move1  ";
    // str += ("move ");
    if (digitalRead(bSwitchLow) == HIGH) {
      moveB(B_SPEED, 1);
    } else {
      str += "bStop ";
      moveB(0, 0);
    }
    if (digitalRead(aSwitchLow) == HIGH) {
      moveA(A_SPEED, 1);
    } else {
      str += "aStop ";
      moveA(0,0);
    }
    if (digitalRead(aSwitchLow) == LOW && digitalRead(bSwitchLow) == LOW) {
      autoDownCmd = false;
    }
  }
}


void autoUp() {
  if (autoUpCmd) {
    str += "auto up running  ";
    // str += "move1  ";
    // str += ("move ");
    if (digitalRead(bSwitchHigh) == HIGH) {
      moveB(B_SPEED, 0);
    } else {
      str += "bStop ";
      moveB(0, 0);
    }
    if (digitalRead(aSwitchHigh) == HIGH) {
      moveA(A_SPEED, 0);
    } else {
      str += "aStop ";
      moveA(0,0);
    }
    if (digitalRead(aSwitchHigh) == LOW && digitalRead(bSwitchHigh) == LOW) {
      autoUpCmd = false;
    }
  }
}