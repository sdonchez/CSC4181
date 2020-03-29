/*
Scanner template file for Math Expressions.
*/
%%

%unicode
%line
%column
%byaccj

%{
	/* Store a reference to the parser object. Needed to reference yylval. */
	private Parser yyparser;

	/* constructor taking an additional parser */
	public Yylex (java.io.Reader r, Parser yyparser)
	{
		this (r);	
		this.yyparser = yyparser;
	}

	/* return the current line number. */
	public int getLine()
	{
		return yyline;
	}

	/* return the current colum number. */
	public int getCol()
	{
		return yycolumn;
	}
	
	public int value;
	
	public void clearErrors()
	{
		yyparser.yyerrflag = 0;
		yyparser.yynerrs = 0;
	}

%}


letter = [A-Za-z]
digit = [0-9]

%%

{digit}+				{ yyparser.yylval = new ParserVal(yytext());
						  return ParserTokens.NUMBER; }
"+"						{ return ParserTokens.ADDOP; }
"-"						{ return ParserTokens.SUBOP; }
"*"						{ return ParserTokens.MULOP; }
"/"						{ return ParserTokens.DIVOP; }
"("						{ return ParserTokens.LPAREN; }
")"						{ return ParserTokens.RPAREN; }
[\r\n]+					{ return ParserTokens.NEWLINE; }
[ \t]+					{/* do nothing-skip whitespace */}
.						{ return ParserTokens.ERROR; }