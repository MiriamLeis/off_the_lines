package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Font;
import es.ucm.gdv.engine.Graphics;

public class TextObject extends GameObject {
    private Font _font;
    private String _text;

    public TextObject(String text, double[] pos, int[] color, State gameManager) {
        super(gameManager);
        _text = text;
        _color = color;
        setPosition(pos[0], pos[1]);
    }

    //Creamos la fuente
    public void createFont(Graphics g, String nameFont, int size, boolean bold){
        _font = g.newFont(nameFont, size, bold);
        setPosition(_position[0], _position[1]);
    }

    //Ponemos la fuente
    public void setFont(Font f){_font = f;}

    //Renderizamos el texto
    public void render(Graphics g){
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
        g.setFont(_font);

        g.drawText(_text, 0, 0);

        g.translate(-transX, -transY);

        g.restore();
    }

    public void setText(String text){_text = text;}
}
