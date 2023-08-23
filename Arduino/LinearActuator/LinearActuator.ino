
// inputs
const int rioIn = 13;
const int horizontalMax = 6;
const int verticalMin = 2;
const int horizontalMid = 3;
const int verticalMax = 4;
const int horizontalMin = 5;

// outputs
const int rioOut = 18;
const int horizontalForward = 7;
const int horizontalBackward = 8;
const int horizontalSpeed = 9;
const int verticalForward = 10;
const int verticalBackward = 11;
const int verticalSpeed = 12;


int phase;
bool inPickup = false;

void setup() {
  // put your setup code here, to run once:

  Serial.begin(115200);

  Serial.println("serial started");

  pinMode(rioIn, INPUT_PULLUP);
  pinMode(horizontalMax, INPUT_PULLUP);
  pinMode(verticalMin, INPUT_PULLUP);
  pinMode(horizontalMid, INPUT_PULLUP);
  pinMode(verticalMax, INPUT_PULLUP);
  pinMode(horizontalMin, INPUT_PULLUP);

  pinMode(rioOut, OUTPUT);
  pinMode(horizontalForward, OUTPUT);
  pinMode(horizontalBackward, OUTPUT);
  pinMode(horizontalSpeed, OUTPUT);
  pinMode(verticalForward, OUTPUT);
  pinMode(verticalBackward, OUTPUT);
  pinMode(verticalSpeed, OUTPUT);


  phase = -2;
}

void loop() {
  // put your main code here, to run repeatedly:

  if (digitalRead(rioIn)) {

    inPickup = true;

    if (pickup()) {
      inPickup = false;
    }

    digitalWrite(rioOut, inPickup ? LOW : HIGH);

  }


}

bool pickup() {

  if (phase == -2) {
    phase = -1;
    Serial.println(phase);
  }
  else if (digitalRead(horizontalMin) == LOW && phase == -1) {
    phase = 0;
    Serial.println(phase);
  }
  else if (digitalRead(verticalMax) == LOW && phase == 0) {
    phase = 1;
    Serial.println(phase);
  }
  else if (digitalRead(horizontalMax) == LOW && phase == 1) {
    phase = 2;
    Serial.println(phase);
  }
  else if (digitalRead(verticalMin) == LOW && phase == 2) {
    phase = 3;
    Serial.println(phase);
  }
  else if (digitalRead(horizontalMid) == LOW && phase == 3) {
    phase = 4;
    Serial.println(phase);
  }
  else if (digitalRead(verticalMax) == LOW && phase == 4) {
    phase = 5;
    Serial.println(phase);
  }
  else if (digitalRead(horizontalMin) == LOW && phase == 5) {
    phase = 6;
    Serial.println(phase);
  }

  switch(phase) {
    case -1:
      driveHorizontalBackward();
      return false;
      break;
    case 0:
      driveVerticalUp();
      return false;
      break;
    case 1: // go horizontal forward
      driveHorizontalForward()
      return false;
      break;
    case 2: // go down
      driveVerticalDown();
      return false;
      break;
    case 3: // go back
      driveHorizontalBackward();
      return false;
      break;
    case 4: // go up
      driveVerticalUp();
      return false;
      break;
    case 5: // go in
      driveHorizontalBackward();
      return false;
      break;
    case 6:
      stop();
      digitalWrite(rioOut, HIGH);
      phase = -2;
      return true;
      break;
    default:
      return false;
      break;
  }
}

void stop() {
  digitalWrite(horizontalForward, LOW);
  digitalWrite(horizontalBackward, LOW);
  digitalWrite(horizontalSpeed, LOW);
  digitalWrite(verticalForward, LOW);
  digitalWrite(verticalBackward, LOW);
  digitalWrite(verticalSpeed, LOW);
}

void driveVerticalUp() {
  digitalWrite(horizontalForward, LOW);
  digitalWrite(horizontalBackward, LOW);
  digitalWrite(horizontalSpeed, LOW);
  digitalWrite(verticalForward, HIGH);
  digitalWrite(verticalBackward, LOW);
  digitalWrite(verticalSpeed, HIGH);
}

void driveVerticalDown() {
  digitalWrite(horizontalForward, LOW);
  digitalWrite(horizontalBackward, LOW);
  digitalWrite(horizontalSpeed, LOW);
  digitalWrite(verticalForward, LOW);
  digitalWrite(verticalBackward, HIGH);
  digitalWrite(verticalSpeed, HIGH);
}

void driveHorizontalForward() {
  digitalWrite(horizontalForward, HIGH);
  digitalWrite(horizontalBackward, LOW);
  digitalWrite(horizontalSpeed, HIGH);
  digitalWrite(verticalForward, LOW);
  digitalWrite(verticalBackward, LOW);
  digitalWrite(verticalSpeed, LOW);
}

void driveHorizontalBackward() {
  digitalWrite(horizontalForward, LOW);
  digitalWrite(horizontalBackward, HIGH);
  digitalWrite(horizontalSpeed, HIGH);
  digitalWrite(verticalForward, LOW);
  digitalWrite(verticalBackward, LOW);
  digitalWrite(verticalSpeed, LOW);
}
