package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

public class GameObject {
    private String _tag;

    protected double[] _position;
    protected int[] _color;
    protected int _alpha;
    protected float _angle;
    protected State _gameManager;

    public GameObject(State gameManager){
        _position = new double[2];
        _color = new int[3];
        _alpha = 255;
        _gameManager = gameManager;
    }

    public void setAlpha(int alpha){_alpha = alpha;}

    public void handleInput(Input.TouchEvent e){}
    public void update(double deltaTime){}
    public void render(Graphics g){}

    public void setPosition(double x, double y){
        _position[0] = x;
        _position[1] = y;
    }
    public double[] getPosition(){
        return _position;
    }

    public void setColor(int r, int g, int b){
        _color[0] = r;
        _color[1] = g;
        _color[2] = b;
    }
    public float getAngle() {
        return _angle;
    }

    public void setTag(String tag){_tag = tag;}
    public String getTag(){return _tag;}
}
