package com.company;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import PL0analyzer.*;

public class Main {

	public static ArrayList<String> stringArrayList = new ArrayList<>();

    private static void debug_test()
    {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        LexicalAnalyzer lexicalAnalyzer = new LexicalAnalyzer();
        lexicalAnalyzer.lineAnalyse(s);
        for(Item item : lexicalAnalyzer.v)
        {
            System.out.print("Name: "+item.getWordName());
            System.out.print("  Value: "+item.getValue());
            System.out.print("\n");
        }
    }

    private static void file_test(String s)
    {
        try
        {
            // read file content from file
            StringBuffer sb = new StringBuffer("");

            FileReader reader = new FileReader(s);
            BufferedReader br = new BufferedReader(reader);

            String str = null;

            while ((str = br.readLine()) != null)
            {
                //sb.append(str + "/n");
                test_line(str);
            }

            br.close();
            reader.close();

            GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
            grammarAnalyzer.ProgramAnalyze(stringArrayList);
            grammarAnalyzer.table.toStringArrayList();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    private static void test_line(String str)
    {
        //System.out.println(str);
		//GrammarAnalyzer grammarAnalyzer = new GrammarAnalyzer();
		stringArrayList.add(str);
    }

    public static void main(String[] args) {
        //debug_test();
        String filename;
        filename = "/home/zbt/Documents/Compile Assignment/test/0.txt";
        /*
        System.out.println(">Please input filename:");
        System.out.print(">>");
        Scanner s=new Scanner(System.in);
        filename=s.next();
        */
        file_test(filename);
    }
}
