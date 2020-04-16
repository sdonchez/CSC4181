/* Computes nth factorial recursively */

int fact(int n)
{
	int f;
	if (n > 1)
		f = n * fact(n-1);
	else
		f = 1;
	return f;
}

/* Main driver program */
void main ( void )
{
    int n;
	int f;
    n = input ();
	f = fact(n);
	print(f);
}
