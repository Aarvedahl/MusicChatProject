package chat;
import java.net.*;
import java.io.*;
 
public class ClientHandler extends Thread {
 
    private Socket client;
    private ClientHandler[] clientThreads;
    private int maxClients;
    private ObjectInputStream inStream;
    private ObjectOutputStream outStream;
 
    public ClientHandler(Socket client, ClientHandler[] clientThreads) {
        this.client = client;
        this.clientThreads = clientThreads;
        maxClients = clientThreads.length;
    }
 
    @Override
    public void run() {
        try {
        	int i;
            inStream = new ObjectInputStream(client.getInputStream());
            outStream = new ObjectOutputStream(client.getOutputStream());
            outStream.flush();
            for (i = 0; i < maxClients; i++) {
                if (clientThreads[i] != null && clientThreads[i] != this) {
                    clientThreads[i].outStream.writeObject("New user connected!");
                    GUIServer.showMessage("New user connected!");
                }
            }
            while (true) {
                String message = (String) inStream.readObject();
                for (i = 0; i < maxClients; i++) {
                    if (clientThreads[i] != null) {
                        clientThreads[i].outStream.writeObject(message);
                    }
                }
                GUIServer.showMessage(message);
            }
 
        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (ClassNotFoundException e) {
 
        }
    }
   
    public void closeStreams(){
        try{
        inStream.close();
        outStream.close();
        } catch(IOException e){
            GUIServer.showMessage(e.getMessage());
        }
    }

 
}