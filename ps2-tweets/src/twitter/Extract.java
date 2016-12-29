package twitter;

import java.util.List;
import java.util.Set;
import java.time.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.regex.*;

/**
 * Extract consists of methods that extract information from a list of tweets.
 * 
 * DO NOT change the method signatures and specifications of these methods, but
 * you should implement their method bodies, and you may add new public or
 * private methods or classes if you like.
 */
public class Extract {

    /**
     * Get the time period spanned by tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return a minimum-length time interval that contains the timestamp of
     *         every tweet in the list.
     */
    public static Timespan getTimespan(List<Tweet> tweets) {
        //check for zero length array of tweets
        if (tweets.size() == 0) {
            Instant currentTime = Instant.now();
            Timespan result = new Timespan(currentTime, currentTime);
            return result;
        }
        //sort tweets list
        Collections.sort(tweets, (o1, o2) -> (o1.getTimestamp().isBefore(o2.getTimestamp()) ? -1 : (o1.getTimestamp()==o2.getTimestamp() ? 0 : 1)));
        //make new Timespan instance of minimum-length time interval
        Timespan result = new Timespan(tweets.get(0).getTimestamp(), tweets.get(tweets.size()-1).getTimestamp());
        return result;
    }
    

    /**
     * Get usernames mentioned in a list of tweets.
     * 
     * @param tweets
     *            list of tweets with distinct ids, not modified by this method.
     * @return the set of usernames who are mentioned in the text of the tweets.
     *         A username-mention is "@" followed by a Twitter username (as
     *         defined by Tweet.getAuthor()'s spec).
     *         The username-mention cannot be immediately preceded or followed by any
     *         character valid in a Twitter username.
     *         For this reason, an email address like bitdiddle@mit.edu does NOT 
     *         contain a mention of the username mit.
     *         Twitter usernames are case-insensitive, and the returned set may
     *         include a username at most once.
     */
    public static Set<String> getMentionedUsers(List<Tweet> tweets) {
        Set<String> usernames = new HashSet<String>();
        Pattern username = Pattern.compile("@[a-zA-Z1-9_-]+");
        
        //cycle through tweets and look for usernames
        for(Tweet t : tweets) {
            Matcher tweetText = username.matcher(t.getText());
            while(tweetText.find()){
                //for case where username is at the beginning of tweet text
                if(tweetText.start() == 0){
                    usernames.add(t.getText().substring(tweetText.start()+1,tweetText.end()).toLowerCase());
                    //System.out.println("if: " + t.getText().substring(tweetText.start()+1, tweetText.end()));
                }
                //case where username is not at beginning of tweet text
                else{
                    Pattern validUsernameChar = Pattern.compile("[a-zA-Z1-9_-]");
                    Matcher firstChar = validUsernameChar.matcher(t.getText().substring(tweetText.start()-1, tweetText.start()));
                    if(!firstChar.matches()){
                        usernames.add(t.getText().substring(tweetText.start()+1,tweetText.end()).toLowerCase()); 
                        //System.out.println("else: " + t.getText().substring(tweetText.start()+1, tweetText.end()));
                    }
                    
                }
                
            }
        }
        //usernames.stream().forEach(e -> System.out.println(e));
        return usernames;
    }

    /* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
