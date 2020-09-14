package assign06;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

/**
 * Class containing the checkFile method for checking if the (, [, and { symbols
 * in an input file are correctly matched.
 * 
 * @author Erin Parker, Brandon Ernst, && Brandon Walters
 */
public class BalancedSymbolChecker {

	/**
	 * Generates a message indicating whether the input file has unmatched
	 * symbols. (Use the helper methods below for constructing messages.)
	 * 
	 * @param filename - name of the input file to check
	 * @return a message indicating whether the input file has unmatched symbols
	 * @throws FileNotFoundException if the file does not exist
	 */
	public static String checkFile(String filename) throws FileNotFoundException {
		//Makes sure a valid file was imported, throws error if one was not
		try{
			File file = new File(filename);
		}
		catch(Exception e) {
			throw new FileNotFoundException("Invalid file. Cannot open.");
		}
		
		//puts file into a scanner and creates an array stack
		Scanner fileScan = new Scanner(new File(filename));
		ArrayStack<Character> stack = new ArrayStack<Character>();
		
		//sets booleans for comments and a line number int
		Boolean stillReading1 = true;
		Boolean stillReading2 = true;
		Boolean stillReading3 = true;
		Boolean quoteWithinQuotes = false;
		int lineNumber = 0;
		
		//starts a loop to go through entire scanner adding 1 to the line number
		//every time we move to the necxt line
		while(fileScan.hasNextLine()) {
			lineNumber++;
			String fileLine = fileScan.nextLine();
			stillReading1 = true;
			stillReading3 = true;
			
			for(int i = 0; i < fileLine.length(); i++) {
				if(i - 1 > 0) {
					if(fileLine.charAt(i) == '/' && fileLine.charAt(i - 1) != '*') {
						if(fileLine.charAt(i + 1) == '*')
							stillReading2 = false;
						if(fileLine.charAt(i + 1) == '/')
							stillReading1 = false;
					}
				}
				if(fileLine.charAt(i) == '*') {
					if(fileLine.charAt(i + 1) == '/' )
						stillReading2 = true;
				}
				if (fileLine.charAt(i) == '\"' && stillReading3) {
					quoteWithinQuotes = true;
				}
				
				if(fileLine.charAt(i) == '"' && stillReading1 && stillReading2 || fileLine.charAt(i) == '\'' && fileLine.charAt(i - 1) != '\\') {	
					stillReading3 = !stillReading3;
				}
				
				//If not in a comment, quote, or char symbolizers reads the symbol
				if(stillReading1 && stillReading2 && stillReading3) {
					char symbol = fileLine.charAt(i);
					
					//pushes it to the stack if it is a symbol that opens
					if((symbol == '{' || symbol == '[' || symbol == '(')) {
						stack.push(symbol);
					}
					
					//pops from the stack if closing symbol and compares the two		
					if((symbol == '}' || symbol == ']' || symbol == ')')) {
						char popped = ' ';
						if(stack.size() != 0) {
							popped = stack.pop();
						}
						
						//if closing symbol matches opening symbol continues
						if(popped == '[' && symbol == ']') {	
						}
						else if(popped == '{' && symbol == '}') {
						}
						else if(popped == '(' && symbol == ')') {
						}
						
						//if closing symbol does not match opening symbol
						//returns error statement
						else {
							if(popped == '(')
								popped = ')';
							if(popped == '{')
								popped = '}';
							if(popped == '[')
								popped = ']';
								
							return unmatchedSymbol(lineNumber, i + 1, symbol, popped);
						}
					}
				}
			}
		}
		
		//checks for whether the program ever ended a comment. returns error statement if not
		if(!stillReading1 || !stillReading2)
			return unfinishedComment();
		
		//checks if stack is empty. returns error statement if not
		if(stack.size() > 0) {
			char popped = stack.pop();
			if(popped == '(')
				popped = ')';
			if(popped == '{')
				popped = '}';
			if(popped == '[')
				popped = ']';
			return unmatchedSymbolAtEOF(popped);
		}

		//return that all symbols have been matched
		return allSymbolsMatch();
	}

	/**
	 * Use this error message in the case of an unmatched symbol.
	 * 
	 * @param lineNumber - the line number of the input file where the matching symbol was expected
	 * @param colNumber - the column number of the input file where the matching symbol was expected
	 * @param symbolRead - the symbol read that did not match
	 * @param symbolExpected - the matching symbol expected
	 * @return the error message
	 */
	private static String unmatchedSymbol(int lineNumber, int colNumber, char symbolRead, char symbolExpected) {
		return "ERROR: Unmatched symbol at line " + lineNumber + " and column " + colNumber + ". Expected " + symbolExpected
				+ ", but read " + symbolRead + " instead.";
	}

	/**
	 * Use this error message in the case of an unmatched symbol at the end of the file.
	 * 
	 * @param symbolExpected - the matching symbol expected
	 * @return the error message
	 */
	private static String unmatchedSymbolAtEOF(char symbolExpected) {
		return "ERROR: Unmatched symbol at the end of file. Expected " + symbolExpected + ".";
	}

	/**
	 * Use this error message in the case of an unfinished comment
	 * (i.e., a file that ends with an open /* comment).
	 * 
	 * @return the error message
	 */
	private static String unfinishedComment() {
		return "ERROR: File ended before closing comment.";
	}

	/**
	 * Use this message when no unmatched symbol errors are found in the entire file.
	 * 
	 * @return the success message
	 */
	private static String allSymbolsMatch() {
		return "No errors found. All symbols match.";
	}
}