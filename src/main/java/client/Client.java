package client;

import java.io.*;
import java.net.ConnectException;
import java.net.Socket;

/**
 * Created by Moein on 11/18/15.
 */
public class Client {
    public static void main(String args[]){
        String serverName = "localhost";
        int port = 8080;
        try
        {
            System.out.println("Connecting to " + serverName +
                    " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("Hello from "
                    + client.getLocalSocketAddress());
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
        } catch(ConnectException e){
            System.out.println("سرور پاسخگو نمی باشد.");
        } catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
