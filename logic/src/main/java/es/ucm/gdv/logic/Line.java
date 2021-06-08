package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Graphics;

public class Line extends GameObject{
    protected double[] _Avertex;
    protected double[] _Bvertex;
    protected double[] _AvertexOri;
    protected double[] _BvertexOri;

    protected double _length;

    private float[] _dir;
    private float[] _jumpDir;

    public Line(double Ax, double Ay, double Bx, double By, int r, int g, int b, State gameManager){
        super(gameManager);
        setTag("line");

        setColor(r, g, b);

        _Avertex = new double[2];
        _Bvertex = new double[2];


        setAVertex(Ax, Ay);
        setBVertex(Bx, By);

        _AvertexOri = new double[] {_Avertex[0], _Avertex[1]};
        _BvertexOri = new double[]{_Bvertex[0], _Bvertex[1]};

        _dir = new float[]{(float)(_Bvertex[0] - _Avertex[0]), (float)(_Bvertex[1] - _Avertex[1])};
        _dir = normalize(_dir);

        _jumpDir = null;

        setPosition((_Avertex[0] + _Bvertex[0]) / 2, (_Avertex[1] + _Bvertex[1]) / 2);

        _length = Math.abs(_Avertex[0] -_Bvertex[0]) + Math.abs(_Avertex[1] -_Bvertex[1]);
    }

    public void setAVertex(double Ax, double Ay){
        _Avertex[0] = Ax;
        _Avertex[1] = Ay;
    }

    public void setBVertex(double Bx, double By){
        _Bvertex[0] = Bx;
        _Bvertex[1] = By;
    }

    public void setJumpDir(float x, float y){ _jumpDir = new float[]{x, y}; }

    public double[] getAVertex(){ return _Avertex; }
    public double[] getBVertex(){ return _Bvertex; }
    public double[] getAVertexOri(){ return _AvertexOri; }
    public double[] getBVertexOri(){ return _BvertexOri; }
    public double getLenght(){ return _length; }

    public float[] getJumpDir(){ return _jumpDir; }
    public float[] getDir(){ return _dir; }

    //Renderiza las lineas
    public void render(Graphics g){
        super.render(g);
        g.save();

        double[] scaled = g.getScaledPosition(_position);
        int transX = (int)scaled[0];
        int transY = (int)scaled[1];
        double scale = scaled[2];

        g.translate(transX, transY);
        g.scale(scale, scale);

        g.setColor(_color[0], _color[1], _color[2], _alpha);

        g.drawLine((int)_Avertex[0] - (int)_position[0], (int)_Avertex[1] - (int)_position[1],
                (int)_Bvertex[0] - (int)_position[0], (int)_Bvertex[1] - (int)_position[1]);

        g.translate(-transX, -transY);

        g.restore();
    }

    //Normaliza un vector
    protected float[] normalize(float[] d) {
        float[] v2 = new float[2];

        float length = (float) Math.sqrt( d[0]*d[0] + d[1]*d[1] );
        if (length != 0) {
            v2[0] = d[0]/length;
            v2[1] = d[1]/length;
        }

        return v2;
    }
}
