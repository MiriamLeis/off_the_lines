package es.ucm.gdv.engine;

public interface GLogic {
    void initialize(Engine engine);
    void update(double deltaTime);
    void render(Graphics g);
    void handleInput(Input input);
    void pause();
}
