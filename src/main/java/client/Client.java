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
import java.util.logging.Logger;

/**
 * Created by Moein on 11/18/15.
 */
public class Client {
    public static void main(String args[]){

        String logFile = "";
        String response = "";
        String serverName = "";
        SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
        int port = -1;


        try
        {
            /*
                parsing xml file
             */
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(new File("./src/main/java/client/terminal.xml"), handler);
            serverName = handler.getServerIP();
            port = handler.getPort();
            logFile = handler.getOutLog();


            // if file doesnt exists, then create it

            File file = new File(logFile);

            if (!file.exists()) {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            /*
                connecting to server.
             */
            System.out.println("Connecting to " + serverName +
                    " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to "
                    + client.getRemoteSocketAddress());


            /*
                send transactions
             */
            for(Transaction t:handler.getTransactionList()) {
                OutputStream outToServer = client.getOutputStream();
                DataOutputStream out = new DataOutputStream(outToServer);
//                out.writeUTF("Hello from "
//                        + client.getLocalSocketAddress());
                out.writeUTF(t.getId() + " " +t.getDeposit() + " " + t.getType() + " " + t.getAmount());
                InputStream inFromServer = client.getInputStream();
                DataInputStream in =
                        new DataInputStream(inFromServer);
                String result = in.readUTF();
                System.out.println("Server says " + result);
                FileWriter fw = new FileWriter(file.getAbsoluteFile() , true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write("transaction " + t.getId() + " " + result + "\n");
                bw.close();

                response = response.concat("<transaction id=\""+t.getId()+"\" result=\"" + result + "\" />\n");
            }
            /*
            Disconnecting from server
             */
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeUTF("END");
            InputStream inFromServer = client.getInputStream();
            DataInputStream in =
                    new DataInputStream(inFromServer);
            System.out.println("Server says " + in.readUTF());
            client.close();
            file = new File("response.xml");

            // if file doesnt exists, then create it
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fw = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write("<response>\n" + response + "</response>");
            bw.close();
        } catch(ConnectException e){
            System.out.println("سرور پاسخگو نمی باشد.");
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
    }
}
