package main.textfiller;

import java.util.*;


/**
 * A ternary-search-tree implementation of a text-autocompletion
 * trie, a simplified version of some autocomplete software.
 * @author Alex Armknecht
 */
public class TernaryTreeTextFiller implements TextFiller {

    // Fields
    // -----------------------------------------------------------
    private TTNode root;
    private int size;
    
    // Constructor
    // -----------------------------------------------------------
    public TernaryTreeTextFiller () {
        this.root = null;
        this.size = 0;
    }
    
    // Methods
    // -----------------------------------------------------------
    
    /**
     * Returns the number of stored *terms* inside of the TextFiller.
     * @return  int  size of the TernaryTreeTextFiller.
     */
    public int size () {
        return this.size;
    }
    
    /**
     * Returns true if the TextFiller has no search terms stored, false otherwise.
     * @return  boolean  value if TernaryTreeTextFiller is empty or not.
     */
    public boolean empty () {
        return this.size == 0;
    }
    
    /**
     * Adds the given search term toAdd to the TextFiller but will not add a repeated term.
     * @param  toAdd  a String word that is to be added to TextFiller.
     */
    public void add (String toAdd) {
        toAdd = normalizeTerm(toAdd);
        this.root = addRec(this.root, toAdd);
        if (!contains(toAdd)) { 
            this.root = addRec(this.root, toAdd);
        }
    }
    
    /**
     * Returns true if the given query String exists within the TextFiller, false otherwise.
     * @param  query  a String word that is to be checked if it exists in TextFiller.
     * @return  boolean  value whether or not the query exists in the TextFiller.
     */
    public boolean contains (String query) {
        query = normalizeTerm(query);
        return findQuery(this.root, query);
    }
    
    /**
     * Returns any search term contained in the TextFiller that possesses the query as a prefix.
     * @param  query  a String word that is potentially a prefix for a word in TextFiller.
     * @return  String  a word that the query is a prefix of.
     */
    public String textFill (String query) {
        query = normalizeTerm(query);
        return findPrefix(this.root, query, "");
    }
    
    /**
     * Returns an ArrayList of Strings consisting of the alphabetically sorted search terms within this TextFiller.
     * @return  ArrayList  of TextFiller's alphabetically sorted terms. 
     */
    public List<String> getSortedList () {
        List<String> sortedList = new ArrayList<String>();
        makeList(this.root, "", sortedList);
        return sortedList;
    }

    // Private Helper Methods
    // -----------------------------------------------------------
    
    /**
     * Normalizes a term to either add or search for in the tree,
     * since we do not want to allow the addition of either null or
     * empty strings within, including empty spaces at the beginning
     * or end of the string (spaces in the middle are fine, as they
     * allow our tree to also store multi-word phrases).
     * @param s The string to sanitize
     * @return The sanitized version of s 
     */
    private String normalizeTerm (String s) {
        // Edge case handling: empty Strings illegal
        if (s == null || s.equals("")) {
            throw new IllegalArgumentException();
        }
        return s.trim().toLowerCase();
    }
    
    /**
     * Given two characters, c1 and c2, determines whether c1 is
     * alphabetically less than, greater than, or equal to c2
     * @param c1 The first character
     * @param c2 The second character
     * @return
     *   - some int less than 0 if c1 is alphabetically less than c2
     *   - 0 if c1 is equal to c2
     *   - some int greater than 0 if c1 is alphabetically greater than c2
     */
    private int compareChars (char c1, char c2) {
        return Character.toLowerCase(c1) - Character.toLowerCase(c2);
    }
    
    /**
     * A private helper recursive method for the Add method. Finds the correct location and adds 
     * the first respective letter of the added word. Then calls a second helper method 
     * to add the rest of the letters of the word.
     * @param  n  a TTNode that the method is currently on.
     * @param  toAdd  a String word that is to be added to TextFiller.
     * @return  TTNode  that was added to TextFiller.
     */
    private TTNode addRec (TTNode n, String toAdd) {
        if (n == null) {
            TTNode beginNode = new TTNode(toAdd.charAt(0), false); //letter where the rest of the word builds off of.
            if (toAdd.length() != 1) {
                beginNode.mid = addTheRest(beginNode.mid, toAdd); //add the rest of the word.
                size++;
                return beginNode;
            }
            return beginNode; 
        }
        else if (toAdd.length() == 1 && n.letter == toAdd.charAt(0)) { 
            if (! n.wordEnd) { //if word already exists then add the wordEnd.
                n.wordEnd = true;
                size++;               
                return n;
            } 
        }
        else if (compareChars(toAdd.charAt(0), n.letter) < 0) {
            n.left = addRec(n.left, toAdd);
        } 
        else if (compareChars(toAdd.charAt(0), n.letter) > 0) {
            n.right = addRec(n.right, toAdd);
        } else {
            toAdd = toAdd.substring(1); 
            n.mid = addRec(n.mid, toAdd); 
        }
        return n; 
    }
    
