import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Hold implements Serializable {
    private Member member;
    private Book book;
    private Calendar date;

    public Hold(Member member, Book book, int duration) {
        this.member = member;
        this.book = book;
        this.date = new GregorianCalendar();
        this.date.setTimeInMillis(System.currentTimeMillis());
        this.date.add(Calendar.DATE,duration);
    }
    public boolean isValid(){
        return (System.currentTimeMillis() < date.getTimeInMillis());
    }

    public Member getMember() {
        return member;
    }

    public Book getBook() {
        return book;
    }

    public Calendar getDate() {
        return date;
    }
}
