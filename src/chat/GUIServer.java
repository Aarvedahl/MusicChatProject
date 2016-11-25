package chat;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
 
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import java.awt.Color;
 
public class GUIServer extends JFrame {
 
    private JPanel contentPane;
    private static JTextArea chatWindow;
    private static Connections serverConnection;
    private static ClientHandler client;
 
    /**
     * Launch the application.
     * @throws InterruptedException 
     */
    public static void main(String[] args) throws InterruptedException {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    GUIServer frame = new GUIServer();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
 
            }
        });
 
        serverConnection = new Connections(5);
        serverConnection.connect();
    }
 
    /**
     * Create the frame.
     */
    public GUIServer() {
        super("This amazing chat");
 
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
 
    // Updates chatWindow
    public static void showMessage(final String text) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                chatWindow.append("\n" + text);
            }
        });
    }
 
    // Close streams and sockets once you are done chatting
    private void closeCrap() {
 
        showMessage("\nClosing Connections... \n");
        try {
            client.closeStreams();
            serverConnection.closeSockets();
        } catch (NullPointerException e) {
            showMessage(e.getMessage());
        } finally {
            System.exit(0);
        }
    }
 
}