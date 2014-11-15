Import.java.Math.Util;


protected int Gap = 0;
protected int[][] AlignmentMatrix;



public void readData(){

};






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

	int sizeFirst = firstSeq.length(); //m
	int sizeSecond = secondSeq.length(); //n

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

			int min = min

		}

	}

}