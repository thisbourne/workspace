package library;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameter;
import org.junit.runners.Parameterized.Parameters;

/**
 * Test suite for Library ADT.
 */
@RunWith(Parameterized.class)
public class LibraryTest {

    /*
     * Note: all the tests you write here must be runnable against any
     * Library class that follows the spec.  JUnit will automatically
     * run these tests against both SmallLibrary and BigLibrary.
     */

    /**
     * Implementation classes for the Library ADT.
     * JUnit runs this test suite once for each class name in the returned array.
     * @return array of Java class names, including their full package prefix
     */
    @Parameters(name="{0}")
    public static Object[] allImplementationClassNames() {
        return new Object[] { 
            "library.SmallLibrary", 
            "library.BigLibrary"
        }; 
    }

    /**
     * Implementation class being tested on this run of the test suite.
     * JUnit sets this variable automatically as it iterates through the array returned
     * by allImplementationClassNames.
     */
    @Parameter
    public String implementationClassName;    

    /**
     * @return a fresh instance of a Library, constructed from the implementation class specified
     * by implementationClassName.
     */
    public Library makeLibrary() {
        try {
            Class<?> cls = Class.forName(implementationClassName);
            return (Library) cls.newInstance();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        } catch (InstantiationException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
    
    
    /*
     * Testing strategy
     * ==================
     * 
     * buy(): input book output copy of book
     * 
     * checkout(): input copy no output
     * 
     * checkin(): input copy to checkin no output
     * 
     * isAvailable(): input copy that is available, unavailable
     *                output either true or false accordingly
     *                
     * allCopies(): input book to find output 1, multiple copies
     * 
     * availableCopies(): input book with avail copies of 1, multiple; 
     *                          without avail copies 
     *                    output all available copies of book
     *                    
     * find(): input: sting with 1, multiple words
     *         output: book match only author, only title, both author title, multiple books
     *         
     * lose(): input copy which is checked in, checked out, no output
     */
    
    // TODO: put JUnit @Test methods here that you developed from your testing strategy
    @Test
    public void testExampleTest() {
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("You Should", "Replace It", "With Your Own Tests"), 1990);
        assertEquals(Collections.emptySet(), library.availableCopies(book));
    }
    
    
    @Test(expected=AssertionError.class)
    public void testAssertionsEnabled() {
        assert false; // make sure assertions are enabled with VM argument: -ea
    }
    
    @Test
    public void testBuy(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        assertTrue("A copuy of the book should be available", library.isAvailable(copyBook));
    }
    
    @Test
    public void testCheckout(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        library.checkout(copyBook);
        assertFalse("Copy of book should not be available", library.isAvailable(copyBook));
    }
    
    @Test
    public void testCheckin(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        library.checkout(copyBook);
        library.checkin(copyBook);
        assertTrue("Copy of book be available again", library.isAvailable(copyBook));
    }
    
    @Test
    public void testIsAvailableTrue(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        assertTrue("Copy of book should be available", library.isAvailable(copyBook));
    }
    
    @Test
    public void testIsAvailableFalse(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        BookCopy copyBook2 = library.buy(book);
        library.checkout(copyBook);
        assertFalse("Copy of book be should not be available", library.isAvailable(copyBook));
    }
    
    @Test
    public void testAllCopiesOneCopy(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        assertEquals(library.allCopies(book).size(), 1);
    }
    
    @Test
    public void testAllCopiesMultCopy(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        BookCopy copyBook2 = library.buy(book);
        library.checkout(copyBook2);
        assertEquals(library.allCopies(book).size(), 2);
    }
    
    @Test
    public void testAllAvailableCopies(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        BookCopy copyBook2 = library.buy(book);
        library.checkout(copyBook2);
        assertEquals(library.availableCopies(book).size(), 1);
    }
    
    @Test
    public void testLose(){
        Library library = makeLibrary();
        Book book = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        BookCopy copyBook = library.buy(book);
        library.lose(copyBook);
        assertFalse("A lost book should not be in existence", 
                library.allCopies(copyBook.getBook()).contains(copyBook));
    }
    
    @Test
    public void testFind(){
        Library library = makeLibrary();
        Book book1 = new Book("This Test Is Just An Example", Arrays.asList("Joe Smoe"), 2000);
        Book book2 = new Book("Avitar", Arrays.asList("Norm Meyer", "Joe Smoe", "Mark Twain"), 2002);
        Book book3 = new Book("Avitar", Arrays.asList("William Defoe"), 2004);
        Book book4 = new Book("Biography of Joe Smoe", Arrays.asList("Joe Smoe", "Mark Twain"), 2002);
        Book book5 = new Book("Avitar", Arrays.asList("Norm Meyer", "Joe Smoe", "Mark Twain"), 2010);
        BookCopy copy1Book1 = library.buy(book1);
        BookCopy copy2Book1 = library.buy(book1);
        BookCopy copy1Book2 = library.buy(book2);
        BookCopy copy1Book3 = library.buy(book3);
        BookCopy copy1Book4 = library.buy(book4);
        BookCopy copy1Book5 = library.buy(book5);
        List<Book> librarySearch = library.find("Joe Smoe");
        assertFalse("expected non-empty list", librarySearch.isEmpty());
        assertTrue("expected list to contain these books", 
                librarySearch.containsAll(Arrays.asList(book1, book2, book4, book5)));
        assertTrue("expect book5 to be before book2", librarySearch.indexOf(book5) < librarySearch.indexOf(book2));
        
    }
    

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
