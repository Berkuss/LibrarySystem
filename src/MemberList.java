import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class MemberList implements Serializable {
    private ArrayList<Member> Members;
    private static MemberList memberList;

    private MemberList() {
        Members =  new ArrayList<>();
    }
    public static MemberList getInstance(){
        if(memberList == null){
           return memberList=new MemberList();
        }
        return memberList;
    }
    public Iterator getMembers(){
        return Members.iterator();
    }
    public Member search(String memberID){
        for (Member temp : Members){
            if(temp.getMemberID().equals(memberID)){
                return temp;
            }
        }
        return null;
    }
    public boolean insertMember(Member m1){
        try{
            Members.add(m1);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

}
