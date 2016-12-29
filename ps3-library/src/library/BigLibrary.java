package library;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Map;
import java.util.HashMap;
import java.util.regex.Pattern;
import java.util.regex.Matcher;


/**
 * BigLibrary represents a large collection of books that might be held by a city or
 * university library system -- millions of books.
 * 
 * In particular, every operation needs to run faster than linear time (as a function of the number of books
 * in the library).
 */
public class BigLibrary implements Library {

    // rep
    private Map<Book, Set<BookCopy>> inLibrary;
    private Map< Book, Set<BookCopy>> checkedOut;
    
    // rep invariant:
    //    the intersection of each books copies in inLibrary and checkedOut
    //    is the empty set
    //
    // abstraction function:
    //    represents the collection of books inLibrary union checkedOut,
    //      where if a book copy is in inLibrary then it is available,
    //      and if a copy is in checkedOut then it is checked out
   // Safety from rep exposure: constructor doesn't have inputs,
    //    there are no producers, and mutators: buy, checkout, 
    //    checkin, and lose only allow access to a BookCopy, which is
    //    not the rep and which is safe from exposure itself.
    
    public BigLibrary() {
        this.inLibrary = new HashMap<Book, Set<BookCopy>>();
        this.checkedOut = new HashMap<Book, Set<BookCopy>>();
    }
    
    // assert the rep invariant
    private void checkRep() {
        Set<Book> books = inLibrary.keySet();
        for(Book book :books) {
            Set<BookCopy> intersection = new HashSet<BookCopy>(inLibrary.get(book));
            if(checkedOut.containsKey(book)){
                intersection.retainAll(checkedOut.get(book));
                assert(intersection.isEmpty());
            }
        }
    }

    @Override
    public BookCopy buy(Book book) {
        BookCopy newCopy = new BookCopy(book);
        if(inLibrary.containsKey(book)){
            Set<BookCopy> bookCopies = inLibrary.get(book);
            bookCopies.add(newCopy);
            inLibrary.put(book, bookCopies);
        }
        else{
            Set<BookCopy> bookCopies = new HashSet<BookCopy>();
            bookCopies.add(newCopy);
            inLibrary.put(book, bookCopies);
        }
        checkRep();
        return newCopy;
    }
    
    @Override
    public void checkout(BookCopy copy) {
        if(this.isAvailable(copy)) {
            Set<BookCopy> bookCopies = inLibrary.get(copy.getBook());
            bookCopies.remove(copy);
            inLibrary.put(copy.getBook(), bookCopies);
            if(checkedOut.containsKey(copy.getBook())) {
                Set<BookCopy> bookCopies2 = checkedOut.get(copy.getBook());
                bookCopies2.add(copy);
                checkedOut.put(copy.getBook(), bookCopies2);
            }
            else {
                Set<BookCopy> bookCopies2 = new HashSet<BookCopy>();
                bookCopies2.add(copy);
                checkedOut.put(copy.getBook(), bookCopies2);
            }
            checkRep();
        }
    }
    
    @Override
    public void checkin(BookCopy copy) {
        if(!this.isAvailable(copy)) {
            Set<BookCopy> bookCopies = checkedOut.get(copy.getBook());
            bookCopies.remove(copy);
            checkedOut.put(copy.getBook(), bookCopies);
            Set<BookCopy> bookCopies2 = inLibrary.get(copy.getBook());
            bookCopies2.add(copy);
            inLibrary.put(copy.getBook(), bookCopies2);
            checkRep();
        }
    }
    
    @Override
    public Set<BookCopy> allCopies(Book book) {
        Set<BookCopy> bookCopies = new HashSet<BookCopy>();
        if(inLibrary.containsKey(book)) bookCopies.addAll(inLibrary.get(book));
        if(checkedOut.containsKey(book)) bookCopies.addAll(checkedOut.get(book));
        return bookCopies;
    }

    @Override
    public Set<BookCopy> availableCopies(Book book) {
        Set<BookCopy> bookCopies = new HashSet<BookCopy>();
        if(inLibrary.containsKey(book)) bookCopies.addAll(inLibrary.get(book));
        return bookCopies;
    }
    
    @Override
    public boolean isAvailable(BookCopy copy) {
        if (inLibrary.containsKey(copy.getBook())) return inLibrary.get(copy.getBook()).contains(copy);
        else return false;
    }
    
    @Override
    public List<Book> find(String query) {
        List<Book> bookList = new ArrayList<Book>();
        Pattern queryString = Pattern.compile(query);
        Map<Integer, SortedSet<Book>> matchesMap = new HashMap<Integer, SortedSet<Book>>();
        
        //obtain a set of books
        Set<Book> books = new HashSet<Book>();
        books.addAll(inLibrary.keySet());
        books.addAll(checkedOut.keySet());
        
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
        int numberOfCopies = 0;
        Set<BookCopy> bookCopies = inLibrary.get(copy.getBook());
        if(bookCopies.contains(copy)){
            bookCopies.remove(copy);
            inLibrary.put(copy.getBook(), bookCopies);
        }
        else {
            bookCopies = checkedOut.get(copy.getBook());
            bookCopies.remove(copy);
            checkedOut.put(copy.getBook(), bookCopies);
        }
        
        if(checkedOut.get(copy.getBook()) != null)
            numberOfCopies += checkedOut.get(copy.getBook()).size();
        if(inLibrary.get(copy.getBook()) != null )
            numberOfCopies += inLibrary.get(copy.getBook()).size();
        if(numberOfCopies == 0) {
            inLibrary.remove(copy.getBook());
            checkedOut.remove(copy.getBook());
        }
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
