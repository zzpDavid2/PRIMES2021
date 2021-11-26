package problems;

import java.io.*;
import java.util.*;
import helpers.*;
import java.awt.*;

public class Problem8 {
	//Array that stores all the branches so that they can be sorted for output.
	private ArrayList<Branch<Character>> branches = new ArrayList<Branch<Character>> ();
	private HashMap<Character, Double> map = new HashMap<Character, Double>();
	private Scanner fileNameSc = new Scanner(System.in);
	private double n = 0; //number of characters
//	private File inputFile;
//	private String inputFileName;
	
	public static void main(String[] args) throws IOException {
		
		Problem8 p8 = new Problem8();
		
		p8.input();
		
		p8.createCode();
		
		p8.output();
	}

	private void input() throws IOException {
	    //Prompting for input files
		System.out.println("Please enter the input file name:");
		File inputFile = new File(fileNameSc.next());
		
		Scanner sc=new Scanner(inputFile);
		
		//create frequency map	
		double total=0; //variable for summing the frequencies.
		double frequency;
		
		while (sc.hasNext()) {
			//inputs
			char character = sc.next().charAt(0);
			if(sc.hasNextInt()) {
				frequency = (double) sc.nextInt();
			}else {
				frequency = sc.nextDouble();
			}
//			System.out.println(character + " " + frequency);
			map.put(character, frequency);
			total+=frequency;
			n++;
	      }
		sc.close();
		
		double error = n*0.005;//each input could introduce maximum of 0.005 rounding error since they are rounded to the hundredth.
		
//		System.out.println(total);
		
		if(total>=100+error||total<=100-error) {
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
		//Calculate the length of the Huffman encoded text for 10000 characters
		int huffmanLength = 0;
	    for(int i=0; i< branches.size(); i++) {
	    	Branch<Character> b = branches.get(i);
	    	double freq = map.get(b.data);
	    	int codeLength = b.code.length();
	    	huffmanLength += codeLength*10000*freq/100;
	    }
	    
	    System.out.println("The length of the Huffman encoding is:");
	    System.out.println(huffmanLength);
	    
	    //Calculate the length of the encoding mentioned in problem 1 for 10000 character
	    int digit=0;
	    while(Math.pow(2, digit)<n) digit++;
	    double standardLength = 10000*digit;
//	    System.out.println(digit);
	    
	    //Calculate compression factor
	    double compressionFactor = huffmanLength / standardLength;
	    
	    System.out.println("The compression factor is:");
	    System.out.println(compressionFactor);
	}
}
