package problems;

import java.util.Scanner;

public class Problem10 {
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		System.out.println(generateEdit(sc.nextInt()));
		sc.close();
	}
	
	public static String generateEdit(int M) {
		String op="";
		
		//determine randomized character number
		Integer characterNumber = (int) (Math.random()*M);
		op += characterNumber.toString();
		
		//determines randomized operation
		int operation = (int) (Math.random()*3);
		
		//determines randomized letter to be added or replacing
		char letter = (char) (Math.random()*26+97);
		
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
