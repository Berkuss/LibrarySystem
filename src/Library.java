import java.io.*;
import java.util.Calendar;
import java.util.Iterator;

public class Library implements Serializable{//Singleton
    private MemberList members;
    private Catalog books;
    private static Library library;
    // singleton olcak.

    private Library() {
        this.members = MemberList.getInstance();
        this.books = Catalog.getInstance();
    }

    public static Library getInstance(){
        if (library == null){
            return library =  new Library();
        }
        return library;
    }

    public Member addMember(String name, String address, String phone){
        Member m1 = new Member(name,address,phone);
        this.members.insertMember(m1);
        return m1;
    }
    public Book addBook(String title, String author, String id){
        Book b1 = new Book(title,author,id);
        this.books.insertBook(b1);
        return b1;
    }
    public Member searchMembership(String memberId){
        return this.members.search(memberId);
    }
    public Book issueBook(String bookId , String memberId){
        Book book = this.books.searchBook(bookId);
        if (book == null){ return null; }

        Member member = searchMembership(memberId);
        if (member == null){ return null; }

        if (book.issue(member) && member.issue(book)){
            return book;
        }
        else{ return null; }
    }
    public int returnBook(String bookID) {
        Book book = this.books.searchBook(bookID);
        if (book == null) {
            return 1;//BookID invalid.
        }
        Member member = book.returnBook();
        boolean hasHold = book.hasHold();
        if (hasHold && member.returnBook(book)) {
            return 2;// işlem başarılı ve kitap icin hold var.
        }
        if (!hasHold && member.returnBook(book)) {
            return 3; //işlem başarılı.Kitap için hold yok. }

        }
        return 4; // işlem başarısız.
    }
    public Iterator<Transaction> getTransactions(String memberID, Calendar date){
        Member member = members.search(memberID);
        if(member == null){
            return null;
        }
        return member.getTransaction(date);
    }
    public int removeBook(String bookID){
        Book book = books.searchBook(bookID);
        if(book == null){return  1;}//BookID invalid.
        if(!book.hasHold() && book.getBorrower()==null){
            if (books.removeBook(book)){
                return 2 ; //İşlem başarılı;
            }
            else{return 3 ;}//işlem başarısız.
        }
        return 4;// kitap silinemez.
    }
    public int placeHold(String memberID, String bookID, int duration){
        Member member = searchMembership(memberID);
        if (member == null){return 1;} // MemberID invalid}
        Book book =  books.searchBook(bookID);
        if (book== null){return 2;}//BookID invalid
        if(book.getBorrower() == null){ return 3; }//Kitap issue edilebilir.
        else{
            Hold hold =  new Hold(member,book,duration);
            member.placeHold(hold);
            book.placeHold(hold);
            return 4; //işlem başarlı
        }
    }
    public Member processHold(String bookID){
        Book book = books.searchBook(bookID);
        if(book == null){return null;}
        Hold hold =  book.getNextHold();
        if(hold ==null){return null;}
        Member member = hold.getMember();
        if(book.removeHold(member.getMemberID()) && member.removeHold(book.getId())){
            return member;
        }
        return null;
    }
    public int removeHold(String memberID, String bookID){
        Member member = searchMembership(memberID);
        if (member == null){return 0;}//MemberID invalid
        Book book =  books.searchBook(bookID);
        if (book == null){return 1;}//BookID invalid
        if(!member.removeHold(bookID) && book.removeHold(memberID)){
            return 2;//islem basarısız Memberdan hold silinmedi.
        }
        else if(member.removeHold(bookID) && !book.removeHold(memberID)){
            return 3;//islem basarısız book dan hold silinemedi
        }
        return 4;//islem basarılı
    }
    public Book renewBook(String bookId, String memberId) {
        Book book = books.searchBook(bookId);
        if (book == null) {
            return(null);
        }
        Member member = searchMembership(memberId);
        if (member == null) {
            return(null);
        }
        if ((book.renew(member) && member.renew(book))) {
            return(book);
        }
        return(null);
    }
    public  boolean save(){
        try {
            FileOutputStream file =  new FileOutputStream("LibraryData.txt");
            ObjectOutputStream output = new ObjectOutputStream(file);
            output.writeObject(library);
            output.writeObject(MemberIDServer.getInstance());
        }
        catch (Exception ex){
            return false;
        }
        return true;
    }
    public static Library retrive(){
        try {
            FileInputStream file = new FileInputStream("LibraryData.txt");
            ObjectInputStream input = new ObjectInputStream(file);
             library = (Library)input.readObject();
             MemberIDServer.retrieve(input);
            return library;
        } catch(IOException | ClassNotFoundException ioe) {
            ioe.printStackTrace();
            return null;
        }
    }

    public Iterator<Member> getMembers() {
        return members.getMembers();
    }

    public Iterator<Book> getBooks() {
        return books.getBooks();
    }

    public int removeUnvalidHolds(){
        Member member;
        Hold hold;
        int st ;
        for (Iterator<Member> memberIterator = this.getMembers();memberIterator.hasNext();){
            member = memberIterator.next();
            for(Iterator<Hold> holdIterator=member.getBooksOnHold();holdIterator.hasNext();){
                hold = holdIterator.next();
                if(! hold.isValid()){
                    st = removeHold(member.getMemberID(),hold.getBook().getId());
                    if (st != 4){// islem basarasızsa
                        return st;
                    }
                }
            }
        }
        return 4;
    }
}
