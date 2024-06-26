package problems;

import java.io.*;
import java.util.*;
import helpers.*;
import java.awt.*;

public class Problem6 {

	private HashMap<Character, String> table = new HashMap<Character, String> ();
	private Scanner fileNameSc = new Scanner(System.in);
	private File inputText;
	private File inputTable;
	private File outputText;
	
	public static void main(String[] args) throws IOException {
		
		Problem6 p6 = new Problem6();
		
		p6.input();
		
		p6.output();
	}

	private void input() throws IOException {
	    //Prompting for input files
		System.out.println("Please enter the input text file name.");
		inputText = new File(fileNameSc.next());
		
		System.out.println("Please enter the encoding table file name.");
		inputTable = new File(fileNameSc.next());
		
		//read code table
		Scanner tableSc = new Scanner(inputTable);
		while(tableSc.hasNext()) {
			char c = tableSc.next().charAt(0);
			String code = tableSc.next();
			table.put(c,code);
		}
		tableSc.close();
		
		System.out.println("Please enter the output file name.");
		outputText = new File(fileNameSc.next());
	}
	
	private void output() throws IOException {
		//setting up file reader
		FileReader fr = new FileReader(inputText);
		PrintWriter out = new PrintWriter(outputText);

		//encoding original text into compressed code
		for(int i = fr.read(); i !=-1; i = fr.read()) {
			char c =  Character.toLowerCase((char) i);
			if(table.containsKey(c)) {
				String next = table.get(c);
				out.print(next);
			} 
			
//			System.out.println(next);

		}
		
		fr.close();
		out.close();
		System.out.println("Outputed to " + outputText + ".");
	}

}