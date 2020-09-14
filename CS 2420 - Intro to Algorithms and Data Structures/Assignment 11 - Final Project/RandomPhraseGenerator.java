package comprehensive;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;

/**
 * This method uses a HashMap to create and store a CFG (context-free grammar),
 * which is then used to randomly generate sentences according to the structure
 * and rules of the generated grammar.
 * 
 * @author Brandon Walters and Brandon Ernst
 */
public class RandomPhraseGenerator {

	// Used to represent the grammar
	HashMap<String, ArrayList<String>> grammar = new HashMap<String, ArrayList<String>>();

	boolean isInsideBrackets = false;

	boolean isNextFromBracket = true;

	String finalPunctuation = "";

	Random rnd = new Random();

	public RandomPhraseGenerator() {

	}

	public static void main(String[] args) throws IOException {
		RandomPhraseGenerator rpg = new RandomPhraseGenerator();
		rpg.getGrammarFromFile(args[0]);
		for (int i = 0; i < Integer.parseInt(args[1]); i++) {
			System.out.println(rpg.generateSentences(rpg.grammar));
		}
	}

	/**
	 * This method reads the given file line-by-line using a BufferedFileReader. It
	 * looks for non-terminal definitions and production rules and adds them to the
	 * backing HashMap grammar as appropriate, ignoring everything outside of
	 * brackets.
	 */
	public HashMap<String, ArrayList<String>> getGrammarFromFile(String filename) throws IOException {
		BufferedReader fileBuffReader;
		FileReader fileReader;

		try {
			fileReader = new FileReader(filename);
			fileBuffReader = new BufferedReader(fileReader);
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException("Grammar file not found.");
		}

		String key = "";

		String line = fileBuffReader.readLine();
		while (line != null) {
			// If the line given is an opening bracket, denote the next line as a
			// non-terminal definition.
			if (line.equals("{") || line.equals("}")) {
				isInsideBrackets = !isInsideBrackets;
				if (line.equals("{")) {
					isNextFromBracket = true;
				}
				// If the line is a non-terminal definition, add the non-terminal to the backing
				// HashMap.
			} else if (line.startsWith("<") && isNextFromBracket) {
				key = line.substring(line.indexOf("<"), line.indexOf(">") + 1);
				grammar.put(key, new ArrayList<String>());
				isNextFromBracket = false;
				// Else if the line is inside brackets, add it to the backing HashMap as a
				// production rule of its given non-terminal.
			} else if (isInsideBrackets) {
				ArrayList<String> productions = grammar.get(key);
				productions.add(line);
				grammar.put(key, productions);
			}
			line = fileBuffReader.readLine();
		}
		fileBuffReader.close();
		return grammar;
	}

	/**
	 * This method takes a grammar and generates random sentences according to the
	 * rules of that grammar.
	 */
	public String generateSentences(HashMap<String, ArrayList<String>> grammarToGenerate) {
		StringBuilder stringBuilder = new StringBuilder();
		String productionToUse = grammar.get("<start>").get(0);
		String[] arrString;
		String[] arrStringKeys;

		// Splits the line into separate strings to be iterated through.
		arrString = productionToUse.split(" ");

		for (String string : arrString) {
			if (string.startsWith("<")) {
				// If the given string is a non-terminal that has punctuation attached to the
				// end, save the punctuation as a separate string and format the non-terminal
				// properly.
				if (!string.endsWith(">")) {
					finalPunctuation = string.substring(string.length() - 1);
					string = string.substring(0, string.length() - 1);
				}
				arrStringKeys = string.split("(?<=>)");
				// For each non-terminal, use our recursive method to randomly pick a production
				// rule and fill the sentence out accordingly.
				for (String stringKeys : arrStringKeys) {
					stringBuilder.append(pickProduction(stringKeys));
				}
				// If the string is a terminal, simply append it to the end of the full sentence being built.
			} else {
				stringBuilder.append(string);
				stringBuilder.append(" ");
			}
		}

		// Gets rid of the extra space between the last word in the sentence and the associated punctuation, if needed.
		char hold = stringBuilder.charAt(stringBuilder.length() - 1);
		if (Character.isWhitespace(hold)) {
			stringBuilder.deleteCharAt(stringBuilder.length() - 1);
		}
		// Add the ending punctuation, if needed, and return the randomized string to be printed.
		return stringBuilder.append(finalPunctuation).toString();
	}

	/**
	 * This method chooses a production rule from the given non-terminal's ruleset and then fills it out, acting recursively as needed.
	 */
	public String pickProduction(String keyToUse) {

		String punctuationToAdd = "";

		// If the non-terminal has attached punctuation, formats the non-terminal properly and saves the punctuation.
		if (!keyToUse.endsWith(">")) {
			punctuationToAdd = keyToUse.substring(keyToUse.length() - 1);
			keyToUse = keyToUse.substring(0, keyToUse.length() - 1);
		}
		String productionPicked = "";
		// Chooses a random production rule from the given non-terminal.
		try {
			productionPicked = grammar.get(keyToUse).get(rnd.nextInt(grammar.get(keyToUse).size()));
		} catch (NullPointerException e) {
			throw new NullPointerException("Improper file format.");
		}

		StringBuilder stringBuilder = new StringBuilder();
		String[] arrString;
		int count = 0;

		// Splits the line into separate strings to be iterated through.
		arrString = productionPicked.split(" ");

		for (String string : arrString) {
			// If the string is another non-terminal, recursively call the method to fill it out appropriately.
			if (string.startsWith("<")) {
				stringBuilder.append(pickProduction(string));
				count++;
			}

			// If the string is the last terminal in the sentence, appends it and adds the ending punctuation.
			else if (count == arrString.length - 1) {
				stringBuilder.append(string);
				stringBuilder.append(punctuationToAdd);
				stringBuilder.append(" ");
				count++;
				// Else, simply appends the string to the full sentence.
			} else {
				stringBuilder.append(string);
				stringBuilder.append(" ");
				count++;
			}
		}
		// Returns the full chunk of the randomized sentence.
		return stringBuilder.toString();
	}
}
