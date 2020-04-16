int x;
int y;

void f(int a, int b) {
  int xx;
  { int yy; xx = yy; }
  xx = y;
}

void g(int aa, int bb) {
  int xxx;
  { int yyy; xxx = yyy; }
  xxx = y;
}

void main(void) {
  int aaa;
  int bbb;
  x = 10;
  y = 20;
}
