package com.company;

import java.io.*;
import java.util.Scanner;
import PL0analyzer.*;

public class Main {

    private static void debug_test()
    {
        Scanner sc = new Scanner(System.in);
        String s = sc.nextLine();
        Analyzer analyzer = new Analyzer();
        analyzer.lineAnalyse(s);
        for(Item item : analyzer.v)
        {
            System.out.print("Name: "+item.getWordName());
            System.out.print("  Value: "+item.getValue());
            System.out.print("\n");
        }
    }

    private void file_test(String s)
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
                sb.append(str + "/n");

                System.out.println(str);
            }

            br.close();
            reader.close();
        }
        catch(FileNotFoundException e) {
            e.printStackTrace();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
	// write your code here
        System.out.println("hello world");
        debug_test();
    }
}
