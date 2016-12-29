package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class SocialNetworkTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     * 
     * Testing strategy for guessFollowsGraph
     * 
     *     Partition:
     *     
     *     Inputs: tweets: >=1;
     *             # of @-mentions: 0, >=1
     *             duplicate @-mentions of same person should only add one entry
     *             @-mentions of the author should not add entry
     *             author should not contain self in Map of followers
     *             
     *     Output: tied to input
     *     
     *     Each of the partition points are tested at least once below  
     *     
     * Testing strategy for influencers
     * 
     *     Partition:
     *     
     *     Inputs: followsGraph contains >=1 entry
     *             entries with no followers: 0, 1
     *             entries with equal followers: 0, 1
     *          
     *             
     *     Output: tied to input
     *     
     *     Each of the partition points are tested at least once below
     */
    
    private static final Instant d1 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d2 = Instant.parse("2016-02-17T11:00:00Z");
    private static final Instant d3 = Instant.parse("2016-02-17T10:00:00Z");
    private static final Instant d4 = Instant.parse("2016-02-17T11:10:59Z");
    private static final Instant d5 = Instant.parse("2016-02-17T10:20:01Z");
    
    private static final Tweet tweet1 = new Tweet(1, "alyssa", "is it reasonable to talk about rivest so much?", d1);
    private static final Tweet tweet2 = new Tweet(2, "bbitdiddle", "rivest talk in 30 minutes #hype, @alyssa and @bbitdiddle", d2);
    private static final Tweet tweet3 = new Tweet(3, "matt", "in 30 minutes @alyssa will talk", d3);
    private static final Tweet tweet4 = new Tweet(4, "matt", "rivest #@alyssa and @bbitdiddle! get together in 10 min.", d4);
    private static final Tweet tweet5 = new Tweet(5, "matthew", "rivest talk @bbitdiddle in 30 minutes #hype", d5);
    private static final Tweet tweet6 = new Tweet(6, "norm", "rivest Talk @matt in 30 minutes #hype", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testGuessFollowsGraphEmpty() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(new ArrayList<>());
        
        assertTrue("expected empty graph", followsGraph.isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphOneTweetNoFollows() {
        
        Map<String, Set<String>> followsGraphInitial = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1));
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        Set<Map.Entry<String, Set<String>>> followsGraphSet = followsGraphInitial.entrySet();
        for(Map.Entry<String, Set<String>> username: followsGraphSet){
            followsGraph.put(username.getKey().toLowerCase(), username.getValue()); 
        }
        
        //assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertTrue("expect no-followers for alyssa", !followsGraph.containsKey("alyssa") || followsGraph.get("alyssa").isEmpty());
    }
    
    @Test
    public void testGuessFollowsGraphMultipleTweetsMultipleFollows() {
        
        Map<String, Set<String>> followsGraphInitial = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet4));
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        Set<Map.Entry<String, Set<String>>> followsGraphSet = followsGraphInitial.entrySet();
        for(Map.Entry<String, Set<String>> username: followsGraphSet){
            followsGraph.put(username.getKey().toLowerCase(), username.getValue()); 
        }
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        if(followsGraph.containsKey("alyssa")){
            assertTrue("expect at most one follower for alyssa", followsGraph.get("alyssa").size()<=1); 
        }
        assertEquals("expect two followers for matt", 2, followsGraph.get("matt").size());
    }
    
    @Test
    public void testGuessFollowsGraphMultipleAuthorDoesNotFollowTheirself() {
        
        Map<String, Set<String>> followsGraphInitial = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet2));
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        Set<Map.Entry<String, Set<String>>> followsGraphSet = followsGraphInitial.entrySet();
        for(Map.Entry<String, Set<String>> username: followsGraphSet){
            followsGraph.put(username.getKey().toLowerCase(), username.getValue()); 
        }
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        assertEquals("expect one follower for bbitdiddle", 1, followsGraph.get("bbitdiddle").size());
        assertTrue("expect bbitdiddle to follow alyssa", followsGraph.get("bbitdiddle").contains("alyssa"));
    }
    
    @Test
    public void testGuessFollowsGraphMultipleTweetsRepeatedFollows() {
        
        Map<String, Set<String>> followsGraphInitial = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet3, tweet4));
        Map<String, Set<String>> followsGraph = new HashMap<String, Set<String>>();
        Set<Map.Entry<String, Set<String>>> followsGraphSet = followsGraphInitial.entrySet();
        for(Map.Entry<String, Set<String>> username: followsGraphSet){
            followsGraph.put(username.getKey().toLowerCase(), username.getValue()); 
        }
        
        assertFalse("expected non-empty graph", followsGraph.isEmpty());
        if(followsGraph.containsKey("alyssa")){
            assertTrue("expect at most one follower for alyssa", followsGraph.get("alyssa").size()<=1);
        }
        
        assertEquals("expect two followers for matt", 2, followsGraph.get("matt").size());
    }
    
    @Test
    public void testInfluencersEmpty() {
        Map<String, Set<String>> followsGraph = new HashMap<>();
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertTrue("expected empty list", influencers.isEmpty());
    }
    
    @Test
    public void testInfluencersMultipleUsers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("expect matt has least followers", 2, influencers.indexOf("matt"));
        assertEquals("expect bbitdiddle has next least followers", 1, influencers.indexOf("bbitdiddle"));
        assertEquals("expect alyssa has most followers", 0, influencers.indexOf("alyssa"));
    }
    
    @Test
    public void testInfluencersMultipleUsersAndEqualFollowers() {
        Map<String, Set<String>> followsGraph = SocialNetwork.guessFollowsGraph(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6));
        List<String> influencers = SocialNetwork.influencers(followsGraph);
        
        assertFalse("expected non-empty list", influencers.isEmpty());
        assertEquals("expect matt has least followers", 2, influencers.indexOf("matt"));
    }

    /*
     * Warning: all the tests you write here must be runnable against any
     * SocialNetwork class that follows the spec. It will be run against several
     * staff implementations of SocialNetwork, which will be done by overwriting
     * (temporarily) your version of SocialNetwork with the staff's version.
     * DO NOT strengthen the spec of SocialNetwork or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in SocialNetwork, because that means you're testing a
     * stronger spec than SocialNetwork says. If you need such helper methods,
     * define them in a different class. If you only need them in this test
     * class, then keep them in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
