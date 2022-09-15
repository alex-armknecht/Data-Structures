package main.util.stringutils;
import java.util.ArrayList;
/**
 * A selection of more static methods for Homework 01.
 * @author Alex Armknecht.
 */
public class StringUtils { 
	
	/**
	 * Accepts 2 strings and compares them. Returns true if the characters in 
	 * the given query String are found in-sequence in the given corpus String.
	 * @param  corpus  a string that the query will be compared to.
	 * @param  query  a string that gets compared to corpus.
	 * @return  boolean value  if query is in corpus or not.
	 */
    public static boolean hasSequence (String corpus, String query) {
    	int count = 0;
    	String foundLetters = "";
    	for (int i = 0; i < corpus.length(); i++) {
    		if (query.charAt(count) == corpus.charAt(i)) {
    			foundLetters += corpus.charAt(i);
    			count++;
    		}
    		if (foundLetters.equals(query)) {
            	return true;
        	}
    	}
    	return false; 
    }
    
    /**
	 * Accepts a String sentence(s) and returns a new String with the 
	 * first letter of any word following a period capitalized.
	 * @param  sents  a string that is a sentence that has wrong capitalization.
	 * @return  string  that has the correct capitalization.
	 */
    public static String sentCap (String sents) {
    	char[] sentChar = sents.toCharArray();
    	boolean foundLetter = false;
    	for (int i = 0; i < sentChar.length; i++) {
    		if (sentChar[i] == '.' || sentChar[i] == ' ') {
    			if (sentChar[i] == '.') {
    				foundLetter = false; //set to false in case there are more letters to capitalize.
    			}
    			if (foundLetter == false ) {
    				if (i == (sentChar.length - 1) ) { //sentence is over or has no spaces
    					return String.valueOf(sentChar);
    				}
        			if (sentChar[i + 1] != ' ') {
        				String convertToCap = Character.toString(sentChar[i + 1]);
            			convertToCap = convertToCap.toUpperCase();
            			sentChar[i + 1] = convertToCap.charAt(0);
            			foundLetter = true;
        			}
    			}
    		}
    	}
        return String.valueOf(sentChar);
    }
    
    /**
	 * Accepts a sentence and searches it for the nth match of the given 
	 * query word, independent of the capitalization of the matched word.
	 * @param  sent  a string that is a sentence that is to be searched through to find the query.
	 * @param  query  a string that is a word that is to be found in the sent string.
	 * @param  n  an int that represents the nth match of desired word.
	 * @return  string  a match of query at the nth match in sent.
	 */
    public static String getNthMatch (String sent, String query, int n) {
    	if (query.equals("") || n < 0 ) {
    		throw new IllegalArgumentException("query must not be an emptry string and n must be greater than 0");
    	}
    	String[] sentCopy = sent.split(" ");
    	String sentLower = sent.toLowerCase();
    	String[] sentWords = sentLower.split(" ");
    	if ( n >= sentCopy.length ) { //prevent index out of bounds
    		return null;
    	}
    	for (int i = 0; i < sentWords.length; i++) {
    		if (sentWords[i].equals(query) && n != 0) {
    			n--;
        	}
    		else if(sentWords[i].equals(query) && n == 0) {
    			return sentCopy[i];
    		}
    		
    	}
    	return null;
    }
    
}
