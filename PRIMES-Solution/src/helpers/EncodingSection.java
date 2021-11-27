package helpers;

public class EncodingSection {
	public boolean isNew;
	public String content;
	public int startLocation;
	public int binaryLength;
	public int charCount;
	
	public EncodingSection(int sl, int l) {
		isNew = false;
		startLocation = sl;
		binaryLength = l;
	}
	
	public EncodingSection(String s) {
		isNew = true;
		content =s;
		binaryLength = 
		charCount = s.length();
	}
	
	public static int findCharCount() {
		
	}
	
	public static int findBinaryLength() {
		
	}
	
}
