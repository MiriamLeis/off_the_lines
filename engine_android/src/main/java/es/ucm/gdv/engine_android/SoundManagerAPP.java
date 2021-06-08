package es.ucm.gdv.engine_android;

import android.content.res.AssetManager;

import java.io.IOException;

import es.ucm.gdv.engine.Sound;
import es.ucm.gdv.engine.SoundManager;

public class SoundManagerAPP implements SoundManager {
    private AssetManager _assetManager = null;
    public SoundManagerAPP(AssetManager assetManager)
    {
        _assetManager = assetManager;
    }

    //Devuelve un sonido creado
    @Override
    public Sound createSound(String filepath, boolean loop) {
        return new SoundAPP(filepath, loop,_assetManager);
    }

    //Reproduce un sonido
    @Override
    public void playSound(Sound sound) {


        ((SoundAPP)sound).getSound().start();
    }

    //Para un sonido
    @Override
    public void stopSound(Sound sound) {
        ((SoundAPP)sound).getSound().pause();

        ((SoundAPP)sound).getSound().seekTo(0);
    }

    //Libera los recursos de un sonido
    @Override
    public void releaseSound(Sound sound) {
        ((SoundAPP)sound).getSound().stop();
        ((SoundAPP)sound).getSound().release();
    }
}
