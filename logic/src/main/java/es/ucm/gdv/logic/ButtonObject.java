package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Font;
import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

public class ButtonObject extends GameObject {
    private double[] _scaled_position;
    private int _width, _height;
    private int _scaled_width, _scaled_height;
    private boolean _haveBox;
    private TextObject _text;
    private FillRectObject _box;

    public ButtonObject(double[] pos, int w, int h, State gameManager) {
        super(gameManager);
        setPosition(pos[0], pos[1]);
        _scaled_position = new double[]{pos[0], pos[1]};
        _width = w;
        _scaled_width = w;
        _height = h;
        _scaled_height = h;
        _haveBox = false;
        _text = null;
    }

    //Creamos el texto del boton
    public void createText(Graphics g, String text, int[] color, String nameFont, int size, boolean bold){
        _text = new TextObject(text, new double[]{_position[0], _position[1] + _height}, color, _gameManager);
        _text.setFont(g.newFont(nameFont, size, bold));
    }

    //Creamos caja si queremos que tenga
    public void createBox(int[] color){
        _haveBox = true;
        _box = new FillRectObject(_position, _width, _height, color, _gameManager);
    }

    @Override
    public void handleInput(Input.TouchEvent e){
        if(e.getType() == Input.Type.PRESSED)
            if(isInside(e.getPosition()))
                onClick();
    }

    //Comprobamos si hemos clicado dentro del boton
    private boolean isInside(float[] pos){
        return (pos[0] >= _scaled_position[0] && pos[0] <= _scaled_position[0] + _scaled_width) &&
                (pos[1] >= _scaled_position[1] && pos[1] <= _scaled_position[1] + _scaled_height);
    }

    protected void onClick(){}

    //Renderizamos to do
    @Override
    public void render(Graphics g){
        double[] scaled = g.getScaledPosition(_position);
        int transX = (int)scaled[0];
        int transY = (int)scaled[1];
        double scale = scaled[2];

        _scaled_position[0] = scaled[0];
        _scaled_position[1] = scaled[1];

        _scaled_width = (int)(_width * scale);
        _scaled_height = (int)(_height * scale);

        if(_haveBox)
            _box.render(g);

        if(_text != null)
            _text.render(g);
    }
}
