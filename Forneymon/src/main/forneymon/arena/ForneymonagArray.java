package main.forneymon.arena;

import java.util.Objects;
import main.forneymon.species.*;


/**
 * Collections of Forneymon ready to fight in the arena!
 * @author Alex Armknecht.
 */
public class ForneymonagArray implements Forneymonagerie {

    // Constants
    // ----------------------------------------------------------
    // [!] DO NOT change this START_SIZE for your collection, and
    // your collection *must* be initialized to this size
    private static final int START_SIZE = 4;

    // Fields
    // ----------------------------------------------------------
    private Forneymon[] collection;
    private int size;
    
    
    // Constructor
    // ----------------------------------------------------------
    public ForneymonagArray () {
    	this.collection = new Forneymon[START_SIZE];
        this.size = 0;
    }
    
    
    // Methods
    // ----------------------------------------------------------
    
    /**
     * Returns true if the ForneymonagArray has no Forneymon inside, false otherwise.
     * @return boolean value if ForneymonagArray is empty or not.
     */
    public boolean empty () {
    	if (this.size == 0 ) {
    		return true;
        }
    	return false;
    }
    
    /**
     * Returns the current size of the ForneymonagArray
     * @return  int  size of the ForneymonagArray.
     */
    public int size () {
        return this.size;
    }
    
    /**
     * Attempts to add a reference to the given Forneymon to the ForneymonagArray's collection
     * as long as the species that doesn't currently exist, unless the reference has a higher level.
     * @param  toAdd  a Forneymon that is trying to be added to the ForneymonagArray.
     * @return  boolean  value true if toAdd was newly added to the ForneymonagArray, false otherwise. 
     */
    public boolean collect (Forneymon toAdd) {
    	for (int i = 0; i < this.size; i++) {
    		if (toAdd == this.collection[i]) {
    			return false;
    		}
    		if (this.collection[i].getSpecies().equals(toAdd.getSpecies())) {
    			if (toAdd.getLevel() > this.collection[i].getLevel()) {
    				this.collection[i] = toAdd; //replace with higher level species.
    				return false;
    			}
    			return false;
    		}
    	}
    	//add ForneymonagArray of a species that doesn't currently exist 
    	checkAndGrow();
    	this.collection[this.size] = toAdd;
    	this.size++;
    	return true;
    }
    
    /**
     * Removes the Forneymon of the given species fmSpecies from the ForneymonagArray, maintaining 
     * the relative order of remaining Forneymon in the collection, and returning true.
     * @param  fmSpecies  string of species that is to be removed from ForneymonagArray.
     * @return  boolean  value true if species released, false otherwise.
     */
    public boolean releaseSpecies (String fmSpecies) {
    	for (int i = 0; i < this.size; i++) {
    		if (this.collection[i].getSpecies().equals(fmSpecies)) {
    			removeAt(i);
    			i--;
    			return true;
    		}
    	}
    	return false;
    }
    
    /**
     * Returns the Forneymon at the requested index in the collection, if valid.
     * @param  index  value of index in ForneymonagArray.
     * @return  Forneymon  at requested index.
     */
    public Forneymon get (int index) {
        if (index < 0 || index > this.size - 1) {
        	throw new IllegalArgumentException("index is invalid");
        }
        return this.collection[index];
    }
    
    /**
     * Returns the "best" Forneymon (rated on level, health, index) currently in 
     * the collection, or null if the collection is empty.
     * @return  Forneymon  that is the best in ForneymonagArray.
     */
    public Forneymon getMVP () {
        if (this.empty()) {
        	return null;
        }
        int highestLevel = this.collection[0].getLevel();
        int winningIndex = 0;
        for (int i = 0; i < this.size; i++) {
        	if (this.collection[i].getLevel() > highestLevel) { // highest level
        		highestLevel = this.collection[i].getLevel();
        		winningIndex = i;
        	}
        	if (this.collection[i].getLevel() == highestLevel) { // if same level then highest health
        		if (this.collection[i].getHealth() > this.collection[winningIndex].getHealth() ) {
        			winningIndex = i;
        		}
        		//if same health then prior index
        		if (this.collection[i].getHealth() == this.collection[winningIndex].getHealth() ) {
        			if (i < winningIndex) {
        				winningIndex = i;
        			}
        		}
        	}
        }
        return this.collection[winningIndex];
    }
    
    /**
     * Removes and returns the Forneymon at the given index, if valid, and 
     * maintains the relative order of remaining Forneymon in the collection.
     * @param  index  location of Forneymon in ForneymonagArray to be removed.
     * @return  Forneymon  at requested index.
     */
    public Forneymon remove (int index) {
    	if (index < 0 || index > this.size - 1) {
        	throw new IllegalArgumentException("index is invalid");
        }
    	Forneymon saveValue = this.collection[index];
    	shiftLeft(index);
    	this.size--;
    	return saveValue;
    }
    
    /**
     * Returns the index of a Forneymon with the given fmSpecies in the 
     * collection, or -1 if that type is not found within.
     * @param  fmSpecies  string of species that is to be found from ForneymonagArray.
     * @return  int  index of requested species.
     */
    public int getSpeciesIndex (String fmSpecies) {
    	for (int i = 0; i < this.size; i++) {
    		if (this.collection[i].getSpecies().equals(fmSpecies)) {
    			return i;
    		}
    	}
    	return -1;
    }
    
