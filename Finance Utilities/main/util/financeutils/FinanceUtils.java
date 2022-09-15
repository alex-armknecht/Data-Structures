package main.util.financeutils;

import java.util.Arrays;

/**
 * A selection of static methods for Homework 01.
 * @author Alex Armknecht.
 */

public class FinanceUtils {
	/**
	 * Accepts a given an array of ints, and returns a new array containing 
	 * the sum of items in amounts evenly distributed between indexes.
	 * @param  amounts  an array of ints that will be evenly distributed.
	 * @return  finalArray  an array filled with evenly distributed ints.
	 */
	
    public static int[] getEvenRedistribution (int[] amounts) {
    	boolean legalArray = true;
    	int sum = 0;
    	int[] finalArray = new int[amounts.length];
    	for (int i = 0; i < amounts.length; i++ ) {
    		if (amounts[i] < 0) {
    			legalArray = false;
    		}
    		sum += amounts[i];
    	}
    	int remainder = sum % amounts.length;
    	if (! legalArray ) {
    		throw new IllegalArgumentException("all array inputs must be over 0");
    	}
    	if ( sum % amounts.length == 0) { //if sum evenly distributes.
    		for (int i = 0; i < amounts.length; i++ ) {
    			finalArray[i] = (sum / amounts.length);
    		}
    	}
    	else {
    		for (int i = 0; i < amounts.length - remainder; i++ ) {
    			finalArray[i] = 2; //fill first corresponding arrays with 2.
    		}
    		for (int i = amounts.length - remainder; i < amounts.length; i++ ) {
    			finalArray[i] = 3; //fill second half of correspond arrays with 3.
    		}
    	}
        return finalArray;
    }
    
    /**
     * Accepts a given an amount of cents and returns the minimal number of
     * pennies, nickels, dimes, and quarters that equal the desired amount.
     * @param  amount  an int of the value of cents that are desired. 
     * @return  coins  an array filled with the minimal number of coins that add up to the amount.
     */
    
    public static int[] greedyChangemaker (int amount) {
    	int[] coins = new int[4];
    	int newAmount = amount;
    	if ( amount < 0 ) {
    		throw new IllegalArgumentException("amount must be greater than 0");
    	}
    	for (int i = 3; i >= 0; i-- ) {
    		if ( i == 3) {
        		coins[i] = (int) ( newAmount/25);
        		newAmount = newAmount % 25;
    		}
    		if ( i == 2) {
        		coins[i] = (int) ( newAmount/10);
        		newAmount = newAmount % 10;
    		}
    		if ( i == 1) {
        		coins[i] = (int) ( newAmount/5);
        		newAmount = newAmount % 5;
    		}
    		if ( i == 0) {
        		coins[i] = (int) ( newAmount/1);
        		newAmount = newAmount % 1;
    		}
    	}
    	return coins;
    }
}
