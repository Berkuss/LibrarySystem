import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;

public class MemberIDServer implements Serializable {
    private int IDcounter;
    private static MemberIDServer server;

    private MemberIDServer(){
        IDcounter = 0;
    }
    public static MemberIDServer getInstance(){
        if(server == null){
            server = new MemberIDServer();
        }
        return server;
    }
    public int getIDcounter(){
        IDcounter++;
        return  IDcounter;
    }

    public static void retrieve(ObjectInputStream input) {
        try {
            server = (MemberIDServer) input.readObject();
        } catch(Exception ioe) {
            ioe.printStackTrace();
        }
    }
    private void writeObject(java.io.ObjectOutputStream output) throws IOException {
        try {
            output.defaultWriteObject();
            output.writeObject(server);
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
    private void readObject(java.io.ObjectInputStream input) throws IOException, ClassNotFoundException {
        try {
            input.defaultReadObject();
            if (server == null) {
                server = (MemberIDServer) input.readObject();
            } else {
                input.readObject();
            }
        } catch(IOException ioe) {
            ioe.printStackTrace();
        }
    }
}
