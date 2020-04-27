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
					//Enter Symbol Table Scope
					symtab.enterScope();

					//Generate Code Prologue
					GenCode.genPrologue();
                } 
                declaration_list 
                {
                	if (usesRead) GenCode.genReadMethod();
                	// generate class constructor code
					GenCode.genClassConstructor();

                	// generate epilog
					GenCode.genEpilogue(symtab);

					// Exit Symbol Table Scope
					symtab.exitScope();
                	if (!seenMain) 
					{
						semerror("No main in file"); 
					}
				}
			;

declaration_list:	declaration_list declaration
			|		declaration
			;

declaration:		var_declaration 
			| 		fun_declaration
			;

var_declaration:	type_specifier ID SEMI
					{
						String name = $2.sval;
						int vartype = $1.ival;

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of name " + name + " in the current scope");
						}
						else if (vartype == VOID)
						{
							semerror("Declaration of variable " + name + " cannot be of type void");
						}
						else
						{
							SymTabRec rec = new VarRec(name, symtab.getScope(), vartype);
							symtab.insert(name, rec);

							//Generate code for variable declaration if in global scope
							if (symtab.getScope() == 0)
							{
								GenCode.genStaticDecl(rec);
							}
						}
					}
			|		type_specifier ID LBRACK NUM RBRACK SEMI
					{
						String name = $2.sval;
						int vartype = $1.ival;
						int arrlen = $4.ival;
							if (symtab.lookup(name))
						{
							semerror("Redeclaration of name " + name + " in the current scope");
						}
						else if (vartype == VOID)
						{
							semerror("Declaration of array " + name + " cannot be of type void");
						}
						else
						{
							SymTabRec rec = new ArrRec(name, symtab.getScope(), vartype, arrlen);
							symtab.insert(name, rec);

							//Generate code for variable declaration if in global scope
							if (symtab.getScope() == 0)
							{
								GenCode.genStaticDecl(rec);
							}
							else
							{
								GenCode.genArrInit(rec);
							}
						}
					}
			;

type_specifier:		INT 	{ $$ = $1; }
			|		VOID 	{ $$ = $1; }
			;

fun_declaration:	type_specifier ID 
					{
						int rettype = $1.ival;
						String name = $2.sval;

						//create FunRec for use in the symbol table
						FunRec rec = new FunRec(name, symtab.getScope(), rettype, null);

						//pass to next part of rule below as $3
						$$ = new ParserVal(rec);

						if (symtab.lookup(name))
						{
							semerror("Redeclaration of name " + name + " in the current scope");
						}
						else if (!seenMain)
						{
							symtab.insert(name, rec);
							SymTabRec.setJVMNum(0); /* reset JVM# to 0 */
							if (name.equals("main"))
							{
								seenMain = true;
								SymTabRec.setJVMNum(1); /* Start JVM # @ 1 for main */
							}
						}
						else
						{
							semerror("Function " + name + " declared after main.");
						}

						//Enter fxn body (new scope)
						symtab.enterScope();
					}
					LPAREN params RPAREN 
					{
						int rettype = $1.ival;
						String name = $2.sval;

						// get params and assign to fxn rec
						@SuppressWarnings("unchecked")
						List<SymTabRec> paramlist = (List<SymTabRec>)$5.obj;
						FunRec rec = (FunRec)$3.obj;
						rec.setParams(paramlist);

						if (name.equals("main"))
						{
							if(rettype != VOID)
							{
								semerror("Return type of main must be void");
							}
							if($5.obj != null)
							{
								semerror("Params of main must be void or empty");
							}
						}

						//Begin the function declaration
						GenCode.genFunBegin(rec);
					}
					compound_stmt
					{
						firstTime = true;
						GenCode.genFunEnd();
					}
			;

params:			param_list 	{ $$ = $1; }
			|	VOID 		{ $$ = new ParserVal(null); }
			|	/* empty */	{ $$ = new ParserVal(null); }
			;

param_list:		param_list COMMA param
				{
					@SuppressWarnings("unchecked")
					List<SymTabRec> reclist = (List<SymTabRec>)$1.obj;
					SymTabRec rec = (SymTabRec)$3.obj;
					reclist.add(rec);
					$$ = new ParserVal(reclist);
				}
			|	param
				{
					List<SymTabRec> reclist = new ArrayList<SymTabRec>();
					SymTabRec rec = (SymTabRec)$1.obj;
					reclist.add(rec);
					$$ = new ParserVal(reclist);
				}
			;

param:			type_specifier ID
				{
					int vartype = $1.ival;
					String name = $2.sval;
					VarRec rec = new VarRec(name, symtab.getScope(), vartype);
					if (symtab.lookup(name))
					{
						semerror("Redclaration of name " + name + " in the current scope.");
					}
					else
					{
						symtab.insert(name, rec);
					}
					$$ = new ParserVal(rec);
				}
			|	type_specifier ID LBRACK RBRACK
				{
					int vartype = $1.ival;
					String name = $2.sval;
					ArrRec rec = new ArrRec(name, symtab.getScope(), vartype, -1);
					if (symtab.lookup(name))
					{
						semerror("Redclaration of name " + name + " in the current scope.");
					}
					else
					{
						symtab.insert(name, rec);
					}
					$$ = new ParserVal(rec);
				}
			;

