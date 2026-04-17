#include <Wire.h>
#include <LiquidCrystal_I2C.h>

#define BUTTON_PIN 2

LiquidCrystal_I2C lcd(0x27, 20, 4);

bool lastButtonState = HIGH;

// Рекурсивный Фибоначчи
long fibonacci(int n) {
  if (n <= 1) return n;
  return fibonacci(n - 1) + fibonacci(n - 2);
}

void setup() {
  pinMode(BUTTON_PIN, INPUT_PULLUP);

  lcd.init();
  lcd.backlight();

  lcd.setCursor(0, 0);
  lcd.print("Press button...");
}

void loop() {
  bool currentButtonState = digitalRead(BUTTON_PIN);

  // Отлов нажатия
  if (lastButtonState == HIGH && currentButtonState == LOW) {

    int n = random(5, 31); // 5–30

    lcd.clear();

    // 📌 СТРОКА 1 — случайное число
    lcd.setCursor(0, 0);
    lcd.print("N: ");
    lcd.print(n);

    // ⏱ замер времени
    unsigned long startTime = micros();
    long fib = fibonacci(n);
    unsigned long endTime = micros();

    float timeSec = (endTime - startTime) / 1000000.0;

    // 📌 СТРОКА 2 — время
    lcd.setCursor(0, 1);
    lcd.print("Time: ");
    lcd.print(timeSec, 5);
    lcd.print(" s");

    // 📌 СТРОКА 3 — число Фибоначчи
    lcd.setCursor(0, 2);
    lcd.print("Fib: ");
    lcd.print(fib);

  }

  lastButtonState = currentButtonState;
}