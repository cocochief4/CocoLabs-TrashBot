#define signalPin 3

void setup()
{
  Serial.begin(115200);

  pinMode(signalPin, OUTPUT);

  digitalWrite(signalPin, HIGH);
  delay(3000);
  digitalWrite(signalPin, LOW);
}

long loopTime = millis();
void loop()
 {
  bool flash;
  char readByte;

  if (Serial.available()) {
//    digitalWrite(signalPin, HIGH);
//    delay(100);
//    digitalWrite(signalPin, LOW);

    readByte = Serial.read();

    if (readByte == 'T') {
      Serial.println("True");
      flash = true;
    }
    else {
      Serial.println("False");
      flash = false;
    }
  } else {
    digitalWrite(signalPin, LOW);
  }

  if (flash) {
    Serial.println("Write");
    digitalWrite(signalPin, HIGH);
  } else {
    digitalWrite(signalPin, LOW);
  }

}
