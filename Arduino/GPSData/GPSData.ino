// Slave
#include <SoftwareSerial.h>
#include <SparkFun_u-blox_GNSS_Arduino_Library.h> //For GNSS
#include <u-blox_config_keys.h>
#include <u-blox_structs.h>
#include <Wire.h> //Needed for I2C to GNSS

#define sendSize 29

String finalSend;
byte toggleFlag;
char buffer[sendSize];

SFE_UBLOX_GNSS myGNSS;
long lastTime = 0; //Simple local timer. Limits amount if I2C traffic to u-blox module.

SoftwareSerial softSerial(8, 9);

void setup (void)
{
  softSerial.begin(115200);
  Serial.begin(115200);
  Wire.begin(); //Begin I2C Protocol
  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }

  // have to send on master in, *slave out*
  pinMode(MISO, OUTPUT);

  // turn on SPI in slave mode
  SPCR |= bit(SPE);

  // turn on interrupts
  SPCR |= bit(SPIE);
}  // end of setup

volatile int pos;
volatile bool active;

// SPI interrupt routine
ISR (SPI_STC_vect)
{
  char buf [sendSize];
  byte c = SPDR;

  if (c == 1)  // starting new sequence?
    {
 //   finalSend.toCharArray(buf, sendSize);
    active = true;
    pos = 0;
    //SPDR = buf [pos++];   // send first byte
    return;
    }

  if (!active)
    {
    SPDR = 0;
    return;
    }

  //SPDR = buf [pos];
  if (buf [pos] == 0 || ++pos >= sizeof (buf))
    active = false;
}  // end of interrupt service routine (ISR) SPI_STC_vect

void loop() {
  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }

  //Query module only every second. Doing it more often will just cause I2C traffic.
  if (millis() - lastTime > 1000)
  {
    lastTime = millis(); //Update the timer
    if (toggleFlag == 1) {
      toggleFlag = 0;
    } else {
      toggleFlag = 1;
    }

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
    
    finalSend = " " + latString + "," + longString + "," + RTK + "," + toggleFlag + "*";
    Serial.println(finalSend);
    
    softSerial.print(finalSend);
  }
}  // end of loop
