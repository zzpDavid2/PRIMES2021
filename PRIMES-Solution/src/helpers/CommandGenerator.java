package helpers;

import java.io.FileNotFoundException;

public class CommandGenerator {
	public static void main(String[] args) throws FileNotFoundException {
		LetterEditGenerator leg = new LetterEditGenerator();
		WordEditGenerator weg = new WordEditGenerator();
		
		int M=5000, n=100;
		for(int i=0; i<n;i++) {
			int r = (int) (Math.random()*2);
//			System.out.println(r);
			if(r==0) {
				System.out.println(leg.generate(M));
			}else {
				System.out.println(weg.generate(M));
			}
		}
	}
}
