import java.io.*;
import javax.swing.*;

public class MathExprScanner implements ParserTokens
{
	public MathExprScanner() throws IOException
	{
		InputStreamReader in = getStandardInput();
		// InputStreamReader in = getInputFromChooser();
		scan(in);
	}

	public InputStreamReader getInputFromChooser() throws FileNotFoundException
	{
		// Uncomment this to use the file chooser to pick a file
		JFileChooser chooser = new JFileChooser();
		chooser.setCurrentDirectory(new File("."));
		if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)
		{
			File file = chooser.getSelectedFile();
			return new InputStreamReader(new FileInputStream(file));
		}
		return new InputStreamReader(System.in);
	}

	public InputStreamReader getStandardInput()
	{
		return new InputStreamReader(System.in);
	}

	public void scan(InputStreamReader in) throws IOException
	{
		Yylex lexer = new Yylex(in);
		int token;
		String expr = "";
		boolean hadError = false;

		while ((token = lexer.yylex()) != Yylex.YYEOF)
		{
			// Gather up the current expression from each token
			expr += lexer.yytext() + " ";
			
			// ADD MORE BELOW to display all of the tokens your scanner recognizes
			switch (token)
			{
				case ADDOP:
					System.out.println(
						"ACCEPT " + lexer.yytext() + " ADDOP " + lexer.value);
					break;
				case MULOP:
					System.out.println(
						"ACCEPT " + lexer.yytext() + " MULOP " + lexer.yytext());
					break;
				case LPAREN:
					System.out.println("ACCEPT " + lexer.yytext() + " LPAREN " + "");
					break;
				case RPAREN:
					System.out.println("ACCEPT " + lexer.yytext() + " RPAREN " + "");
					break;
				case NUMBER:
					System.out.println("ACCEPT " + lexer.yytext() + " NUMBER " + "");
					break;
				case ERROR:
					hadError = true;
					System.out.println("REJECT " + lexer.yytext() + " ERROR " + " line = "
									+ lexer.getLine() + " col = "
									+ lexer.getCol());
					break;
			}
		}
	}

	public static void main(String[] args) throws IOException
	{
		new MathExprScanner();
	}
}
