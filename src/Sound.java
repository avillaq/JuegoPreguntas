import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class Sound {

	private static Clip clip;
	
	//Este metodo estara en Main.java
	public static void reproducir() {
		try {
			//Solo funciona con archivos .wav
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("./back1.wav").getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        
	        //El audio se reproducira 5 veces? seguidas
	        clip.loop(5);
	 
	       } catch(UnsupportedAudioFileException | IOException | LineUnavailableException ex) {
	         System.out.println("Error al reproducir el sonido.");
	       }
	}
	
}
