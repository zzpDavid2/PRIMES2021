package problems;

import java.io.*;
import java.util.*;
import helpers.*;
import java.awt.*;

public class Problem7 {

	static HashMap<String, Character> table = new HashMap<String, Character> ();
	private HashMap<Character, Double> map = new HashMap<Character, Double>();
	private Scanner fileNameSc = new Scanner(System.in);
	private File inputText;
	private File inputTable;
	private File outputText;
	
	public static void main(String[] args) throws IOException {
		
		Problem7 p7 = new Problem7();
		
		p7.input();

		p7.output();
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
			table.put(code, c);
		}
		tableSc.close();
		
		//Prompting for output file
		System.out.println("Please enter the output file name.");
		outputText = new File(fileNameSc.next());
	}
	
	private void output() throws IOException {
		//setting up files
		FileReader fr = new FileReader(inputText);
		PrintWriter out = new PrintWriter(outputText);
		
		//decoding
		String data = "";	
		for(int i = fr.read(); i !=-1; i = fr.read()) {
			data += (char) i;
			if(table.containsKey(data)) {
				System.out.print(table.get(data));

				out.print(table.get(data));
				data = "";
			}

		}
		System.out.println();
		
		fr.close();
		out.close(); 
		System.out.println("Outputed to " + outputText + ".");
	}
}