// master
#include <SoftwareSerial.h>
#include <Wire.h>
#include <SPI.h>

#define sendSize 29

long spiCounter;
long i2cCounter;
char buf[sendSize];
char i2cBuf[sendSize];
String receiveFromUART;
byte toggle;

SoftwareSerial softSerial(8, 9);

void setup (void)
{
  Wire.begin(8);                // join i2c bus with address #8
  Wire.onRequest(requestEvent);

  softSerial.begin(115200);
  
  Serial.begin (115200);
  //Serial.println ("Starting");

  // digitalWrite(SS, HIGH);  // ensure SS stays high for now

  // Put SCK, MOSI, SS pins into output mode
  // also put SCK, MOSI into LOW state, and SS into HIGH state.
  // Then put SPI hardware into Master mode and turn SPI on
  //SPI.begin ();

  // Slow down the master a bit
  //SPI.setClockDivider(SPI_CLOCK_DIV16);

}  // end of setup

void loop (void)
{
  int i = 0;
  while (softSerial.available()) {
    receiveFromUART = softSerial.readStringUntil('*');
    receiveFromUART.toCharArray(buf, sendSize);
    Serial.println(buf);
  }
  
  
}  // end of loop


void requestEvent() {
  for (int i = 0; i < sendSize; i++) {
    i2cBuf[i] = buf[i];
  }
  Wire.write(i2cBuf);
}
