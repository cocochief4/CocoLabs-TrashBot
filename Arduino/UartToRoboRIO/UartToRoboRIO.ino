void setup() {
  Serial3.begin(115200, SERIAL_8O1);

}
int i = 0;
void loop() {
  delay(10);
  Serial3.print(String(i) + "|");
  i++;

}
