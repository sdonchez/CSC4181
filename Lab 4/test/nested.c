int a;
void f(int b)
{
  {
    int c;
	c = 6;
    {
      int d;
      {
	    int e;
		d = 7;
	    e = d + c + b + a;
      }
    }
  }
}

void main(void)
{
	a = 5;
	print(a);
}
