import java.io.Serializable;
import java.util.*;

public class Member implements Serializable {

    private String name;
    private String address;
    private String phone;
    private List<Book> booksBorrowed;
    private List<Hold> booksOnHold;
    private List<Transaction> transaction;
    private final String memberID;

    public Member(String name, String address, String phone) {
        this.name = name;
        this.address = address;
        this.phone = phone;
        booksOnHold = new LinkedList<>();
        transaction =  new LinkedList<>();

        memberID = Integer.toString(MemberIDServer.getInstance().getIDcounter());
        booksBorrowed = new LinkedList<>();
    }

    public String getMemberID() {
        return memberID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Iterator<Hold> getBooksOnHold() {
        return booksOnHold.iterator();
    }

    public Iterator<Transaction> getTransaction(Calendar date) {
        List<Transaction> result =  new LinkedList<>();
        for (Transaction transaction : transaction) {
            if (transaction.onDate(date)) {
                result.add(transaction);
            }
        }
        return result.iterator();
    }

    public boolean issue(Book book){
        if(booksBorrowed.add(book)){
            transaction.add(new Transaction(book.getTitle(),"Book Issue"));
            return true;
        }
        return false;
    }

    public boolean returnBook(Book book) {
        if(this.booksBorrowed.remove(book)){
            transaction.add(new Transaction(book.getTitle(),"Book return"));
            return true;
        }
        return false;
    }

    public void placeHold(Hold hold) {
        this.booksOnHold.add(hold);
    }

    public boolean removeHold(String bookID) {
        boolean removed = false;
        Hold hold;
        String id;
        for(ListIterator<Hold> iterator =booksOnHold.listIterator(); iterator.hasNext();){

            hold =  iterator.next();
            id =  hold.getBook().getId();
            if(id.equals(bookID)){
                transaction.add(new Transaction(hold.getBook().getTitle(),"Hold removed"));
                removed = true;
                iterator.remove();
            }
        }
        return removed;
    }
    public boolean renew(Book book) {
        for (Book aBook : booksBorrowed) {
            String id = aBook.getId();
            if (id.equals(book.getId())) {
                transaction.add(new Transaction("Book renewed ", book.getTitle()));
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "Member{" +
                "name='" + name + '\'' +
                ", address='" + address + '\'' +
                ", phone='" + phone + '\'' +
                ", booksBorrowed=" + booksBorrowed +
                ", transaction=" + transaction +
                ", memberID='" + memberID + '\'' +
                '}';
    }
}
