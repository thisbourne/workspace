package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.*;

import org.junit.Test;

public class ExtractTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     * 
     * Test Strategy for getTimespan
     * 
     *     Partition:
     * 
     *     Inputs: tweets length = 1, 2, 5
     *   
     *     Output: minimum-length time interval = 0, 1hr, > 1 hr; test when multiple tweets have same
     *             time index which are either earliest or latest date
     *         
     *     Each of the partition points are tested at least once below
     *     
     * Test Strategy for getMentionedUsers
     * 
     *     Partition:
     *     
     *     Inputs: tweet with the following mentions: 0, 1, 2
     *             tweet with username followed by non-whitespace invalid tweet character
     *             tweet with username preceeded by non-whitespace invalid tweet character
     *             Multiple tweets
     *             Tweets with username mentioned more than once
     *             
     *     Output: tied to input partition space
     *     
     *     Each of the partition points are tested at least once below
     *    
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T11:10:59Z");
    private static final Instant d5 = Instant.parse("2016-02-17T10:20:01Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype", d2);
    private static final Tweet tweet3 = new Tweet(3, "norm", "in 30 minutes @alyssa will talk", d3);
    private static final Tweet tweet4 = new Tweet(4, "matt", "rivest #@alyssa and @bbitdiddle! talk in 10 min.", d4);
    private static final Tweet tweet5 = new Tweet(5, "renee", "rivest talk in 30 minutes #hype", d5);
    private static final Tweet tweet6 = new Tweet(6, "matt", "rivest #@alyssa and @bbitdiddle! norm@mit talk in 10 min.", d5);
    private static final Tweet tweet7 = new Tweet(7, "matt", "rivest @alyssa #@Alyssa and @bbitdiddle! norm@mit talk in 10 min.", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    // lenth = 2, min-length time interval = 1hrs
    @Test
    public void testGetTimespanTwoTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d2, timespan.getEnd());
    }
    
    // lenth = 2, min-length time interval = 0hrs
    @Test
    public void testGetTimespanZero() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet3));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d3, timespan.getEnd());
    }
    
    // lenth = 1, min-length time interval = 0hrs
    @Test
    public void testGetTimespanOneTweet() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1));
        
        assertEquals("expected start", d1, timespan.getStart());
        assertEquals("expected end", d1, timespan.getEnd());
    }
    
    // lenth = 5, min-length time interval = 2hrs
    @Test
    public void testGetTimespanFiveTweets() {
        Timespan timespan = Extract.getTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5));
        
        assertEquals("expected start", d3, timespan.getStart());
        assertEquals("expected end", d4, timespan.getEnd());
    }
    
 // lenth = 0, min-length time interval = 0hrs
    @Test
    public void testGetTimespanEmptyList() {
        Timespan timespan = Extract.getTimespan(Arrays.asList());
        
        assertEquals("expected zero length time span", timespan.getStart(), timespan.getEnd());
    }
    
    @Test
    public void testGetMentionedUsersNoMention() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet1));
        
        assertTrue("expected empty set", mentionedUsers.isEmpty());
    }
    
    @Test
    public void testGetMentionedUsersOneMention() {
        Set<String> mentionedUsersInitial = Extract.getMentionedUsers(Arrays.asList(tweet3));
        Set<String> mentionedUsers = new HashSet<String>();
        for(String username : mentionedUsersInitial){
            mentionedUsers.add(username.toLowerCase());
        }
        
        
        assertTrue("expected alyssa", mentionedUsers.contains("alyssa"));
    }
    
    @Test
    public void testGetMentionedUsersTwoMentionsWithNonWhitespaceInvalidChars() {
        Set<String> mentionedUsersInitial = Extract.getMentionedUsers(Arrays.asList(tweet4));
        Set<String> mentionedUsers = new HashSet<String>();
        for(String username : mentionedUsersInitial){
            mentionedUsers.add(username.toLowerCase());
        }
        
        assertTrue("expected alyssa and bbitdiddle", mentionedUsers.contains("alyssa") && mentionedUsers.contains("bbitdiddle"));
    }
    
    @Test
    public void testGetMentionedUsersMultipleTweets() {
        Set<String> mentionedUsersInitial = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet3, tweet4));
        Set<String> mentionedUsers = new HashSet<String>();
        for(String username : mentionedUsersInitial){
            mentionedUsers.add(username.toLowerCase());
        }
        
        assertTrue("expected alyssa and bbitdiddle", mentionedUsers.contains("alyssa") && mentionedUsers.contains("bbitdiddle"));
        assertFalse("should not contain mit", mentionedUsers.contains("mit"));
    }
    
    @Test
    public void testGetMentionedUsersMultipleTweetsWithEmail() {
        Set<String> mentionedUsersInitial = Extract.getMentionedUsers(Arrays.asList(tweet1, tweet3, tweet6));
        Set<String> mentionedUsers = new HashSet<String>();
        for(String username : mentionedUsersInitial){
            mentionedUsers.add(username.toLowerCase());
        }
        
        assertTrue("expected alyssa and bbitdiddle", mentionedUsers.contains("alyssa") && mentionedUsers.contains("bbitdiddle"));
        assertFalse("should not contain mit", mentionedUsers.contains("mit"));
    }  
    
    @Test
    public void testGetMentionedUsersMultipleTweetsWithCapitalLetter() {
        Set<String> mentionedUsers = Extract.getMentionedUsers(Arrays.asList(tweet7));

        
        assertFalse("should not contain mit", mentionedUsers.contains("mit"));
        assertEquals("expected only two usernames", 2, mentionedUsers.size());
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * Extract class that follows the spec. It will be run against several staff
     * implementations of Extract, which will be done by overwriting
     * (temporarily) your version of Extract with the staff's version.
     * DO NOT strengthen the spec of Extract or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Extract, because that means you're testing a
     * stronger spec than Extract says. If you need such helper methods, define
     * them in a different class. If you only need them in this test class, then
     * keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
