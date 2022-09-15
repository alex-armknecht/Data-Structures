package main.forneymon.arena;

import main.forneymon.species.Forneymon;

/**
 * Contains methods for facing off LinkedForneymonagerie against one another!
 */
public class ForneymonArena {
    
    public static final int BASE_DAMAGE = 5;
    
    /**
     * Conducts a fight between two Forneymonageries, consisting of the following
     * steps:
     * <ol>
     *   <li>Forneymon from each Forneymonagerie are paired to fight, in sequence
     *     starting from index 0.</li>
     *  <li>Forneymon that faint (have 0 or less health) are removed from their
     *     respective Forneymonagerie.</li>
     *  <li>Repeat until one or both Forneymonagerie have no remaining Forneymon.</li>     
     * </ol>
     * @param fm1 One of the fighting Forneymonagerie
     * @param fm2 One of the fighting Forneymonagerie
     * @param verbose Whether or not the fight's steps are printed to the console
     */
    public static void fight (LinkedForneymonagerie fm1, LinkedForneymonagerie fm2, boolean verbose) {
        StringBuilder logString = new StringBuilder();
        LinkedForneymonagerie.Iterator it1 = fm1.getIterator(),
                                       it2 = fm2.getIterator();

        logString.append("[!] Combat Starting!\n");
        while (!(fm1.empty() || fm2.empty())) {
            Forneymon f1 = it1.getCurrent();
            Forneymon f2 = it2.getCurrent();
            logString.append("  [VS] New Round: " + f1 + " vs " + f2 + "\n");

            // Attack phase
            if (f1.takeDamage(BASE_DAMAGE + f2.getLevel(), f2.getDamageType()) <= 0) {
                it1.removeCurrent();
            }
            if (f2.takeDamage(BASE_DAMAGE + f1.getLevel(), f1.getDamageType()) <= 0) {
                it2.removeCurrent();
            }
            logString.append("    [>] Combat Results: " + f1 + " vs " + f2 + "\n");

            if (it1.isValid()) {
                it1.next();
            }
            if (it2.isValid()) {
                it2.next();
            }
        }

        logString.append("[!] Combat Finished! Victor: " + ((fm1.empty() && fm2.empty()) ? "TIE MATCH!" : "Forneymonagerie " + (fm1.empty() ? 2 : 1) + "\n"));
        if (verbose) { System.out.println(logString.toString()); }
    }
    
}
