package es.ucm.gdv.engine;

public abstract class AbstractGraphics implements Graphics {
    protected int _logic_width;
    protected int _logic_height;

    private double _scale = 1;
    private int _newTamX = 0;
    private int _newTamY = 0;
    public AbstractGraphics(int logic_width, int logic_height)
    {
        _logic_width = logic_width;
        _logic_height = logic_height;
        _newTamX = logic_width;
        _newTamY = logic_height;
    }

    //Calculamos la escala y el tamaño en la X e Y para que se mantenga el ratio original
    public void calculateLogicSize()
    {
        if(((double)this.getWidth() / (double)this.getHeight()) >= ((double)_logic_width /(double)_logic_height))
        {
            _scale = (double)this.getHeight() /(double)_logic_height;

            _newTamX = (int) ((double) this.getHeight() * ((double) _logic_width/ (double)_logic_height));
            _newTamY = this.getHeight();
        }
        else if (((double)this.getWidth() / (double)this.getHeight()) < ((double)_logic_width /(double)_logic_height))
        {
            _scale = (double)this.getWidth() /(double)_logic_width;

            _newTamX = this.getWidth();
            _newTamY = (int) ((double) this.getWidth() / ((double) _logic_width / (double) _logic_height));
        }
    }
    //Dada unas coordenadas en tamaño lógico devuelve la posición real en pantalla
    public double[] getScaledPosition(double[] position)
    {
        int transX = 0;
        int transY = 0;
        if(((double)this.getWidth() / (double)this.getHeight()) >= ((double)_logic_width /(double)_logic_height)) {
            transY = (int) (position[1] * this.getHeight()) / _logic_height;
            transX = (int) ((position[0] * _newTamX) / _logic_width) + (this.getWidth() - _newTamX) / 2;
        }
        else if(((double)this.getWidth() / (double)this.getHeight()) < ((double)_logic_width /(double)_logic_height))
        {
            transX = (int) (position[0] * this.getWidth()) / _logic_width;
            transY = (int) ((position[1] * _newTamY) / _logic_height) + (this.getHeight() - _newTamY) / 2;
        }


        return new double[]{transX, transY, _scale};
    }
}
