package main.wiki;

import java.util.*;

/**
 * a simple Site Map consisting of Wikipedia Article Names mapped to the other 
 * Articles they link to, as well as the number of times that each link is clicked.
 * @author Alex Armknecht.
 */
public class WikiWalker {
    HashMap<String, HashMap<String, Integer>> WW;
    
    public WikiWalker() {
        WW = new HashMap<>();
    }

    /**
     * Adds an article with the given name to the site map and associates the
     * given linked articles found on the page. Duplicate links in that list are
     * ignored, as should an article's links to itself.
     * 
     * @param articleName
     *            The name of the page's article
     * @param articleLinks
     *            List of names for those articles linked on the page
     */
    public void addArticle(String articleName, List<String> articleLinks) {
        if (!WW.containsKey(articleName)) {
            HashMap<String, Integer> addedMap = new HashMap<String, Integer>();
            for (int i = 0; i < articleLinks.size(); i++) {
                addedMap.put(articleLinks.get(i), 0);
            }
            WW.put(articleName, addedMap);
        }
    }

    /**
     * Determines whether or not, based on the added articles with their links,
     * there is *some* sequence of links that could be followed to take the user
     * from the source article to the destination.
     * 
     * @param src
     *            The beginning article of the possible path
     * @param dest
     *            The end article along a possible path
     * @return boolean representing whether or not that path exists
     */
    public boolean hasPath(String src, String dest) {
        Queue<String> frontier = new PriorityQueue<String> (); //queue of articles.
        Set<String> visited =new HashSet<String>(); //a set of all visited articles.
        frontier.add(src);
        while (!frontier.isEmpty()) {
            String current = frontier.poll();
            if (current.equals(dest)) {
                return true; //a link exists.
            } 
            if (visited.contains(current)) { 
                continue; //have already visited so skip.
            }
            if (WW.get(current) == null) {
                continue;
            }
            for (String key : WW.get(current).keySet()) { //check through every key that current has.
                if (!visited.contains(key)) {
                    frontier.add(key); //Hasn't been checked yet so add to queue.
                }
            }
            visited.add(current);
        }
        return false;
    }

    /**
     * Increments the click counts of each link along some trajectory. For
     * instance, a trajectory of ["A", "B", "C"] will increment the click count
     * of the "B" link on the "A" page, and the count of the "C" link on the "B"
     * page. Assume that all given trajectories are valid, meaning that a link
     * exists from page i to i+1 for each index i
     * 
     * @param traj
     *            A sequence of a user's page clicks; must be at least 2 article
     *            names in length
     */
    public void logTrajectory(List<String> traj) {
        if (traj.size() < 2) { 
            throw new IllegalArgumentException("must include at least 2 items");
        }
        for (int i = 0; i <traj.size() - 1; i++) {
            String current = traj.get(i);
            String next = traj.get(i+1);
            WW.get(current).put(next, WW.get(current).get(next) + 1);
        }
    }

    /**
     * Returns the number of clickthroughs recorded from the src article to the
     * destination article. If the destination article is not a link directly
     * reachable from the src, returns -1.
     * 
     * @param src
     *            The article on which the clickthrough occurs.
     * @param dest
     *            The article requested by the clickthrough.
     * @throws IllegalArgumentException
     *             if src isn't in site map
     * @return The number of times the destination has been requested from the
     *         source.
     */
    public int clickthroughs(String src, String dest) {
        if (! WW.containsKey(src)) {
            throw new IllegalArgumentException("src isn't in site map");
        }
        if (WW.get(src).get(dest) != null) {
            return WW.get(src).get(dest);
        }
        return -1;
    }

    /**
     * Based on the pattern of clickthrough trajectories recorded by this
     * WikiWalker, returns the most likely trajectory of k clickthroughs
     * starting at (but not including in the output) the given src article.
     * Duplicates and cycles are possible outputs along a most likely trajectory. In
     * the event of a tie in max clickthrough "weight," this method will choose
     * the link earliest in the ascending alphabetic order of those tied.
     * 
     * @param src
     *            The starting article of the trajectory (which will not be
     *            included in the output)
     * @param k
     *            The maximum length of the desired trajectory (though may be
     *            shorter in the case that the trajectory ends with a terminal
     *            article).
     * @return A List containing the ordered article names of the most likely
     *         trajectory starting at src.
     */
    public List<String> mostLikelyTrajectory(String src, int k) {
        if (! WW.containsKey(src)) {
            throw new IllegalArgumentException("src isn't in site map");
        }
        List<String> trajectories = new ArrayList<String>();
        String current = src; 
        int mostCLicks = -1;
        String bestPath = "";
        while (k > 0 &&  WW.get(current) != null) {
            for (Map.Entry<String, Integer> entry : WW.get(current).entrySet()) { //Iterate over map.
                if (entry.getValue() > mostCLicks) { //has more clicks than past article.
                    mostCLicks = entry.getValue();
                    bestPath = entry.getKey();
                }
                if (entry.getValue() == mostCLicks) { //has equal clicks so go by alphabet.
                    if (entry.getKey().compareTo(bestPath) <= 0) {
                        mostCLicks = entry.getValue(); 
                        bestPath = entry.getKey();
                    }
                }
            }
            if (! bestPath.equals("")) {
                trajectories.add(bestPath); //add to list of best path.
            }
            current = bestPath; //go to the next map.
            mostCLicks = -1;
            k--;
        }
        return trajectories; 
    }
}
