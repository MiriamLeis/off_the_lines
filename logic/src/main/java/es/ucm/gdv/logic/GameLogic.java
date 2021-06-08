package es.ucm.gdv.logic;

import java.awt.Menu;
import java.util.ArrayList;
import java.util.HashMap;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.GLogic;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.Sound;
import es.ucm.gdv.engine.SoundManager;

public class GameLogic implements GLogic {
    private SoundManager _soundManager;
    private Sound _mainSound;

    private HashMap<String, State> _states;
    private String _currentState;
    private String _nextState;

    public GameLogic(){
        _states = new HashMap<String, State>();
        _nextState = "";
    }

    //Creamos el sonido de fondo
    public void initialize(Engine engine){
        _soundManager = engine.getSoundManager();
        _mainSound = _soundManager.createSound("data/sounds/mainMenu.wav", true);
        createStates(engine);
        _soundManager.playSound(_mainSound);
    }

    //Creamos los estados del juego y ponemos como el actual el menu
    public void createStates(Engine engine){
        GameState game = new GameState(this);
        game.init(engine);
        MenuState menu = new MenuState(this);
        menu.init(engine);

        _states.put("game", game);
        _states.put("menu", menu);

        _currentState = "menu";
        prepareState(_currentState);
    }

    public SoundManager getSoundManager(){
        return _soundManager;
    }

    //Llamamos al estado para que se prepare
    public void prepareState(String id){
        _states.get(id).prepare();
    }

    //Preparamos el siguiente estado para cambiarlo cuando termine el update
    public void changeState(String id){
        _nextState = id;
        prepareState(id);
    }

    public State getState(String id){
        return _states.get(id);
    }

    //Llamamos al handle input del estado actual
    @Override
    public void handleInput(Input input) {
        _states.get(_currentState).handleInput(input);
    }

    //Quitamos el sonido de fondo si el sistema nos avisa
    @Override
    public void pause() {
        _soundManager.releaseSound(_mainSound);
    }

    //Llamamos al update del estado actual y si hay que cambiar de estado lo hacemos
    @Override
    public void update(double deltaTime) {
        _states.get(_currentState).update(deltaTime);
        handleStates();
    }

    @Override
    public void render(Graphics g){
        _states.get(_currentState).render(g);
    }

    //Cambiamos de estado definitivamente
    private void handleStates(){
        if(_nextState != ""){
            _states.get(_currentState).reset();
            _currentState = _nextState;
            _nextState = "";
        }
    }
}
