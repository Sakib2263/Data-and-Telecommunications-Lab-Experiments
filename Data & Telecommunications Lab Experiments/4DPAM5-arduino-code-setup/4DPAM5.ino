const int PIN[4] = {5, 7, 9, 11};
const int code_levels[4] = {0, 7, 63, 255};

void setup() {
  // put your setup code here, to run once:
  Serial.begin(9600);
  for (int i=0; i<4; i++) pinMode(PIN[i], OUTPUT);
}

void show_character(char in) {
  int codes[4];
  Serial.print(in);
  for (int i=0; i<4; i++) {
    int shift = 6-2*i;
    codes[i] = in & (3<<shift);
    codes[i] >>= shift;
    Serial.print(codes[i]);
    analogWrite(PIN[i], code_levels[codes[i]]);
    delay(1000);
  }
}

void loop() {
  // put your main code here, to run repeatedly:
  if (Serial.available() > 0) {
    char input = Serial.read();
    show_character(input);
    delay(1000);
  }
}
