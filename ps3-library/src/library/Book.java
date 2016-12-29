package library;

import java.util.List;
import java.util.regex.Pattern;
import java.util.ArrayList;

/**
 * Book is an immutable type representing an edition of a book -- not the physical object, 
 * but the combination of words and pictures that make up a book.  Each book is uniquely
 * identified by its title, author list, and publication year.  Alphabetic case and author 
 * order are significant, so a book written by "Fred" is different than a book written by "FRED".
 */
public class Book {

    // TODO: rep
    private final String title;
    private final List<String> authors;
    private final int year;
    
    // Rep Invariant: title contains at least one non-space character
    //                authors must have at least one name and each name must contain
    //                        at least one non-space character
    //                year is nonnegative integer
    // Abstration Function: title is the tile of the book; authors is a list of authors; year is the common era calendar year
    // Safety from rep exposure:
    //   All fields are private;
    //   title is a string and year is an int, so are guaranteed immutable;
    //   authors is a mutable List, so Book() constructor and getAuthors() 
    //        make defensive copies to avoid sharing the rep's Date object with clients.
    
    /**
     * Make a Book.
     * @param title Title of the book. Must contain at least one non-space character.
     * @param authors Names of the authors of the book.  Must have at least one name, and each name must contain 
     * at least one non-space character.
     * @param year Year when this edition was published in the conventional (Common Era) calendar.  Must be nonnegative. 
     */
    public Book(String title, List<String> authors, int year) {
        this.title = title;
        this.year = year;
        this.authors = new ArrayList<String>(authors);
        checkRep();
    }
    
    // assert the rep invariant
    private void checkRep() {
        Pattern whiteSpaceCheck = Pattern.compile("[^\\s]+");
        assert(whiteSpaceCheck.matcher(title).find());
        assert(authors.size() >=1);
        for (String name : authors) {
            assert(whiteSpaceCheck.matcher(name).find());
        }
        assert(year >= 0);
        assert(year%1 == 0);
    }
    
    /**
     * @return the title of this book
     */
    public String getTitle() {
        return title;
    }
    
    /**
     * @return the authors of this book
     */
    public List<String> getAuthors() {
        List<String> authorsCopy = new ArrayList(authors);
        return authorsCopy;
    }

    /**
     * @return the year that this book was published
     */
    public int getYear() {
        return year;
    }

    /**
     * @return human-readable representation of this book that includes its title,
     *    authors, and publication year
     */
    public String toString() {
        String authorsString = "";
        for (String name : authors){
            authorsString = authorsString + name + ", ";
        }
        authorsString = authorsString.substring(0, authorsString.length() - 2);
        String stringToReturn = "The book title is: " + title + "\nThe author(s) is(are): "
                + authorsString + "\nPublished in: " + year;
        //System.out.println(stringToReturn);
        return stringToReturn;
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof Book)) return false;
        Book thatBook = (Book) that;
        Boolean titlesAreEqual = this.getTitle().equals(thatBook.getTitle());
        Boolean authorsAreEqual = this.getAuthors().equals(thatBook.getAuthors());
        Boolean yearsAreEqual = this.getYear() == (thatBook.getYear());
        return  titlesAreEqual && authorsAreEqual && yearsAreEqual;

    }
    
    @Override
    public int hashCode() {
        return this.toString().hashCode();
    }



    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */

}
