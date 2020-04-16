/* Find the maximum of two values */

int x;
int y;

int max(int a, int b)
{
  if (a > b)
    return a;
  else
	return b;
}

void main(void)
{
	x = input();
	y = input();
	print( max(x, y) );
}
