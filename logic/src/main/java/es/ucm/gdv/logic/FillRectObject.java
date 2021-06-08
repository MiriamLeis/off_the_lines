package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Graphics;

public class FillRectObject extends GameObject {
    private int _width, _height;

    //Objeto que dibuja un rectángulo
    public FillRectObject(double[] pos, int w, int h, int[] color, State gameManager) {
        super(gameManager);
        setPosition(pos[0], pos[1]);
        _color = color;
        _width = w;
        _height = h;
    }

    @Override
    public void render(Graphics g) {
        super.render(g);

        g.save();

        double[] scaled = g.getScaledPosition(_position);
        int transX = (int)scaled[0];
        int transY = (int)scaled[1];
        double scale = scaled[2];

        g.translate(transX, transY);
        g.scale(scale, scale);

        g.rotate(_angle);

        g.setColor(_color[0], _color[1], _color[2], _alpha);

        g.fillRect(0, 0, _width, _height);

        g.translate(-transX, -transY);

        g.restore();
    }
}