    /**
     * Returns true if the given fmSpecies is found within the ForneymonagArray's collection.
     * @param  fmSpecies  string of species that is to be found from ForneymonagArray.
     * @return  boolean  value true if species in ForneymonagArray, false otherwise. 
     */
    public boolean containsSpecies (String toCheck) {
    	int test = this.getSpeciesIndex(toCheck);
    	if (test != -1) {
    		return true;
    	}
    	return false;
    }
    
    /**
     * Swaps the contents of the calling ForneymonagArray and the other specified.
     * @param  other  Forneymonagerie that will be traded with this Forneymonagerie. 
     */
    public void trade (Forneymonagerie other) {
        //save other to tempVar. 
        ForneymonagArray tempVar =  new ForneymonagArray();
        tempVar.size = ((ForneymonagArray)other).size;
        tempVar.collection = ((ForneymonagArray)other).collection;
        //set other to this.
        ((ForneymonagArray)other).size = this.size;
        ((ForneymonagArray)other).collection = this.collection;
        //set this to other (tempVar).
        this.size = tempVar.size();
        this.collection = tempVar.collection;
        
        
    }
    
    /**
     * Moves the Forneymon of the given fmSpecies from its current position in the 
     * ForneymonagArray to the one specified by the index, shifting any existing 
     * Forneymon around the requested index so that the relative indexing is preserved.
     * @param  fmSpecies  string of species that is to be relocated in ForneymonagArray.
     * @param  index  location in ForneymonagArray where fmSpecies is to be moved.
     */
    public void rearrange (String fmSpecies, int index) {
    	if (index < 0 || index > this.size - 1) {
    	    throw new IllegalArgumentException("index invlaid");
    	}
    	int foundIndex = this.getSpeciesIndex(fmSpecies);
    	if (foundIndex == -1) { //cant rearange because species isnt in the array
    	    return;
    	}
    	Forneymon saveValue = this.collection[foundIndex];
    	if (index == foundIndex ) {//don't change if trying to rearrange to index its already there.
            return;
        }
    	else {
    	    this.shiftLeft(foundIndex);
    	    this.insertAt(saveValue, index);
    	}
    	
    }
    
    // Overridden Methods
    // ----------------------------------------------------------
    
    /**
     * Produce a copy of the calling ForneymonagArray that operates independently from the original.
     * @return  ForneymonagArray  clone of ForneymonagArray.
     */
    @Override
    public ForneymonagArray clone () {
    	ForneymonagArray copy =  new ForneymonagArray();
    	for (int i = 0; i < this.size; i++) { 
    		copy.collect((Forneymon)this.collection[i].clone());
    	}
        return copy;
    }
    
    /**
     * Returns whether or not the given Object other is a property-equivalent ForneymonagArray to this one
     * @param  other  object that will be tested for its equivalence to ForneymonagArray.
     * @return  boolean  value true if other and ForneymonagArray are equivalent, otherwise false.
     */
    @Override
    public boolean equals (Object other) {
    	for (int i = 0; i < this.size; i++ ) {
    		if (! this.collection[i].equals(((ForneymonagArray)other).get(i)) ) {
    			return false;
    		}
    	}
    	return true; 
    }
    
    /**
     * Returns a convenient String representation of the ForneymonagArray.
     * @return A String representing this each Forneymon's type, level, and health in ForneymonagArray.
     */
    @Override
    public String toString () {
        String[] result = new String[size];
        for (int i = 0; i < size; i++) {
            result[i] = collection[i].toString();
        }
        return "[ " + String.join(", ", result) + " ]";
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(this.collection, this.size);
    }
    
    
    // Private helper methods
    // ----------------------------------------------------------
    
    /**
     * Expands the size of the list whenever it is at
     * capacity
     */
    private void checkAndGrow () {
        if (this.size < this.collection.length) {
            return;
        }
        Forneymon[] newItems = new Forneymon[this.collection.length * 2];
        for (int i = 0; i < this.collection.length; i++) {
            newItems[i] = this.collection[i];
        }
        this.collection = newItems;
    }
    
    /**
     * Shifts all elements to the right of the given
     * index one left
     * 
     * @param The index at which to shift all elements to the right
     * left by 1
     */
    private void shiftLeft (int index) {
        for (int i = index; i < this.size-1; i++) {
            this.collection[i] = this.collection[i+1];
        }
    }
    
    private void insertAt (Forneymon toAdd, int index) {
        if (index >= this.size) {
            throw new IllegalArgumentException();
        }
        for (int i = this.size; i > index; i--) {
            checkAndGrow();
            this.collection[i] = this.collection[i-1];
        }
        this.collection[index] = toAdd;
    }
    
    
    /**
     * Ensures that the requested index is within a legal range,
     * as also specified
     * 
     * @param index The index to check
     * @param lower The legal lower bound (exclusive)
     * @param upper The legal upper bound (inclusive)
     */
    private void indexValidityCheck (int index, int lower, int upper) {
        if (index < lower || index >= upper) {
            throw new IllegalArgumentException("index outside of legal range");
        }
    }
    
    /**
     * Removes the int at the given index
     * 
     * @param index The index at which to remove an element
     */
    private void removeAt (int index) {
        indexValidityCheck(index, 0, this.size);
        shiftLeft(index);
        this.size--;
    }
    
    
    
}
