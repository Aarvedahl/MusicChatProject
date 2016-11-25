package chat;

import java.net.*;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.*;

public class Server extends JFrame{
	
	
private JTextField userText;
	private JTextArea chatWindow;
	private ObjectOutputStream output;
	private ObjectInputStream input;
	private ServerSocket server;
	private Socket connection;
	
	
	
	
	//ConstructorServerSocket
	public Server(){
		super("This Amazing Chatroom");
		userText = new JTextField();
		userText.setEditable(false); // Gör så att man måste ha en annan användare för att kunna sända ut meddelanden
		userText.addActionListener(
				new ActionListener(){
					public void actionPerformed(ActionEvent event){
						sendMessage(event.getActionCommand());
						userText.setText("");
					}
				}
			);
		add(userText, BorderLayout.NORTH);
		chatWindow = new JTextArea();
		add(new JScrollPane(chatWindow));
		setSize(600, 300);
		setVisible(true);
		}
		
		// set up and run the server
		public void startRunning(){
			try{
				server = new ServerSocket(6789,10);//Port number and queue length 
				while(true){
					try{
						//connect and have conversation
						waitForConnection();
						setupStreams();
						whileChatting();
						// run();
					}catch(EOFException eofException){
						showMessage("\n Server ended the connection! ");
					}
					finally{
						closeCrap();
					}
					
				}
				
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
	
		// Wait for connection, then display connection information
		private void waitForConnection() throws IOException{
			showMessage("Waiting for someone to connect..." + "\n");
			connection = server.accept();
			showMessage("Now connected to " +connection.getInetAddress().getHostName());
			
		}
		// get stream to send and receive data
		private void setupStreams() throws IOException{
			output = new ObjectOutputStream(connection.getOutputStream());
			output.flush();
			input = new ObjectInputStream(connection.getInputStream());
			showMessage("\nStreams are now setup! \n");
		}
		
		// during the chat conversation
		private void whileChatting() throws IOException{
			String message = "You are now connected!";
			showMessage(message);
			ableToType(true);
			do{
				try{
					message = (String) input.readObject(); //Read whatever message they send us
					showMessage("\n" + message);
				}catch(ClassNotFoundException classNotFoundException){
					showMessage("\n Input error, try to send something else ");
					//If the user sent something that was not found
				}
			}while(!message.equals("end"));
		}
		//Close streams and sockets once you are done chatting
		private void closeCrap(){
			showMessage("\nClosing Connections... \n");
			ableToType(false);
			try{
				output.close();
				input.close();
			//	connection.close();
			}catch(IOException ioException){
				ioException.printStackTrace();
			}
		}
		//Send a message to client
		private void sendMessage(String message){
			try{
				output.writeObject("SERVER: " + message);
				output.flush();
				showMessage("\nSERVER: " + message);
			}catch(IOException ioException){
				chatWindow.append("\n ERROR: I can't send that message");
			}
		}
		
		//Updates chatWindow
		private void showMessage (String text){
			SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						chatWindow.append(text);
					}
				}
			);
		}
		
		// Let the user type stuff into their box
		private void ableToType(boolean tof){
			SwingUtilities.invokeLater(
				new Runnable(){
					public void run(){
						userText.setEditable(tof);
					}
				}
			);
		}

		public Socket getConnection() {
			return connection;
		}
	
	}
	
	


