/* 
File with actions to add ids to symbol table.  class 2/20/20 
*/
%{
import java.io.*;
%}

%token <sval> NUMBER ADDOP SUBOP MULOP DIVOP LPAREN RPAREN NEWLINE ERROR 
%type <sval> number

%%
explist:   exp 					{ printResults(); } NEWLINE explist	
         | exp					{ printResults(); }
;
exp:       term addop exp
         | term
		 | error 				{ foundError = true; 
								  errorMessage("Invalid expression");}
;
addop:     ADDOP				{ line += "+"; }
         | SUBOP				{ line += "-"; }
		 | ADDOP ADDOP 			{ line += "++"; 
								  errorMessage("You can't have two + in a row!");
								  foundError = true; }
		 | SUBOP SUBOP			{ line += "--"; 
								  errorMessage("You can't have two - in a row!");
								  foundError = true; }
; 
term:      factor mulop term
         | factor
;
mulop:     MULOP				{ line += "*"; }
         | DIVOP				{ line += "/"; }
		 | MULOP MULOP			{ line += "**"; 
								  errorMessage("You can't have two * in a row!");
								  foundError = true; }
		 | DIVOP DIVOP			{ line += "//"; 
								  errorMessage("You can't have two / in a row!");
								  foundError = true; }
; 
factor:    LPAREN 				{ line += "("; lparen++; } exp RPAREN { line += ")"; rparen++; }
		 | LPAREN 				{ line += "("; lparen++; } exp {errorMessage("missing right parenthese");
									foundError = true; }
         | number				{ line += $1; }
; 
number:    NUMBER				{ $$ = $1; }
		 | SUBOP number			{ $$ = "-" + $2; }
;
%%
  
/* per-expression variables */
private String line = "";
private boolean foundError = false;
private int lparen = 0;
private int rparen = 0;
private String errorStr;

private void printResults()
{
	System.out.println("Expression: " + line);
	System.out.println(errorStr);
	System.out.println(foundError ? "REJECT" : "ACCEPT");
	System.out.println("_______________________________________");
	line = "";
	errorStr = "";
	foundError = false;
	lparen = 0;
	rparen = 0;
}

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

public void errorMessage(String error)
{
    errorStr = "Error: " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol() + ". Got: " + lexer.yytext();
}
	
/* error reporting */
public void yyerror (String error)
{
}

/* constructor taking in File Input */
public Parser (Reader r)
{
	lexer = new Yylex(r, this);
}
