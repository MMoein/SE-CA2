package client;

import deposit.Transaction;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Moein on 11/18/15.
 */
public class XMLHandler extends DefaultHandler {

    private String type = null;
    private int id = -1;
    private String serverIP = "";
    private int port = -1;
    private String outLog = "";
    private List<Transaction> transactionList = new ArrayList<Transaction>();

    public List<Transaction> getTransactionList() {
        return transactionList;
    }

    public String getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public int getPort() {
        return port;
    }

    public String getServerIP() {
        return serverIP;
    }

    public String getOutLog() {
        return outLog;
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes){
        if(qName.equalsIgnoreCase("terminal")) {
            type = attributes.getValue("type");
            id = Integer.parseInt(attributes.getValue("id"));
        }else if(qName.equalsIgnoreCase("server")){
            serverIP = attributes.getValue("ip");
            port = Integer.parseInt(attributes.getValue("port"));
        }else if(qName.equalsIgnoreCase("outLog")){
            outLog = attributes.getValue("path");
        }else if(qName.equalsIgnoreCase("transaction")){
            int id = Integer.parseInt(attributes.getValue("id"));
            String type = attributes.getValue("type");
            String amount = attributes.getValue("amount");
            String deposit = attributes.getValue("deposit");
            transactionList.add(new Transaction(id,type,deposit,amount));
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName){

    }

    @Override
    public void characters(char ch[], int start, int length){

    }
}
