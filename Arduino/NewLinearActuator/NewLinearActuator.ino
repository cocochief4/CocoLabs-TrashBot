#define a 2
#define f1 3
#define b1 4
#define f2 5
#define b2 6
#define b 7

#define rioIn 10
#define rioOut 11

#define A_SPEED 140
#define B_SPEED 150

#define intakeRun 5
#define backLimit 10
#define frontLimit 11

//variables
int phase;
bool inPickup = false;

// speed = 0-255, direction = 0 for forward, 1 for backward
void moveA(int speed, int direction);
void moveB(int speed, int direction);

void setup() {
  Serial.begin(115200);

  pinMode(f1, OUTPUT);
  pinMode(b1, OUTPUT);
  pinMode(f2, OUTPUT);
  pinMode(b2, OUTPUT);
  pinMode(a, OUTPUT);
  pinMode(b, OUTPUT);

  pinMode(rioIn, INPUT); // stuff comes in from the rio
  pinMode(rioOut, OUTPUT); // stuff comes out from the rio


  phase = 0;
}

void loop() {

    if (digitalRead(rioIn) ) {
      inPickup = true;
    }

    if (inPickup) {
      
      if (digitalRead(f1) == LOW && digitalRead(f2) == LOW && phase = 0) {
        // stop here
        inPickup = false;
      }
      else {
        moveA(255, 0);
        moveB(255, 0);
      }
      
    }

    digitalWrite(rioOut, inPickup ? LOW : HIGH);




  // if (Serial.available()) {
  //   String cmd = Serial.readStringUntil('\n');

  //   if (cmd == "ru") { // right up
  //     moveA(A_SPEED, 0);
  //   } else if (cmd == "rd") { // right down
  //     moveA(A_SPEED, 1);
  //   } else if (cmd == "lu") { // right up
  //     moveB(B_SPEED, 1);
  //   } else if (cmd == "ld") { // right down
  //     moveB(B_SPEED, 0);
  //   } else if (cmd == "u") {
  //     moveA(A_SPEED, 0);
  //     moveB(B_SPEED, 1);
  //   } else if (cmd == "d") {
  //     moveA(A_SPEED, 1);
  //     moveB(B_SPEED, 0);
  //   } else if (cmd == "s") {
  //     moveA(0, 0);
  //     moveB(0, 0);
  //   }

  // }
}


void pickup() {

  moveA(255, 0);
  moveB(255, 0);
}

void moveA(int speed, int direction) {
  if (speed == 0) { // coast
    digitalWrite(f1, LOW);
    digitalWrite(b1, LOW);
    analogWrite(a, 0);
    return;
  }
  if (direction == 0) { // forward
    digitalWrite(f1, HIGH);
    digitalWrite(b1, LOW);
  }
  else if (direction == 1) {
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
  if (direction == 0) { // forward
    digitalWrite(f2, HIGH);
    digitalWrite(b2, LOW);
  }
  else if (direction == 1) {
    digitalWrite(f2, LOW);
    digitalWrite(b2, HIGH);
  }
  analogWrite(b, speed);
}