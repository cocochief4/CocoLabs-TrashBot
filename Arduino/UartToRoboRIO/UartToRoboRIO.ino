void setup() {
  Serial.begin(115200, SERIAL_8O1);

}
int i = 0;
void loop() {
  delay(50);
  Serial.write(i);
  i++;

}
