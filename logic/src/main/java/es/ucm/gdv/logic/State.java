package es.ucm.gdv.logic;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;
import es.ucm.gdv.engine.SoundManager;

public class State {
    protected GameLogic _gameLogic;
    protected ArrayList<GameObject> _gameObjects;
    protected ArrayList<GameObject> _gameObjectsToDelete;

    public State(GameLogic gameLogic){
        _gameObjects = new ArrayList<GameObject>();
        _gameObjectsToDelete = new ArrayList<GameObject>();
        _gameLogic = gameLogic;
    }

    public SoundManager getSoundManager(){
        return _gameLogic.getSoundManager();
    }

    public void init(Engine engine){}

    public void prepare(){}

    //Borramos los objetos
    public void reset(){
        _gameObjects.clear();
        _gameObjectsToDelete.clear();
    }

    //Llamamos al handleInput de los objetos
    public void handleInput(Input input){
        List<Input.TouchEvent> events;

        events = input.getTouchEvents();

        while(!events.isEmpty()){
            Input.TouchEvent e = events.get(0);

            for (GameObject gO : _gameObjects)
                gO.handleInput(e);

            events.remove(0);
        }
    }
    //Actualizamos los objetos
    public void update(double deltaTime){
        for (GameObject gO : _gameObjects)
            gO.update(deltaTime);
        postUpdate();
    }

    //Borramos los objetos cuando terminamos el update
    private void postUpdate() {
        int i = _gameObjectsToDelete.size() - 1;
        while(i >= 0)
        {
            Object obj = _gameObjectsToDelete.get(i);
            _gameObjects.remove(_gameObjectsToDelete.get(i));
            _gameObjectsToDelete.remove(i);
            i--;

            obj = null;
        }
    }

    //Añadimos un objeto para eliminarlo mas tarde
    public void deleteGameObject(GameObject obj) {
        _gameObjectsToDelete.add(obj);
    }

    //Renderizamos los objetos
    public void render(Graphics g){
        g.clear(0, 0, 0);

        for (GameObject gO : _gameObjects)
            gO.render(g);
    }
}
