void setup() {
  Serial1.begin(115200);
  Serial.begin(115200);

}

void loop() {
  Serial1.print("pong");
  Serial.println((byte) Serial1.read() + " ");

}
