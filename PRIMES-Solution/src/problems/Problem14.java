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
	private char[] memorySim; // an more realistic representation of the memory to allow for location based memory access 

	private LinkedList<EncodingSection> sectionData = new LinkedList<EncodingSection>();
	
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
		
		p14.outputToFile();
		
		p14.efficiencyReport();
	}
	
	public Problem14() {
		fileNameSc = new Scanner(System.in);
	}
	
	private void userInput() throws IOException {
	    //Prompting for input files
		
//		System.out.println("Please enter the encoding table file name.");
//		inputTable = new File(fileNameSc.next());
//		
//		System.out.println("Please enter the input text file name.");
//		inputText = new File(fileNameSc.next());
//
//		System.out.println("Please enter the edit command file name.");
//		inputEdit = new File(fileNameSc.next());
//		
//		System.out.println("Please enter the output file name.");
//		outputText = new File(fileNameSc.next());
		
		inputTable = new File("14_table.txt");
		inputText = new File("14.3_text.txt");
		inputEdit = new File("14.3.4_command.txt");
		outputText = new File("14.1_op.txt");
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
		
		System.out.println(table);
	}
	
	private void dataSetup() throws IOException {
		// set up the memory simulated with the string "memory" with Huffman codes;
		
		FileReader fr = new FileReader(inputText);
		
		memory = "";

		//encoding original text into Huffman code
		for(int i = fr.read(); i !=-1; i = fr.read()) {
			
			char c =  Character.toLowerCase((char) i);
			
//			System.out.println(c);
			
			if(table.containsKey(c)) {
				String next = table.get(c);
				memory+=next;
				
//				System.out.println(next);
			} 
		}
		
		fr.close();
		
		System.out.println(memory);
		
		maxMemoryAllowed = memory.length()/20;
	}
	
	private void editSetup() throws FileNotFoundException {
		
		Scanner sc = new Scanner(inputEdit);

		while(sc.hasNext()) {
			edits.add(new Edit(sc.nextLine()));
			System.out.println(edits.getLast());
		}
		
		sc.close();
	}
	
	private void processEdit() {
		EncodingSection origin = new EncodingSection(0,memory.length(),findCharCount(memory));
		
		System.out.println(origin);
		
		sectionData.add(new EncodingSection(0,memory.length(),findCharCount(memory)));
		
		int stackCount=0;
		
		while(!edits.isEmpty()) {

			Edit e = edits.pop();
			
			if(e.command == 'd' || e.command == 'D') {
				// delete command
				delete(e.location, e.deleteLength);
			}else if(e.command == 'a' || e.command == 'A' ) {
				// add command
				add(e.location, e.length, e.content);
			}else {
				//replace command
				delete(e.location, e.deleteLength);
				add(e.location, e.length, e.content);
			}
			
			stackCount ++;
			
			if(stackCount >= 5) {
				commit();
				stackCount = 0;
			}
			
		}
		
		for(int i=0; i < sectionData.size(); i++) {
			System.out.println(sectionData.get(i));
		}
		
		commit();
		
		System.out.println(memory);
	}
	
	private void delete(int sl, int dl) {
		// inputs sl: start location, dl: delete length
		int currIndex=0;
		
		LinkedList<EncodingSection> tempData = (LinkedList<EncodingSection>) sectionData.clone();
		
		int llIndex = 0; // index in the sectionData linked list
		
		for(EncodingSection es : tempData) {
			
			int sectionStart = currIndex;
			int sectionEnd = currIndex + es.charCount - 1;
			
			int deleteStart = sl;
			int deleteEnd = sl + dl - 1;
			
			if(deleteEnd < sectionStart || sectionEnd < deleteStart ){
				// the delete command does not interfere with the current section
			}else if(deleteStart <= sectionStart  && sectionEnd <= deleteEnd ) {
				// removes the section entirely when its coverd by the delete
				sectionData.remove(llIndex);
				llIndex--;
			}else if(deleteStart <= sectionStart && deleteEnd < sectionEnd) {
				//remove the first N characters that are deleted
				int N = deleteEnd - sectionStart + 1;
				sectionData.set(llIndex, removeLeftSection(es,N));
				
			}else if(sectionStart < deleteStart && deleteEnd < sectionEnd) {
				//inserts a new section to become the split section on the right
				int Nr = sectionEnd - deleteStart + 1;
				EncodingSection leftSection = removeRightSection(es, Nr);
				
				int Nl = deleteEnd - sectionStart + 1;
				EncodingSection rightSection = removeLeftSection(es, Nl);
				
				System.out.println(deleteStart);
				System.out.println(deleteEnd);
				System.out.println(Nl);
				
				sectionData.remove(llIndex);
				sectionData.add(llIndex, rightSection);
				sectionData.add(llIndex, leftSection);
				llIndex++;

			}else if(sectionStart < deleteStart && sectionEnd <= deleteEnd) {
				// removes the last N characters that are deleted
				int N = sectionEnd - deleteStart + 1;
				sectionData.set(llIndex, removeRightSection(es,N));
				
			}else if (deleteEnd < sectionStart) {
				break;
			}else {
				System.out.println("Intersection detection error");
			}
			
			llIndex ++;
			currIndex += es.charCount;
		}
		
	}
	
	private void add(int location, int length, String c) {
		
		int tarIndex = location;
		
		int currIndex=0;
		
		int llIndex = 0; // index in the sectionData linked list
		
		LinkedList<EncodingSection> tempData = (LinkedList<EncodingSection>) sectionData.clone();
		
		EncodingSection addedSection = new EncodingSection(c, findBinaryLength(c));
		
		for(EncodingSection es : tempData) {
			
			int sectionStart = currIndex;
			int sectionEnd = currIndex + es.charCount - 1;
			
//			System.out.println(sectionStart + " " + sectionEnd);
			
			if(tarIndex == sectionStart) {
				sectionData.add(llIndex, addedSection);
				llIndex++;
				return;
			}else if(sectionStart < tarIndex && tarIndex <= sectionEnd) {
				//breaks a section and inserts the new text in the middle
				EncodingSection left = removeRightSection(es, sectionEnd - tarIndex + 1);
				EncodingSection right = removeLeftSection(es, tarIndex - sectionStart);
				
//				System.out.println(sectionEnd - tarIndex + 1);
//				System.out.println(tarIndex - sectionStart);
				
				sectionData.remove(llIndex);
				sectionData.add(llIndex, right);
				sectionData.add(llIndex, addedSection);
				sectionData.add(llIndex, left);
				
				llIndex += 2;
				return;
			}
			
			llIndex ++;
			currIndex += es.charCount;
		}
		sectionData.add(addedSection);
	}
	
	private EncodingSection removeLeftSection(EncodingSection es, int N) {
		
		if(es.isNew) {
			String content = es.content.substring(N);
			return new EncodingSection(content, findBinaryLength(content));
		}else {
			
			String temp = memory.substring(es.startLocation, es.startLocation + es.binaryLength);
					
			int binaryN = locateMemoryIndex(temp, N+1) - 1;
			
//			System.out.println(temp);
//			System.out.println(binaryN);
			
			int startLocation = es.startLocation + binaryN;
			int binaryLength = es.binaryLength - binaryN;
			
			int charCount = findCharCount(memory.substring(startLocation, startLocation + binaryLength));
			
			return new EncodingSection(startLocation, binaryLength, charCount);
		}
	}
	
	private EncodingSection removeRightSection(EncodingSection es, int N) {
		
		if(es.isNew) {
			String content = es.content.substring(0, es.content.length() - N);
			return new EncodingSection(content, findBinaryLength(content));
		}else {
			String temp = memory.substring(es.startLocation, es.startLocation + es.binaryLength);
					
			int binaryN =  es.binaryLength - locateMemoryIndex(temp, es.charCount - N + 1) + 1;
			
//			System.out.println(es.charCount - N);
//			System.out.println(binaryN);
			
			int startLocation = es.startLocation;
			int binaryLength = es.binaryLength - binaryN;
			
//			System.out.println(es.binaryLength);
			
			int charCount = findCharCount(memory.substring(startLocation, startLocation + binaryLength));
			
			return new EncodingSection(startLocation, binaryLength, charCount);
		}
	}
	
	private int findBinaryLength(String text) {
		int op=0;

		for(int i = 0; i < text.length(); i ++) {
			char temp = text.charAt(i);
			if(table.containsKey(temp)) {
				op += table.get(temp).length();
			}
		}
		
		return op;
	}
	
	private int findCharCount(String encoding) {
		int op=0;
		
		String temp = "";
		for(int i = 0; i < encoding.length(); i ++) {
			temp += encoding.charAt(i);
			if(table.containsValue(temp)) {
				op++;
				temp = "";
			}
			bitLookups ++;
		}
		return op;
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
	
	ArrayList<Integer> originals;
	ArrayList<Target> targets;
	
	private void commit() {
		
		memorySim = memory.toCharArray();
		
		originals = new ArrayList<Integer>();
		
		ArrayList<Integer> originals = new ArrayList<Integer>();
		
		// add all original sections to the array
		for(int i=0; i< sectionData.size(); i++) {
			if(sectionData.get(i).isNew == false) {
				originals.add(i);
			}
		}
		
		targets = createTargets();
		
		while(originals.isEmpty() == false) {
			
			for(int i=0; i< originals.size(); i++) {
				//writes the original sections
				
				System.out.println(originals.get(i));

				int sectionIndex = originals.get(i);
				EncodingSection es = sectionData.get(sectionIndex);
				
				if(es.isNew && noInterfere(targets.get(sectionIndex))) {
					writeOriSection(i);
					es.isWritten = true;
					originals.remove(i);
				}
			}
		}
		
		for(int i=0; i< sectionData.size(); i++) {
			if(sectionData.get(i).isNew == true) {
				//write the new sections
				writeNewSection(i);
			}
		}
		
		//transfer the simulation back to the string
		memory = new String(memorySim);
		
		//reset setcionData
		sectionData.clear();
		sectionData.add(new EncodingSection(0,memory.length(),findCharCount(memory)));
	}
	
	private ArrayList<Target> createTargets() {
		ArrayList<Target> targets = new ArrayList<Target>();
		
		int currIndex = 0;
		for(int i=0; i<sectionData.size(); i++) {
			EncodingSection es = sectionData.get(i);
			
			Target t = new Target(currIndex, currIndex + es.binaryLength - 1, es.isNew);
			
			targets.add(t);
			currIndex += es.binaryLength;
		}
		return targets;
	}
	
	private class Target{
		
		int targetStart;
		int targetEnd;
		
		public Target(int ts, int tt, boolean in) {
			targetStart = ts;
			targetEnd = tt;
		}
	}
	
	private boolean noInterfere(Target t) {
		int left = t.targetStart;
		int right = t.targetEnd;
		for(int i=0; i< originals.size(); i++) {
			EncodingSection es = sectionData.get(originals.get(i));
			
			int endLocation = es.startLocation + es.binaryLength - 1;
			
			if(!es.isWritten && es.isNew && left <= es.startLocation && es.startLocation <= right) {
				return false;
			}else if(!es.isWritten && es.isNew && left <= endLocation && endLocation <= right) {
				return false;
			}
//			System.out.println("noInterfere");
		}
		return true;
	}
	
	private void writeOriSection(int index) {
		
		EncodingSection es = sectionData.get(index);
		
		int tarLocation = targets.get(index).targetStart;
		
		for(int i=0; i<es.binaryLength; i++) {
			// writes to memory
			int head = tarLocation + i;
			char data = memorySim[es.startLocation + i]; // code fetched from memory
			memorySim[head] = data;
		}
	}
	
	private void writeNewSection(int index) {
		EncodingSection es = sectionData.get(index);
		
		int tarLocation = targets.get(index).targetStart;
		String content = es.content;
		
		String temp = "";
		
		for(int i=0; i<content.length(); i++) {
			//convert to Huffman code
			temp += table.get(content.charAt(i));
		}
		
		for(int i=0; i<es.binaryLength; i++) {
			// writes to memory
			int head = tarLocation + i;
			char data = temp.charAt(i);
			memorySim[head] = data;
		}
		
	}
	
	
	private void outputToFile() throws FileNotFoundException {
	    PrintWriter out = new PrintWriter(outputText);
	    out.print(memory);
	    out.close();
	}
	
	private void efficiencyReport(){
		
	}
}
