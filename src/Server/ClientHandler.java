package Server;

import java.net.*;
import java.io.*;

public class ClientHandler extends Thread {

	private Socket client;
	private ClientHandler[] clientThreads;
	private int maxClients;
	public boolean passed;
	private ObjectInputStream inStream;
	private ObjectOutputStream outStream;
	
	/**
	 * Empty constructor 
	 */
	public ClientHandler() {

	}
	
	/**
	 * Constructor for the different client threads.
	 * @param client - the socket that was setup in connections. 
	 * @param clientThreads - array of clients currently connected.
	 * @throws IOException
	 */
	public ClientHandler(Socket client, ClientHandler[] clientThreads) throws IOException {
		this.client = client;
		this.clientThreads = clientThreads;
		maxClients = clientThreads.length;
	}

	/**
	 * Starts when .start() is called in the Connection class.
	 * In here is where the server does most of the work, 
	 * handling incoming messages and sending them out again.
	 */
	@Override
	public void run() {
		String welcomeNewUser = "*** New user connected!\n*** Welcome!\n";
		passed = false;
		try {
			inStream = new ObjectInputStream(client.getInputStream());
			outStream = new ObjectOutputStream(client.getOutputStream());
			outStream.flush();
			for (int i = 0; i < maxClients; i++) {
				if (clientThreads[i] != null && clientThreads[i] != this) {
					clientThreads[i].outStream.writeObject(welcomeNewUser);
					ServerMain.showMessage(welcomeNewUser);
				}
			}
			while (true) {
				
					String message = (String) inStream.readObject();
					writeToClients(message);
					ServerMain.showMessage(message);
					passed = true;
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	/**
	 * Sends the message to all currently connected clients.
	 * @param message - Spreads the message!
	 * @throws IOException
	 */
	public void writeToClients(String message) throws IOException{
		for (int i = 0; i < maxClients; i++) {
			if (clientThreads[i] != null) {
				clientThreads[i].outStream.writeObject(message);
			}
		}
	}
	/**
	 * Called by closeCrap in ServerMain for shutting down the streams.
	 */
	public void closeStreams() {
		try {
			inStream.close();
			outStream.close();
		} catch (IOException e) {
			ServerMain.showMessage(e.getMessage());
		}
	}

}