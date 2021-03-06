/* Scanner for C-Minus JFlex specification to work with BYACCJ. Spec taken from
 * Louden's "Compiler Construction..." Appendix A. 
 *
 * Regular expression for COMMENT below is not quite right. It expects a space
 * before and after the opening and closing comment symbols. See the JFlex manual
 * and other online resoures for a better regular expression for handling nested
 * C-style comments.
 * 
 * Authors: Vijay Gehlot & Tom Way

 To generate ParserTokens.java interface containing token constants:
   yacc -d -J cminus.y

 */
%%

%unicode
%line
%column
%byaccj

%{
/* store a reference to the parser object */
private Parser yyparser;

/* constructor taking an additional parser */
public Yylex (java.io.Reader r, Parser yyparser)
{
	this (r);	
	this.yyparser = yyparser;
}

/*	return the current line number. We need this
  	because yyline is made private and we don't have
	a mechanism like extern in C.
*/
public int getLine()
{
	// add 1 because lineno starts at 0
	return yyline + 1;
}

public int getCol() 
{
	return yycolumn;
}
	
public int value;

%}

letter = [A-Za-z]
digit  = [0-9]
eol    = \r|\n|\r\n
ws     = {eol} | [ \t\f]
comment = "/*" [^*] ~"*/" | "/*" "*"+ "/"

%%



print			{return Parser.PRINT; }

return 			{return Parser.RETURN;}
input			{return Parser.INPUT;}
while 			{return Parser.WHILE;}
else 			{return Parser.ELSE;}
void 			{yyparser.yylval = new ParserVal(Parser.VOID); return Parser.VOID;}
int 			{yyparser.yylval = new ParserVal(Parser.INT); return Parser.INT;}
if 				{return Parser.IF;}

{letter}({letter}|{digit})*	{
								yyparser.yylval = new ParserVal(yytext());
								return Parser.ID;
							}
{digit}{digit}*				{
								value = Integer.parseInt(yytext());
								yyparser.yylval = new ParserVal(value);
								return Parser.NUM;
							}


"<="			{yyparser.yylval = new ParserVal(Parser.LTE); return Parser.LTE;}
">="			{yyparser.yylval = new ParserVal(Parser.GTE); return Parser.GTE;}
"<"				{yyparser.yylval = new ParserVal(Parser.LT); return Parser.LT;}
">"				{yyparser.yylval = new ParserVal(Parser.GT); return Parser.GT;}
"=="			{yyparser.yylval = new ParserVal(Parser.EQ); return Parser.EQ;}
"!="			{yyparser.yylval = new ParserVal(Parser.NOTEQ); return Parser.NOTEQ;}

"+"				{yyparser.yylval = new ParserVal(Parser.PLUS); return Parser.PLUS;}
"-"				{yyparser.yylval = new ParserVal(Parser.MINUS); return Parser.MINUS;}
"*"				{yyparser.yylval = new ParserVal(Parser.MULT); return Parser.MULT;}
"/"				{yyparser.yylval = new ParserVal(Parser.DIVIDE); return Parser.DIVIDE;}

"="				{return Parser.ASSIGN;}

";"				{return Parser.SEMI;}

","				{return Parser.COMMA;}

"("				{return Parser.LPAREN;}
")"				{return Parser.RPAREN;}
"["				{return Parser.LBRACK;}
"]"				{return Parser.RBRACK;}
"{"				{return Parser.LBRACE;}
"}"				{return Parser.RBRACE;}


{ws}			{/* ignore */}
{comment}       {/* ignore */}

.               {return Parser.ERROR;}
