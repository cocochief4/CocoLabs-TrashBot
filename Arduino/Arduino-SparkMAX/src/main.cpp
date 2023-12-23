#include <Arduino.h>
#include <Servo.h>

#define rpPin 8
#define rpSwitchBack 22
#define rpSwitchForward 24

// 
int speed = 13; // range is (0, 90)

Servo rev550;

int cmd = 0; // Default stop

/**
 * Load pins and init Servo object for rack and pinion, including limit switches
*/
void rackAndPinionInit();
/**
 * cmd = 1. Moves the rack back until it hits limit switch
*/
void rpAutoBack();
/**
 * cmd = 2. Moves the rack forward until it hits limit switch
*/
void rpAutoForward();
/**
 * cmd = 3. Moves backward until stopped
*/
void backward();
/**
 * cmd = 4. Moves forward until stopped
*/
void forward();
/**
 * cmd = 5. stops, sets cmd = 0
*/
void stop();

void setup() {
  Serial.begin(115200);

  rackAndPinionInit();
}

String str;

void loop() {
  str = "";

  str = str + "backSwitch: " + digitalRead(rpSwitchBack) + "  ";

  if (Serial.available()) {
    cmd = Serial.readStringUntil('\n').toInt();
  }

  rpAutoBack();
  rpAutoForward();
  forward();
  backward();
  stop();

  if (!str.equals("")) {
    Serial.println(str);
  }
}

void stop() {
  switch (cmd) {
    case 5:
      str += "stopped  ";
      cmd = 0;
    case 0:
      rev550.write(90);
  }
}

void rpAutoBack() {
  switch (cmd) {
    case 1:
      if (digitalRead(rpSwitchBack) == HIGH) {
        int writeSpeed = 90 - speed;
        rev550.write(writeSpeed);
      } else {
        cmd = 0;
      }
  }
}

void rpAutoForward() {
  switch (cmd) {
    case 2:
      if (digitalRead(rpSwitchForward) == HIGH) {
        int writeSpeed = 90 + speed;
        rev550.write(writeSpeed);
      } else {
        cmd = 0;
      }
  }
}

void backward() {
  switch (cmd) {
    case 3:
      str += "moving backwards  ";
      rev550.write(90 - speed);
  }
}

void forward() {
  switch (cmd) {
    case 4:
      rev550.write(90 + speed);
  }
}

void rackAndPinionInit() {
  // Servo Lib
  rev550.attach(rpPin, 1000, 2000);

  pinMode(rpPin, OUTPUT);
  pinMode(rpSwitchBack, INPUT_PULLUP);
  // pinMode(rpSwitchForward, INPUT_PULLUP);
}