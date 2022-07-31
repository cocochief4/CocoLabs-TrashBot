// master

#include <Wire.h>
#include <SPI.h>

#define sendSize 29

long spiCounter;
long i2cCounter;
char buf[sendSize];
char i2cBuf[sendSize];

void setup (void)
{
  Wire.begin(8);                // join i2c bus with address #8
  Wire.onRequest(requestEvent);
  
  Serial.begin (115200);
  Serial.println ("Starting");

  digitalWrite(SS, HIGH);  // ensure SS stays high for now

  // Put SCK, MOSI, SS pins into output mode
  // also put SCK, MOSI into LOW state, and SS into HIGH state.
  // Then put SPI hardware into Master mode and turn SPI on
  SPI.begin ();

  // Slow down the master a bit
  SPI.setClockDivider(SPI_CLOCK_DIV16);

}  // end of setup

void loop (void)
{
  if (spiCounter > 100000) {
    spiCounter = 0;
    
    // enable Slave Select
    digitalWrite(SS, LOW);    
    SPI.transfer(1); // initiate transmission
    for (int pos = 0; pos < sizeof (buf) - 1; pos++)
      {
      delayMicroseconds (15);
      buf [pos] = SPI.transfer (0);
      if (buf [pos] == 0)
        {
        break;
        }
      }
    
    buf [sizeof (buf) - 1] = 0;  // ensure terminating null
    
    // disable Slave Select
    digitalWrite(SS, HIGH);
    
    Serial.print ("We received: ");
    Serial.println (buf);

    for (int i = 0; i < sendSize; i++) {
      i2cBuf[i] = buf[i];
    }
  }
  spiCounter++;
  
}  // end of loop


void requestEvent() {
  Wire.write(i2cBuf);
}
