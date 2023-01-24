package spell;

import java.io.IOException;

/**
 * A simple main class for running the spelling corrector. This class is not
 * used by the passoff program.
 */
public class Main {
	
	/**
	 * Give the dictionary file name as the first argument and the word to correct
	 * as the second argument.
	 */
	public static void main(String[] args) throws IOException {

		//reading in the arguments
		String dictionaryFileName = args[0];
		String inputWord = args[1];

		//
        //Create an instance of your corrector here
        //

		//creating a spell corrector object
		ISpellCorrector corrector = new SpellCorrector();

		//using the usedictionary method
		corrector.useDictionary(dictionaryFileName);
		System.out.println(corrector.suggestSimilarWord(inputWord));
	}

}
