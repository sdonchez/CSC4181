/* 
File with actions to add ids to symbol table.

*/
%{
import java.io.*;
%}

%token <ival> NUMBER
%token <sval> ADDOP SUBOP MULOP DIVOP LPAREN RPAREN NEWLINE ERROR 
%%
explist:   exp NEWLINE explist	{ System.out.println("explist: exp NEWLINE explist"); }
         | exp					{ System.out.println("explist: exp"); }
;
exp:       term addop exp
         | term
		 | error 				{ System.out.println("error"); } // error recovery
;
addop:     ADDOP				{ System.out.println("addop: ADDOP"); }
         | SUBOP
; 
term:      factor mulop term
         | factor
;
mulop:     MULOP
         | DIVOP
; 
factor:    LPAREN exp RPAREN	{ System.out.println("factor: LPAREN exp RPAREN"); }
         | number				{ System.out.println("factor: number"); }
; 
number:    NUMBER				{ System.out.println("number: NUMBER: "+$1); }
;
%%
   
/* reference to the lexer object */
private static Yylex lexer;

/* interface to the lexer */
private int yylex()
{
    int retVal = -1;
    try
	{
		retVal = lexer.yylex();
    }
	catch (IOException e)
	{
		System.err.println("IO Error:" + e);
    }
    return retVal;
}
	
/* error reporting */
public void yyerror (String error)
{
    System.err.println("Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol() + ". Got: " + lexer.yytext());
}

/* constructor taking in File Input */
public Parser (Reader r)
{
	lexer = new Yylex(r, this);
}
