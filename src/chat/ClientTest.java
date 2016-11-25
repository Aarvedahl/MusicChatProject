package chat;
import java.awt.EventQueue;
import javax.swing.*;
 
public class ClientTest {
 
    public static void main(String[] args) {
        Client client;
        client = new Client("Localhost");
        client.setVisible(true);
        client.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        client.startRunning();
    }
}