package es.ucm.gdv.engine_android;

import android.content.res.AssetManager;
import android.view.SurfaceView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.GLogic;
import es.ucm.gdv.engine.Game;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.SoundManager;

public class EngineAPP implements Engine {

    private SoundManagerAPP _soundManager = null;
    private GraphicsAPP _graphics = null;
    private InputAPP _input = null;
    private Game _game = null;
    private GLogic _gameLogic = null;
    private AssetManager _assetManager = null;

    //Inicializamos to do
    public EngineAPP(int logic_width, int logic_height, SurfaceView view, GLogic gLogic, AssetManager assetManager){
        _assetManager = assetManager;

        _gameLogic = gLogic;

        _soundManager = new SoundManagerAPP(_assetManager);
        _graphics = new GraphicsAPP(logic_width, logic_height, view, _assetManager);
        _input = new InputAPP();
        view.setOnTouchListener(_input);

        _game = new GameAPP(this);
    }

    @Override
    public SoundManager getSoundManager() {
        return _soundManager;
    }

    @Override
    public GraphicsAPP getGraphics() {
        return _graphics;
    }

    @Override
    public InputAPP getInput() {
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

    //Devolvemos un InputStream con la ruta especificada
    @Override
    public InputStream openInputStream(String filename) {

            try {
                InputStream file = _assetManager.open(filename);;
                return file;
            }catch(java.io.IOException e){
                android.util.Log.e("EngineAPP", e.getMessage());
                return null;
        }
    }
}
