package server;

import deposit.Deposit;

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
            DataOutputStream out = new DataOutputStream(server.getOutputStream());
            DataInputStream in = new DataInputStream(server.getInputStream());
            System.out.println("Just connected to " + server.getRemoteSocketAddress());
            while(true) {
                String input = in.readUTF();
                if(input.equals("END")){
                    out.writeUTF("FINISH");
                    break;
                }
                String[] command = input.split(" ");
                boolean result = processCommand(command);
                if(result)
                    out.writeUTF("OK");
                else
                    out.writeUTF("ERR");
            }
            server.close();
        }catch(SocketTimeoutException s) {
            System.out.println("Socket timed out!");
        }catch(IOException e) {
            e.printStackTrace();
        }
    }

    private boolean processCommand(String[] command) {
        for (Deposit dep:
             Server.deposits) {
            if(dep.getId() == Integer.parseInt(command[1])){
                boolean result;
                if(command[2].equals("deposit")){
                    result = dep.deposit(Integer.parseInt(command[3]));
                }else{
                    result = dep.withdraw(Integer.parseInt(command[3]));
                }
                Server.logger.info(command.toString() + " done with result of " + result );
                return result;
            }
        }
        Server.logger.info("deposit id : " + command[1] + " Not found" );
        return false;
    }

    public ClientHandler(Socket server){
        this.server = server;
    }
}