    /**
     * A private helper method for addRec that adds the rest of the letters in the word.
     * @param  n  a TTNode that the method is currently on.
     * @param  toAdd  a String word that is to be added to TextFiller.
     * @return  TTNode  that was added to TextFiller.
     */
    private TTNode addTheRest (TTNode n, String toAdd) { 
        toAdd = toAdd.substring(1); 
        if (toAdd.length() == 1) {
            return new TTNode(toAdd.charAt(0), true); //add the last letter. 
        }
        n = new TTNode(toAdd.charAt(0), false); //add letter.
        n.mid = addTheRest(n.mid, toAdd); //add the rest of the word.
        return n;
    }
    
    /**
     * A private helper method for the contains method that returns true if the given query
     * String exists within the TextFiller, false otherwise.
     * @param  n  a TTNode that the method is currently on.
     * @param  query  a String word that is to be checked if it exists in TextFiller.
     * @return  boolean  value whether or not the query exists in the TextFiller.
     */
    private boolean findQuery(TTNode n, String query) {
        if (n == null || query.length() == 0) {
            return false;
        }
        if (query.charAt(0) == n.letter && query.length() == 1 && n.wordEnd) { //have found all the letters.
              return true;
        }
        if (compareChars(query.charAt(0), n.letter) < 0)  {
          return findQuery(n.left, query);
        } 
        if (compareChars(query.charAt(0), n.letter) > 0)  {            
            return findQuery(n.right, query);
        } else {
          query = query.substring(1); 
          return findQuery(n.mid, query);  
        }
    }
    
    /**
     * A private helper method for the textFill method that finds the given query in TextFiller
     * and finds the rest of the word that the query is a prefix of.
     * @param  n  a TTNode that the method is currently on.
     * @param  query  a String word that is potentially a prefix for a word in TextFiller.
     * @param  word  a String that is the word that the query is a prefix of.
     * @return  String  word that the query is a prefix of.
     */
    private String findPrefix(TTNode n, String query, String word) {
        if (n == null) {
            return null;
        }
        if (query.length() > 0 ) {
            if (compareChars(query.charAt(0), n.letter) < 0) {
                return findPrefix(n.left, query, word);
            }
            else if (compareChars(query.charAt(0), n.letter) > 0) {
                return findPrefix(n.right, query, word);
            } else {
                word += n.letter;
                query = query.substring(1);
            }
        }
        else {
            word += n.letter;
        }
        if (n.wordEnd && query.length() < 1) {
            return word;
        }
        else {
            return findPrefix(n.mid, query, word);
        }
    }
    
    /**
     * A private helper method for the getSortedList method that adds the words from 
     * Textfiller into a list that is alphabetically sorted. 
     * @param  n  a TTNode that the method is currently on.
     * @param  word  a String that is currently being added to the ArrayList.
     * @param  TTList  an ArrayList that contains TextFiller's alphabetically sorted terms. 
     * @return  ArrayList  of TextFiller's alphabetically sorted terms. 
     */
    private void makeList(TTNode n, String word, List<String> TTList)  {
        if (n.left != null) {
            makeList(n.left, word, TTList);
        }
        if (n.wordEnd) {
            String addedWord = word;
            TTList.add(addedWord + n.letter); 
        }
        if (n.mid != null) {
            makeList(n.mid, word + n.letter, TTList);
        }
        if (n.right != null) {
            makeList(n.right, word, TTList);
        }
    }

    
    // Extra Credit Methods
    // -----------------------------------------------------------
    
    public void add (String toAdd, int priority) {
        throw new UnsupportedOperationException();
    }
    
    public String textFillPremium (String query) {
        throw new UnsupportedOperationException();
    }
     
    
    // TTNode Internal Storage
    // -----------------------------------------------------------
    
    /**
     * Internal storage of textfiller search terms
     * as represented using a Ternary Tree (TT) with TTNodes
     * [!] Note: these are currently implemented for the base-assignment;
     *     those endeavoring the extra-credit may need to make changes
     *     below (primarily to the fields and constructor)
     */
    private class TTNode {
        
        boolean wordEnd;
        char letter;
        TTNode left, mid, right;
        
        /**
         * Constructs a new TTNode containing the given character
         * and whether or not it represents a word-end, which can
         * then be added to the existing tree.
         * @param letter Letter to store at this node
         * @param wordEnd Whether or not this is a word-ending letter
         */
        TTNode (char letter, boolean wordEnd) {
            this.letter  = letter;
            this.wordEnd = wordEnd;
        }
        
    }
    
}
