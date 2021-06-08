package es.ucm.gdv.engine_android;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.GLogic;
import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

//Clase que implementa Runnable para no que el engine no tenga que implementarlo
public class GameAPP implements Game, Runnable {
    private GLogic _gameLogic = null;
    private GraphicsAPP _graphics = null;
    private InputAPP _input = null;

    volatile boolean _running = false;
    Thread _renderThread;

    public GameAPP(EngineAPP engine){
        _gameLogic = engine.getGameLogic();
        _gameLogic.initialize(engine);
        _graphics = engine.getGraphics();
        _input = engine.getInput();
    }

    //Creamos una nueva hebra cuando volvamos a tener el control
    public void resume(){
        if(!_running){
            _running = true;

            _renderThread = new Thread(this);
            _renderThread.start();
        }
    }
    //Cerramos la hebra y hacemos que el GameLogic reaccione por si tiene que hacer algo
    public void pause(){
        _running = false;
        while(true) {
            try {
                _renderThread.join();
                _gameLogic.pause();
                break;
            } catch (InterruptedException ie) {
            }
        }
    }
    //Bucle del engine
    public void run() {
        long lastFrameTime = System.nanoTime();

        while(_running){
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = System.nanoTime();
            double delta = (double) nanoElapsedTime/1.0e9;


            _gameLogic.handleInput(_input);
            _gameLogic.update(delta);

            _graphics.prepare();
            _graphics.calculateLogicSize();
            _gameLogic.render(_graphics);
            _graphics.present();
        }
    }
}
