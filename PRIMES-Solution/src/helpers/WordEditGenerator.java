package helpers;

import java.io.FileNotFoundException;

public class WordEditGenerator {
	
	LetterGenerator lg;
	
	public WordEditGenerator() throws FileNotFoundException {
		lg = new LetterGenerator();
	}
	
	public String generate(int M) {
		String op="";
		
		//determine randomized character number
		Integer characterNumber = (int) (Math.random()*M);
		op += characterNumber.toString();
		
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
			op += " R";
			op += " " + word;
		}else if (operation ==1) {
			//delete
			op += " D";
		}else {
			//add
			op += " A";
			op += " " + word;
		}
		
		return op;
	}
}
