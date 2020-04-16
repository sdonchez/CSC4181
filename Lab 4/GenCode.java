import java.util.*;

// A utility class for generating JVM code
public class GenCode implements ParserTokens
{
	// name of generated class (= prog name)
	public static String className = "Main";

	// default name of input file
	public static String fileName = "stdin";

	// to generate new label numbers
	private static int labNum = 0;
	
	public static void outputCode(String code)
	{
		if (ParseMain.CODE_GEN_OUTPUT)
		{
			System.out.println(code);
		}
	}

	public static void genStaticDecl(SymTabRec r)
	{
		// for global variables
		String type;
		if (r.isVar())
		{
			type = " I"; // integer type
		}
		else
		{
			// array
			type = " "; // TODO type for integer array

		}
		outputCode(".field static " + r.name + type);
	}

	public static void genArrInit(SymTabRec rec)
	{
		String type;
		ArrRec ar = (ArrRec) rec;
		type = "int";

		// size of array
		genLoadConst(ar.size);
		outputCode("newarray " + type);

		// local array var
		if (ar.scope > 0)
		{
			outputCode("astore " + ar.jvmnum);
		}
		else
		{
			// global (i.e. static arr var)
			type = " [I";
			outputCode("putstatic " + className + "/" + ar.name + type);
		}
	}

	public static String computeTypeDesc(SymTabRec rec)
	{
		// type decriptors for functions
		FunRec fr = (FunRec) rec;
		List<SymTabRec> params = fr.getParams();
		StringBuffer typeDesc = new StringBuffer();
		typeDesc.append('(');
		if (params != null)
		{
			for (SymTabRec r : params)
			{
				if (r.isVar())
					typeDesc.append('I');
				else /* must be an array name since void is not allowed */
					typeDesc.append("[I");
			}
		}
		typeDesc.append(')');
		if (rec.type == INT)
			typeDesc.append('I');
		else
			typeDesc.append('V');
		return typeDesc.toString();
	}

	public static void genFunCall(SymTabRec rec)
	{
		outputCode("invokestatic " + className + "/" + rec.name
			+ computeTypeDesc(rec));
	}

	public static void genFunBegin(SymTabRec rec)
	{
		FunRec fr = (FunRec) rec;
		boolean ismain = fr.name.equals("main");
		outputCode("; >> FUNCTION " + fr.name + " <<");
		if (ismain)
		{
			// TODO
			// outputCode for .method for main method
			// outputCode for .throws any exceptions thrown by main
		}
		else
		{
			// TODO
			// outputCode for static .method (method name from fr.name)
			// also need the computeTypeDescr
		}
		// requires analysis for exact count TODO change this value?
		outputCode(".limit stack 32");
		// can be kept track of TODO change this value?
		outputCode(".limit locals 32");
	}

	public static void genFunEnd()
	{
		// TODO outputCode for end of function, including return
		/*
		 * always put a return to avoid jumping an illegal instruction if for
		 * example the else has return e and is the last statement. See gcd1.c
		 * as an example
		 */
		// TODO outputCode for end of .method
	}

	public static void genReturn()
	{
		// TODO outputCode for void return
	}

	public static void genIReturn()
	{
		// TODO outputCode for integer return
	}

	public static void genLoadArrAddr(SymTabRec r)
	{
		ArrRec ar = (ArrRec) r;
		if (ar.scope == 0)
		{
			// global array
			// TODO outputCode for global (static) integer array, loading value from
		}
		else
		{
			// local array
			// TODO outputCode for array load (need the ar.jvmnum)
		}
	}

	public static void genIAStore()
	{
		// TODO outputCode for integer array store
	}

	public static void genIALoad()
	{
		// TODO outputCode for integer array load
	}

	public static void genStore(SymTabRec r)
	{
		VarRec vr = (VarRec) r;
		if (vr.scope == 0)
		{
			// global var
			// TODO outputCode for storing to a global (static) integer variable
		}
		else
		{
			// TODO outputCode for storing value of local int variable (need vr.jvmnum)
		}
	}

	public static void genLoadVar(SymTabRec r)
	{
		VarRec vr = (VarRec) r;
		if (vr.scope == 0)
		{
			// global variable
			// TODO outputCode for loading value of global (static) int variable
			outputCode("getstatic " + className + "/" + vr.name + " I");
		}
		else
		{
			// TODO outputCode for loading local int variable
			// initialize local variable to 0
			//outputCode("ldc 0");
			//outputCode("istore " + vr.jvmnum);
			outputCode("iload " + vr.jvmnum);
		}
	}

