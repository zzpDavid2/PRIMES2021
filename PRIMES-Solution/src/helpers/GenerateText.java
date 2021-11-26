package helpers;

import java.io.FileNotFoundException;

import helpers.*;

public class GenerateText {
	public static void main(String[] args) throws FileNotFoundException {
		LetterGenerator lg = new LetterGenerator();
		int n=50000;
		for(int i=0; i<n;i++) {
			System.out.print(lg.generate());
		}
	}
}
