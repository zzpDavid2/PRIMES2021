package problems;

import java.io.*;
import java.util.*;
import helpers.*;
import java.awt.*;

public class Problem4 {
	//Array that stores all the branches so that they can be sorted for output.
	private ArrayList<Branch<Character>> branches = new ArrayList<Branch<Character>> ();
	
	//map that stores the frequency of each character
	private HashMap<Character, Double> map = new HashMap<Character, Double>();
	
	private Scanner fileNameSc = new Scanner(System.in);
	
	public static void main(String[] args) throws IOException {
		
		Problem4 p4 = new Problem4();
		
		p4.input();
		
		p4.createCode();
		
		p4.output();
	}

	private void input() throws IOException {
	    //Prompting for input file
		System.out.println("Please enter the input file name:");
		File inputFile = new File(fileNameSc.next());
		
		Scanner sc=new Scanner(inputFile);
		
		//create frequency map
		double total=0; //variable for summing the frequencies.
		double n =0; //number of characters

		while (sc.hasNext()) {
			//inputs
			double frequency; // Temporary variable that stores the frequency
			char character = sc.next().charAt(0);
			
			//checks if the file have ended
			if(sc.hasNextInt()) {
				frequency = (double) sc.nextInt();
			}else {
				frequency = sc.nextDouble();
			}
			
//			System.out.println(character + " " + frequency);
			//put data into map
			map.put(character, frequency);
			total+=frequency; //add to total percentage
			n++; //add count to total characters
	      }
		sc.close();
		
		n*=0.005;//each input could introduce maximum of 0.005 rounding error since they are rounded to the hundredth.
		
//		System.out.println(total);
		
		//check if total percentages add to 100% with a margin for rounding error.
		if(total>=100+n||total<=100-n) {
			System.out.println("The total persentage does not add up to 100%. Sum: "+ total);
		}
//		for(Map.Entry<Character, Integer> entry : map.entrySet()) {
//			System.out.println("Key : " + entry.getKey() + " Value : " + entry.getValue());
//		}
		
		sc.close();
	}
	
	private void createCode() {
		//creates a custom priority to create the tree
		MyPriorityQueue<Branch<Character>> pq = new MyPriorityQueue<Branch<Character>>();
		
		//put map into priority queue
		for(Character element : map.keySet()) {
			Branch<Character> b = new Branch<Character>(element);
			pq.add(b, map.get(element));
			branches.add(b);
		}	
		
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
		
		//collecting codes from the built tree
		Branch<Character> root = new Branch<Character>(pq.popFront(),pq.popFront());	
		triverse(root,""); //recursive function that creates the code table
		
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

	    //prompt for file name and creates the output file
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