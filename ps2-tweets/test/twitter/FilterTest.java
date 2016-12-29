package twitter;

import static org.junit.Assert.*;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class FilterTest {

    /*
     * TODO: your testing strategies for these methods should go here.
     * Make sure you have partitions.
     * 
     * Testing strategy for writtenBy
     * 
     *     Partition:
     *     
     *     Inputs: number of tweets written by username: 0, >=1
     *             number of tweets not written by username: >=1
     *             number of tweets written by similar name; eg. matt and matthew: 0, 1
     *             
     *     Output is tied to input
     *     
     *     Each of the partition points are tested at least once below
     *     
     * Testing strategy for inTimespan
     * 
     *     Partition:
     *     
     *     Inputs: number of tweets in timespan: 0, >=1
     *             tweet at each endpoint of timespan
     *             number of tweets not in timespan: 0, >=1
     *             
     *     Output is tied to input
     *     
     *     Each of the partition points are tested at least once below  
     *     
     * Testing strategy containing
     * 
     *     Partition:
     *     
     *     Inputs: number of tweets: >=1
     *             number of words 1, >1
     *             tweet contains a matching word but different letter case: 0, >1
     *             tweet contains word within a word so shouldn't match
     *          
     *             
     *     Output: number of tweets: 0, >1
     *             should only contain the tweet once even if multiple matches
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
    private static final Tweet tweet3 = new Tweet(3, "matt", "in 30 minutes @alyssa will talk", d3);
    private static final Tweet tweet4 = new Tweet(4, "matt", "rivest #@alyssa and @bbitdiddle! get together in 10 min.", d4);
    private static final Tweet tweet5 = new Tweet(5, "matthew", "rivest talk in 30 minutes #hype", d5);
    private static final Tweet tweet6 = new Tweet(6, "norm", "rivest Talk in 30 minutes #hype", d5);
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testWrittenByMultipleTweetsSingleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2), "alyssa");
        
        assertEquals("expected singleton list", 1, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testWrittenByMultipleTweetsMultipleResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), "matt");
        
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet3) && writtenBy.contains(tweet4));
    }
    
    @Test
    public void testWrittenByMultipleTweetsNoResult() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet2, tweet3, tweet4, tweet5), "alyssa");
        
        assertEquals("expected singleton list", 0, writtenBy.size());
        //assertTrue("expected list to contain tweet", writtenBy.contains(tweet1));
    }
    
    @Test
    public void testWrittenByMultipleTweetsMultipleResultsCorrectOrder() {
        List<Tweet> writtenBy = Filter.writtenBy(Arrays.asList(tweet1, tweet2, tweet4, tweet3, tweet5), "matt");
        
        assertEquals("expected singleton list", 2, writtenBy.size());
        assertTrue("expected list to contain tweet", writtenBy.contains(tweet3) && writtenBy.contains(tweet4));
        assertTrue("expected tweet4 to be before tweet3", writtenBy.indexOf(tweet4) == 0);
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleResults() {
        Instant testStart = Instant.parse("2016-02-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, inTimespan.indexOf(tweet1));
    }
    
    @Test
    public void testInTimespanMultipleTweetsZeroResults() {
        Instant testStart = Instant.parse("2016-01-17T09:00:00Z");
        Instant testEnd = Instant.parse("2016-01-17T12:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5), new Timespan(testStart, testEnd));
        //inTimespan.stream().forEach(e -> System.out.println(e.getTimestamp()));
        //System.out.println(inTimespan.size());
        assertTrue("expected empty list", inTimespan.isEmpty());
    }
    
    @Test
    public void testInTimespanMultipleTweetsMultipleEdgeResults() {
        Instant testStart = Instant.parse("2016-02-17T10:00:00Z");
        Instant testEnd = Instant.parse("2016-02-17T11:00:00Z");
        
        List<Tweet> inTimespan = Filter.inTimespan(Arrays.asList(tweet1, tweet5, tweet2, tweet4, tweet3), new Timespan(testStart, testEnd));
        
        assertFalse("expected non-empty list", inTimespan.isEmpty());
        assertTrue("expected list to contain tweets", inTimespan.containsAll(Arrays.asList(tweet1, tweet5, tweet2, tweet3)));
        assertEquals("expected same order", 1, inTimespan.indexOf(tweet5));
    }
    
    @Test
    public void testContaining() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
    }
    
    @Test
    public void testContainingMultipleHitsInSameTweet() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("talk", "rivest"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2)));
        assertEquals("expected same order", 0, containing.indexOf(tweet1));
        assertEquals("expect only two tweets", 2, containing.size());
    }
    
    @Test
    public void testContainingZeroResults() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2), Arrays.asList("basketball"));
        
        assertTrue("expected empty list", containing.isEmpty());
    }
    
    @Test
    public void testContainingMultipleWords() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet5, tweet4, tweet2), Arrays.asList("talk", "minutes"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet5)));
        assertEquals("expected same order", 1, containing.indexOf(tweet5));
    }
    
    @Test
    public void testContainingCapitalLetter() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet2, tweet3, tweet4, tweet5, tweet6), Arrays.asList("talk", "minutes"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet3, tweet5, tweet6)));
        assertEquals("expected same order", 3, containing.indexOf(tweet5));
    }
    
    @Test
    public void testContainingWordWithInWord() {
        List<Tweet> containing = Filter.containing(Arrays.asList(tweet1, tweet5, tweet4, tweet2, tweet3), Arrays.asList("rivest", "minute"));
        
        assertFalse("expected non-empty list", containing.isEmpty());
        assertTrue("expected list to contain tweets", containing.containsAll(Arrays.asList(tweet1, tweet2, tweet4, tweet5)));
        assertEquals("expected same order", 1, containing.indexOf(tweet5));
    }
    
    

    /*
     * Warning: all the tests you write here must be runnable against any Filter
     * class that follows the spec. It will be run against several staff
     * implementations of Filter, which will be done by overwriting
     * (temporarily) your version of Filter with the staff's version.
     * DO NOT strengthen the spec of Filter or its methods.
     * 
     * In particular, your test cases must not call helper methods of your own
     * that you have put in Filter, because that means you're testing a stronger
     * spec than Filter says. If you need such helper methods, define them in a
     * different class. If you only need them in this test class, then keep them
     * in this test class.
     */


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
