package Client;
import javax.swing.*;
 
public class ClientMain {
	/**
	 * The main method for the client, where it all begins.
	 * @param args
	 */
    public static void main(String[] args) {
        Client client;
        client = new Client("127.0.0.1");
        client.setVisible(true);
        client.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
			try {
				client.startRunning();
			} catch (ConnectionException e) {
				client.showMessage(e.getMessage());
				client.closeCrap();
			}
    }
}