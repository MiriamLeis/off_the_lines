package es.ucm.gdv.engine_pc;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.GLogic;
import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

public class GameDSK implements Game {
    private GLogic _gameLogic = null;
    private GraphicsDSK _graphics = null;
    private InputDSK _input = null;

    public GameDSK(EngineDSK engine){
        _gameLogic = engine.getGameLogic();
        _gameLogic.initialize(engine);
        _graphics = engine.getGraphics();
        _input = engine.getInput();
    }

    //Bucle del engine
    public void run() {
        long lastFrameTime = System.nanoTime();

        while(true){
            long currentTime = System.nanoTime();
            long nanoElapsedTime = currentTime - lastFrameTime;
            lastFrameTime = System.nanoTime();
            double delta = (double) nanoElapsedTime/1.0e9;

            _gameLogic.handleInput(_input);
            _gameLogic.update(delta);


            _graphics.prepare();
            _graphics.calculateLogicSize();
            try {
                _gameLogic.render(_graphics);
            }finally {
                _graphics.dispose();
            }
            _graphics.present();
        }

    }
}
