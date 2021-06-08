package es.ucm.gdv.logic;

public class BlinkText extends TextObject {
    private int[][] _colors;
    private int _idColor;
    private double _timeToChange;
    private double _cont;

    public BlinkText(String text, double[] pos, int[][] colors, State gameManager) {
        super(text, pos, colors[0], gameManager);
        _idColor = 0;
        _colors = colors;
        _timeToChange = 0.5;
    }

    public void setTime(double time){ _timeToChange = time;}

    //Cambiamos el color del texto
    private void changeColor(){
        _idColor++;
        if(_idColor >= _colors.length)
            _idColor = 0;
        _color = _colors[_idColor];
    }

    @Override
    public void update(double deltaTime){
        super.update(deltaTime);

        _cont += deltaTime;
        if(_cont >= _timeToChange){
            _cont = 0;
            changeColor();
        }
    }
}
