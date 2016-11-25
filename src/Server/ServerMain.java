package Server;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;

public class ServerMain extends JFrame {

	private JPanel contentPane;
	private static JTextArea chatWindow;
	private static Connections serverConnection;
	private ClientHandler clientHandler;
	private static int maxClients;

	/**
	 * Starts the server.
	 * 
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ServerMain frame = new ServerMain();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		String tempMax = JOptionPane.showInputDialog(null, "Set the number of clients that can connect", "Max clients", JOptionPane.PLAIN_MESSAGE);
		if (tempMax == null)
			System.exit(0);
		maxClients = Integer.parseInt(tempMax);
		serverConnection = new Connections(maxClients);
		serverConnection.connect();
	}

	/**
	 * Constructor Creates the server window.
	 */
	public ServerMain() {
		super("This amazing chat");
		clientHandler = new ClientHandler();
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent event) {
				closeCrap();
			}
		});
		contentPane.setBackground(Color.DARK_GRAY);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		chatWindow = new JTextArea();
		chatWindow.setForeground(Color.DARK_GRAY);
		chatWindow.setEditable(false);
		chatWindow.setVisible(true);
		chatWindow.setText("");
		chatWindow.setFont(new Font("Cambria", Font.PLAIN, 16));

		JScrollPane scroll = new JScrollPane(chatWindow);
		scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 398, Short.MAX_VALUE).addContainerGap()));
		gl_contentPane.setVerticalGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup().addContainerGap()
						.addComponent(scroll, GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE).addContainerGap()));
		contentPane.setLayout(gl_contentPane);

	}

	/*
	 * Updates the serverWindow with the messages sent by users.
	 */
	static void showMessage(final String text) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				chatWindow.append("\n" + text);
			}
		});
	}

	/**
	 * Closes all the streams and the sockets before finally closing the window.
	 */
	private void closeCrap() {
		showMessage("\n\n*** This server and all its clients will selfdestruct in 3 secunds.***"
				+ "\n\n*** Hava a nice day ***\n");

		try {
			if ((serverConnection.passed) && (clientHandler.passed)) {
				clientHandler.writeToClients("serverShuttingDown");
				Thread.sleep(3000);
				clientHandler.closeStreams();
				serverConnection.closeSockets();
			} else if ((serverConnection.passed) && (!clientHandler.passed)) {
				serverConnection.closeSockets();
				
			} else if ((!serverConnection.passed) && (!clientHandler.passed)) {
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (NullPointerException e) {
			e.printStackTrace();
		} finally {
			System.exit(0);
		}
	}

}