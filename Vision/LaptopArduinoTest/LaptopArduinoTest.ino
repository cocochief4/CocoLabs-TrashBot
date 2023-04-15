#define ledPin 3

void setup()
{
  Serial.begin(115200); // opens serial port with baud rate

  pinMode(ledPin, OUTPUT);
}

long loopTime = millis();
void loop()
 {
  if (millis()-loopTime < 1000) {
    digitalWrite(ledPin, HIGH);
  } else {
    digitalWrite(ledPin, LOW);
  }
  char readByte;

  if (Serial.available()) {

    readByte = Serial.read();

    Serial.print(readByte); // echoing the byte
    if (readByte == 'A') {
      loopTime = millis();
    }
  }

}
