package helpers;

import java.io.FileNotFoundException;

public class LetterEditGenerator {
	
	private LetterGenerator lg;
	
	public static void main(String[] args) throws FileNotFoundException {
		LetterEditGenerator leg = new LetterEditGenerator();
		int n=10;
		for(int i=0; i<n; i++) {
			System.out.println(leg.generate(50000));
		}
	}
	
	public LetterEditGenerator() throws FileNotFoundException {
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
		char letter = lg.generate();
		
//		System.out.print(operation);
		if(operation ==0) {
			//replace
			op += " r";
			op += " " + letter;
		}else if (operation ==1) {
			//delete
			op += " d";
		}else {
			//add
			op += " a";
			op += " " + letter;
		}
		
		return op;
	}
}
