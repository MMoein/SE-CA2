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
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

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
        Logger logger = Logger.getLogger("ClientLog");

        try
        {
            SAXParser saxParser = saxParserFactory.newSAXParser();
            XMLHandler handler = new XMLHandler();
            saxParser.parse(new File("./src/main/java/client/terminal.xml"), handler);
            serverName = handler.getServerIP();
            port = handler.getPort();
            logFile = handler.getOutLog();
            FileHandler fh = new FileHandler(logFile);
            logger.addHandler(fh);
            SimpleFormatter formatter = new SimpleFormatter();
            fh.setFormatter(formatter);
            logger.info("client started");
            System.out.println("Connecting to " + serverName + " on port " + port);
            Socket client = new Socket(serverName, port);
            System.out.println("Just connected to " + client.getRemoteSocketAddress());
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            InputStream inFromServer = client.getInputStream();
            DataInputStream in = new DataInputStream(inFromServer);
            for(Transaction t:handler.getTransactionList()) {
                out.writeUTF(t.getId() + " " +t.getDeposit() + " " + t.getType() + " " + t.getAmount());
                String result = in.readUTF();
                System.out.println("Server says " + result);
                logger.info("transaction " + t.getId() + " " + result);

                response = response.concat("\t<transaction id=\""+t.getId()+"\" result=\"" + result + "\" />\n");
            }
            out.writeUTF("END");
            System.out.println("Server says " + in.readUTF());
            client.close();
            makeResponse(response);
        } catch(ConnectException e){
            System.out.println("سرور پاسخگو نمی باشد.");
        } catch(IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            System.out.println("فرمت فایل ورودی صحیح نمی باشد.");
        } catch (SAXException e) {
            System.out.println("فرمت فایل ورودی صحیح نمی باشد.");
        }
    }


    private static void makeResponse(String response) throws IOException {
        File file = new File("response.xml");
        if (!file.exists()) {
            file.createNewFile();
        }
        FileWriter fw = new FileWriter(file.getAbsoluteFile());
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write("<response>\n" + response + "</response>");
        bw.close();
    }
}
