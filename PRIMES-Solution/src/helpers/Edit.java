package helpers;

import java.util.Scanner;

public class Edit {
	//this class stores an edit
	
	// I will not worry about the security of this code 
	//and set everything to public for convince
	public boolean isWord; //marks if this edit is a word edit, otherwise a letter edit
	public int location; // the location of the edit
	public char command; // the type of edit
	public String content; // the inserted text, if not a delete command
	public int deleteLength; // the length of the deleted section for replace or delete
	public int length; // the length of the inserted text
	String input; // the original input string that created this object
	
	public Edit(String in) {
		//stores the original input
		input = in;
		
		//uses a scanner to extract information
		Scanner sc = new Scanner(input);
		
		//stores information according to the edit type
		if(sc.hasNextInt()) {
			//letter edit
			isWord = false;
			location = sc.nextInt();
			command = sc.next().charAt(0);
			length = 1;
			if(sc.hasNext()) {
				content = sc.next();
				length = content.length();
			}

		}else {
			//word edit
			isWord = true;
			command = sc.next().charAt(0);
			location = sc.nextInt();
			if(sc.hasNextInt()) {
				deleteLength = sc.nextInt();
			}
			if(sc.hasNext()) {
				content = sc.next();
			}
		}
		
		sc.close();
	}
	
	public String toString() {
		return input;
	}
}
