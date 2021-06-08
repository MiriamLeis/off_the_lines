package es.ucm.gdv.engine_pc;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import es.ucm.gdv.engine.Sound;

public class SoundDSK implements Sound {
    private Clip _sound;

    //Creamos un sonido
    public SoundDSK(String filepath, boolean loop){
        try {
            AudioInputStream audioInputStream =
            AudioSystem.getAudioInputStream(new File(filepath).getAbsoluteFile());
            _sound = AudioSystem.getClip();
            _sound.open(audioInputStream);
            if(loop)
                _sound.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public Clip getSound(){
        return _sound;
    }

    public void setVolume(float volume){
        FloatControl vol = (FloatControl) _sound.getControl(FloatControl.Type.MASTER_GAIN);
        vol.setValue(volume);
    }
}
