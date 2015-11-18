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
import java.sql.Time;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;


/**
 * Created by Moein on 11/18/15.
 */
public class Server extends Thread {

    private ServerSocket serverSocket;
    public static ArrayList<Deposit> deposits;
    public static Long port;
    public static String logPath;
    public static Logger logger = Logger.getLogger("ServerLog");


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
        deposits = new ArrayList<Deposit>();
        port = null;
        logPath = "";
        try {

            Object obj = parser.parse(new FileReader("./src/main/java/server/core.json"));

            JSONObject jsonObject = (JSONObject) obj;

            port = (Long) jsonObject.get("port");
            logPath = (String) jsonObject.get("outLog");
            FileHandler fh = new FileHandler(logPath);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("server started");
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
            System.out.println("فرمت فایل ورودی صحیح نمی باشد.");
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
                syncDeposits();
            }else{
                System.out.println("ورودی صحیح نیست.");
            }
        }
    }

    private static void syncDeposits() {
        JSONObject toFile = new JSONObject();
        JSONArray jsonDeposits = new JSONArray();

        for (Deposit dep:
             deposits) {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("customer",dep.getCustomer());
            jsonObject.put("id",dep.getId().toString());
            jsonObject.put("initialBalance",Utils.encodeAmount(dep.getBalance()));
            jsonObject.put("upperBound",Utils.encodeAmount(dep.getBound()));
            jsonDeposits.add(jsonObject);
        }
        toFile.put("port",port);
        toFile.put("deposits",jsonDeposits);
        toFile.put("outLog" , logPath);


        FileWriter fw = null;
        try {
            fw = new FileWriter("./src/main/java/server/core.json");
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(toFile.toString());
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("syncing has been finished");
    }
}
