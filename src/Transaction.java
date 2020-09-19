import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class Transaction implements Serializable {
    private Calendar date;
    private String bookTitle;
    private String type;

    public Transaction(String bookTitle, String type) {
        this.date = new GregorianCalendar();
        this.date.setTimeInMillis(System.currentTimeMillis());
        this.bookTitle = bookTitle;
        this.type = type;
    }
    public boolean onDate(Calendar date) {
        return ((date.get(Calendar.YEAR) == this.date.get(Calendar.YEAR))
                &&(date.get(Calendar.MONTH) == this.date.get(Calendar.MONTH))
                &&(date.get(Calendar.DATE) == this.date.get(Calendar.DATE)));
    }

    public Calendar getDate() {
        return date;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getType() {
        return type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "date=" + date +
                ", bookTitle='" + bookTitle + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
