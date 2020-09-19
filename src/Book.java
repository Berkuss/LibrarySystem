import java.io.Serializable;
import java.util.*;

public class Book implements Serializable {
    private String title;
    private String author;
    private String id;
    private Member borrowdBy;
    private List<Hold> holds;
    private Calendar dueDate;

    public Book(String title, String author, String id) {
        this.title = title;
        this.author = author;
        this.id = id;
        holds =  new LinkedList<>();
    }

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public String getId() {
        return id;
    }

    public Member getBorrower() {
        return borrowdBy;
    }

    public Iterator<Hold> getHolds() {
        return holds.iterator();
    }

    public Calendar getDueDate() {
        return dueDate;
    }

    public boolean issue(Member member){
        this.borrowdBy= member;
        dueDate =  new GregorianCalendar();
        dueDate.setTimeInMillis(System.currentTimeMillis());
        dueDate.add(Calendar.MONTH,1);
        return true;
    }

    public Member returnBook() {
        Member member =  this.borrowdBy;
        this.borrowdBy =  null;
        return member;
    }

    public boolean hasHold() {
        return !(this.holds.isEmpty());
    }

    public void placeHold(Hold hold) {
        this.holds.add(hold);
    }

    public Hold getNextHold() {
        Hold hold ;
        for(ListIterator<Hold> iterator = holds.listIterator();iterator.hasNext();){
            hold =  iterator.next();
            iterator.remove();
            if(hold.isValid()){
                return hold;
            }
        }
        return null;
    }
    public boolean removeHold(String memberID){
        boolean removed = false;
        String ID;
        Hold hold;
        for(ListIterator<Hold> iterator = holds.listIterator(); iterator.hasNext();){
            hold = iterator.next();
            ID = hold.getMember().getMemberID();
            if(ID.equals(memberID)){
                removed =  true;
                iterator.remove();
            }
        }
        return removed;
    }
    public boolean renew(Member member) {
        if (hasHold()) {
            return false;
        }
        if ((member.getMemberID()).equals(borrowdBy.getMemberID())) {
            return (issue(member));
        }
        return false;
    }

    @Override
    public String toString() {
        return "Book{" +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", id='" + id + '\'' +
                ", borrowdBy=" + borrowdBy +
                '}';
    }
}
