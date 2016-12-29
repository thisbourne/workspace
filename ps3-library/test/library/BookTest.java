package library;

import java.util.Arrays;
import java.util.regex.Pattern;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test suite for Book ADT.
 */
public class BookTest {

    /*
     * Testing strategy
     * ==================
     * 
     * TODO: your testing strategy for this ADT should go here.
     * Make sure you have partitions.
     * 
     * Test Strategy for getTitle
     * 
     *     Partition:
     * 
     *     Inputs:
     *   
     *     Output: title without space and with space and with quotes
     *         
     *     Each of the partition points are tested at least once below
     *     
     * Test Strategy for getAuthors
     * 
     *     Partition:
     * 
     *     Inputs:
     *   
     *     Output: one author, multiple authors
     *         
     *     Each of the partition points are tested at least once below
     *     
     * Test Strategy for getYear
     * 
     *     Partition:
     * 
     *     Inputs:
     *   
     *     Output: one digit year, four digit year; all positive
     *         
     *     Each of the partition points are tested at least once below
     *     
     * Test Strategy for toString
     * 
     *     Partition:
     * 
     *     Inputs:
     *   
     *     Output: a book meeting spec
     *         
     *     Each of the partition points are tested at least once below
     *     
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals("This Test Is Just An Example", book.getTitle());
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test public void testGetterForTitleNoSpace(){
        Book book = new Book("Jaws", Arrays.asList("Who Knows"), 2000);
        Pattern titleCheck = Pattern.compile("[^\\s]+");
        assert(titleCheck.matcher(book.getTitle()).find());
        assert(book.getTitle().equals("Jaws"));
    }
    
    @Test public void testGetterForTitleWithSpace(){
        Book book = new Book("Jaws \"Goes\" Swimming", Arrays.asList("Who Knows"), 2000);
        Pattern titleCheck = Pattern.compile("[^\\s]+");
        assert(titleCheck.matcher(book.getTitle()).find());
        assert(book.getTitle().equals("Jaws \"Goes\" Swimming"));
    }
    
    @Test public void testGetterForAuthorsOneName(){
        Book book = new Book("Jaws Goes Swimming", Arrays.asList("Who Knows"), 2000);
        Pattern titleCheck = Pattern.compile("[^\\s]+");
        assert(titleCheck.matcher(book.getTitle()).find());
    }
    
    @Test public void testGetterForAuthorsMultipleNames(){
        Book book = new Book("Jaws Goes Swimming", Arrays.asList("Who Knows", "Johnny Does"), 2000);
        Pattern titleCheck = Pattern.compile("[^\\s]+");
        assert(titleCheck.matcher(book.getTitle()).find());
    }
    
    @Test public void testGetterForYearOneDigitYear(){
        Book book = new Book("Jaws Goes Swimming", Arrays.asList("Who Knows", "Johnny Does"), 3);
        int year = book.getYear();
        assert(year >=0);
        assert(year%1 == 0);
    }
    
    @Test public void testGetterForYearMultipleDigitYear(){
        Book book = new Book("Jaws Goes Swimming", Arrays.asList("Who Knows", "Johnny Does"), 2000);
        int year = book.getYear();
        assert(year >=0);
        assert(year%1 == 0);
    }
    
    @Test public void testToString(){
        Book book = new Book("Jaws \"Goes\" Swimming", Arrays.asList("Who Knows", "Johnny Does"), 2000);
        String bookString = "The book title is: Jaws \"Goes\" Swimming\nThe author(s) is(are): Who Knows, Johnny Does\nPublished in: 2000";
        assert(book.toString().equals(bookString));
        
    }

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
