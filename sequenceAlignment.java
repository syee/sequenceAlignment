import java.lang.Math;
import java.io.*;

public class sequenceAlignment{

	static	int Gap = 0;
	static	int sizeFirst;
	static	int sizeSecond;
	static	int[][] AlignmentMatrix;
	static	int[][] SimilarityMatrix = new int[4][4];
	static	String firstSequenceString;
	static	String secondSequenceString;

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
				sizeFirst = firstSequenceString.length();
				line = textFile.readLine();
				secondSequenceString = line;
				sizeSecond = secondSequenceString.length();

				createAlignmentMatrix(firstSequenceString, secondSequenceString);
				System.out.println("The least edit cost is: " + AlignmentMatrix[sizeSecond - 1][sizeFirst - 1]);
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

		char[] firstCharArray = firstSeq.toCharArray();
		char[] secondCharArray = secondSeq.toCharArray();

		AlignmentMatrix = new int [sizeSecond][sizeFirst];

		for (int i = 0; i < sizeFirst; i++){
			AlignmentMatrix[0][i] = i * Gap;
			//columns
		}
		for (int j = 0; j < sizeSecond; j++){
			AlignmentMatrix[j][0] = j * Gap;
			//rows
		}

		for (int i = 1; i < sizeFirst; i++){
			for (int j = 1; j < sizeSecond; j++){
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

	public static void main (String... Arguments) throws IOException{

		File testing = new File(System.getProperty("user.dir") + "/test");
		readData(testing);
	};
}