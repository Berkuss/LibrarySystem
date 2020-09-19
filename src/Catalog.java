import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Catalog implements Serializable {
    private List<Book> books;
    private static Catalog catalog;

    private Catalog() {
        this.books = new LinkedList<>();
    }

    public static Catalog getInstance(){
        if(catalog == null){
            return catalog = new Catalog();
        }
        return catalog;
    }

    public Book searchBook(String bookId){
        for (Book b1 : books){
            if (b1.getId().equals(bookId)){
                return b1;
            }
        }
        return null;
    }
    public Iterator<Book> getBooks(){
        return this.books.iterator();
    }
    public boolean insertBook(Book book){
        try {
            this.books.add(book);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }
    public boolean removeBook(Book book){
        if(this.books.remove(book)){
            return true;
        }
        return false;
    }

}
