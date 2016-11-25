package chat;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import java.awt.Font;
import java.awt.SystemColor;
 
public class Client extends JFrame implements ActionListener {
    private JPanel contentPane;
    private JTextField userText;
    private JTextArea chatWindow;
    private JButton playBtn;
    private JButton stopBtn;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private String message = "";
    private String serverIP;
    private Socket connection;
    private String clientName;
    private JScrollPane scroll;
    private MusicPlayer player = new MusicPlayer();
 
    // Constructor
    public Client(String host) {
        super("Client This Amazing Chat");
        setBackground(Color.WHITE);
        serverIP = host;
        userText = new JTextField();
        userText.setForeground(Color.DARK_GRAY);
        userText.setBackground(Color.WHITE);
        userText.setFont(new Font("Cambria", Font.PLAIN, 16));
        userText.setColumns(10);
        userText.setEditable(false);
        userText.addActionListener((ActionListener)this);
       
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 442, 394);
        contentPane = new JPanel();
        contentPane.setBackground(Color.DARK_GRAY);
        contentPane.setForeground(SystemColor.inactiveCaption);
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
       
        chatWindow = new JTextArea();
        chatWindow.setBackground(Color.WHITE);
        chatWindow.setForeground(Color.DARK_GRAY);
        chatWindow.setFont(new Font("Cambria", Font.PLAIN, 16));
        chatWindow.setEditable(false);
        chatWindow.setLineWrap(true);
 
        scroll = new JScrollPane(chatWindow);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
 
        playBtn = new JButton("Play");
        playBtn.setForeground(Color.DARK_GRAY);
        playBtn.setBackground(Color.WHITE);
        playBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        playBtn.addActionListener((ActionListener) this);
 
        stopBtn = new JButton("Stop");
        stopBtn.setForeground(Color.DARK_GRAY);
        stopBtn.setBackground(Color.WHITE);
        stopBtn.setFont(new Font("Tahoma", Font.BOLD, 18));
        stopBtn.addActionListener((ActionListener) this);
 
        GroupLayout gl_contentPane = new GroupLayout(contentPane);
        gl_contentPane.setHorizontalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                        .addGroup(gl_contentPane.createSequentialGroup()
                            .addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
                                .addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(userText, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
                            .addContainerGap())
                        .addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
                            .addGap(55)
                            .addComponent(playBtn, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                            .addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
                            .addComponent(stopBtn, GroupLayout.PREFERRED_SIZE, 120, GroupLayout.PREFERRED_SIZE)
                            .addGap(60))))
        );
        gl_contentPane.setVerticalGroup(
            gl_contentPane.createParallelGroup(Alignment.LEADING)
                .addGroup(gl_contentPane.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
                        .addComponent(playBtn, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
                        .addComponent(stopBtn, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
                    .addGap(18)
                    .addComponent(scroll, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
                    .addGap(18)
                    .addComponent(userText, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        contentPane.setLayout(gl_contentPane);
 
    }
 
    // Connect to server
    public void startRunning() {
        setClientName(JOptionPane.showInputDialog(null, "Write your name", "Set name", JOptionPane.PLAIN_MESSAGE));
        try {
            connectToServer();
            setupStreams();
            whileChatting();
        } catch (EOFException eofException) {
            showMessage("\n Client Terminated the connection");
        } catch (IOException ioException) {
            ioException.printStackTrace();
        } finally {
            closeCrap();
        }
    }
 
    // connect to server
    private void connectToServer() throws IOException {
        showMessage("Attempting connection... \n");
        connection = new Socket(InetAddress.getByName(serverIP), 6789);
        showMessage("You are now connected: " + connection.getInetAddress().getHostName());
    }
 
    // set up streams to send and receive messages
    private void setupStreams() throws IOException {
        output = new ObjectOutputStream(connection.getOutputStream());
        output.flush();
        input = new ObjectInputStream(connection.getInputStream());
        showMessage("\nYour streams are now good to go!");
    }
 
    // While chatting with server
    private void whileChatting() throws IOException {
        ableToType(true);
        do {
            try {
                message = (String) input.readObject();
                if(message.equals("Play")){
                	playBtn.doClick();
                }
                showMessage("\n" + message);
            } catch (ClassNotFoundException classNotFoundException) {
                showMessage("Invalid object type");
            }
        } while (!message.equals("end"));
    }
 
    // Close te streams and sockets
    private void closeCrap() {
        showMessage("\n closing crap down...");
        ableToType(false);
        try {
            output.close();
            input.close();
            connection.close();
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
 
    }
 
    // Send message to server
    private void sendMessage(String message) {
        try {
            output.writeObject(clientName + ": " + message);
            output.flush();
        } catch (IOException ioException) {
            chatWindow.append("\n Something went wrong sending message");
        }
    }
    
    private void sendCommand(String message) {
        try {
        	output.writeObject(message);
        	output.flush();
        } catch (IOException ioException) {
        	
        }
    }
 
    // Updating the Chat Window
    private void showMessage(final String message) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatWindow.append(message);
            }
        });
    }
 
    // Gives user permission to type crap into the text box
    private void ableToType(final boolean tof) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                userText.setEditable(tof);
            }
        });
    }
 
    public void setClientName(String name) {
            clientName = name;
    }
	
	@Override
	
	// The events when something on the chat windows is triggered
	public void actionPerformed(ActionEvent event) {
		Object source = event.getSource();
		 Thread playerThread = new Thread(player);
		 
        sendMessage(event.getActionCommand());
        userText.setText("");
        
        if(source == playBtn){
        	playerThread.start();
        	sendCommand("Play");
        	playBtn.setEnabled(false);
        	if(playerThread.isAlive()){
        		playBtn.setEnabled(false);
        	}
        }
        
        if(source == stopBtn){
        	try {
				playerThread.join(1);;
			  	player.closePlayer();
	        	playBtn.setEnabled(true);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
        }
        
		
	}
}