package chat;

import static org.junit.Assert.*;
import org.junit.*;
import org.junit.Test;

public class ConnectionMusicTest {
    static Connections c;
    static MusicPlayer mp3player;
    @BeforeClass
    public static void setUp() {
        c = new Connections(5);
       mp3player = new MusicPlayer();
    }
    
    @Test
    public void testMethodRun(){
    	assertEquals(false, mp3player.value);
    }
    @Test
    public void testMethodClosePlayer(){
    	assertEquals(false, mp3player.closePlayer());
    }
    
    
    @Test
    public void testMethodClose(){
    	assertEquals(false, c.closeSockets());
    }
    @Test
    public void testMethodConnect(){
    	assertEquals(false, c.value);
    	assertEquals(false, c.value1);
    } 
}
