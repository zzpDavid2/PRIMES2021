package helpers;

import java.io.FileNotFoundException;

public class WordEditGenerator {
	
	LetterGenerator lg;
	
	public WordEditGenerator() throws FileNotFoundException {
		lg = new LetterGenerator();
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		WordEditGenerator weg = new WordEditGenerator();
		for(int i=0; i<100;i++) {
			System.out.println(weg.generate(i));
		}
	}
	
	public String generate(int M) {
		String op="";
		
		//determine randomized character number
		Integer characterNumber = (int) (Math.random()*M);
		
		//determines randomized operation
		int operation = (int) (Math.random()*3);
		
		//determines randomized letter to be added or replacing
		String word = "" + lg.generate();
		while(Math.random()>=0.15) {
			word += lg.generate();
		}
		
//		System.out.print(operation);
		if(operation ==0) {
			//replace
			op += "R";
			op += " " + characterNumber.toString();
			op += " " + word.length();
			op += " " + word;
		}else if (operation ==1) {
			//delete
			op += "D";
			op += " " + characterNumber.toString();
			op += " " + word.length();
		}else {
			//add
			op += "A";
			op += " " + characterNumber.toString();
			op += " " + word.length();
			op += " " + word;
		}
		
		return op;
	}
}
