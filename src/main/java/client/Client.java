package client;

import deposit.Deposit;
import deposit.Transaction;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.*;
import java.net.ConnectException;
import java.net.Socket;
import java.util.List;

/**
 * Created by Moein on 11/18/15.
 */
public class Client {
    public static void main(String args[]){
        /* TODO
            save response
         */
        String serverName = "";
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        int port = -1;
        try
        {

            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(new File("/Users/Moein/IdeaProjects/SE-CA2/src/main/java/client/terminal.xml"), handler);
            serverName = handler.getServerIP();
            port = handler.getPort();
            System.out.println(handler.getType());
            System.out.println(handler.getServerIP());
//            List<Deposit> depositList = handler.getDepositList();
            for (Transaction t:handler.getTransactionList()
                 ) {
                System.out.println(t.getId());
            }

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
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
