#include <SparkFun_u-blox_GNSS_Arduino_Library.h> //For GNSS
#include <u-blox_config_keys.h>
#include <u-blox_structs.h>
#include <SPI.h> //SPI Library
#include <Wire.h> //Needed for I2C to GNSS

#define sendSize 25

int i;
String finalSend;
volatile boolean active;
volatile int pos;
volatile char buf[sendSize] = "2834u9";

SFE_UBLOX_GNSS myGNSS;
long lastTime = 0; //Simple local timer. Limits amount if I2C traffic to u-blox module.

byte StringToByte(char * src){
    return byte(atoi(src));
}

void setup() {
  Serial.begin(115200); //Start Serial Monitor

  Wire.begin(); //Begin I2C Protocol
  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }
  
    SPI.begin(); //Begin SPI Protocol
    SPCR |= _BV(SPE); //Turn on Slave mode
    SPCR |= _BV(SPIE);
    SPI.attachInterrupt(); //Interuupt ON is set for SPI commnucation

    i = 0;
    
}

//Interrupt Routine function
ISR (SPI_STC_vect)
{
  Serial.println("interrupted");
  byte c = SPDR;
  Serial.println(c);

  if (c == 1)  // starting new sequence?
    {
      active = true;
      pos = -1;
      SPDR = buf [pos++];   // send first byte
      
      return;
    }

  if (!active)
    {
      SPDR = 0;
      return;
    }

  SPDR = buf [pos];
  if (buf [pos] == 0 || ++pos >= sizeof (buf)) {
    active = false;
  }
}

void loop() {
  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }

  //Query module only every second. Doing it more often will just cause I2C traffic.
  if (millis() - lastTime > 1000)
  {
    lastTime = millis(); //Update the timer

    long latitude = myGNSS.getLatitude();
    Serial.print(F("Lat: "));
    Serial.print(latitude);

    long longitude = myGNSS.getLongitude();
    Serial.print(F(" Long: "));
    Serial.print(longitude);

    byte RTK = myGNSS.getCarrierSolutionType();
    Serial.print(" RTK: ");
    Serial.print(RTK);
    if (RTK == 0) Serial.print(F(" (No solution)"));
    else if (RTK == 1) Serial.print(F(" (High precision floating fix)"));
    else if (RTK == 2) Serial.print(F(" (High precision fix)"));

    Serial.println();
    String latString = String(latitude);
    String longString = String(longitude);
    while (latString.length() < 10) {
      latString.concat(" ");
    }
    while (longString.length() < 11) {
      longString.concat(" ");
    }
    
    finalSend = latString + "," + longString + "," + RTK;
    Serial.println(finalSend);
  }
}
