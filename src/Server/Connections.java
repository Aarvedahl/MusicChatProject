package Server;

import java.net.*;

import javax.swing.JOptionPane;
public class Connections {
 
    private ServerSocket server;
    private Socket clientSocket;
    public boolean value;
    public boolean value1;
    public boolean passed; // Not used for JUnit test!
    private int maxClients;
    private ClientHandler[] clientList;
    
    /**
     * Constructor, creates an array that holds the max number of clients set by the user.
     * @param maxClients
     */
    public Connections(int maxClients) {
        clientList = new ClientHandler[maxClients];
        this.maxClients = maxClients;
    }
    
    /**
     * Waiting for connection then placing all sockets in a list
     */
    public void connect() {
    	passed = false; 
        try {
            server = new ServerSocket(6789);
            value = true;                
        } catch (Exception e) {
            System.out.println(e.getMessage());
            value = false;
        }
        while (true) {
            try {
                ServerMain.showMessage("*** Waiting for connections...");                    
                Thread.sleep(5000);
                clientSocket = server.accept();
                passed = true;
                int i = 0;
                for (i = 0; i < maxClients; i++)
                    if (clientList[i] == null) {
                        (clientList[i] = new ClientHandler(clientSocket, clientList)).start();
                        break;
                    }  
                value1 = true;
                }                
                catch (Exception e) {
                System.out.println(e.getMessage());
                value1 = false;
            }
        }
   
    }
  
   /**
    * Close the sockets. 
    * @return true if sockets are closed.
    */
    public boolean closeSockets() {
        try {
            clientSocket.close();
            server.close();
            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
 
        }
 
    }
}