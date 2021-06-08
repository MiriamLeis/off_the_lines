package es.ucm.gdv.logic;

import es.ucm.gdv.engine.Graphics;

public class Enemy extends Line {
    private int _speed;
    private float _time1;
    private float _time2;
    private int[] _iniPos;
    private int[] _finPos;
    private boolean _myDir;
    float[] _dir;
    float[] _dist;
    double _cont;

    double cont = 0;
    int time = 2;

    public Enemy(double Ax, double Ay, double Bx, double By, int r, int g, int b, State gameManager)
    {
        super(Ax, Ay, Bx, By, r, g, b, gameManager);
        setTag("enemy");

        _iniPos = new int[]{(int)_position[0], (int)_position[1]};
        _myDir = true;
    }

    public void setValues(int angle, int speed, int offset1, int offset2, float t1, float t2, double length) {
        _angle = angle;
        _speed = speed;
        _length = length;

        _finPos = new int[] {offset1, offset2};

        if(offset1 != Integer.MAX_VALUE) {
            _finPos[0] += (int) _position[0];
            _finPos[1] += (int) _position[1];

            _dist = new float[] {Math.abs(offset1), Math.abs(offset2)};

            _dir = new float[]{_finPos[0] - _iniPos[0], _finPos[1] - _iniPos[1]};
            _dir = normalize(_dir);
        }

        _time1 = t1;
        _time2 = t2;
        _cont = _time2 + 1;
    }

    //Rotamos y movemos a los enemigos
    @Override
    public void update(double deltaTime) {
        super.update(deltaTime);
        if(_time1 != Float.MAX_VALUE)
            move(deltaTime);
        if(_speed != 0)
            rotate(deltaTime);

    }
    //Actualizamos el angulo
    private void rotate(double deltaTime)
    {
        _angle -= _speed * deltaTime;
        _angle = _angle % 360;

    }
    //Actualizamos la posición y los vértices de las lineas
    private void move(double deltaTime)
    {
        if(_cont >= _time2) {
            _position[0] += (_dist[0] / _time1) * deltaTime * _dir[0];
            _position[1] += (_dist[1] / _time1) * deltaTime * _dir[1];

            setAVertex((_position[0] - _length / 2), (_position[1]));
            setBVertex((_position[0] + _length / 2), (_position[1]));

            changeDir();
        }
        else
            _cont += deltaTime;
    }

    //Comprobamos si hay que cambiar la direccion
    private void changeDir(){
        int[] check;
        // choose which vertex pay attention.
        if(_myDir)
            check = _finPos;
        else
            check = _iniPos;

        // check if we surpased the vertex.
        boolean surpased = false;
        if(_dir[0] < 0) {
            if (_position[0] < check[0])
                surpased = true;
        }
        else if(_dir[0] > 0) {
            if (_position[0] > check[0])
                surpased = true;
        }
        if(_dir[1] < 0) {
            if (_position[1] < check[1])
                surpased = true;
        }
        else if(_dir[1] > 0) {
            if (_position[1] > check[1])
                surpased = true;
        }

        // change my dir if surpased
        if(surpased) {
            _myDir = !_myDir;

            int[] init;
            if(_myDir)
            {
                _dir[0] = _finPos[0] - _iniPos[0];
                _dir[1] = _finPos[1] - _iniPos[1];
                init = _iniPos;
            }
            else
            {
                _dir[0] = _iniPos[0] - _finPos[0];
                _dir[1] = _iniPos[1] - _finPos[1];
                init = _finPos;
            }
            _dir = normalize(_dir);

            setPosition(init[0], init[1]);
            setAVertex((int)(_position[0] - _length/2), (int)(_position[1]));
            setBVertex((int)(_position[0] + _length/2), (int)(_position[1]));

            _cont = 0;
        }
    }

    //Renderizamos al enemigo
    public void render(Graphics g){

        g.save();

        double[] scaled = g.getScaledPosition(_position);
        int transX = (int)scaled[0];
        int transY = (int)scaled[1];
        double scale = scaled[2];

        g.translate(transX, transY);
        g.scale(scale, scale);

        g.rotate(_angle);
        g.setColor(_color[0], _color[1], _color[2], _alpha);

        g.drawLine((int)_Avertex[0] - (int)_position[0], (int)_Avertex[1] - (int)_position[1],
                (int)_Bvertex[0] - (int)_position[0], (int)_Bvertex[1] - (int)_position[1]);

        g.translate(-transX, -transY);

        g.restore();
    }
}

