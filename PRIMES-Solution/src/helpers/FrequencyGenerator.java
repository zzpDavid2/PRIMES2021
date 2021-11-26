package helpers;

import java.util.Scanner;

public class FrequencyGenerator {
	public static void main(String[] args) {
		//input distribution
		Scanner sc =new Scanner(System.in);	
		int n=50;
		double[] dis = new double[n];
		double sum =0;
		for(int i=0;i<n;i++) {
			dis[i] = Math.abs(sc.nextDouble());
			sum += dis[i];
		}
		
		for(int i=0;i<n;i++) {
			System.out.println((char) (i+33) + " " + 100 * dis[i]/sum);
		}
	}
}
