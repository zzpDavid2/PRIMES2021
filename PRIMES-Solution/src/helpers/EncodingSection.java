package helpers;

public class EncodingSection {
	public boolean isWritten;
	public boolean isNew;
	public String content;
	public int startLocation;
	public int binaryLength;
	public int charCount;
	
	public EncodingSection(int sl, int l, int cc) {
		isWritten = false;
		isNew = false;
		startLocation = sl;
		binaryLength = l;
		charCount = cc;
	}
	
	public EncodingSection(String s, int bl) {
		isWritten = false;
		isNew = true;
		content = s;
		binaryLength = bl;
		charCount = s.length();
	}
	
	public String toString() {
		return content + " " + ((Integer) startLocation).toString() 
				+ " " + ((Integer) binaryLength).toString() 
				+ " " + ((Integer) charCount).toString();
	}
	
}
