//### This file created by BYACC 1.8(/Java extension  1.15)
//### Java capabilities added 7 Jan 97, Bob Jamison
//### Updated : 27 Nov 97  -- Bob Jamison, Joe Nieten
//###           01 Jan 98  -- Bob Jamison -- fixed generic semantic constructor
//###           01 Jun 99  -- Bob Jamison -- added Runnable support
//###           06 Aug 00  -- Bob Jamison -- made state variables class-global
//###           03 Jan 01  -- Bob Jamison -- improved flags, tracing
//###           16 May 01  -- Bob Jamison -- added custom stack sizing
//###           04 Mar 02  -- Yuval Oren  -- improved java performance, added options
//###           14 Mar 02  -- Tomas Hurka -- -d support, static initializer workaround
//### Please send bug reports to tom@hukatronic.cz
//### static char yysccsid[] = "@(#)yaccpar	1.8 (Berkeley) 01/20/90";






//#line 6 "cminus.y"
import java.io.*;
import java.util.*;
//#line 20 "Parser.java"




public class Parser
             implements ParserTokens
{

boolean yydebug;        //do I want debug output?
int yynerrs;            //number of errors so far
int yyerrflag;          //was there an error?
int yychar;             //the current working character

//########## MESSAGES ##########
//###############################################################
// method: debug
//###############################################################
void debug(String msg)
{
  if (yydebug)
    System.out.println(msg);
}

//########## STATE STACK ##########
final static int YYSTACKSIZE = 500;  //maximum stack size
int statestk[] = new int[YYSTACKSIZE]; //state stack
int stateptr;
int stateptrmax;                     //highest index of stackptr
int statemax;                        //state when highest index reached
//###############################################################
// methods: state stack push,pop,drop,peek
//###############################################################
final void state_push(int state)
{
  try {
		stateptr++;
		statestk[stateptr]=state;
	 }
	 catch (ArrayIndexOutOfBoundsException e) {
     int oldsize = statestk.length;
     int newsize = oldsize * 2;
     int[] newstack = new int[newsize];
     System.arraycopy(statestk,0,newstack,0,oldsize);
     statestk = newstack;
     statestk[stateptr]=state;
  }
}
final int state_pop()
{
  return statestk[stateptr--];
}
final void state_drop(int cnt)
{
  stateptr -= cnt; 
}
final int state_peek(int relative)
{
  return statestk[stateptr-relative];
}
//###############################################################
// method: init_stacks : allocate and prepare stacks
//###############################################################
final boolean init_stacks()
{
  stateptr = -1;
  val_init();
  return true;
}
//###############################################################
// method: dump_stacks : show n levels of the stacks
//###############################################################
void dump_stacks(int count)
{
int i;
  System.out.println("=index==state====value=     s:"+stateptr+"  v:"+valptr);
  for (i=0;i<count;i++)
    System.out.println(" "+i+"    "+statestk[i]+"      "+valstk[i]);
  System.out.println("======================");
}


//########## SEMANTIC VALUES ##########
//public class ParserVal is defined in ParserVal.java


String   yytext;//user variable to return contextual strings
ParserVal yyval; //used to return semantic vals from action routines
ParserVal yylval;//the 'lval' (result) I got from yylex()
ParserVal valstk[];
int valptr;
//###############################################################
// methods: value stack push,pop,drop,peek.
//###############################################################
void val_init()
{
  valstk=new ParserVal[YYSTACKSIZE];
  yyval=new ParserVal();
  yylval=new ParserVal();
  valptr=-1;
}
void val_push(ParserVal val)
{
  if (valptr>=YYSTACKSIZE)
    return;
  valstk[++valptr]=val;
}
ParserVal val_pop()
{
  if (valptr<0)
    return new ParserVal();
  return valstk[valptr--];
}
void val_drop(int cnt)
{
int ptr;
  ptr=valptr-cnt;
  if (ptr<0)
    return;
  valptr = ptr;
}
ParserVal val_peek(int relative)
{
int ptr;
  ptr=valptr-relative;
  if (ptr<0)
    return new ParserVal();
  return valstk[ptr];
}
final ParserVal dup_yyval(ParserVal val)
{
  ParserVal dup = new ParserVal();
  dup.ival = val.ival;
  dup.dval = val.dval;
  dup.sval = val.sval;
  dup.obj = val.obj;
  return dup;
}
//#### end semantic value section ####
public final static short YYERRCODE=256;
final static short yylhs[] = {                           -1,
    2,    0,    1,    1,    3,    3,    4,    4,    6,    6,
    7,   10,    5,    8,    8,    8,   11,   11,   12,   12,
   13,    9,   14,   14,   15,   15,   16,   16,   16,   16,
   16,   16,   16,   16,   16,   23,   17,   26,   17,   27,
   28,   29,   18,   30,   31,   19,   32,   21,   22,   20,
   20,   25,   25,   34,   34,   34,   34,   34,   34,   33,
   33,   35,   35,   36,   36,   37,   37,   38,   38,   39,
   38,   38,   38,   24,   40,   40,   41,   41,
};
final static short yylen[] = {                            2,
    0,    2,    2,    1,    1,    1,    3,    6,    1,    1,
    0,    0,    8,    1,    1,    0,    3,    1,    2,    4,
    0,    5,    2,    0,    2,    0,    1,    1,    1,    1,
    1,    1,    1,    1,    1,    2,    4,    0,    8,    0,
    0,    0,   10,    0,    0,    7,    0,    6,    6,    2,
    3,    3,    1,    1,    1,    1,    1,    1,    1,    3,
    1,    1,    1,    3,    1,    1,    1,    3,    1,    0,
    5,    1,    1,    4,    1,    0,    3,    1,
};
final static short yydefred[] = {                         1,
    0,    0,   10,    9,    0,    4,    5,    6,    0,    3,
    0,    7,    0,    0,    0,    0,    0,    0,    0,    0,
    0,   18,    8,    0,   12,    0,    0,   21,   17,   20,
   13,    0,   24,    0,   23,    0,    0,    0,    0,    0,
    0,    0,   35,   22,    0,   28,   25,   27,   29,   30,
   31,   32,   33,   34,    0,    0,    0,    0,    0,    0,
   73,   50,    0,   72,    0,    0,    0,   65,   44,   47,
   36,   78,    0,    0,    0,    0,    0,    0,    0,    0,
   51,   63,   62,   55,   56,   54,   57,   58,   59,    0,
    0,   66,   67,    0,    0,    0,   74,    0,    0,   37,
    0,   40,    0,   68,    0,    0,   64,   45,    0,   77,
    0,    0,    0,    0,    0,    0,   49,    0,   41,   71,
    0,   48,    0,    0,   46,   39,   42,    0,   43,
};
final static short yydgoto[] = {                          1,
    5,    2,    6,    7,    8,    9,   14,   20,   46,   28,
   21,   22,   32,   34,   37,   47,   48,   49,   50,   51,
   52,   53,   54,   64,   65,   58,  113,  124,  128,   95,
  115,   96,   66,   90,   91,   67,   94,   68,   79,   73,
   74,
};
final static short yysindex[] = {                         0,
    0, -189,    0,    0, -189,    0,    0,    0, -229,    0,
 -255,    0, -216, -199, -213, -120, -204,    0, -191, -196,
 -183,    0,    0, -182,    0, -189, -178,    0,    0,    0,
    0, -174,    0, -189,    0, -149, -233, -255, -232, -157,
 -221, -143,    0,    0, -141,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -159, -208, -252, -134, -208, -130,
    0,    0, -208,    0, -124, -151, -266,    0,    0,    0,
    0,    0, -126, -123, -122, -121, -208, -119, -118, -116,
    0,    0,    0,    0,    0,    0,    0,    0,    0, -208,
 -208,    0,    0, -208, -208, -208,    0, -208, -115,    0,
 -114,    0, -208,    0, -218, -266,    0,    0, -113,    0,
 -117, -131, -210, -112, -111, -106,    0, -208,    0,    0,
 -210,    0, -105, -107,    0,    0,    0, -210,    0,
};
final static short yyrindex[] = {                         0,
    0,    0,    0,    0,  147,    0,    0,    0,    0,    0,
 -104,    0,    0,    0,    0, -103,    0, -234,    0,    0,
 -102,    0,    0, -206,    0,    0,    0,    0,    0,    0,
    0,    0,    0, -246,    0,    0, -110,    0, -100,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,  -98,    0,    0,    0, -180,
    0,    0,    0,    0,    0, -154, -201,    0,    0,    0,
    0,    0,    0,  -97,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0,    0,    0,    0,    0,    0,    0,
    0,    0,    0,    0, -132, -161,    0,    0,    0,    0,
    0,    0, -110,    0,    0,    0,    0,    0,    0,    0,
 -110,    0,    0,    0,    0,    0,    0, -110,    0,
};
final static short yygindex[] = {                         0,
    0,    0,  145,  128,    0,  -13,    0,    0,  136,    0,
    0,  141,    0,    0,    0, -101,    0,    0,    0,    0,
    0,    0,    0,  -37,  -55,    0,    0,    0,    0,    0,
    0,    0,   78,    0,    0,   81,    0,   79,    0,    0,
    0,
};
final static int YYTABLESIZE=173;
static short yytable[];
static { yytable();}
static void yytable(){
yytable = new short[]{                         55,
   72,   76,   19,   78,   60,   61,   92,   80,   93,   12,
   26,  119,   19,   13,   63,   26,   26,   26,   26,  125,
   36,  101,   10,   39,   26,   26,  129,   11,   40,   41,
   42,   43,   75,   15,   56,   60,   61,   26,   44,  108,
  109,   15,  110,   62,   57,   63,   39,  114,   60,   61,
   45,   40,   41,   42,   43,   82,   17,   83,   63,   19,
   23,   19,  123,   61,   61,   24,   61,   16,   61,    3,
    4,   25,   61,   45,   61,   55,   61,   61,   61,   61,
   61,   61,   26,   55,   69,   69,   27,   69,   70,   69,
   55,   30,   69,   69,   69,   69,   33,   69,   69,   69,
   69,   69,   69,   60,   60,   71,   60,   38,   60,   59,
   53,   53,   60,   53,   60,   53,   60,   60,   60,   60,
   60,   60,   82,   69,   83,   70,   84,   85,   86,   87,
   88,   89,   52,   52,   77,   52,   56,   52,   18,    4,
   81,   97,   98,  100,   99,  118,    2,  117,  102,   10,
  103,  104,  111,  127,  116,  112,  121,  120,  122,  126,
   21,   35,   11,   31,   16,   14,   29,  105,   38,   76,
   75,  106,  107,
};
}
static short yycheck[];
static { yycheck(); }
static void yycheck() {
yycheck = new short[] {                         37,
   56,   57,   16,   59,  257,  258,  273,   63,  275,  265,
  257,  113,   26,  269,  267,  262,  263,  264,  265,  121,
   34,   77,  257,  257,  271,  272,  128,  257,  262,  263,
  264,  265,  285,  268,  267,  257,  258,  284,  272,   95,
   96,  258,   98,  265,  277,  267,  257,  103,  257,  258,
  284,  262,  263,  264,  265,  274,  270,  276,  267,  266,
  265,  268,  118,  265,  266,  257,  268,  267,  270,  259,
  260,  268,  274,  284,  276,  113,  278,  279,  280,  281,
  282,  283,  266,  121,  265,  266,  269,  268,  269,  270,
  128,  270,  273,  274,  275,  276,  271,  278,  279,  280,
  281,  282,  283,  265,  266,  265,  268,  257,  270,  267,
  265,  266,  274,  268,  276,  270,  278,  279,  280,  281,
  282,  283,  274,  267,  276,  267,  278,  279,  280,  281,
  282,  283,  265,  266,  269,  268,  267,  270,  259,  260,
  265,  268,  266,  265,  267,  277,    0,  265,  268,    5,
  269,  268,  268,  261,  268,  270,  268,  270,  265,  265,
  271,   34,  267,   28,  268,  268,   26,   90,  269,  268,
  268,   91,   94,
};
}
final static short YYFINAL=1;
final static short YYMAXTOKEN=286;
final static String yyname[] = {
"end-of-file",null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,null,
null,null,null,"ID","NUM","VOID","INT","ELSE","IF","RETURN","WHILE","SEMI",
"COMMA","LPAREN","RPAREN","LBRACK","RBRACK","LBRACE","RBRACE","MULT","MINUS",
"DIVIDE","PLUS","ASSIGN","LT","GT","LTE","GTE","EQ","NOTEQ","PRINT","INPUT",
"ERROR",
};
final static String yyrule[] = {
"$accept : program",
"$$1 :",
"program : $$1 declaration_list",
"declaration_list : declaration_list declaration",
"declaration_list : declaration",
"declaration : var_declaration",
"declaration : fun_declaration",
"var_declaration : type_specifier ID SEMI",
"var_declaration : type_specifier ID LBRACK NUM RBRACK SEMI",
"type_specifier : INT",
"type_specifier : VOID",
"$$2 :",
"$$3 :",
"fun_declaration : type_specifier ID $$2 LPAREN params RPAREN $$3 compound_stmt",
"params : param_list",
"params : VOID",
"params :",
"param_list : param_list COMMA param",
"param_list : param",
"param : type_specifier ID",
"param : type_specifier ID LBRACK RBRACK",
"$$4 :",
"compound_stmt : $$4 LBRACE local_declarations statement_list RBRACE",
"local_declarations : local_declarations var_declaration",
"local_declarations :",
"statement_list : statement_list statement",
"statement_list :",
"statement : assign_stmt",
"statement : compound_stmt",
"statement : selection_stmt",
"statement : iteration_stmt",
"statement : return_stmt",
"statement : print_stmt",
"statement : input_stmt",
"statement : call_stmt",
"statement : SEMI",
"call_stmt : call SEMI",
"assign_stmt : ID ASSIGN expression SEMI",
"$$5 :",
"assign_stmt : ID $$5 LBRACK expression RBRACK ASSIGN expression SEMI",
"$$6 :",
"$$7 :",
"$$8 :",
"selection_stmt : IF LPAREN expression RPAREN $$6 statement $$7 ELSE $$8 statement",
"$$9 :",
"$$10 :",
"iteration_stmt : WHILE LPAREN $$9 expression $$10 RPAREN statement",
"$$11 :",
"print_stmt : PRINT LPAREN $$11 expression RPAREN SEMI",
"input_stmt : ID ASSIGN INPUT LPAREN RPAREN SEMI",
"return_stmt : RETURN SEMI",
"return_stmt : RETURN expression SEMI",
"expression : additive_expression relop additive_expression",
"expression : additive_expression",
"relop : LTE",
"relop : LT",
"relop : GT",
"relop : GTE",
"relop : EQ",
"relop : NOTEQ",
"additive_expression : additive_expression addop term",
"additive_expression : term",
"addop : PLUS",
"addop : MINUS",
"term : term mulop factor",
"term : factor",
"mulop : MULT",
"mulop : DIVIDE",
"factor : LPAREN expression RPAREN",
"factor : ID",
"$$12 :",
"factor : ID $$12 LBRACK expression RBRACK",
"factor : call",
"factor : NUM",
"call : ID LPAREN args RPAREN",
"args : arg_list",
"args :",
"arg_list : arg_list COMMA expression",
"arg_list : expression",
};

//#line 507 "cminus.y"

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

//#line 418 "Parser.java"
//###############################################################
// method: yylexdebug : check lexer state
//###############################################################
void yylexdebug(int state,int ch)
{
String s=null;
  if (ch < 0) ch=0;
  if (ch <= YYMAXTOKEN) //check index bounds
     s = yyname[ch];    //now get it
  if (s==null)
    s = "illegal-symbol";
  debug("state "+state+", reading "+ch+" ("+s+")");
}





//The following are now global, to aid in error reporting
int yyn;       //next next thing to do
int yym;       //
int yystate;   //current parsing state from state table
String yys;    //current token string


//###############################################################
// method: yyparse : parse input and execute indicated items
//###############################################################
int yyparse()
{
boolean doaction;
  init_stacks();
  yynerrs = 0;
  yyerrflag = 0;
  yychar = -1;          //impossible char forces a read
  yystate=0;            //initial state
  state_push(yystate);  //save it
  val_push(yylval);     //save empty value
  while (true) //until parsing is done, either correctly, or w/error
    {
    doaction=true;
    if (yydebug) debug("loop"); 
    //#### NEXT ACTION (from reduction table)
    for (yyn=yydefred[yystate];yyn==0;yyn=yydefred[yystate])
      {
      if (yydebug) debug("yyn:"+yyn+"  state:"+yystate+"  yychar:"+yychar);
      if (yychar < 0)      //we want a char?
        {
        yychar = yylex();  //get next token
        if (yydebug) debug(" next yychar:"+yychar);
        //#### ERROR CHECK ####
        if (yychar < 0)    //it it didn't work/error
          {
          yychar = 0;      //change it to default string (no -1!)
          if (yydebug)
            yylexdebug(yystate,yychar);
          }
        }//yychar<0
      yyn = yysindex[yystate];  //get amount to shift by (shift index)
      if ((yyn != 0) && (yyn += yychar) >= 0 &&
          yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
        {
        if (yydebug)
          debug("state "+yystate+", shifting to state "+yytable[yyn]);
        //#### NEXT STATE ####
        yystate = yytable[yyn];//we are in a new state
        state_push(yystate);   //save it
        val_push(yylval);      //push our lval as the input for next rule
        yychar = -1;           //since we have 'eaten' a token, say we need another
        if (yyerrflag > 0)     //have we recovered an error?
           --yyerrflag;        //give ourselves credit
        doaction=false;        //but don't process yet
        break;   //quit the yyn=0 loop
        }

    yyn = yyrindex[yystate];  //reduce
    if ((yyn !=0 ) && (yyn += yychar) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yychar)
      {   //we reduced!
      if (yydebug) debug("reduce");
      yyn = yytable[yyn];
      doaction=true; //get ready to execute
      break;         //drop down to actions
      }
    else //ERROR RECOVERY
      {
      if (yyerrflag==0)
        {
        yyerror("syntax error");
        yynerrs++;
        }
      if (yyerrflag < 3) //low error count?
        {
        yyerrflag = 3;
        while (true)   //do until break
          {
          if (stateptr<0)   //check for under & overflow here
            {
            yyerror("stack underflow. aborting...");  //note lower case 's'
            return 1;
            }
          yyn = yysindex[state_peek(0)];
          if ((yyn != 0) && (yyn += YYERRCODE) >= 0 &&
                    yyn <= YYTABLESIZE && yycheck[yyn] == YYERRCODE)
            {
            if (yydebug)
              debug("state "+state_peek(0)+", error recovery shifting to state "+yytable[yyn]+" ");
            yystate = yytable[yyn];
            state_push(yystate);
            val_push(yylval);
            doaction=false;
            break;
            }
          else
            {
            if (yydebug)
              debug("error recovery discarding state "+state_peek(0)+" ");
            if (stateptr<0)   //check for under & overflow here
              {
              yyerror("Stack underflow. aborting...");  //capital 'S'
              return 1;
              }
            state_pop();
            val_pop();
            }
          }
        }
      else            //discard this token
        {
        if (yychar == 0)
          return 1; //yyabort
        if (yydebug)
          {
          yys = null;
          if (yychar <= YYMAXTOKEN) yys = yyname[yychar];
          if (yys == null) yys = "illegal-symbol";
          debug("state "+yystate+", error recovery discards token "+yychar+" ("+yys+")");
          }
        yychar = -1;  //read another
        }
      }//end error recovery
    }//yyn=0 loop
    if (!doaction)   //any reason not to proceed?
      continue;      //skip action
    yym = yylen[yyn];          //get count of terminals on rhs
    if (yydebug)
      debug("state "+yystate+", reducing "+yym+" by rule "+yyn+" ("+yyrule[yyn]+")");
    if (yym>0)                 //if count of rhs not 'nil'
      yyval = val_peek(yym-1); //get current semantic value
    yyval = dup_yyval(yyval); //duplicate yyval if ParserVal is used as semantic value
    switch(yyn)
      {
//########## USER-SUPPLIED ACTIONS ##########
case 1:
//#line 23 "cminus.y"
{ 
					/*Enter Symbol Table Scope*/
					symtab.enterScope();

					/*Generate Code Prologue*/
					GenCode.genPrologue();
                }
break;
case 2:
//#line 31 "cminus.y"
{
                	if (usesRead) GenCode.genReadMethod();
                	/* generate class constructor code*/
					GenCode.genClassConstructor();

                	/* generate epilog*/
					GenCode.genEpilogue(symtab);

					/* Exit Symbol Table Scope*/
					symtab.exitScope();
                	if (!seenMain) 
					{
						semerror("No main in file"); 
					}
				}
break;
case 7:
//#line 57 "cminus.y"
{
						String name = val_peek(1).sval;
						int vartype = val_peek(2).ival;

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

							/*Generate code for variable declaration if in global scope*/
							if (symtab.getScope() == 0)
							{
								GenCode.genStaticDecl(rec);
							}
						}
					}
break;
case 8:
//#line 82 "cminus.y"
{
						String name = val_peek(4).sval;
						int vartype = val_peek(5).ival;
						int arrlen = val_peek(2).ival;
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

							/*Generate code for variable declaration if in global scope*/
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
break;
case 9:
//#line 112 "cminus.y"
{ yyval = val_peek(0); }
break;
case 10:
//#line 113 "cminus.y"
{ yyval = val_peek(0); }
break;
case 11:
//#line 117 "cminus.y"
{
						int rettype = val_peek(1).ival;
						String name = val_peek(0).sval;

						/*create FunRec for use in the symbol table*/
						FunRec rec = new FunRec(name, symtab.getScope(), rettype, null);

						/*pass to next part of rule below as $3*/
						yyval = new ParserVal(rec);

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

						/*Enter fxn body (new scope)*/
						symtab.enterScope();
					}
break;
case 12:
//#line 150 "cminus.y"
{
						int rettype = val_peek(5).ival;
						String name = val_peek(4).sval;

						/* get params and assign to fxn rec*/
						@SuppressWarnings("unchecked")
						List<SymTabRec> paramlist = (List<SymTabRec>)val_peek(1).obj;
						FunRec rec = (FunRec)val_peek(3).obj;
						rec.setParams(paramlist);

						if (name.equals("main"))
						{
							if(rettype != VOID)
							{
								semerror("Return type of main must be void");
							}
							if(val_peek(1).obj != null)
							{
								semerror("Params of main must be void or empty");
							}
						}

						/*Begin the function declaration*/
						GenCode.genFunBegin(rec);
					}
break;
case 13:
//#line 176 "cminus.y"
{
						firstTime = true;
						GenCode.genFunEnd();
					}
break;
case 14:
//#line 182 "cminus.y"
{ yyval = val_peek(0); }
break;
case 15:
//#line 183 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 16:
//#line 184 "cminus.y"
{ yyval = new ParserVal(null); }
break;
case 17:
//#line 188 "cminus.y"
{
					@SuppressWarnings("unchecked")
					List<SymTabRec> reclist = (List<SymTabRec>)val_peek(2).obj;
					SymTabRec rec = (SymTabRec)val_peek(0).obj;
					reclist.add(rec);
					yyval = new ParserVal(reclist);
				}
break;
case 18:
//#line 196 "cminus.y"
{
					List<SymTabRec> reclist = new ArrayList<SymTabRec>();
					SymTabRec rec = (SymTabRec)val_peek(0).obj;
					reclist.add(rec);
					yyval = new ParserVal(reclist);
				}
break;
case 19:
//#line 205 "cminus.y"
{
					int vartype = val_peek(1).ival;
					String name = val_peek(0).sval;
					VarRec rec = new VarRec(name, symtab.getScope(), vartype);
					if (symtab.lookup(name))
					{
						semerror("Redclaration of name " + name + " in the current scope.");
					}
					else
					{
						symtab.insert(name, rec);
					}
					yyval = new ParserVal(rec);
				}
break;
case 20:
//#line 220 "cminus.y"
{
					int vartype = val_peek(3).ival;
					String name = val_peek(2).sval;
					ArrRec rec = new ArrRec(name, symtab.getScope(), vartype, -1);
					if (symtab.lookup(name))
					{
						semerror("Redclaration of name " + name + " in the current scope.");
					}
					else
					{
						symtab.insert(name, rec);
					}
					yyval = new ParserVal(rec);
				}
break;
case 21:
//#line 237 "cminus.y"
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
break;
case 22:
//#line 248 "cminus.y"
{
					symtab.exitScope();
				}
break;
case 37:
//#line 276 "cminus.y"
{
					String name = val_peek(3).sval;
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
break;
case 38:
//#line 290 "cminus.y"
{
					SymTabRec rec = symtab.get(val_peek(0).sval);
					GenCode.genLoadArrAddr(rec);
				}
break;
case 39:
//#line 295 "cminus.y"
{
					String name = val_peek(7).sval;
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
break;
case 40:
//#line 314 "cminus.y"
{
					String label1 = GenCode.getLabel();
					yyval = new ParserVal(label1); /*becomes $5 below*/
					GenCode.genFGoto(label1);
				}
break;
case 41:
//#line 320 "cminus.y"
{
					String label2 = GenCode.getLabel();
					yyval = new ParserVal(label2); /*becomes $7 below*/
					GenCode.genGoto(label2);
				}
break;
case 42:
//#line 326 "cminus.y"
{
					GenCode.genLabel(val_peek(3).sval);
				}
break;
case 43:
//#line 330 "cminus.y"
{
					GenCode.genLabel(val_peek(3).sval);
				}
break;
case 44:
//#line 336 "cminus.y"
{
					String label1 = GenCode.getLabel();
					yyval = new ParserVal(label1); /*becomes $3 below*/
					GenCode.genLabel(label1);
				}
break;
case 45:
//#line 342 "cminus.y"
{
					String label2 = GenCode.getLabel();
					yyval = new ParserVal(label2); /*becomes $5 below*/
					GenCode.genFGoto(label2);
				}
break;
case 46:
//#line 348 "cminus.y"
{
					GenCode.genGoto(val_peek(4).sval);
					GenCode.genLabel(val_peek(2).sval);
				}
break;
case 47:
//#line 355 "cminus.y"
{
					GenCode.genBeginPrint();	
				}
break;
case 48:
//#line 359 "cminus.y"
{
					GenCode.genEndPrint();
				}
break;
case 49:
//#line 365 "cminus.y"
{
					String name = val_peek(5).sval;
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
break;
case 50:
//#line 385 "cminus.y"
{
					GenCode.genReturn();
				}
break;
case 51:
//#line 389 "cminus.y"
{
					GenCode.genIReturn();
				}
break;
case 52:
//#line 395 "cminus.y"
{
				GenCode.genRelOper(val_peek(1).ival);
			}
break;
case 60:
//#line 410 "cminus.y"
{
					GenCode.genArithOper(val_peek(1).ival);
				}
break;
case 64:
//#line 420 "cminus.y"
{
					GenCode.genArithOper(val_peek(1).ival);
				}
break;
case 69:
//#line 432 "cminus.y"
{
					String name = val_peek(0).sval;
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
						GenCode.genLoadArrAddr(rec); /*is this right for array ID?*/
					}
				}
break;
case 70:
//#line 453 "cminus.y"
{
					SymTabRec rec = symtab.get(val_peek(0).sval);
					GenCode.genLoadArrAddr(rec);
				}
break;
case 71:
//#line 458 "cminus.y"
{
					String name = val_peek(4).sval;
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
break;
case 73:
//#line 476 "cminus.y"
{
					int number = val_peek(0).ival;
					GenCode.genLoadConst(number);
				}
break;
case 74:
//#line 483 "cminus.y"
{
					String name = val_peek(3).sval;
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
break;
//#line 1042 "Parser.java"
//########## END OF USER-SUPPLIED ACTIONS ##########
    }//switch
    //#### Now let's reduce... ####
    if (yydebug) debug("reduce");
    state_drop(yym);             //we just reduced yylen states
    yystate = state_peek(0);     //get new state
    val_drop(yym);               //corresponding value drop
    yym = yylhs[yyn];            //select next TERMINAL(on lhs)
    if (yystate == 0 && yym == 0)//done? 'rest' state and at first TERMINAL
      {
      if (yydebug) debug("After reduction, shifting from state 0 to state "+YYFINAL+"");
      yystate = YYFINAL;         //explicitly say we're done
      state_push(YYFINAL);       //and save it
      val_push(yyval);           //also save the semantic value of parsing
      if (yychar < 0)            //we want another character?
        {
        yychar = yylex();        //get next character
        if (yychar<0) yychar=0;  //clean, if necessary
        if (yydebug)
          yylexdebug(yystate,yychar);
        }
      if (yychar == 0)          //Good exit (if lex returns 0 ;-)
         break;                 //quit the loop--all DONE
      }//if yystate
    else                        //else not done yet
      {                         //get next state and push, for next yydefred[]
      yyn = yygindex[yym];      //find out where to go
      if ((yyn != 0) && (yyn += yystate) >= 0 &&
            yyn <= YYTABLESIZE && yycheck[yyn] == yystate)
        yystate = yytable[yyn]; //get new state
      else
        yystate = yydgoto[yym]; //else go to new defred
      if (yydebug) debug("after reduction, shifting from state "+state_peek(0)+" to state "+yystate+"");
      state_push(yystate);     //going again, so push state & val...
      val_push(yyval);         //for next action
      }
    }//main loop
  return 0;//yyaccept!!
}
//## end of method parse() ######################################



//## run() --- for Thread #######################################
/**
 * A default run method, used for operating this parser
 * object in the background.  It is intended for extending Thread
 * or implementing Runnable.  Turn off with -Jnorun .
 */
public void run()
{
  yyparse();
}
//## end of method run() ########################################



//## Constructors ###############################################
/**
 * Default constructor.  Turn off with -Jnoconstruct .

 */
public Parser()
{
  //nothing to do
}


/**
 * Create a parser, setting the debug to true or false.
 * @param debugMe true for debugging, false for no debug.
 */
public Parser(boolean debugMe)
{
  yydebug=debugMe;
}
//###############################################################



}
//################### END OF CLASS ##############################
