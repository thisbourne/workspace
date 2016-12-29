package library;

import java.util.Comparator;

public class BookSetComp implements Comparator<Book>{
    public int compare(Book book1, Book book2){
        return book2.hashCode() - book1.hashCode();
    }

}
