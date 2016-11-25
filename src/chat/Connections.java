package chat;
import java.net.*;
import java.util.concurrent.Semaphore;
import java.io.*;
 
public class Connections {
 
    private ServerSocket server;
    private Socket clientSocket;

 
    private int maxClients;
    private ClientHandler[] clientList;
    boolean value;
    boolean value1;
 
    public Connections(int maxClients) {
        clientList = new ClientHandler[maxClients];
        this.maxClients = maxClients;
 
    }
 
    public void connect() throws InterruptedException {
 
        try {
            server = new ServerSocket(6789);
            value = true;
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
 
        while (true) {
            try {
            	int i = 0;
                GUIServer.showMessage("Waiting for connections...");
                clientSocket = server.accept();
                for (i = 0; i < maxClients; i++) {
                    if (clientList[i] == null) {
                        (clientList[i] = new ClientHandler(clientSocket, clientList)).start();
                        Thread.sleep(2000);
                        break;
                    }
                }
                value1 = true;
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }
    }
 
    public boolean closeSockets() {
        try {
            server.close();
            clientSocket.close();
            return true;
        } catch (IOException e) {
            GUIServer.showMessage(e.getMessage());
            return false;
        }
    }
 
}