import java.lang.Math;
import java.io.*;



protected int Gap = 0;
protected int sizeFirst;
protected int sizeSecond;
protected int[][] AlignmentMatrix;
protected int[][] SimilarityMatrix = new int[4][4];
protected String firstSequenceString;
protected String secondSequenceString;



static public void readData(File file){

	try{
		BufferedReeader TextFile = new BufferedReader(new FileReader(file));
		try{
			String line = null;
			line = TextFile.readLine();
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
			System.out.println("The least edit cost is: " + AlignmentMatrix[sizeFirst - 1][sizeSecond - 1]);
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	catch(IOException ex){
		ex.printStackTrace();
	}
};

public void createSimilarityMatrix(String line, int row){

	char[] lineArray = line.toCharArray();
	int count = 0;
	for (int i = 0; i < 7; i += 2){
		SimilarityMatrix[row][count++] = lineArray[i];
	};
}


public int cost(char first, char second){

	int firstBase = convertBase(first);
	int secondBase = convertBase(second);

	return costMatrix[firstBase][secondBase];
}

public int convertBase(char letter){

	switch(letter){
		case 'A': return 0;
		case 'C': return 1;
		case 'G': return 2;
		case 'T': return 3;
	};
}

public void createAlignmentMatrix(String firstSeq, String secondSeq){

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

	for (i = 1; i < sizeFirst; i++){
		for (j = 1; j < sizeSecond; j++){
			char firstChar = firstCharArray[i - 1];
			char secondChar = secondCharArray[j - 1];
			
			int top = Gap + AlignmentMatrix[j - 1][i];
			int left = Gap + AlignmentMatrix[j][i - 1];
			int diag = cost(firstChar, secondChar) + AlignmentMatrix[j - 1][i - 1];

			int least = Math.min(top, Math.min(left, diag));
			AlignmentMatrix[j, i] = least;
		}
	}
}

public static void main (String... Arguments) throws IOException{

	File = testing = new File(System.getProperty("user.dir") + "/test");
	readData(testing);
}