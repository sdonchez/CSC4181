int x;
int y;

void f(int a, int b) {
  int x;
  { int y; x = y; }
  x = y;
}

void main(void) {
  int a;
  int b;
  x = 10;
  y = 20;
}
