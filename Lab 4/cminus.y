/* 
 * A BYACCJ specification for the Cminus language.
 * Author: Vijay Gehlot
 */
%{
import java.io.*;
import java.util.*;
%}
  
%token ID NUM 
%token VOID INT 
%token ELSE IF RETURN WHILE
%token SEMI COMMA LPAREN RPAREN LBRACK RBRACK LBRACE RBRACE
%token MULT MINUS DIVIDE PLUS ASSIGN
%token LT GT LTE GTE EQ NOTEQ
%token PRINT INPUT
%token ERROR

/* */

%%

program:		{ 
					// TODO enter scope in symbol table
					// TODO generate code prologue
                } 
                declaration_list 
                {
                	if (usesRead) GenCode.genReadMethod();
                	// TODO generate class constructor code
                	// TODO generate epilog
                	// TODO exit symbol table scope
                	if (!seenMain) semerror("No main in file"); 
				}
			;

declaration_list:	declaration_list declaration
			|		declaration
			;

declaration:		var_declaration 
			| 		fun_declaration
			;

var_declaration:	type_specifier ID SEMI
			|		type_specifier ID LBRACK NUM RBRACK SEMI
			;

type_specifier:		INT
			|		VOID
			;

fun_declaration:	type_specifier ID LPAREN params RPAREN compound_stmt
			;

params:			param_list { $$ = $1; }
			|	VOID
			|		/* empty */
			;

param_list:		param_list COMMA param
			|	param
			;

param:			type_specifier ID
			|	type_specifier ID LBRACK RBRACK
			;

compound_stmt:	LBRACE local_declarations statement_list RBRACE
			;

local_declarations:	local_declarations var_declaration
			|		/* empty */
			;

statement_list:	statement_list statement
			|		/* empty */
			;

statement:		assign_stmt
			|	compound_stmt
			|	selection_stmt
			|	iteration_stmt
			|	return_stmt
			|	print_stmt
			|	input_stmt
			|	call_stmt
			|	SEMI
			;

call_stmt:  	call SEMI
			;

assign_stmt:	ID ASSIGN expression SEMI
			|	ID LBRACK expression RBRACK ASSIGN expression SEMI
			;

selection_stmt:	IF LPAREN expression RPAREN statement ELSE statement
			;

iteration_stmt:	WHILE LPAREN expression RPAREN statement
			;

print_stmt:		PRINT LPAREN expression RPAREN SEMI
			;

input_stmt:		ID ASSIGN INPUT LPAREN RPAREN SEMI
			;

return_stmt:	RETURN SEMI
			|	RETURN expression SEMI
			;

expression:	  	additive_expression relop additive_expression
			|	additive_expression
			;

relop:		 	LTE
			|	LT
			|	GT
			|	GTE
			|	EQ
			|	NOTEQ	
			;

additive_expression: 	additive_expression addop term
			|			term
			;

addop:			PLUS | MINUS
			;

term:			term mulop factor
			|	factor
			;

mulop:			MULT
			|	DIVIDE
			;

factor:			LPAREN expression RPAREN
			|	ID
			|	ID LBRACK expression RBRACK
			|	call
			|	NUM
			;

call:			ID LPAREN args RPAREN
			;

args:			arg_list
			|	/* empty */
			;

arg_list:		arg_list COMMA expression
			|	expression
			;

%%

/* reference to the lexer object */
private static Yylex lexer;

/* The symbol table */
public final SymTab<SymTabRec> symtab = new SymTab<SymTabRec>();

/* To check if main has been encountered and is the last declaration */
private boolean seenMain = false;

/* To take care of nuance associated with params and decls in compound stsmt */
private boolean firstTime = true;

/* To gen boilerplate code for read only if input was encountered  */
private boolean usesRead = false;

/* Interface to the lexer */
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
    System.err.println("Parse Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol() + ". Got: " + lexer.yytext());
}

/* For semantic errors */
public void semerror (String error)
{
    System.err.println("Semantic Error : " + error + " at line " + 
		lexer.getLine() + " column " + 
		lexer.getCol());
}

/* constructor taking in File Input */
public Parser (Reader r)
{
	lexer = new Yylex(r, this);
}

/* This is how to invoke the parser

public static void main (String [] args) throws IOException
{
	Parser yyparser = new Parser(new FileReader(args[0]));
	yyparser.yyparse();
}

*/

