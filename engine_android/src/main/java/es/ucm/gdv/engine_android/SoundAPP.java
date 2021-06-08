package es.ucm.gdv.engine_android;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;

import java.io.File;
import java.io.IOException;

import es.ucm.gdv.engine.Sound;

public class SoundAPP implements Sound {
    private MediaPlayer _sound;
    private AssetFileDescriptor file;

    //Creamos un MediaPlayer por cada sonido y lo dejamos preparado
    public SoundAPP(String filepath, boolean loop, AssetManager assetsManager){
        _sound = new MediaPlayer();
        try {

            file = assetsManager.openFd(filepath);
            _sound.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
            _sound.setLooping(loop);
            _sound.prepare();
            setVolume(1.0f);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public MediaPlayer getSound(){
        return _sound;
    }
    public AssetFileDescriptor getFile(){
        return file;
    }
    public void setVolume(float volume){
        _sound.setVolume(volume, volume);
    }
}
