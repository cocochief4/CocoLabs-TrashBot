#define ledPin 3

void setup()
{
  Serial1.begin(115200); // opens serial port with baud rate
  Serial.begin(115200);

  pinMode(ledPin, OUTPUT);

  digitalWrite(ledPin, HIGH);
  delay(3000);
  digitalWrite(ledPin, LOW);
}

long loopTime = millis();
void loop()
 {
  bool flash;
  char readByte;

  if (Serial1.available()) {
    Serial.println("reading");

    readByte = Serial1.read();

    if (readByte == 'T') {
      Serial.println("True");
      flash = true;
    }
    else {
      Serial.println("False");
      flash = false;
    }
  } else {
//    digitalWrite(ledPin, LOW);
  }

  if (flash) {
    Serial.println("Write");
    digitalWrite(ledPin, HIGH);
  } else {
//    digitalWrite(ledPin, LOW);
  }

}
