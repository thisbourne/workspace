package twitter;

import java.util.List;
import java.util.ArrayList;

/**
 * Filter consists of methods that filter a list of tweets for those matching a
 * condition.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Filter {

    /**
     * Find tweets written by a particular user.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param username
     *            Twitter username, required to be a valid Twitter username as
     *            defined by Tweet.getAuthor()'s spec.
     * @return all and only the tweets in the list whose author is username,
     *         in the same order as in the input list.
     */
    public static List<Tweet> writtenBy(List<Tweet> tweets, String username) {
        List<Tweet> userTweets = new ArrayList<Tweet>();
        
        for(Tweet t : tweets){
            if(t.getAuthor().toLowerCase().equals(username.toLowerCase())){
                userTweets.add(t);
            }
        }
        
        return userTweets;
    }

    /**
     * Find tweets that were sent during a particular timespan.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param timespan
     *            timespan
     * @return all and only the tweets in the list that were sent during the timespan,
     *         in the same order as in the input list.
     */
    public static List<Tweet> inTimespan(List<Tweet> tweets, Timespan timespan) {
        List<Tweet> tweetsInTimespan = new ArrayList<Tweet>();
        
        for(Tweet t : tweets){
            if((t.getTimestamp().isAfter(timespan.getStart()) && t.getTimestamp().isBefore(timespan.getEnd())) ||
                    t.getTimestamp().equals(timespan.getEnd()) || t.getTimestamp().equals(timespan.getStart())){
                tweetsInTimespan.add(t);
            }
        }
        //tweetsInTimespan.stream().forEach(e -> System.out.println(e.getTimestamp().toString()));
        return tweetsInTimespan;
    }

    /**
     * Find tweets that contain certain words.
     * 
     * @param tweets
     *            a list of tweets with distinct ids, not modified by this method.
     * @param words
     *            a list of words to search for in the tweets. 
     *            A word is a nonempty sequence of nonspace characters.
     * @return all and only the tweets in the list such that the tweet text (when 
     *         represented as a sequence of nonempty words bounded by space characters 
     *         and the ends of the string) includes *at least one* of the words 
     *         found in the words list. Word comparison is not case-sensitive,
     *         so "Obama" is the same as "obama".  The returned tweets are in the
     *         same order as in the input list.
     */
/*  public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> tweetsWithWords = new ArrayList<Tweet>();
        for(Tweet t: tweets){
            String[] tweetWords = t.getText().split(" +");
            Boolean foundHit = false;
            for(String word : words){
                if(foundHit) break;
                for(String tweetWord : tweetWords){
                    if (tweetWord.equalsIgnoreCase(word)){
                        tweetsWithWords.add(t);
                        foundHit = true;
                        break;
                    }
                }
            }
        }
        
        
        return tweetsWithWords;
    }*/
    
    public static List<Tweet> containing(List<Tweet> tweets, List<String> words) {
        List<Tweet> tweetsWithWords = new ArrayList<Tweet>();
        for(Tweet t: tweets){
            if(containsWord(t, words)) tweetsWithWords.add(t);
        }
        return tweetsWithWords;
    }
    
    private static Boolean containsWord(Tweet t, List<String> words){
        String[] tweetWords = t.getText().split(" +");
        for(String word : words){
            for(String tweetWord : tweetWords){
                if (tweetWord.equalsIgnoreCase(word)){
                    return true;
                }
            }
        }
        return false;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
