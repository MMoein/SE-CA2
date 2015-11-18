package server;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;


/**
 * Created by Moein on 11/18/15.
 */
public class Server extends Thread {

    private ServerSocket serverSocket;

    public Server(int port) throws IOException
    {
        serverSocket = new ServerSocket(port);
    }

    public void run()
    {
        while(true)
        {
            try
            {
                System.out.println("Waiting for client on port " +
                        serverSocket.getLocalPort() + "...");
                Socket server = serverSocket.accept();
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
                break;
            }catch(IOException e)
            {
                e.printStackTrace();
                break;
            }
        }
    }

    public static void main(String args[]){
        JSONParser parser = new JSONParser();
        Long port = null;
        try {

            Object obj = parser.parse(new FileReader("/Users/Moein/IdeaProjects/SE-CA2/src/main/java/server/core.json"));

            JSONObject jsonObject = (JSONObject) obj;

            port = (Long) jsonObject.get("port");
            System.out.println(port);
            JSONArray deposits = (JSONArray) jsonObject.get("deposits");
            for (Object deposit:
                 deposits) {
                JSONObject dep = (JSONObject)deposit;
                System.out.println(dep.get("customer"));
            }
//            String name = (String) jsonObject.get("name");
//            System.out.println(name);
//
//            long age = (Long) jsonObject.get("age");
//            System.out.println(age);
//
//            // loop array
//            JSONArray msg = (JSONArray) jsonObject.get("messages");
//            Iterator<String> iterator = msg.iterator();
//            while (iterator.hasNext()) {
//                System.out.println(iterator.next());
//            }
//            while (true);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        try
        {
            Thread t = new Server(port.intValue());
            t.start();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
