#define signalPin 3

void setup() {
  Serial.begin(115200);

  pinMode(signalPin, OUTPUT);

  digitalWrite(signalPin, HIGH);
  delay(3000);
  digitalWrite(signalPin, LOW);
  }

long loopTime = millis();
bool flash = false;
void loop()
 {
  char readByte;

  if (Serial.available() > 0) {
//    digitalWrite(signalPin, HIGH);
//    delay(100);
//    digitalWrite(signalPin, LOW);

    readByte = Serial.read();

    if (readByte == 'T') {
      Serial.println("True");
      flash = true;
    }
    else if (readByte == 'F') {
      Serial.println("False");
      flash = false;
    }
  }

  if (flash) {
    Serial.println("Write");
    digitalWrite(signalPin, HIGH);
  } else {
    digitalWrite(signalPin, LOW);
  }

}
