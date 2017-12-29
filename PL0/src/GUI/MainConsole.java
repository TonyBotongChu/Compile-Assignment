package GUI;

import java.io.*;
import java.util.ArrayList;
import PL0analyzer.*;

public class MainConsole
{
	public static ArrayList<String> stringArrayList = new ArrayList<>();
	public PVM pvm = null;

	public static void test_line(String str)
	{
		stringArrayList.add(str);
	}

	public void openfile(File file)
	{
		try
		{
			FileReader reader = new FileReader(file);
			BufferedReader br = new BufferedReader(reader);

			String str = null;

			while ((str = br.readLine()) != null)
			{
				test_line(str);
			}

			br.close();
			reader.close();

			GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
			grammarAnalyzer.ProgramAnalyze(stringArrayList);
			grammarAnalyzer.table.toStringArrayList();
			pvm = new PVM(grammarAnalyzer.codelist, grammarAnalyzer.table);
		}
		catch(FileNotFoundException e) {
			e.printStackTrace();
		}
		catch(IOException e) {
			e.printStackTrace();
		}
	}

	public void reset()
	{
		stringArrayList.clear();
		pvm = null;
	}
} 