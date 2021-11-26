package helpers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Scanner;

public class LetterGenerator {
	
	// this main method tests the class
	public static void main(String[] args) throws FileNotFoundException {
		LetterGenerator lg = new LetterGenerator();
		
		//tests input
		for(int i=0;i<26;i++) {
		System.out.println((char)boundaries[i][0] + " " +  boundaries[i][1]);
		}
		
		//tests output
		for(int i=0; i<10;i++) {
			System.out.println(lg.generate());
		}
		
	}
	
	private static double[][] boundaries = new double[26][2];
	
	//this method generates letters of the given frequency 
	//by arranging them in to consecutive ranges with in the range 0-100
	//and then determines the generated letter by generating a random number in the range 0-100 
	//and checking which letter's range does it fall into
	public LetterGenerator() throws FileNotFoundException{
		File inputFile = new File("EnglishFrequency.txt");
		
		Scanner sc = new Scanner(inputFile);
		
		double b=0; //the previous boundary
		int i=0;
		while (sc.hasNext()) {
			//inputs 
			//the boundaries array stores the upper boundary of the range 
			double frequency; // Temporary variable that stores the frequency
			char character = sc.next().charAt(0);
			
			//checks if the file have ended
			if(sc.hasNextInt()) {
				frequency = (double) sc.nextInt();
			}else {
				frequency = sc.nextDouble();
			}
			
			//accumulates the frequency to get the next boundary
			b += frequency;
			
			//stores the character and its range into the array of tuples (2d array).
			boundaries[i][0] = character;
			boundaries[i][1] = b;
			
			i++;
	      }
		sc.close();
	}
	
	//this method will be called by other programs to generate letter according the English frequency
	public char generate() {
		char op='a';
		//random number from 0-100 but does not reach 100;
		double r = Math.random()*100;
		
		//checks for the range the random number r fall into
		for(int i =0; i<26; i++) {
			op = (char) boundaries[i][0];
			
			if(r<boundaries[i][1]) {
//				System.out.println(r + " " + boundaries[i][1]);
				return op;
			}
		}
		//since frequencies does not add up to 100
		//in the case where r is greater then all the frequencies 'z' will be returned
		return op;
	}
}
