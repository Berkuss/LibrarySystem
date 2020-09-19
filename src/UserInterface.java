import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserInterface {
    private static UserInterface userInterface;
    private static Library library;
    public static UserInterface getInstance(){
        if (userInterface == null){
            return userInterface= new UserInterface();
        }
        return userInterface;
    }

    private UserInterface() {
        File file = new File("LibraryData.txt");
        if (file.exists() && file.canRead()){
            if(YesorNO("Saved data exists. Use it ?")){
                retrive();
            }
        }
        library = Library.getInstance();
    }


    public void retrive(){
        Library tempLibrary = Library.retrive();
        if (tempLibrary != null){
            System.out.println(" The library has been successfully retrieved from the file LibraryData \n" );
            library = tempLibrary;
        }else {
            System.out.println("File doesnt exist; creating new library" );
            library = Library.getInstance();
        }
    }
    public boolean YesorNO(String str){
        String  answer = getDatafromUser(str);
        while (true){
            if(answer.equals("yes")){
                return true;
            }
            else if(answer.equals("no")){
                return false;
            }
            else {
                answer = getDatafromUser("This is an invalid answer.Please Answer again (Yes / NO)");
            }
        }
    }
    private void save(){
        if (library.save()){
            System.out.println("The library has been successfully saved");
        }
        else {
            System.out.println("There has been an error in saving");
        }
    }

    public String getDatafromUser(String str){
        Scanner scanner = new Scanner(System.in);
        System.out.println(str);
        String answer ;
        while (true){
            try {
                answer = scanner.nextLine();
                answer = answer.toLowerCase();
                return answer;
            }
            catch (Exception e){
                e.printStackTrace();
                System.exit(0);
            }
        }
    }

    public void help(){
        System.out.println("0" + " to Exit\n");
        System.out.println("1" + " to add a member");
        System.out.println("2" + " to  add books");
        System.out.println("3" + " to  issue books to a  member");
        System.out.println("4" + " to  return books ");
        System.out.println("5"+ " to  renew books ");
        System.out.println("6" + " to  remove books");
        System.out.println("7" + " to  place a hold on a book");
        System.out.println("8" + " to  remove a hold on a book");
        System.out.println("9" + " to  process holds");
        System.out.println("10" + " to  print members");
        System.out.println("11" + " to  print books");
        System.out.println("12" + " for help");
        System.out.println("13"+"  to Remove all invalid holds");
        System.out.println("14"+ " Print Transactions");

    }
    public int getOrder(){
        while (true){
            try{
                int value = Integer.parseInt(getDatafromUser("Enter Order :  "));
                if (value >=0 && value <=14 ){
                    return value;
                }
            }
            catch (NumberFormatException ex){
                System.out.println("Enter number please ...");
            }
        }
    }
    public void process(){
        help();
        int opearation = -1;
        while(opearation != 0){
            opearation = getOrder();
            switch (opearation){
                case 1: addMember();
                        break;
                case 2: addBook();
                        break;
                case 3: issueBook();
                        break;
                case 4: returnBook();
                        break;
                case 6: removeBook();
                        break;
                case 7: placeHold();
                        break;
                case 8: removeHold();
                        break;
                case 9: processHold();
                        break;
                case 10:printMembers();
                        break;
                case 11:printBooks();
                        break;
                case 12:help();
                        break;
                case 13:removeInvalidHolds();
                        break;
                case 14:getTranscations();
                        break;
            }
        }
        save();
    }

    private void getTranscations() {
        Iterator<Transaction> result;
        String memberID = getDatafromUser("Enter member id");
        Calendar date  = getDate("Please enter the date for which you want " +"records as mm/dd/yy");
        result = library.getTransactions(memberID,date);
        if (result == null) {System.out.println("Invalid Member ID");}
        else {while(result.hasNext()) {
            Transaction transaction = result.next();
            System.out.println(transaction.getType() + "   "   +transaction.getBookTitle() + "\n");
        }
        System.out.println("\n  There are no more transactions \n" );
        }
    }

    public Calendar getDate(String prompt) {
        do {
            try {
                Calendar date = new GregorianCalendar();
                String item = getDatafromUser(prompt);
                DateFormat df = SimpleDateFormat.getDateInstance(DateFormat.SHORT);
                date.setTime(df.parse(item));
                return date;
            } catch (Exception fe) {
                System.out.println("Please input a date as mm.dd.yy");
            }
        } while (true);
    }

    private void removeInvalidHolds() {
        int control = library.removeUnvalidHolds();
        if (control== 0 ){
            System.out.println("MemberID invalid.");
        }
        else if(control ==1 ){
            System.out.println("BookID invalid");
        }else if(control == 2 || control ==3){
            System.out.println("The operation was failed.");
        }else{
            System.out.println("The operation is successful.");
        }

    }

    private void printBooks() {
        for(Iterator<Book> iterator = library.getBooks();iterator.hasNext();){
            System.out.println(iterator.next());
        }
    }

    private void printMembers() {
        for (Iterator<Member>  iterator1= library.getMembers();iterator1.hasNext();){
            System.out.println(iterator1.next());
        }
    }

    private void processHold() {
        String bookID ;
        do{
            bookID = getDatafromUser("Enter Book ID");
            Member m1 = library.processHold(bookID);
            if (m1 != null){
                System.out.println("The operation was succsessful.");
            }else {
                System.out.println("The operation has failed.");
            }
        }while (YesorNO("Again ?"));
    }

    public void addMember(){
        do{
            String name = getDatafromUser("Enter Name : ");
            String address = getDatafromUser("Enter  Adresss:");
            String phone = getDatafromUser("Enter Phone Number:");
            Member m1 =  library.addMember(name,address,phone);
            if( m1 != null){
                System.out.println("Membership is Successfull");
                System.out.println(m1);
            }
            else {
                System.out.println("Something goes wrong. Exiting...");
                System.exit(0);
            }
        }
        while (YesorNO("Add more ? "));
    }
    public void addBook(){
        do{
            String title = getDatafromUser("Enter Book's Title :");
            String author = getDatafromUser("Enter Book's Author");
            String id = getDatafromUser("Enter Book's ID");
            Book b1 = library.addBook(title,author,id);
            if (b1 == null) {
                System.out.println("Something goes wrong. Exiting...");
                System.exit(0);
            } else {
                System.out.println("Book added succesfully.");
                System.out.println(b1);
            }
        }
        while (YesorNO("Any more  ??"));
    }
    public void issueBook(){
        String memberID = getDatafromUser("Enter Member ID:");
        Member member = library.searchMembership(memberID);
        if (member == null){
            System.out.println("Invalid ID .");
        }
        else {
            do{
                String bookID = getDatafromUser("EnterBook ID");
                Book b1 = library.issueBook(bookID,memberID);
                if (b1 != null){
                    System.out.println("Book issued.");
                }
            }
            while (YesorNO("Any more Book ?"));
        }
    }
    public void returnBook(){
        do{
            String bookID = getDatafromUser("Enter Book ID");
            int control = library.returnBook(bookID);
            if(control == 1){
                System.out.println("Book ID invalid.");
            }
            else if (control == 2){
                System.out.println("The  operation  was  successful  and  there  is  a  hold  on  the  book");
            }else{
                System.out.println("The operation was successful.");
            }
        }
        while(YesorNO("Again ? "));
    }
    public void removeBook(){
        String bookID;
        int control;
        do{
            bookID = getDatafromUser("Enter Book's ID ");
            control = library.removeBook(bookID);
            if(control == 1){
                System.out.println("ID invalid");
            }
            else if(control ==3){
                System.out.println("The operation was failed.");
            }
            else if( control ==2){
                System.out.println("The operation was successful.");
            }else {
                System.out.println("Book can not remove.");
            }

        }while (YesorNO("Again ?"));
    }
    public void placeHold(){
        String userID = getDatafromUser("Enter member ID");
        String bookID = getDatafromUser("Enter Book ID");
        int duracition = Integer.parseInt(getDatafromUser("Enter duration:"));
        int control = library.placeHold(userID,bookID,duracition);
        if (control == 1){
            System.out.println("Member ID invalid.");
        }
        else if(control == 2){
            System.out.println("Book ID invalid.");
        }
        else if(control ==3){
            System.out.println("Book can issue");
        }
        else {
            System.out.println("The operation is successful.");
        }
    }
    public void removeHold(){
        String memberID = getDatafromUser("Enter Member ID:");
        String bookID =  getDatafromUser("Enter Book ID");
        int control = library.removeHold(memberID,bookID);
        if(control == 0 ){
            System.out.println("MemberID invalid.");
        }
        else if(control ==1 ){
            System.out.println("BookID invalid");
        }else if(control == 2 || control ==3){
            System.out.println("The operation was failed.");
        }else{
            System.out.println("The operation is successful.");
        }
    }

}
