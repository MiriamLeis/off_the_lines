package es.ucm.gdv.engine;

public interface SoundManager {
    Sound createSound(String filepath, boolean loop);
    void playSound(Sound sound);
    void stopSound(Sound sound);
    void releaseSound(Sound sound);
}
