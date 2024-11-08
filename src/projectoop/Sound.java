package projectoop;

import java.io.FileNotFoundException;
import javax.sound.sampled.*;
import java.io.InputStream;

public class Sound {

    private Clip clip;

    public Sound(String soundFilePath) {
        try {
            // โหลดไฟล์เสียงจาก resources
            InputStream audioSrc = getClass().getResourceAsStream(soundFilePath);
            if (audioSrc == null) {
                throw new FileNotFoundException("Sound file not found: " + soundFilePath);
            }
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioSrc);
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            clip.start();
        }
    }
}
