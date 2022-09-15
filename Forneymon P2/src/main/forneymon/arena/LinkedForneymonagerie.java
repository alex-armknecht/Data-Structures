package main.forneymon.arena;

import java.util.Objects;
import main.forneymon.species.Forneymon;


/**
 * Collections of Forneymon ready to fight in the arena!
 * @author Alex Armknecht.
 */
public class LinkedForneymonagerie implements Forneymonagerie {

    // Fields
    // -----------------------------------------------------------
    private Node sentinel;
    private int size, modCount;
    
    
    // Constructor
    // -----------------------------------------------------------
    public LinkedForneymonagerie () {
        // [!] Leave this constructor as-is, you may not modify!
        this.size = this.modCount = 0;
        this.sentinel = new Node(null);
        this.sentinel.next = this.sentinel;
        this.sentinel.prev = this.sentinel;
    }
    
    
    // Methods
    // -----------------------------------------------------------
    
    /**
     * Returns true if the LinkedForneymonagerie has no Forneymon inside, false otherwise.
     * @return boolean value if LinkedForneymonagerie is empty or not.
     */
    public boolean empty () {
        return this.size == 0;
    }
    
    /**
     * Returns the current size of the LinkedForneymonagerie
     * @return  int  size of the LinkedForneymonagerie.
     */
    public int size () {
        return this.size;
    }
    
