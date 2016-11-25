package chat;
import java.io.BufferedInputStream;
import java.io.InputStream;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;


public class MusicPlayer implements Runnable{
	Player mp3player = null;
	boolean value;
	@Override
	
	// Runs a music player with a specific tune
	public void run() {
		// TODO Auto-generated method stub
        try {
        	InputStream is = getClass().getResourceAsStream("Crypsis.mp3");
        	BufferedInputStream bis = new BufferedInputStream(is);
        	mp3player = new Player(bis);
        	mp3player.play();
        	value = true;
        } catch (JavaLayerException e) {
        } catch (NullPointerException ex) {
        }
		}
	
	// Closes the music player
	public boolean closePlayer(){
		try{
			mp3player.close();
			return true;
		}catch(Exception e){
			return false;
		}
	}

}
