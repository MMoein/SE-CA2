package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketTimeoutException;

/**
 * Created by Moein on 11/18/15.
 */
public class ClientHandler extends Thread {
    Socket server;
    public void run(){
        try {
            System.out.println("Just connected to "
                    + server.getRemoteSocketAddress());
            DataInputStream in =
                    new DataInputStream(server.getInputStream());
            System.out.println(in.readUTF());
            DataOutputStream out =
                    new DataOutputStream(server.getOutputStream());
            out.writeUTF("Thank you for connecting to "
                    + server.getLocalSocketAddress() + "\nGoodbye!");
            server.close();
        }catch(SocketTimeoutException s)
        {
            System.out.println("Socket timed out!");
//            break;
        }catch(IOException e)
        {
            e.printStackTrace();
//            break;
        }
    }
    public ClientHandler(Socket server){
        this.server = server;
    }
}
