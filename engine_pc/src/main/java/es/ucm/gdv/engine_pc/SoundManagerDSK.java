package es.ucm.gdv.engine_pc;

import es.ucm.gdv.engine.Sound;
import es.ucm.gdv.engine.SoundManager;

public class SoundManagerDSK implements SoundManager {

    //Devolvemos un sonido
    @Override
    public Sound createSound(String filepath, boolean loop) {
        return new SoundDSK(filepath, loop);
    }

    //Reproducimos el sonido
    @Override
    public void playSound(Sound sound) {
        ((SoundDSK)sound).getSound().setMicrosecondPosition(0);
        ((SoundDSK)sound).getSound().start();
    }

    //Paramos el sonido
    @Override
    public void stopSound(Sound sound) {
        ((SoundDSK)sound).getSound().stop();
    }

    //Tambien paramos el sonido
    @Override
    public void releaseSound(Sound sound) {
        ((SoundDSK)sound).getSound().stop();
    }
}
