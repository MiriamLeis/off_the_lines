package es.ucm.gdv.engine;

import java.io.InputStream;

// Interfaz que aglutina lo demás. En condiciones normales, Graphics
// e Input serían singleton. Sin embargo, al ser interfaces y no existir en Java
// precompilador no es tan sencillo. El interfaz Engine puede ser el encargado de mantener
// las instancias y otros métodos útiles de acceso a la plataforma
public interface Engine {

    SoundManager getSoundManager();

    // Devuelve la instancia del motor gráfico.
    Graphics getGraphics();

    // Devuelve la instancia del gestor de entrada.
    Input getInput();

    // Devuelve la instancia del gestor del juego.
    Game getGame();

    // Devuelve la instancia de la logica del juego.
    GLogic getGameLogic();

    // Devuelve un stream de lectura de un fichero.
    InputStream openInputStream(String filename);
}
