package es.ucm.gdv.logic;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

public class GameState extends State {
    private Mode _mode;
    private Graphics _graphics;
    private ReadJSON _reader;
    private Player _player;
    private int _level;
    private int _maxLevel;


    private boolean _endGame;

    private ArrayList<GameObject> _endGameObjects;

    public GameState(GameLogic gameLogic){
        super(gameLogic);
        _mode = Mode.EASY;
        _maxLevel = 20;

        _endGame = false;

        _endGameObjects = new ArrayList<GameObject>();
    }

    @Override
    public void init(Engine engine) {
        _reader = new ReadJSON(engine);
        _graphics = engine.getGraphics();
    }

    public void setMode(Mode mode){
        _mode = mode;
    }

    //Creamos el jugador y cargamos el primer nivel
    @Override
    public void prepare(){
        _player = new Player(_mode, this);
        _level = 0;
        loadLevel(_level);
    }

    //Creamos los objetos del final de la partida
    private void prepareEndGame(boolean win) {
        _endGame = true;

        // Objetos de final de partida
        FillRectObject blackBox = new FillRectObject(new double[]{0.0, 0.0}, Utils._WIDTH, Utils._HEIGHT, new int[]{0, 0, 0}, this);
        blackBox.setAlpha(200);
        _endGameObjects.add(blackBox);

        FillRectObject greyBox = new FillRectObject(new double[]{0.0, 60.0}, Utils._WIDTH, 150, new int[]{50, 50, 50}, this);
        _endGameObjects.add(greyBox);

        TextObject finalText;
        TextObject mode;
        TextObject state;
        String textMode = _mode == Mode.EASY ? "Easy Mode" : "Hard Mode";

        if (win) {
            int[][] textColors = new int[][]{{255, 255, 0}, {0, 150, 255}};
            finalText = new BlinkText("Congratulations", new double[]{(double) (Utils._WIDTH / 2) - 200.0, 100.0}, textColors, this);
            mode = new TextObject(textMode +  " Complete", new double[]{(double) (Utils._WIDTH / 2) - 150.0, 140.0}, new int[]{255, 255, 255}, this);
            state = new TextObject("Click to quit to main menu", new double[]{(double) (Utils._WIDTH / 2) - 200.0, 180.0}, new int[]{255, 255, 255}, this);
        } else {
            finalText = new TextObject("Game Over", new double[]{(double) (Utils._WIDTH / 2) - 110.0, 100.0}, new int[]{255, 0, 0}, this);
            mode = new TextObject(textMode, new double[]{(double) (Utils._WIDTH / 2) - 75.0, win ? 180 : 140.0}, new int[]{255, 255, 255}, this);
            state = new TextObject("Score: " + (_level + 1), new double[]{(double) (Utils._WIDTH / 2) - 65.0, 180.0}, new int[]{255, 255, 255}, this);
        }

        finalText.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 36, false);
        _endGameObjects.add(finalText);

        mode.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 24, false);
        _endGameObjects.add(mode);

        state.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 24, false);
        _endGameObjects.add(state);

    }

    //Borramos todos los objetos del estado
    @Override
    public void reset() {
        super.reset();
        _endGameObjects.clear();
        _endGame = false;
        _player = null;
    }

    //Borramos los objetos que no son ni el jugador ni los de final de partida
    private void cleanObjects(){
        super.reset();
    }

    //Cambiamos de nivel o preparamos los objetos del final de la partida
    public void nextLevel(){
        _level++;
        if(_level >= _maxLevel)
            prepareEndGame(true);
        else
            loadLevel(_level);
    }

    //Borramos los objetos que no sean que no sean el jugador y creamos el nuevo nivel
    private void loadLevel(int level){
        cleanObjects();
        _player.prepare();
        _reader.readLevel(level, _player, _mode, _gameObjects, this);
    }

    //Llamamos al input normal o al de los objetos de final de partida
    @Override
    public void handleInput(Input input){
        if(!_endGame)
            super.handleInput(input);
        else if(_endGame)
            endHandleInput(input);
    }

    //LLamamos al input del final de juego
    public void endHandleInput(Input input) {
        List<Input.TouchEvent> events;

        events = input.getTouchEvents();

        while(!events.isEmpty()){
            Input.TouchEvent e = events.get(0);

            if(e.getType() == Input.Type.PRESSED)
                _gameLogic.changeState("menu");

            events.remove(0);
        }
    }

    //Actualizamos todos los objetos y hacemos una serie de comprobaciones
    @Override
    public void update(double deltaTime){

        if(!_endGame) {
            super.update(deltaTime);

            if (_player.isDead())
                prepareEndGame(false);
            else if (_player.isDeath())
                loadLevel(_level);
            else if (_player.passLevel())
                nextLevel();
        }
        else if(_endGame)
            for(GameObject gO :_endGameObjects)
                gO.update(deltaTime);
    }

    //Renderizamos los objetos
    @Override
    public void render(Graphics g){
        super.render(g);

        if(_endGame)
            for(GameObject gO :_endGameObjects)
                gO.render(g);
    }

}
