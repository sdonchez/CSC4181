import java.io.*;
import javax.swing.*;

public class MathExprParser implements ParserTokens
{
	public MathExprParser() throws IOException
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
		Parser yyparser = new Parser(in);
		int result = yyparser.yyparse();
		if (result == 0) // 0 if no error
		{
			System.out.println("Parse successful");
		}
		else
		{
			System.out.println("Parse errors\n");
		}
	}

	public static void main(String[] args) throws IOException
	{
		new MathExprParser();
	}
}
