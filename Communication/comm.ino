void setup()
{

  Serial.begin(9600); // opens serial port with baud rate

}

void loop() {

    char readByte;

    if (Serial.available()) {

        readByte = Serial.read()

        Serial.print(readByte); // echoing the byte
    }

}