    /**
     * Attempts to add a reference to the given Forneymon to the LinkedForneymonagerie's collection
     * as long as the species that doesn't currently exist, unless the reference has a higher level.
     * @param  toAdd  a Forneymon that is trying to be added to the LinkedForneymonagerie.
     * @return  boolean  value true if toAdd was newly added to the LinkedForneymonagerie, false otherwise. 
     */
    public boolean collect (Forneymon toAdd) {
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (toAdd == n.fm) {
                return false;
            }
            if (n.fm.getSpecies().equals(toAdd.getSpecies())) { //just use containSpecies
                if (n.fm.getLevel() == toAdd.getLevel() ) {
                    return false; //keep the original if exactly the same.
                }
                if (n.fm.getLevel() < toAdd.getLevel() ) {
                    n.fm = toAdd;
                    this.modCount++;
                    return false;
                }
                return false;
            }
        }
        this.append(toAdd); //does not exist so add it.
        this.modCount++;
        return true;
    }
    
    /**
     * Removes the Forneymon of the given species fmSpecies from the LinkedForneymonagerie, maintaining 
     * the relative order of remaining Forneymon in the collection, and returning true.
     * @param  fmSpecies  string of species that is to be removed from LinkedForneymonagerie.
     * @return  boolean  value true if species released, false otherwise.
     */
    public boolean releaseSpecies (String fmSpecies) {
        int indexremoved = this.getSpeciesIndex(fmSpecies);
        if (indexremoved == -1) { return false; }
        this.remove(this.getSpeciesIndex(fmSpecies));
        this.modCount++;
        return true;
    }
    
    /**
     * Returns the Forneymon at the requested index in the collection, if valid.
     * @param  index  value of index in LinkedForneymonagerie.
     * @return  Forneymon  at requested index.
     */
    public Forneymon get (int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IllegalArgumentException("index is invalid");
        }
        Node current;
        for (current = this.sentinel.next; index > 0; current = current.next) {
            index--;
        }
        return current.fm;
    }
    
    /**
     * Returns the "best" Forneymon (rated on level, health, index) currently in 
     * the collection, or null if the collection is empty.
     * @return  Forneymon  that is the best in LinkedForneymonagerie.
     */
    public Forneymon getMVP () {
        Node winningNode = new Node(this.sentinel.next.fm);
        if (this.empty()) {
            return null;
        }
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (n.fm.getLevel() > winningNode.fm.getLevel()) { //higher level so replace.
                winningNode = n;
            }
            if (n.fm.getLevel() == winningNode.fm.getLevel()) {
                if (n.fm.getHealth() > winningNode.fm.getHealth()) { //higher health so replace.
                    winningNode = n;
                }
                
            }
        }
        return winningNode.fm;
    }
    
    /**
     * Removes and returns the Forneymon at the given index, if valid, and 
     * maintains the relative order of remaining Forneymon in the collection.
     * @param  index  location of Forneymon in LinkedForneymonagerie to be removed.
     * @return  Forneymon  at requested index.
     */
    public Forneymon remove (int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IllegalArgumentException("index is invalid");
        }
        Forneymon saveFM = this.get(index);
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (n.fm.getSpecies().equals(saveFM.getSpecies())) { //find node to remove.
                n.next.prev = n.prev; //re-point arrows. 
                n.prev.next = n.next;
                this.modCount++;
            }
        }
        this.size--;
        return saveFM;
    }
    
    /**
     * Returns the index of a Forneymon with the given fmSpecies in the 
     * collection, or -1 if that type is not found within.
     * @param  fmSpecies  string of species that is to be found from LinkedForneymonagerie.
     * @return  int  index of requested species.
     */
    public int getSpeciesIndex (String fmSpecies) {
        int index = 0;
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (n.fm.getSpecies().equals(fmSpecies)) {
                return index;
            }
            index++;
        }
        return -1;
    }
    
    /**
     * Returns true if the given fmSpecies is found within the LinkedForneymonagerie's collection.
     * @param  fmSpecies  string of species that is to be found from LinkedForneymonagerie.
     * @return  boolean  value true if species in LinkedForneymonagerie, false otherwise. 
     */
    public boolean containsSpecies (String toCheck) { 
        return this.getSpeciesIndex(toCheck) != -1;
    }
    
    /**
     * Swaps the contents of the calling LinkedForneymonagerie and the other specified.
     * @param  other  Forneymonagerie that will be traded with this Forneymonagerie. 
     */
    public void trade (Forneymonagerie other) {
        //save other variables
        int saveSize = ((LinkedForneymonagerie)other).size;
        int saveModCount = ((LinkedForneymonagerie)other).modCount;
        Node saveSentinel = ((LinkedForneymonagerie)other).sentinel;
        //set other to this
        ((LinkedForneymonagerie)other).size = this.size;
        ((LinkedForneymonagerie)other).modCount = this.modCount;
        ((LinkedForneymonagerie)other).sentinel = this.sentinel;
        //set this to other
        this.size = saveSize;
        this.modCount = saveModCount;
        this.sentinel = saveSentinel;
        //Increment both modCounts.
        this.modCount++;
        ((LinkedForneymonagerie)other).modCount++;   
    }
    
    /**
     * Moves the Forneymon of the given fmSpecies from its current position in the 
     * LinkedForneymonagerie to the one specified by the index, shifting any existing 
     * Forneymon around the requested index so that the relative indexing is preserved.
     * @param  fmSpecies  string of species that is to be relocated in LinkedForneymonagerie.
     * @param  index  location in LinkedForneymonagerie where fmSpecies is to be moved.
     */
    public void rearrange (String fmSpecies, int index) {
        if (index < 0 || index > this.size - 1) {
            throw new IllegalArgumentException("index is invalid");
        }
        int foundIndex = this.getSpeciesIndex(fmSpecies);
        if (foundIndex == -1) { //species isn't in the array don't rearrange.
            return;
        }
        Forneymon saveFM = this.get(foundIndex); 
        if (index == foundIndex ) {//rearrange to index where it currently is don't rearrange. 
            return;
        }
        else {
            this.remove(foundIndex);
            this.insertAt(saveFM, index);
        } 
    }
    
    /**
     * Returns a new Iterator on the calling LinkedForneymonagerie that begins on the first Node in the sequence.
     * @return  LinkedForneymonagerie.Iterator  an iterator for user access of LinkedForneymonagerie
     */
    public LinkedForneymonagerie.Iterator getIterator () {
        if (this.size <= 0) {
            throw new IllegalStateException();
        }
        return new Iterator(this);  
    }
    
    /**
     * Produce a copy of the calling LinkedForneymonagerie that operates independently from the original.
     * @return  LinkedForneymonagerie  clone of LinkedForneymonagerie.
     */
    @Override
    public LinkedForneymonagerie clone () {
        LinkedForneymonagerie copy = new LinkedForneymonagerie();
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            copy.collect((Forneymon)n.fm.clone());
        }
        return copy;
    }
    
    /**
     * Returns whether or not the given Object other is a property-equivalent LinkedForneymonagerie to this one
     * @param  other  object that will be tested for its equivalence to LinkedForneymonagerie.
     * @return  boolean  value true if other and LinkedForneymonagerie are equivalent, otherwise false.
     */
    @Override
    public boolean equals (Object other) {
        int index = 0;
        for (Node n = this.sentinel.next; n != this.sentinel; n = n.next) {
            if (! n.fm.equals(((LinkedForneymonagerie)other).get(index))) {
                return false;
            }
            index++;
        }
        return true;
    }
    
    @Override
    public int hashCode () {
        return Objects.hash(this.sentinel, this.size, this.modCount);
    }
    
    /**
     * Returns a convenient String representation of the LinkedForneymonagerie.
     * @return A String representing this each Forneymon's type, level, and health in LinkedForneymonagerie.
     */
    @Override
    public String toString () {
        String[] result = new String[size];
        int i = 0;
        for (Node curr = this.sentinel.next; curr != this.sentinel; curr = curr.next, i++) {
            result[i] = curr.fm.toString();
        }
        return "[ " + String.join(", ", result) + " ]";
    }
    
    
    // Private helper methods
    // -----------------------------------------------------------

    /**
     * Adds a new node containing a specified Forneymon to the end of LinkedForneymonagerie.
     * @param  toAdd  Forneymon that is to be added to LinkedForneymonagerie.
     */
    private void append(Forneymon toAdd) {
        Node toAppend = new Node(toAdd);
        Node lastNode = this.sentinel.prev;
        this.size++;
        lastNode.next = toAppend;
        this.sentinel.prev = toAppend;
        toAppend.next = this.sentinel;
        toAppend.prev = lastNode;
        
    }
    
    /**
     * Inserts a new node containing a specified Forneymon at a specific index in LinkedForneymonagerie.
     * @param  toAdd  Forneymon that is to be added to LinkedForneymonagerie.
     * @param  index  int location where the new node is to be inserted.
     */
    private void insertAt (Forneymon toAdd, int index) {
        if (index > this.size) {
            throw new IllegalArgumentException("invalid index");
        }
        if (index == this.size) {
            this.append(toAdd);
        }
        Node insertedNode = new Node(toAdd);
        Node nodeAtIndex = this.sentinel;
        this.size++;
        while (index >= 0) { //find the node at the index that new node will be placed.
            nodeAtIndex = nodeAtIndex.next;
            index--;
        } 
        Node beforeNode =  nodeAtIndex.prev;
        Node afterNode =  nodeAtIndex;
        insertedNode.next = afterNode;
        insertedNode.prev = beforeNode;
        afterNode.prev = insertedNode;
        beforeNode.next = insertedNode;
        
        
    }
    
    
    // Iterator Inner Class
    // -----------------------------------------------------------
    
    public class Iterator {
        private LinkedForneymonagerie host;
        private Node current;
        private int itModCount;
        
        Iterator (LinkedForneymonagerie host) {
            this.host = host;
            this.current = this.host.sentinel.next;
            this.itModCount = this.host.modCount;
        }
        
        /**
         * Returns true if the Iterator is valid and its current.next is the host's Sentinel node, false otherwise.
         * @return  boolean  value if iterator is at the end of the LinkedForneymonagerie
         */
        public boolean atEnd () {
            return (this.isValid() && current.next == host.sentinel);
        }
        
        /**
         * Returns true if the Iterator is valid and its current.prev is the host's Sentinel node, false otherwise.
         * @return  boolean  value if iterator is at the start of the LinkedForneymonagerie
         */
        public boolean atStart () {
            return (this.isValid() && current.prev == host.sentinel);
        }
        
        /**
         * Returns true if this Iterator's itModCount agrees with that of its owner's modCount 
         * and if the host LinkedForneymonagerie has at least one element, false otherwise.
         * @return  boolean  value if the iterator is valid or not.
         */
        public boolean isValid () {
            return (host.modCount == itModCount && this.host.size >=1 );
        }
        
        /**
         * Returns the Forneymon stored in the Node that the Iterator is currently pointing at.
         * @return  Forneymon  that the index is currently referring to.
         */
        public Forneymon getCurrent () {
            if (! this.isValid()) {
                throw new IllegalStateException("Iterator invalid");
            }
            return current.fm;
        }

        /**
         * Advances the Iterator's current reference to point to the next Node in the sequence
         */
        public void next () {
            if (!this.isValid()) {
                throw new IllegalStateException("Iterator invalid");
            }
            current = current.next;
            if (current == host.sentinel) {
                current = current.next;
            }
        }
        
        /**
         * Advances the Iterator's current reference to point to the previous Node in the sequence.
         */
        public void prev () {
            if (!this.isValid()) {
                throw new IllegalStateException("Iterator invalid");
            }
            current = current.prev;
            if (current == host.sentinel) {
                current = current.prev;
            }
        }
        
        /**
         * Removes the Node that this Iterator references from the LinkedForneymonagerie and maintains the relative order of Nodes.
         * @return  Forneymon  that has been removed.
         */
        public Forneymon removeCurrent () {
            if (!this.isValid()) {
                throw new IllegalStateException("Iterator invalid");
            }
            Forneymon saveFM = current.fm;
            current.prev.next = current.next; //re point arrows.
            current.next.prev = current.prev;
            this.prev();
            host.size--;
            itModCount++;
            modCount++;
            return saveFM;
        }  
    }
    
    
    // Node Inner Class
    // -----------------------------------------------------------
    
    private class Node {
        Node next, prev;
        Forneymon fm;
        
        Node (Forneymon fm) {
            this.fm = fm;
        }
        
        // [!] You may, but need not, add any methods to Node
    }
    
}
