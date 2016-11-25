package Client;

import java.awt.event.*;
import java.awt.*;
import javax.swing.*;
import java.io.*;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.SocketException;

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
	private int count = 4;

	/**
	 * Constructor, creates the Client frame.
	 * @param host
	 */
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
		userText.addActionListener((ActionListener) this);

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				showMessage("\n\n*** Unable to close client,\n*** Client is closed from server.\n");
			}
		});
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
		gl_contentPane
				.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING).addGroup(
						gl_contentPane.createSequentialGroup().addContainerGap().addGroup(gl_contentPane
								.createParallelGroup(Alignment.LEADING)
								.addGroup(gl_contentPane.createSequentialGroup()
										.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
												.addComponent(scroll, GroupLayout.PREFERRED_SIZE,
														GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
												.addComponent(userText, GroupLayout.DEFAULT_SIZE, 402, Short.MAX_VALUE))
										.addContainerGap())
								.addGroup(Alignment.TRAILING,
										gl_contentPane.createSequentialGroup().addGap(55)
												.addComponent(playBtn, GroupLayout.PREFERRED_SIZE, 109,
														GroupLayout.PREFERRED_SIZE)
												.addPreferredGap(ComponentPlacement.RELATED, 58, Short.MAX_VALUE)
												.addComponent(stopBtn, GroupLayout.PREFERRED_SIZE, 120,
														GroupLayout.PREFERRED_SIZE)
												.addGap(60)))));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(playBtn, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE)
								.addComponent(stopBtn, GroupLayout.PREFERRED_SIZE, 48, GroupLayout.PREFERRED_SIZE))
						.addGap(18).addComponent(scroll, GroupLayout.PREFERRED_SIZE, 182, GroupLayout.PREFERRED_SIZE)
						.addGap(18).addComponent(userText, GroupLayout.PREFERRED_SIZE, 47, GroupLayout.PREFERRED_SIZE)
						.addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)));
		contentPane.setLayout(gl_contentPane);

	}
	/**
	 * Ties the methods in client class into a working program.
	 * @throws ConnectionException
	 */
	public void startRunning() throws ConnectionException {
		clientName = JOptionPane.showInputDialog(null, "Write your name", "Set name", JOptionPane.PLAIN_MESSAGE);
		if(clientName == null)
			System.exit(0);
		try {
			connectToServer();
			setupStreams();
			whileChatting();
		} catch (SocketException e) {
			throw new ConnectionException();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Connects the client to the server.
	 * @throws IOException
	 * @throws InterruptedException
	 */
	void connectToServer() throws IOException, InterruptedException {
		showMessage("Attempting connection... \n");
		try {
			connection = new Socket(InetAddress.getByName(serverIP), 6789);
		} catch (ConnectException conEx) {
				showMessage("*** Well this isn't working...."
						+ "\n*** Gues the server is not up yet..."
						+ "\n*** I'll just go ahead and close this for you...");
				Thread.sleep(4567);
				System.exit(0);
		
		}

		showMessage("You are now connected: " + connection.getInetAddress().getHostName());
	}

	/**
	 * Creates the streams used for communication.
	 * @throws IOException
	 */
	private void setupStreams() throws IOException {
		output = new ObjectOutputStream(connection.getOutputStream());
		output.flush();
		input = new ObjectInputStream(connection.getInputStream());
		showMessage("\n*** Your streams are now good to go!"
				+ "\n*** Remeber, you can't close this window,"
				+ "\n*** It's closed by the server."
				+ "\n*** Have fun!\n");
		
	}
	/**
	 * A loop that sends and receive messages from the server. 
	 * @throws IOException
	 * 
	 */
	private void whileChatting() throws IOException {
		ableToType(true);
		do {
			try {
				if (message.equals("Play")) {
					playBtn.doClick();
				}
				message = (String) input.readObject();
				if (message.equals(null)){
					throw new ConnectionException();
				}
				showMessage("\n" + message);
			} catch (EOFException e){
				try {
					throw new ConnectionException();
				} catch (ConnectionException e1) {
					e1.getMessage();
					message = e1.specialDelivery();
				}
			} catch (ConnectionException e){
				e.getMessage();
				message = e.specialDelivery();
			} catch (ClassNotFoundException e) {
				showMessage("Invalid object type");
			}
		} while (!message.equals("serverShuttingDown"));
		showMessage("\n*** Chat closing down \n*** Hava a nice day ");
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		closeCrap();
	}
	/**
	 * Closes the streams and the socket before closing the window.
	 */
	void closeCrap() {
		showMessage("\n\n*** Closing crap down... ***");
		ableToType(false);

		try {
			Thread.sleep(3000);
			output.close();
			input.close();
			connection.close();
		} catch (IOException ioException) {
			ioException.printStackTrace();
		} catch (InterruptedException e) {

		} finally {
			System.exit(0);
		}

	}
	/**
	 * Sends the the message given to it to the server.
	 * @param message
	 */
	private void sendMessage(String message) {
		try {
			output.writeObject(clientName + ": " + message);
			output.flush();
		} catch (IOException ioException) {
			chatWindow.append("\n*** Something went wrong sending message ***");
		}
	}

	private void sendCommand(String message) {
		try {
			output.writeObject(message);
			output.flush();
		} catch (IOException ioException) {

		}
	}

	/**
	 * Updates the client window with the messages from the server.
	 * @param message
	 */
	void showMessage(final String message) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append(message);
			}
		});
	}

	/**
	 * Sets the text field in the chat window to editable, so the user can type in messages.
	 * @param tof
	 */
	private void ableToType(final boolean tof) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				userText.setEditable(tof);
			}
		});
	}
	/**
	 * Lets the user set their name.
	 * @param name
	 */
	public void setClientName(String name) {
		clientName = name;
	}

	/**
	 * Controls the how the user interacts with the chat window.
	 */
	@Override
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