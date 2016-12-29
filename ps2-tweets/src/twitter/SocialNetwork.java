package twitter;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Comparator;

/**
 * SocialNetwork provides methods that operate on a social network.
 * 
 * A social network is represented by a Map<String, Set<String>> where map[A] is
 * the set of people that person A follows on Twitter, and all people are
 * represented by their Twitter usernames. Users can't follow themselves. If A
 * doesn't follow anybody, then map[A] may be the empty set, or A may not even exist
 * as a key in the map; this is true even if A is followed by other people in the network.
 * Twitter usernames are not case sensitive, so "ernie" is the same as "ERNie".
 * A username should appear at most once as a key in the map or in any given
 * map[A] set.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class SocialNetwork {

    /**
     * Guess who might follow whom, from evidence found in tweets.
     * 
     * @param tweets
     *            a list of tweets providing the evidence, not modified by this
     *            method.
     * @return a social network (as defined above) in which Ernie follows Bert
     *         if and only if there is evidence for it in the given list of
     *         tweets.
     *         One kind of evidence that Ernie follows Bert is if Ernie
     *         @-mentions Bert in a tweet. This must be implemented. Other kinds
     *         of evidence may be used at the implementor's discretion.
     *         All the Twitter usernames in the returned social network must be
     *         either authors or @-mentions in the list of tweets.
     */
    public static Map<String, Set<String>> guessFollowsGraph(List<Tweet> tweets) {
        Map<String, Set<String>> followersGraph = new HashMap<String, Set<String>>();
        
        for(Tweet t: tweets){
            if(followersGraph.get(t.getAuthor()) == null){
                followersGraph.put(t.getAuthor().toLowerCase(), getMentions(t,t.getAuthor()));
            }
            else{
                Set<String> followers = followersGraph.get(t.getAuthor().toLowerCase());
                followers.addAll(getMentions(t, t.getAuthor()));
                followersGraph.put(t.getAuthor(), followers);
            }
        }
        
        return followersGraph;
    }
    
    private static Set<String> getMentions(Tweet tweet, String author){
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet));
        Boolean authorMentioned = false;
        for(String user : mentionedUsers){
            if(user.equalsIgnoreCase(author)) authorMentioned = true;
        }
        if(authorMentioned) mentionedUsers.remove(author);
        return mentionedUsers;
    }
    


    /**
     * Find the people in a social network who have the greatest influence, in
     * the sense that they have the most followers.
     * 
     * @param followsGraph
     *            a social network (as defined above)
     * @return a list of all distinct Twitter usernames in followsGraph, in
     *         descending order of follower count.
     */
    public static List<String> influencers(Map<String, Set<String>> followsGraph) {
        List<String> influencerList = new ArrayList<String>();
        Set<Map.Entry<String, Set<String>>> followsGraphSet = followsGraph.entrySet();
        Map<String, Integer> users = new HashMap<String, Integer>();
        
        //processes followsGraphSet to output Map "users" with key: username, value: # of followers
        for(Map.Entry<String, Set<String>> username: followsGraphSet){
            for(String mention : username.getValue()){
                if(users.containsKey(mention.toLowerCase())){
                    Integer followers = users.get(mention.toLowerCase());
                    followers += 1;
                    users.put(mention.toLowerCase(), followers);
                }
                else{
                    users.put(mention.toLowerCase(), 1);
                }
            } 
            if(!users.containsKey(username.getKey().toLowerCase())) users.put(username.getKey().toLowerCase(), 0);
        }
        
        //create set from Map of users
        Set<Map.Entry<String, Integer>> usersSet = users.entrySet();
        
        //System.out.println("This is set of users");
        //for(Map.Entry<String, Integer> user : usersSet) System.out.println(user.getKey() + ": " + user.getValue());
        
        //create sorted set from set of usersSet; note created my own comparator, "UsersSetComp" which assumes no duplicated usernames
        SortedSet<Map.Entry<String, Integer>> sortedUsersSet = new TreeSet<Map.Entry<String, Integer>>(new UsersSetComp());
        sortedUsersSet.addAll(usersSet);
        
        //System.out.println("This is the ordered users set");
        //for(Map.Entry<String, Integer> user3 : sortedUsersSet) System.out.println(user3.getKey() + ": " + user3.getValue());
        
        //now create ordered list, "influencerList" from ordered set, "sortedUsersSet"
        for(Map.Entry<String, Integer> user : sortedUsersSet){
            influencerList.add(user.getKey());
        }
        //System.out.println("Ordered list of users");
        //for(String name : influencerList) System.out.println(name);
        return influencerList;
    }
    


    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
