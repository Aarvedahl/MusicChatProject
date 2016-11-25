package Client;

import java.io.BufferedInputStream;
import java.io.InputStream;


import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.*;


public class MusicPlayer implements Runnable{
	Player mp3player = null;
	public boolean value;

	@Override
	
	/**
	 * Starts a new thread that plays awesome music from a file.
	 */
	public void run() {
	 try {
        	InputStream is = getClass().getResourceAsStream("Crypsis.mp3");
        	BufferedInputStream bis = new BufferedInputStream(is);
        	mp3player = new Player(bis);
        	mp3player.play();
        	value = true;
        } catch (JavaLayerException e) {
        	value = false;
        } catch (NullPointerException ex) {
        	value = false;
        }
		}
	

    /**
     * Method used to close the music player.
     * @return
     */
	public boolean closePlayer(){
		try {
		mp3player.close();
		return true;
		} catch (Exception e){
			return false;
		}
	}

}