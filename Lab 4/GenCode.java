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
			type = " [I"; // integer array

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
			// outputCode for .method for main method
			outputCode(".method public static main([Ljava/lang/String;)V");

			// outputCode for .throws any exceptions thrown by main
			outputCode(".throws java/io/IOException");
		}

		else
		{
			// outputCode for static .method (method name from fr.name)
			// also need the computeTypeDescr
			outputCode(".method static " + fr.name + computeTypeDesc(rec));
		}
		// requires analysis for exact count change this value? Nope.
		outputCode(".limit stack 32");
		// can be kept track of change this value? Nope.
		outputCode(".limit locals 32");
	}

	public static void genFunEnd()
	{
		// outputCode for end of function, including return
		genReturn();
		/*
		 * always put a return to avoid jumping an illegal instruction if for
		 * example the else has return e and is the last statement. See gcd1.c
		 * as an example
		 */
		// outputCode for end of .method
		outputCode(".end method\n");
	}

	public static void genReturn()
	{
		// outputCode for void return
		outputCode("return");
	}

	public static void genIReturn()
	{
		// outputCode for integer return
		outputCode("ireturn");
	}

	public static void genLoadArrAddr(SymTabRec r)
	{
		ArrRec ar = (ArrRec) r;
		if (ar.scope == 0)
		{
			// global array
			// outputCode for global (static) integer array, loading value from
			outputCode("getstatic " + className +"/" + ar.name + " [I");
		}
		else
		{
			// local array
			// outputCode for array load (need the ar.jvmnum)
			outputCode("aload " + ar.jvmnum);
		}
	}

	public static void genIAStore()
	{
		// outputCode for integer array store
		outputCode("iastore");
	}

	public static void genIALoad()
	{
		// outputCode for integer array load
		outputCode("iaload");
	}

	public static void genStore(SymTabRec r)
	{
		VarRec vr = (VarRec) r;
		if (vr.scope == 0)
		{
			// global var
			// outputCode for storing to a global (static) integer variable
			outputCode("putstatic " + className + "/" + vr.name + " I");
		}
		else
		{
			// outputCode for storing value of local int variable (need vr.jvmnum)
			outputCode("istore " + vr.jvmnum);
		}
	}

	public static void genLoadVar(SymTabRec r)
	{
		VarRec vr = (VarRec) r;
		if (vr.scope == 0)
		{
			// global variable
			// outputCode for loading value of global (static) int variable
			outputCode("getstatic " + className + "/" + vr.name + " I");
		}
		else
		{
			// outputCode for loading local int variable
			// initialize local variable to 0
			//outputCode("ldc 0");
			//outputCode("istore " + vr.jvmnum);
			outputCode("iload " + vr.jvmnum);
		}
	}

	public static void genLoadConst(int i)
	{
		// outputCode for loading constant value
		outputCode("ldc " + i);
	}

	public static void genXOR()
	{
		//we don't actually support this in c-, but a todo is a todo...
		outputCode("ixor");
	}

	public static void genArithOper(int t)
	{
		switch (t)
		{
			case PLUS:
				// outputCode for integer add
				outputCode("iadd");
				break;
			case MINUS:
				// outputCode for integer subtraction
				outputCode("isub");
				break;
			case MULT:
				// outputCode for integer multiplication
				outputCode("imul");
				break;
			case DIVIDE:
				// outputCode for integer division
				outputCode("idiv");
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
		// outputCode for integer negate - just to get rid of the todo
		outputCode("ineg");
	}

	public static void genFGoto(String lab)
	{
		// if false (= 0) then goto
		// outputcode for ifeq
		outputCode("ifeq " + lab);
	}

	public static void genGoto(String lab)
	{
		// outputCode for goto
		outputCode("goto " + lab);
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
		// outputCode for start of println of a single int value
		outputCode("getstatic java/lang/System/out Ljava/io/PrintStream;");
	}

	public static void genEndPrint()
	{
		// outputcode for ending of println (calling println here)
		outputCode("invokevirtual java/io/PrintStream/println(I)V");
	}

	// use this to generate code to read a value into a variable
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
		outputCode(".source " + fileName);
		outputCode(".class synchronized " + className);
		outputCode(".super java/lang/Object\n");
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
