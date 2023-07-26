

void setup() {
  // put your setup code here, to run once:

  pinMode(9, OUTPUT);
  pinMode(8, OUTPUT);
  pinMode(7, OUTPUT);
  

}

void loop() {
  // put your main code here, to run repeatedly:

  analogWrite(9, 200);
  digitalWrite(8, HIGH);
  digitalWrite(7, LOW);

}

