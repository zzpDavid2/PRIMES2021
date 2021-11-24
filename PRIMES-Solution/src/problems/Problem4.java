package problems;

import java.io.*;
import java.util.*;
import helpers.*;
import java.awt.*;

public class Problem4 {

	private ArrayList<Branch<Character>> branches = new ArrayList<Branch<Character>> ();
	private HashMap<Character, Double> map = new HashMap<Character, Double>();
	private Scanner fileNameSc = new Scanner(System.in);
//	private File inputFile;
//	private String inputFileName;
	
	public static void main(String[] args) throws IOException {
		
		Problem4 p4 = new Problem4();
		
		p4.input();
		
		p4.createCode();
		
		p4.output();
	}

	private void input() throws IOException {
//		file selector
//		FileDialog inputDialog = new FileDialog((Frame)null, "Select Input File");
//	    inputDialog.setMode(FileDialog.LOAD);
//	    inputDialog.setVisible(true);
//	    
//	    String fileDir = inputDialog.getDirectory();
//	    inputFileName = inputDialog.getFile();
//	    String inputFilePath = fileDir+inputFileName;
//	    System.out.println( inputFilePath + " chosen.");
//		inputFile = new File(inputFilePath);
//		Scanner sc = new Scanner(inputFile); 
		
	    //create file reader
		System.out.println("Please enter the input file name:");
		File inputFile = new File(fileNameSc.next());
		
		Scanner sc=new Scanner(inputFile);
		
		//create frequency map
		
		double total=0; //variable for summing the frequencies.
		
		double n =0; //number of characters
		
		double frequency;
		
		while (sc.hasNext()) {
			//inputs
			char character = sc.next().charAt(0);
			System.out.println();
			if(sc.hasNextInt()) {
				frequency = (double) sc.nextInt();
			}else {
				frequency = sc.nextDouble();
			}
			System.out.println(character + " " + frequency);
			map.put(character, frequency);
			total+=frequency;
			n++;
	      }
		sc.close();
		
		n*=0.005;//each input could introduce maximum of 0.005 rounding error since they are rounded to the hundredth.
		
		System.out.println(total);
		
		if(total>=100+n||total<=100-n) {
			System.out.println("The total persentage does not add up to 100%. Sum: "+ total);
		}
//		for(Map.Entry<Character, Integer> entry : map.entrySet()) {
//			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
//		}
		
		sc.close();
	}
	
	private void createCode() {
MyPriorityQueue<Branch<Character>> pq = new MyPriorityQueue<Branch<Character>>();
		//put map in priority queue
		for(Character element : map.keySet()) {
			Branch<Character> b = new Branch<Character>(element);
			pq.add(b, map.get(element));
			branches.add(b);
		}	
		
//		System.out.println(pq);	
		//put node of end mark in pq
		
		//create tree with pq
		while(pq.size()>2) {
			double lp = pq.frontPriority();
			Branch<Character> left = pq.popFront();
			double rp = pq.frontPriority();
			Branch<Character> right = pq.popFront();
			double p = lp+rp;
			
			if(lp==rp) {
				if(right.data!=null && (left.data==null || left.data<right.data)) {
					Branch<Character> temp = right;
					right = left;
					left = temp;
				}
			}
			Branch<Character> parent = new Branch<Character>(left, right);
			pq.add(parent, p);
		}	
		//collecting the built tree
		Branch<Character> root = new Branch<Character>(pq.popFront(),pq.popFront());	
		
		//recursive function that creates the code table
		triverse(root,"");
		
	}
	
	private void triverse(Branch<Character> n, String prev) {
		//recursive function that traverses the binary tree and creates the codes
		if(n.isLeaf) {
			n.code = prev;
			return;
		}else {
			triverse(n.left, prev+"0");
			triverse(n.right, prev+"1");
		}
		
	} 
	
	private void output() throws FileNotFoundException {
		System.out.println(branches);
		
		//sorting the branches by the codes' short-lex order
	    Collections.sort(branches, new Comparator<Branch<Character>>() {
	    	public int compare(Branch<Character> x, Branch<Character> y) {
	    		String a = x.code;
	    		String b = y.code;
	    		if(a.length()==b.length()) {
	    			int i=0;
	    			while(a.charAt(i)==b.charAt(i)) {
	    				i++;
	    			}
	    			return a.charAt(i)-b.charAt(i);
	    		}else {
	    			return a.length()-b.length();
	    		}
	    	}
	    	
	    });
	    
	    System.out.println(branches);
	    
	    //finding the most appropriate name for the output file
	    //this does work, but not perfectly, when the input file has no extension, which is good enough
//	    int index=inputFileName.lastIndexOf(".");
//	    String FileName = "";
//	    if(index!=-1) FileName =  inputFileName.substring(0,index);
	    
	    //file dialog prompts for saving the output file
//	    FileDialog outputDialog = new FileDialog((Frame)null, "Select Output File Path");
//	    outputDialog.setMode(FileDialog.SAVE);
//	    outputDialog.setFile(FileName + "_Output.txt");
//	    outputDialog.setVisible(true);
	    
	    //creates the output file
		System.out.println("Please enter the output file name:");
		File outputFile = new File(fileNameSc.next());
		fileNameSc.close();
		
	    //writes in the output file
	    PrintWriter out = new PrintWriter(outputFile);
	    for(int i=0; i< branches.size(); i++) {
	    	System.out.println(branches.get(i));
		    out.println(branches.get(i));
	    }
	    out.close();
	}
}