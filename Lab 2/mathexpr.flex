/* Scanner template file for Math Expressions.  */
%%

%unicode
%line
%column
%standalone

%{

	/* return the current line number. We need this
	   because yyline is made private and we don't have
	   a mechanism like extern in C. */
	public int getLine() {
		return yyline;
	}

	public int getCol() {
		return yycolumn;
	}
	
	public int value;

%}

digit = [0-9]

%%

{digit}+				{ value = Integer.parseInt(yytext()); return ParserTokens.NUMBER; }
"+"						{ return ParserTokens.ADDOP; }
"-"						{ return ParserTokens.ADDOP; }
"*"						{ return ParserTokens.MULOP; }
"/"						{ return ParserTokens.MULOP; }
"("						{ return ParserTokens.LPAREN; }
")"						{ return ParserTokens.RPAREN; }
[\r\n]+					{/* do nothing-skip whitespace */}
[ \t]+					{/* do nothing-skip whitespace */}
.						{ return ParserTokens.ERROR; }