compound_stmt:	
				{
					if (firstTime)
					{
						firstTime = false;	
					}
					else
					{
						symtab.enterScope();
					}
				}
				LBRACE local_declarations statement_list RBRACE
				{
					symtab.exitScope();
				}
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
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec == null)
					{
						semerror("Undeclared name " + name + " in the current scope");
					}
					else if (!rec.isVar())
					{
						semerror("Name " + name + " is not a variable in the current scope");
					}
					GenCode.genStore(rec);
				}
			|	ID 
				{
					SymTabRec rec = symtab.get($1.sval);
					GenCode.genLoadArrAddr(rec);
				}
				LBRACK expression RBRACK ASSIGN expression SEMI
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec == null)
					{
						semerror("Undeclared name " + name + " in the current scope");
					}
					else if (!rec.isArr())
					{
						semerror("Name " + name + " is not an array in the current scope");
					}
					else
					{
						GenCode.genIAStore();
					}
				}
			;

selection_stmt:	IF LPAREN expression RPAREN 
				{
					String label1 = GenCode.getLabel();
					$$ = new ParserVal(label1); //becomes $5 below
					GenCode.genFGoto(label1);
				}
				statement 
				{
					String label2 = GenCode.getLabel();
					$$ = new ParserVal(label2); //becomes $7 below
					GenCode.genGoto(label2);
				}
				ELSE 
				{
					GenCode.genLabel($5.sval);
				}
				statement
				{
					GenCode.genLabel($7.sval);
				}
			;

iteration_stmt:	WHILE LPAREN 
				{
					String label1 = GenCode.getLabel();
					$$ = new ParserVal(label1); //becomes $3 below
					GenCode.genLabel(label1);
				}
				expression
				{
					String label2 = GenCode.getLabel();
					$$ = new ParserVal(label2); //becomes $5 below
					GenCode.genFGoto(label2);
				}
				RPAREN statement
				{
					GenCode.genGoto($3.sval);
					GenCode.genLabel($5.sval);
				}
			;

print_stmt:		PRINT LPAREN 
				{
					GenCode.genBeginPrint();	
				}
				expression RPAREN SEMI
				{
					GenCode.genEndPrint();
				}
			;

input_stmt:		ID ASSIGN INPUT LPAREN RPAREN SEMI
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec == null)
					{
						semerror("Undeclared name " + name + " in the current scope.");
					}
					else if (!rec.isVar())
					{
						semerror("Name " + name + " is not a variable in the current scope.");
					}
					else
					{
						usesRead = true;
						GenCode.genRead(rec);
					}
				}
			;

return_stmt:	RETURN SEMI
				{
					GenCode.genReturn();
				}
			|	RETURN expression SEMI
				{
					GenCode.genIReturn();
				}
			;

expression:	  	additive_expression relop additive_expression
			{
				GenCode.genRelOper($2.ival);
			}
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
				{
					GenCode.genArithOper($2.ival);
				}
			|			term
			;

addop:			PLUS | MINUS
			;

term:			term mulop factor
				{
					GenCode.genArithOper($2.ival);
				}
			|	factor
			;

mulop:			MULT
			|	DIVIDE
			;

factor:			LPAREN expression RPAREN
			|	ID
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec ==null)
					{
						semerror("Undeclared name " + name + " in the current scope.");
					}
					else if (!(rec.isVar() || rec.isArr()))
					{
						semerror("Name " + name + " is not a variable or array in the current scope");
					}
					if (rec.isVar())
					{
						GenCode.genLoadVar(rec);
					}
					else
					{
						GenCode.genLoadArrAddr(rec); //is this right for array ID?
					}
				}
			|	ID
				{
					SymTabRec rec = symtab.get($1.sval);
					GenCode.genLoadArrAddr(rec);
				} 
				LBRACK expression RBRACK
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec ==null)
					{
						semerror("Undeclared name " + name + " in the current scope.");
					}
					else if (!rec.isArr())
					{
						semerror("Name " + name + " is not an array in the current scope");
					}
					else
					{
						GenCode.genIALoad();
					}
				}
			|	call
			|	NUM
				{
					int number = $1.ival;
					GenCode.genLoadConst(number);
				}
			;

call:			ID LPAREN args RPAREN
				{
					String name = $1.sval;
					SymTabRec rec = symtab.get(name);
					if (rec ==null)
					{
						semerror("Undeclared name " + name + " in the current scope.");
					}
					else if (!rec.isFun())
					{
						semerror("Name " + name + " is not a function in the current scope");
					}
					GenCode.genFunCall(rec);
				}
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

