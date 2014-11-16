import java.lang.Math;
import java.io.*;
import java.util.Stack;


public class sequenceAlignment{

	static	int Gap = 0;
	static	int sizeFirst;
	static	int sizeSecond;
	static	int[][] AlignmentMatrix;
	static	int[][] SimilarityMatrix = new int[4][4];
	static	String firstSequenceString;
	static	String secondSequenceString;
	static  char[] firstCharArray;
	static  char[] secondCharArray;

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
				displayBestAlignment();
				System.out.println("with the minimum edit distance of " + AlignmentMatrix[sizeSecond][sizeFirst] + ".");
			}
			catch(IOException ex){
				ex.printStackTrace();
			}
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	};

	public static void createSimilarityMatrix(String line, int row){

		char[] lineArray = line.toCharArray();
		int count = 0;
		for (int i = 0; i < 7;){
			SimilarityMatrix[row][count] = (int)(lineArray[i] - '0');
			count++;
			i = i + 2;
		};
	};


	public static int cost(char first, char second){

		int firstBase = convertBase(first);
		int secondBase = convertBase(second);

		return SimilarityMatrix[firstBase][secondBase];
	};

	public static int convertBase(char letter){

		switch(letter){
			case 'A': return 0;
			case 'C': return 1;
			case 'G': return 2;
			default: return 3;
		}
	};

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
				int least = Math.min(top, diag);

				AlignmentMatrix[j][i] = least;
			}
		}
	};

	public static void displayBestAlignment(){

		Stack firstFinal = new Stack();
		Stack secondFinal = new Stack();
		Stack finalCosts = new Stack();
		int firstIndex = sizeFirst;
		int secondIndex = sizeSecond;

		if (firstIndex == 0){
			for (int i = 1; i < secondIndex; i++){
				secondFinal.push(secondCharArray[secondIndex - i]);
			};
		}

		if (secondIndex == 0){
			for (int i = 1; i < firstIndex; i++){
				firstFinal.push(firstCharArray[firstIndex - i]);
			};
		}

		while (firstIndex > 0){
			if (secondIndex == 0){
					for (int i = 1; i < firstIndex; i++){
						firstFinal.push(firstCharArray[firstIndex - i]);
					}
					firstIndex = 0;
			};
			while (secondIndex > 0){

				if (firstIndex == 0){
					for (int i = 1; i < secondIndex; i++){
						secondFinal.push(secondCharArray[secondIndex - i]);
						firstFinal.push('-');
						finalCosts.push(Gap);
					}
					secondIndex = 0;
				};
				int top = AlignmentMatrix[secondIndex - 1][firstIndex];
				int left = AlignmentMatrix[secondIndex][firstIndex - 1];
				int diag = AlignmentMatrix[secondIndex-1][firstIndex-1];
				int least = Math.min(top, Math.min(left, diag));
				if (top == diag){
					firstFinal.push(firstCharArray[firstIndex - 1]);
					secondFinal.push(secondCharArray[secondIndex - 1]);
					finalCosts.push(cost(firstCharArray[firstIndex - 1], secondCharArray[secondIndex - 1]));
					firstIndex--;
					secondIndex--;
				}
				else if(least == left){
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
			}
		}
	}

	public static void main (String... Arguments) throws IOException{

		File testing = new File(System.getProperty("user.dir") + "/test");
		readData(testing);
	};
}