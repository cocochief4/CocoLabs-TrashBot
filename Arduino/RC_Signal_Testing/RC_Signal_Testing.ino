#define ch1 5
#define ch2 6

void setup() {
  Serial.begin(115200);
  
  pinMode(ch1, INPUT);
  pinMode(ch2, INPUT);

}

void loop() {
  // put your main code here, to run repeatedly:
  int ch1In = pulseIn(ch1, HIGH);
  int ch2In = pulseIn(ch2, HIGH);
  Serial.print("ch1: ");
  Serial.print(ch1In);
  Serial.print(", ch2: ");
  Serial.println(ch2In);

}
