/*====================================================================================*/
/* most - a nonsense program that uses just about all the syntax C-Minus has to offer */
/*====================================================================================*/

	/* how about a /* nested comment? * / more comment... */

int aNumber1;
int aNumber2;
int anArray[123];
int aVariable;
int anotherArray[234];

/*=========================*/
/* function declaration */
/*=========================*/

void function1(void)
{
	/* completely empty */
}

/* implicit void argument */
void function2()
{
	int a;
	int b;
	a = 5;
	b = a * 8 / 3 + 1;
	print(b);
}

/* function to read a value, return an int */
int readAValue()
{
	int value;
	value = input();
	return value;
}

int combinevalues ( int x, int y )
{
	int c;
    ; ; ; ; ; /* lots of empty semicolons */
	c = x * y / 3 - x + y; ; ; ;
	return c;
}

void manyRelationalOps(int value)
{
	if (value < 10)
		print(0);
	else
		print(10);
	if (value <= 20)
		print(20);
	else
		print(0);
	if (value == 30)
		print(30);
	else
		print(0);
	if (value != 40)
		print(0);
	else
		print(40);
	if (value >= 50)
		print(50);
	else
		print(0);
	if (value > 60)
		print(60);
	else
		print(0);
	if (value > 5)
	{
		/* value between 5 and 10 */
		if (value < 10)
			print(7);
		/* value 10 or greater */
		else
			print(15);
	}
	else
	{
		if (value > 2)
			print(3);
		else
			print(0);
	}
}

int sumarray(int length, int array1[])
{
    int sum;
	int emptyarray[300];
	int i;
	
	i = 0;
	sum = 0;
	
	while (i < length)
	{
		int value;
		value = array1[i];
		print(value);
		sum = sum + value;
		i = i + 1;
	}
	
	if (sum > length*3)
	{
		/* print sum if avg value is greater than 3 */
		print(sum);
	}
	else
	{
		/* print a bunch of nines otherwise */
		print(99999999);
		sum = 0;
	}
	return sum;
}

/*==============*/
/* main routine */
/*==============*/

void main ( void )
{
	int i;
	int n;
	int total;
	int array[10];
	i = 0;
	
    n = readAValue();
	while(i < 10)
	{
		array[i] = n;
		i = i + 1;
	}
	total = sumarray(10, array);
	print(total);
	manyRelationalOps(total);
}