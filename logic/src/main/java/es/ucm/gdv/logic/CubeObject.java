package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Sound;

public class CubeObject extends GameObject {
    protected int _tam;
    protected double _ownScale = 1;
    protected boolean _died = false;
    protected Sound _sound;

    public CubeObject(int tam, int thickness, State gameManager){
        super(gameManager);
        setTag("cube");

        _angle = 0;
        _tam = tam;
    }

    //Actualizamos los angulos
    public void update(double deltaTime){
        super.update(deltaTime);
        _angle -= 180 * deltaTime;
        _angle = _angle % 360;
    }

    //Renderizamos la líneas de los cubos
    public void render(Graphics g){
        super.render(g);
        g.save();

        int radio = _tam / 2;

        double[] scaled = g.getScaledPosition(_position);
        int transX = (int)scaled[0];
        int transY = (int)scaled[1];
        double scale = scaled[2];


        g.translate(transX, transY);
        g.scale(scale * _ownScale, scale * _ownScale);

        g.rotate(_angle);

        g.setColor(_color[0], _color[1], _color[2], _alpha);


        g.drawLine(-radio, -radio, radio, -radio);
        g.drawLine(radio, -radio, radio, radio);
        g.drawLine(radio, radio, -radio, radio);
        g.drawLine(-radio, radio, -radio, -radio);


        g.translate(-transX, -transY);


        g.restore();
    }

    //Reproducimos el sonido de muerte
    public void die()
    {
        _died = true;
        _gameManager.getSoundManager().playSound(_sound);
    }
}
