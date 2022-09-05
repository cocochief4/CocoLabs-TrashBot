#include <SparkFun_u-blox_GNSS_Arduino_Library.h>
#include <u-blox_config_keys.h>
#include <u-blox_structs.h>

#include <Wire.h>

// RC Control Pins
#define rc1Pin 5
#define rc2Pin 6
#define rc8Pin 7

#define rcRange 60 // The total range around 1500 which is 1500

#define rcCooldown 10 // RC delay in milliseconds
#define gpsCooldown 1000 // GPS delay in milliseconds

long gpsTime = 0; //Simple local timer. Limits amount if I2C traffic to u-blox module.
long rcTime = 0;

int rc1Val;
int rc2Val;
int rc8Val;

SFE_UBLOX_GNSS myGNSS;

void setup() {
  // Init Serials
  Serial.begin(115200);
  Serial1.begin(115200, SERIAL_8O1);
  
  pinMode(rc1Pin, INPUT);
  pinMode(rc2Pin, INPUT);
  pinMode(rc8Pin, INPUT);

  Wire.begin(); // Join I2C bus as master

  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }
  
} // End of Setup

void loop() {

} // End of Loop

String readRC() {
  rc1Val = pulseIn(rc1Pin, HIGH);
  rc2Val = pulseIn(rc2Pin, HIGH);
  rc8Val = pulseIn(rc8Pin, HIGH);
  if (rc1Val < 1000) {
    rc1Val = 1000;
  } else if (rc1Val > 2000) {
    rc1Val = 2000;
  } else if (rc1Val > 1500 - rcRange/2 && rc1Val < 1500 + rcRange/2) {
    rc1Val = 1500;
  }

  if (rc2Val < 1000) {
    rc2Val = 1000;
  } else if (rc2Val > 2000) {
    rc2Val = 2000;
  } else if (rc2Val > 1500 - rcRange/2 && rc2Val < 1500 + rcRange/2) {
    rc2Val = 1500;
  }

  if (rc8Val < 1500) {
    rc8Val = 0;
  } else if (rc8Val > 1500) {
    rc8Val = 1;
  }

  String string = String(rc1Val) + "," + String(rc2Val) + "," + String(rc8Val);

  return string;
}

String readGPS() {
  byte toggleFlag;

  if (myGNSS.begin() == false) //Connect to the u-blox module using Wire port
  {
    Serial.println(F("u-blox GNSS not detected at default I2C address. Please check wiring. Freezing."));
  }
  
  gpsTime = millis(); //Update the timer
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

  String string = String(latitude) + "," + String(longitude) + "," + String(RTK);

  return string;
  
}
