package library;

import org.junit.Test;

import library.BookCopy.Condition;

import static org.junit.Assert.*;

import java.util.Arrays;

/**
 * Test suite for BookCopy ADT.
 */
public class BookCopyTest {

    /*
     * Testing strategy
     * ==================
     * 
     * getBook():
     *           simple case no inputs only one output, an object of type Book
     *           
     * getCondition():
     *           no inputs, output either GOOD or DAMAGED; relies on setCondition to test DAMAGED condition
     *           
     * setCondition():
     *           inputs: GOOD or DAMAGED
     *           no outputs
     *           
     * toString():
     *           no inputs, output a String that matches expected string
     * 
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        BookCopy copy = new BookCopy(book);
        assertEquals(book, copy.getBook());
    }
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test 
    public void testGetBook() {
        Book book = new Book("This is a good book", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copy = new BookCopy(book);
        assertEquals(book, copy.getBook());
    }
    
    @Test 
    public void testGetConditionGood() {
        Book book = new Book("This is a good book", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copy = new BookCopy(book);
        assertEquals(Condition.GOOD, copy.getCondition());
    }
    
    @Test 
    public void testSetConditionDamaged() {
        Book book = new Book("This is a good book", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copy = new BookCopy(book);
        copy.setCondition(Condition.DAMAGED);
        assertEquals(Condition.DAMAGED, copy.getCondition());
    }
    
    @Test 
    public void testToString() {
        Book book = new Book("Jaws \"Goes\" Swimming", Arrays.asList("Who Knows", "Johnny Does"), 2000);
        BookCopy copy = new BookCopy(book);
        String copyString = "The book title is: Jaws \"Goes\" Swimming\nThe author(s) is(are): Who Knows, Johnny Does\nPublished in: 2000"
                + "\nThe Condition is: good";
        assert(copy.toString().equals(copyString));
    }


    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
