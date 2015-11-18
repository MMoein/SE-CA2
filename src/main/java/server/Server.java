package server;
import deposit.Deposit;
import deposit.Utils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;


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
                Thread t = new ClientHandler(server);
                t.start();
//                System.out.println("Just connected to "
//                        + server.getRemoteSocketAddress());
//                DataInputStream in =
//                        new DataInputStream(server.getInputStream());
//                System.out.println(in.readUTF());
//                DataOutputStream out =
//                        new DataOutputStream(server.getOutputStream());
//                out.writeUTF("Thank you for connecting to "
//                        + server.getLocalSocketAddress() + "\nGoodbye!");
//                server.close();
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
        ArrayList <Deposit> deposits = new ArrayList<Deposit>();
        Long port = null;
        String logPath = "";
        try {

            Object obj = parser.parse(new FileReader("/Users/Moein/IdeaProjects/SE-CA2/src/main/java/server/core.json"));

            JSONObject jsonObject = (JSONObject) obj;

            port = (Long) jsonObject.get("port");
            logPath = (String) jsonObject.get("outLog");
            JSONArray JSONDeposits = (JSONArray) jsonObject.get("deposits");
            for (Object deposit:
                 JSONDeposits) {
                JSONObject dep = (JSONObject)deposit;
                int id = Integer.parseInt((String) dep.get("id"));
                int initialBalance = Utils.decodeAmount((String) dep.get("initialBalance"));
                int upperBound = Utils.decodeAmount((String) dep.get("upperBound"));
                deposits.add(new Deposit(id , initialBalance , upperBound , (String)dep.get("customer")));
            }
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
        Scanner input = new Scanner(System.in);
        while (input.hasNext()) {
            String command = input.next();
            if(command.equals("sync")){

            }else{
                System.out.println("ورودی صحیح نیست.");
            }
        }
    }
}
