/**
 * File: sequenceAlignment.java
 *
 * This is my implementation of a class to determine a best alignment of 
 * two sequences with the least edit distance.
 * @author Steven Yee
 * @version 1.0 11/23/14
*/

/*
 * I imported these classes to help me determine the minimum of numbers, 
 * read in text files, and backtrace to return an actual best alignment.
 */
import java.lang.Math;
import java.io.*;
import java.util.Stack;
import java.util.Scanner;


public class sequenceAlignment{

	protected static	int Gap = 0;
	protected static	int sizeFirst;
	protected static	int sizeSecond;
	protected static	int[][] AlignmentMatrix;
	protected static	int[][] SimilarityMatrix = new int[4][4];
	protected static	String firstSequenceString;
	protected static	String secondSequenceString;
	protected static  char[] firstCharArray;
	protected static  char[] secondCharArray;


	/*
	 * This method takes in a constant gap cost, a similarity matrix indicating costs of mismatches, 
	 * and two sequences defined over the alphabet {A,C,G,T}. It returns a least edit distance alignment of the two sequences.
	 * @param file where file contains the data.
	 */
	static public void readData(File file){

		try{
			BufferedReader textFile = new BufferedReader(new FileReader(file));
			try{
				String line = null;
				line = textFile.readLine();
				Gap = Integer.parseInt(line);
				for (int i = 0; i < 4; i++){
					line = textFile.readLine();
					createSimilarityMatrix(line, i);
				}

				line = textFile.readLine();
				firstSequenceString = line;
				if (line == null){
					sizeFirst = 0;
				}
				else{
					sizeFirst = firstSequenceString.length();
				}
				line = textFile.readLine();
				secondSequenceString = line;
				if (line == null){
					sizeSecond = 0;
				}
				else{
					sizeSecond = secondSequenceString.length();
				}

				createAlignmentMatrix(firstSequenceString, secondSequenceString);
				System.out.println();
				displayBestAlignment();
				System.out.println("with the minimum edit distance of " + AlignmentMatrix[sizeSecond][sizeFirst] + ".");
				System.out.println();
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	};

	/*
	 * This method creates a similarity matrix indicating the costs of mismatches.
	 * @param line where line is a string containing the penalties for a single character and all other characters.
	 * @param row where row specifies which "single character" we are observing.
	 */
	public static void createSimilarityMatrix(String line, int row){

		char[] lineArray = line.toCharArray();
		int count = 0;
		for (int i = 0; i < 7;){
			SimilarityMatrix[row][count] = (int)(lineArray[i] - '0');
			count++;
			i = i + 2;
		};
	};

	/*
	 * This method returns the cost of a mismatch between two characters.
	 * @param first where first is a character we are interested in.
	 * @param second where second is the other character we are interested in.
	 * @return the cost of a mismatch between the two characters based on the similarity matrix.
	 */
	public static int cost(char first, char second){

		int firstBase = convertBase(first);
		int secondBase = convertBase(second);

		return SimilarityMatrix[firstBase][secondBase];
	};

	/*
	 * This method converts a character into an integer so we can easily access teh similarity matrix.
	 * @param letter where letter is the character we wish to convert.
	 * @return the integer representation of the character.
	 */
	public static int convertBase(char letter){

		switch(letter){
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			default: return 3;
		}
	};
	/*
	 * This method creates an (m+1) x (n+1) matrix where each element is the least edit distance of
	 * the first j characters of the second sequence and the first i characters of the first sequence.
	 * @param firstSeq where firstSeq is the first sequence.
	 * @param secondSeq where secondSeq is the second sequence.
	 */
	public static void createAlignmentMatrix(String firstSeq, String secondSeq){

		if (firstSeq == null){
			firstCharArray = null;
		}
		else{
			firstCharArray = firstSeq.toCharArray();
		}
		if (secondSeq == null){
			secondCharArray = null;
		}
		else{
			secondCharArray = secondSeq.toCharArray();
		}

		AlignmentMatrix = new int [sizeSecond + 1][sizeFirst + 1];

		for (int i = 0; i <= sizeFirst; i++){
			AlignmentMatrix[0][i] = i * Gap;
		}
		for (int j = 0; j <= sizeSecond; j++){
			AlignmentMatrix[j][0] = j * Gap;
		}

		for (int i = 1; i <= sizeFirst; i++){
			for (int j = 1; j <= sizeSecond; j++){
				char firstChar = firstCharArray[i - 1];
				char secondChar = secondCharArray[j - 1];
				
				int top = Gap + AlignmentMatrix[j - 1][i];
				int left = Gap + AlignmentMatrix[j][i - 1];
				int diag = cost(firstChar, secondChar) + AlignmentMatrix[j - 1][i - 1];
				int least = Math.min(top, Math.min(left, diag));

				AlignmentMatrix[j][i] = least;
			}
		}
	};

	/*
	 * This method backtraces the least cost matrix to output a best alignment of the two sequences.
	 */
	public static void displayBestAlignment(){

		Stack firstFinal = new Stack();
		Stack secondFinal = new Stack();
		Stack finalCosts = new Stack();
		int firstIndex = sizeFirst;
		int secondIndex = sizeSecond;

		if (firstIndex == 0){
			for (int i = 1; i <= secondIndex; i++){
				firstFinal.push('-');
				secondFinal.push(secondCharArray[secondIndex - i]);
				finalCosts.push(Gap);
			};
			secondIndex = 0;
		}

		if (secondIndex == 0){
			for (int i = 1; i <= firstIndex; i++){
				firstFinal.push(firstCharArray[firstIndex - i]);
				secondFinal.push('-');
				finalCosts.push(Gap);
			};
			firstIndex = 0;
		}

		while (firstIndex > 0){
			if (secondIndex == 0){
					for (int i = 1; i <= firstIndex; i++){
						firstFinal.push(firstCharArray[firstIndex - i]);
						secondFinal.push('-');
						finalCosts.push(Gap);
					}
					firstIndex = 0;
			};
			while (secondIndex > 0){

				if (firstIndex == 0){
					for (int i = 1; i <= secondIndex; i++){
						secondFinal.push(secondCharArray[secondIndex - i]);
						firstFinal.push('-');
						finalCosts.push(Gap);
					}
					secondIndex = 0;
				}
				else{
					int top = Gap + AlignmentMatrix[secondIndex - 1][firstIndex];
					int left = Gap + AlignmentMatrix[secondIndex][firstIndex - 1];
					int diag = AlignmentMatrix[secondIndex - 1][firstIndex - 1] + cost(firstCharArray[firstIndex - 1], secondCharArray[secondIndex - 1]);
					int least = Math.min(top, Math.min(left, diag));
					if (least == diag){
						firstFinal.push(firstCharArray[firstIndex - 1]);
						secondFinal.push(secondCharArray[secondIndex - 1]);
						finalCosts.push(cost(firstCharArray[firstIndex - 1], secondCharArray[secondIndex - 1]));
						firstIndex--;
						secondIndex--;
					}
					else if(least == top){
						firstFinal.push("-");
						secondFinal.push(secondCharArray[secondIndex - 1]);
						finalCosts.push(Gap);
						secondIndex--;
					}
					else{
						firstFinal.push(firstCharArray[firstIndex - 1]);
						secondFinal.push("-");
						finalCosts.push(Gap);
						firstIndex--;
					}
				};
			}
		}

		System.out.println("The best alignment is");
		System.out.println();
		while(!firstFinal.empty()){
			System.out.print(firstFinal.pop());
			System.out.print(' ');
		}
		System.out.println();
		while(!secondFinal.empty()){
			System.out.print(secondFinal.pop());
			System.out.print(' ');
		}
		System.out.println();
		while(!finalCosts.empty()){
			System.out.print(finalCosts.pop());
			System.out.print(' ');
		}
		System.out.println();
		System.out.println();
	}
	
	/*
	 * This is my main for the assignment.
	 */
	public static void main (String... Arguments) throws IOException{

		System.out.println("Please enter the name of your test file: ");
		Scanner sc = new Scanner(System.in);
		String fileName = sc.nextLine();
		File testing = new File(System.getProperty("user.dir") + '/' + fileName);
		readData(testing);
	};
}