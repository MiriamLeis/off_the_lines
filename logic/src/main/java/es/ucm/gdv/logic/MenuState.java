package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Engine;
import es.ucm.gdv.engine.Graphics;

public class MenuState extends State {
    private Graphics _graphics;
    private ModeGameButton _easyButton;
    private ModeGameButton _hardButton;

    public MenuState(GameLogic gameLogic) {
        super(gameLogic);
    }

    public void init(Engine engine){
        _graphics = engine.getGraphics();
    }

    //Creamos todos los obbjetos del menu
    @Override
    public void prepare(){
        TextObject titulo = new TextObject("Off The Lines", new double[]{25.0, 60.0}, new int[]{0, 136, 255}, this);
        titulo.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 36, false);
        _gameObjects.add(titulo);

        TextObject sub_titulo = new TextObject("A game copied to Bryan Perfetto", new double[]{25.0, 90.0}, new int[]{0, 136, 255}, this);
        sub_titulo.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 22, false);
        _gameObjects.add(sub_titulo);

        _easyButton = new ModeGameButton(Mode.EASY, new double[]{25.0, 300.0}, 180, 25, this);
        _easyButton.createText(_graphics, "Easy Mode", new int[]{255, 255, 255}, "data/fonts/Bungee-Regular.ttf", 28, false);
        _gameObjects.add(_easyButton);

        TextObject easy_text = new TextObject("(Slow speed, 10 Lifes)", new double[]{215.0, 320.0}, new int[]{100, 100, 100}, this);
        easy_text.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 12, false);
        _gameObjects.add(easy_text);

        _hardButton = new ModeGameButton(Mode.HARD, new double[]{25.0, 340.0}, 180, 25, this);
        _hardButton.createText(_graphics, "Hard Mode", new int[]{255, 255, 255}, "data/fonts/Bungee-Regular.ttf", 28, false);
        _gameObjects.add(_hardButton);

        TextObject hard_text = new TextObject("(Fast speed, 5 Lifes)", new double[]{215.0, 360.0}, new int[]{100, 100, 100}, this);
        hard_text.createFont(_graphics, "data/fonts/Bungee-Regular.ttf", 12, false);
        _gameObjects.add(hard_text);
    }

    //Borramos todos los objetos
    @Override
    public void reset() {
        super.reset();
        _easyButton = null;
        _hardButton = null;
    }

    //Actualizamos los objetos
    public void update(double deltaTime){
        super.update(deltaTime);

        if(_easyButton.isClicked())
            playGame(_easyButton.getMode());
        else if(_hardButton.isClicked())
            playGame(_hardButton.getMode());
    }

    //Cambiamos de estado
    public void playGame(Mode mode){
        ((GameState)_gameLogic.getState("game")).setMode(mode);
        _gameLogic.changeState("game");
    }

}
