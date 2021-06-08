package es.ucm.gdv.engine_pc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JFrame;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.GLogic;
import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.SoundManager;

public class EngineDSK implements Engine {

    private SoundManagerDSK _soundManager = null;
    private GraphicsDSK _graphics = null;
    private InputDSK _input = null;
    private Game _game = null;
    private GLogic _gameLogic = null;

    //Inicializamos todos
    public EngineDSK(boolean fullScreen, int logic_width, int logic_height, String title, JFrame jFrame, GLogic gLogic){
        _gameLogic = gLogic;

        _soundManager = new SoundManagerDSK();
        _graphics = new GraphicsDSK(fullScreen, logic_width, logic_height, title, jFrame);
        _input = new InputDSK();

        jFrame.addMouseListener(_input);
        jFrame.addKeyListener(_input);
        jFrame.addMouseMotionListener(_input);

        _game = new GameDSK(this);
    }

    @Override
    public SoundManager getSoundManager() {
        return _soundManager;
    }

    @Override
    public GraphicsDSK getGraphics() {
        return _graphics;
    }

    @Override
    public InputDSK getInput() {
        return _input;
    }

    @Override
    public Game getGame() {
        return _game;
    }

    @Override
    public GLogic getGameLogic() {
        return _gameLogic;
    }

    //Devolvemos un InputStream
    @Override
    public InputStream openInputStream(String filename) {
        File initialFile = new File(filename);
        try {
            InputStream file = new FileInputStream(initialFile);
            return file;
        }catch(FileNotFoundException e){
            System.out.println("ERROR: " + e.getMessage());
            return null;
        }
    }
}