	public static void genLoadConst(int i)
	{
		// TODO outputCode for loading constant value
	}

	public static void genXOR()
	{
		// TODO outputCode for xor
	}

	public static void genArithOper(int t)
	{
		switch (t)
		{
			case PLUS:
				// TODO outputCode for integer add
				break;
			case MINUS:
				// TODO outputCode for integer subtraction
				outputCode("isub");
				break;
			case MULT:
				// TODO outputCode for integer multiplication
				break;
			case DIVIDE:
				// TODO outputCode for integer division
				break;
		}
	}

	public static void genCmp(String s)
	{
		// label if result false
		String labF = getLabel();
		
		// label if result true
		String labT = getLabel();
		
		outputCode(s + " " + labT);
		outputCode("iconst_0");
		
		genGoto(labF);
		genLabel(labT);
		outputCode("iconst_1");
		genLabel(labF);
	}

	public static void genRelOper(int t)
	{
		switch (t)
		{
			case LT:
				genCmp("if_icmplt");
				break;
			case GT:
				genCmp("if_icmpgt");
				break;
			case LTE:
				genCmp("if_icmple");
				break;
			case GTE:
				genCmp("if_icmpge");
				break;
			case EQ:
				genCmp("if_icmpeq");
				break;
			case NOTEQ:
				genCmp("if_icmpne");
				break;
		}
	}

	public static void genIneg()
	{
		// TODO outputCode for integer negate
	}

	public static void genFGoto(String lab)
	{
		// if false (= 0) then goto
		// TODO outputcode for ifeq
	}

	public static void genGoto(String lab)
	{
		// TODO outputCode for goto
	}

	public static String getLabel()
	{
		return "Label" + labNum++;
	}

	public static void genLabel(String lab)
	{
		outputCode(lab + ":");
	}

	public static void genBeginPrint()
	{
		// TODO outputCode for start of println of a single int value
	}

	public static void genEndPrint()
	{
		// TODO outputcode for ending of println (calling println here)
	}

	// TODO use this to generate code to read a value into a variable
	// indicated by the SymTabRec parameter
	public static void genRead(SymTabRec r)
	{
		outputCode("invokestatic " + className + "/myRead()I");
		genStore(r);
	}

	public static void genReadMethod()
	{
		// boilerplate code for read method
		String code = 
			"; >> READ METHOD <<\n" +
			".method public static myRead()I\n" +
			".throws java/io/IOException\n" + 
			"\n" +
			".limit stack 5\n" +
			".limit locals 2\n" +
			"\n" +
			"new java/io/BufferedReader\n" +
			"dup\n" +
			"new java/io/InputStreamReader\n" +
			"dup\n" +
			"getstatic java/lang/System/in Ljava/io/InputStream;\n" +
			"invokenonvirtual java/io/InputStreamReader/<init>(Ljava/io/InputStream;)V\n" +
			"invokenonvirtual java/io/BufferedReader/<init>(Ljava/io/Reader;)V\n" +
			"astore_0\n" +
			"aload_0\n" +
			"invokevirtual java/io/BufferedReader/readLine()Ljava/lang/String;\n" +
			"astore_1\n" +
			"aload_1\n" +
			"invokestatic java/lang/Integer/parseInt(Ljava/lang/String;)I\n" +
			"ireturn\n" +
			".end method\n" +
			"\n";
		outputCode(code);
	}

	public static void genPrologue()
	{
		// TODO outputCode for prologue that starts a Java program
		// note we have variables available for fileName and className
	}

	public static void genEpilogue(SymTab<SymTabRec> st)
	{
		// gen code to init global arrays
		outputCode("; >> CLASS INIT FOR GLOBAL ARRAYS <<");
		outputCode(".method static <clinit>()V");
		outputCode(".limit stack 1");
		outputCode(".limit locals 0");
		for (SymTabRec r : st.getScopeTable(0).values())
		{
			if (r.isArr())
			{
				genArrInit(r);
			}
		}
		outputCode("return");
		outputCode(".end method\n");
	}

	public static void genClassConstructor()
	{
		// boilerpalte code
		outputCode("; >> CONSTRUCTOR <<");
		outputCode(".method <init>()V");
		outputCode(".limit stack 1");
		outputCode(".limit locals 1");
		outputCode("aload_0");
		outputCode("invokenonvirtual java/lang/Object/<init>()V");
		outputCode("return");
		outputCode(".end method\n");
	}
}
