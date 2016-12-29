package library;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.HashSet;
import java.util.Map;
import java.util.HashMap;
import java.util.SortedSet;
import java.util.TreeSet;

/** 
 * SmallLibrary represents a small collection of books, like a single person's home collection.
 */
public class SmallLibrary implements Library {

    // This rep is required! 
    // Do not change the types of inLibrary or checkedOut, 
    // and don't add or remove any other fields.
    // (BigLibrary is where you can create your own rep for
    // a Library implementation.)

    // rep
    private Set<BookCopy> inLibrary;
    private Set<BookCopy> checkedOut;
    
    // rep invariant:
    //    the intersection of inLibrary and checkedOut is the empty set
    //
    // abstraction function:
    //    represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out

    // Safety from rep exposure: constructor doesn't have inputs,
    //    there are no producers, and mutators: buy, checkout, 
    //    checkin, and lose only allow access to a BookCopy, which is
    //    not the rep and which is safe from exposure itself.
    //    
    
    public SmallLibrary() {
        this.inLibrary = new HashSet<BookCopy>();
        this.checkedOut = new HashSet<BookCopy>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        Set<BookCopy> intersection = new HashSet<BookCopy>(inLibrary);
        intersection.retainAll(checkedOut);
        assert(intersection.isEmpty());
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy newCopy = new BookCopy(book);
        inLibrary.add(newCopy);
        checkRep();
        return newCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        if(this.isAvailable(copy)) {
            this.inLibrary.remove(copy);
            this.checkedOut.add(copy);
            checkRep();
        }
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if(!this.isAvailable(copy)) {
            this.checkedOut.remove(copy);
            this.inLibrary.add(copy);
            checkRep();
        }
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        return this.inLibrary.contains(copy);
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> bookCopies = new HashSet<BookCopy>();
        for(BookCopy bookCopy : inLibrary){
            if(bookCopy.getBook().equals(book)) bookCopies.add(bookCopy);
        }
        for(BookCopy bookCopy : checkedOut){
            if(bookCopy.getBook().equals(book)) bookCopies.add(bookCopy);
        }
        return bookCopies;
    }
    
    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> bookCopies = new HashSet<BookCopy>();
        for(BookCopy bookCopy : inLibrary) {
            if (bookCopy.getBook().equals(book)) bookCopies.add(bookCopy);
        }
        return bookCopies;
    }

    @Override
    public List<Book> find(String query) {
        Map<Integer, SortedSet<Book>> matchesMap = new HashMap<Integer, SortedSet<Book>>();
        Pattern queryString = Pattern.compile(query);
        List<Book> bookList = new ArrayList<Book>();
        
        //obtain a set of books
        Set<Book> books = new HashSet<Book>();
        for(BookCopy copy : inLibrary){
            books.add(copy.getBook());
        }
        for(BookCopy copy : checkedOut){
            books.add(copy.getBook());
        }
        
        //determine level of match
        int max_matches = 0;
        
        //construct matchesMap
        for(Book book : books){
            //start with 0 mathes
            int matches = 0;
            
            //get # of matches
            Matcher bookString = queryString.matcher(book.toString());
            while(bookString.find()){
                matches++;
            }
            
            if(matches > max_matches) max_matches = matches;
            
            if(matchesMap.get(matches) == null) matchesMap.put(matches, new TreeSet<Book>(new BookSetComp()));
            
            matchesMap.get(matches).add(book);
        }
        
        //process matchesMap into bookList to return
        
        for( int i=max_matches; i >=1; i--) {
            bookList.addAll(matchesMap.get(i));
        }
        
        return bookList;
       
    }
    
    @Override
    public void lose(BookCopy copy) {
        if(this.isAvailable(copy)) inLibrary.remove(copy);
        else checkedOut.remove(copy);
        checkRep();
    }

    // uncomment the following methods if you need to implement equals and hashCode,
    // or delete them if you don't
    // @Override
    // public boolean equals(Object that) {
    //     throw new RuntimeException("not implemented yet");
    // }
    // 
    // @Override
    // public int hashCode() {
    //     throw new RuntimeException("not implemented yet");
    // }
    

    /* Copyright (c) 2016 MIT 6.005 course staff, all rights reserved.
     * Redistribution of original or derived work requires explicit permission.
     * Don't post any of this code on the web or to a public Github repository.
     */
}
