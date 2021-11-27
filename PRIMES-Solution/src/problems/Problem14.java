package problems;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Scanner;
import helpers.*;

public class Problem14 {
	
	private HashMap<Character, String> table = new HashMap<Character, String> ();
	
	private Scanner fileNameSc;
	
	private File inputText;
	private File inputTable;
	private File inputEdit;
	private File outputText;
	
	private String memory;
	private char[] memorySim; // an alternate representation of the memory to allow out of current length memory access 
	
	private LinkedList<EncodingSection> tempData = new LinkedList<EncodingSection>();
	
	private Deque<Edit> edits = new ArrayDeque<Edit>();
	
	private int bitChanges;
	private int bitLookups;
	private int extraMemory;
	
	private int maxMemoryAllowed;
	
	public static void main(String[] args) throws IOException {
		
		Problem14 p14 = new Problem14();
		
		p14.userInput();
		
		p14.codeTableSetup();
		
		p14.dataSetup();
		
		p14.editSetup();
		
		p14.processEdit();
		
		p14.efficiencyReport();
	}
	
	public Problem14() {
		fileNameSc = new Scanner(System.in);
	}
	
	private void userInput() throws IOException {
	    //Prompting for input files
		
		System.out.println("Please enter the encoding table file name.");
		inputTable = new File(fileNameSc.next());
		
		System.out.println("Please enter the input text file name.");
		inputText = new File(fileNameSc.next());

		System.out.println("Please enter the encoding table file name.");
		inputEdit = new File(fileNameSc.next());
		
		System.out.println("Please enter the output file name.");
		outputText = new File(fileNameSc.next());
	}
	
	private void codeTableSetup() throws FileNotFoundException {
		
		//read code table
		Scanner tableSc = new Scanner(inputTable);
		while(tableSc.hasNext()) {
			char c = tableSc.next().charAt(0);
			String code = tableSc.next();
			table.put(c,code);
		}
		tableSc.close();
		
	}
	
	private void dataSetup() throws IOException {
		// set up the memory simulated with the string "memory" with Huffman codes;
		
		FileReader fr = new FileReader(inputText);

		//encoding original text into Huffman code
		for(int i = fr.read(); i !=-1; i = fr.read()) {
			
			char c =  Character.toLowerCase((char) i);
			
			if(table.containsKey(c)) {
				String next = table.get(c);
				memory+=next;
			} 
		}
		
		fr.close();
		
		memorySim = memory.toCharArray();
		
		maxMemoryAllowed = memory.length()/20;
	}
	
	private void editSetup() throws FileNotFoundException {
		
		Scanner sc = new Scanner(inputEdit);

		while(sc.hasNext()) {
			edits.add(new Edit(sc.nextLine()));
		}
		
		sc.close();
	}
	
	private void processEdit() {
		tempData.add(new EncodingSection(0,memory.length()));
		
		int stackCount=0;
		
		while(!edits.isEmpty()) {

			Edit e = edits.pop();
			
			if(e.command == 'd' || e.command == 'D') {
				// delete command
				delete(e.location, e.length);
			}else if(e.command == 'a' || e.command == 'A' ) {
				// add command
				add(e.location, e.length, e.content);
			}else {
				//replace command
				delete(e.location, e.length);
				add(e.location, e.length, e.content);
			}
			
			stackCount ++;
			
			if(stackCount == 5) {
				commit();
				stackCount = 0;
			}
			
		}
	}
	
	private void delete(int sl, int l) {
		
		int index=0;
		
		for(int i=0; i<tempData.size(); i++) {
			
			index += 
			
			EncodingSection es = tempData.get(i);
			
			int sectionStart = es.startLocation;
			int sectionEnd = es.startLocation + es.length - 1;
			
			int deleteStart = sl;
			int deleteEnd = sl + l - 1;
			
			if(sectionEnd < deleteStart || deleteEnd < sectionStart){
				
			}else if(deleteStart <= sectionStart  && sectionEnd <= deleteEnd ) {
				tempData.remove(i);
			}else if(deleteStart < sectionStart && deleteEnd < sectionEnd) {
				//remove the first N characters that are deleted
				int N = deleteEnd - sectionStart + 1;
				tempData.set(i, removeLeftSection(es,N));
			}else if(sectionStart < deleteStart && deleteEnd < sectionEnd) {
				//inserts a new section to become the split section on the right
				int Nr = deleteEnd - sectionEnd + 1;
				EncodingSection rightSection = new EncodingSection();
			}else if(sectionStart < deleteStart && sectionEnd < deleteEnd) {
				int N = deleteEnd - sectionEnd + 1;
			}else {
				System.out.println("Intersection detection error");
			}
		}
	}
	
	private void add(int sl, int l, String c) {
		
	}
	
	private void commit() {
		
	}
	
	private EncodingSection removeLeftSection(EncodingSection es, int N) {
		if(es.isNew) {
			String content = es.content.substring(N);
			return new EncodingSection(content);
		}else {
			int startLocation = es.startLocation + N;
			int length = es.length - N;
			return new EncodingSection(startLocation, length);
		}
	}
	
	private EncodingSection removeRightSection(EncodingSection es, int N) {
		if(es.isNew) {
			String content = es.content.substring(0, es.content.length() - N);
			return new EncodingSection(content);
		}else {
			int startLocation = es.startLocation;
			int length = es.length - N;
			return new EncodingSection(startLocation, length);
		}
	}
	
	private int locateMemoryIndex(String s, int n) {
		// this method locates the binary index of number nth character in the binary string s
		int op=0; // binary index count
		int i=0; //character index count
		
		String temp = "";
		
		// finds the end of the n-1th character
		while(i<n-1){
			// add to the binary index count
			temp += s.charAt(i);
			op++;
			
			if(table.containsValue(temp)) {
				// add to the character count
				i++;
				temp = "";
			}
		}
		
		op++; // moves op to the start of the nth character
		
		return op;
	}
	
	private void efficiencyReport(){
		
	}
}
