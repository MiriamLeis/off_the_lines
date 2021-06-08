package es.ucm.gdv.logic;

import java.util.ArrayList;

import es.ucm.gdv.engine.Graphics;
import es.ucm.gdv.engine.Input;

public class Player extends CubeObject {
    // propiedades del jugador
    private int _lifes;
    private int _totalLifes;
    private int _velocity;
    private int _jumpVel;
    private int _distCol;
    private double _distInLine;

    private boolean _jumping;
    private boolean _myDir;

    float[] _dir;
    float[] _dirJump;
    double[] _posBeforeJump;

    //conocimiento de su entorno
    private ArrayList<ArrayList<Line>> _paths;
    private ArrayList<Coin> _coins;
    private ArrayList<Enemy> _enemies;
    private int _pathID;
    private int _lineID;
    private double[] _lastPos;

    //contador de muerte, en segundos
    private ArrayList<ArrayList<Double>> _animInfo;
    private boolean _dead;
    private boolean _doAnim;
    private double _animCont;
    private double _waitCont;
    private double _cont;

    private boolean _passLevel;

    public Player(Mode mode, State gameManager){
        super(12, 2, gameManager);
        setTag("player");

        setColor(0, 136, 255);

        _animInfo = new ArrayList<ArrayList<Double>>();

        _jumpVel = 1500;
        _distCol = 20;

        if(mode == Mode.EASY)
        {
            _totalLifes = 10;
            _velocity = 250;
        }
        else
        {
            _totalLifes = 5;
            _velocity = 400;
        }
        _lifes = _totalLifes;

        _dirJump = new float[2];
        _dir = new float[2];
        _posBeforeJump = new double[2];

        _animCont = 0.5;
        _waitCont = _animCont + 0.25;


        _sound = _gameManager.getSoundManager().createSound("data/sounds/death.wav", false);
    }

    public void prepare(){
        _died = false;
        _dead = false;
        _doAnim = true;

        _passLevel = false;

        _myDir = true;
        _distInLine = 0;

        _cont = 0;
    }

    //Saltamos si presionamos el boton
    @Override
    public void handleInput(Input.TouchEvent e){
        if(e.getType() == Input.Type.PRESSED && !_jumping)
           jump();
    }

    //Si estamos muertos hacemos la animacion de muerte
    //Si cogemos todas la monedas esperamos un segundo hasta cambiar de nivel
    //Si estamos saltando comprobamos colisiones con to do
    //Si nos estamos moviendo solo comprobamos colision con los enemigos
    @Override
    public void update(double deltaTime){
        super.update(deltaTime);
        if(!_died) {
            if (_coins.isEmpty()) {
                _cont += deltaTime;
                if (_cont >= 1)
                    _passLevel = true;
            }

            if (_jumping) {
                moveJump(deltaTime);
                checkCollision();
            } else {
                moveInLine(deltaTime);
                checkCollisionEnemies();
            }
        }
        else{
            _cont += deltaTime;

            if(_cont >= _animCont){
                _doAnim = false;
                if(_cont >= _waitCont) {
                    _gameManager.getSoundManager().stopSound(_sound);
                    if (_lifes <= 0)
                        _dead = true;
                }
            }

            if(_doAnim)
                updateInfoLinesDeath(deltaTime);
        }
    }

    //Renderizamos la animacion de muerte o el jugador y las vidas
    @Override
    public void render(Graphics g) {
        if(_died)
            drawDeath(g);
        else
            super.render(g);

        drawLifes(g);
    }

    //Actualizamos la animcaion de muerte, posicion en la x, y, angulo
    private void updateInfoLinesDeath(double deltaTime){
        for(int i = 0; i < 10; i++) {
            ArrayList<Double> info = _animInfo.get(i);

            info.set(0, info.get(0) + (info.get(2) * info.get(4) * deltaTime));
            info.set(1, info.get(1) + (info.get(3) * info.get(4) * deltaTime));

            info.set(5, (info.get(5) + (info.get(6) * deltaTime)) % 360);
        }
    }

    //Creamos los datos de la animacion de muerte
    private void setInfoLinesDeath(){
        _animInfo.clear();
        int radio = 6 / 2;
        for(int i = 0; i < 10; i++){
            ArrayList<Double> info = new ArrayList<Double>();

            //posicion
            info.add(_position[0]);
            info.add(_position[1]);
            // direccion
            info.add(Math.random() * 2 - 1);
            info.add(Math.random() * 2 - 1);
            // velocidad
            info.add((Math.random() * 80) + 30);
            //angulo
            info.add(Math.random() * 360);
            //sentido de rotacion
            info.add(Math.random() < 0.5 ? -1.0 : 1.0);

            _animInfo.add(info);
        }
    }

    //Dibujamos las lineas de la animacion de muerte
    private void drawDeath(Graphics g){
        int radio = 6 / 2;

        for(int i = 0; i < 10; i++){
            g.save();

            ArrayList<Double> info = _animInfo.get(i);

            double[] scaled = g.getScaledPosition(new double[]{info.get(0), info.get(1)});
            int transX = (int)scaled[0];
            int transY = (int)scaled[1];
            double scale = scaled[2];

            g.translate(transX, transY);
            g.scale(scale * _ownScale, scale * _ownScale);

            g.rotate(info.get(5).floatValue());

            g.setColor(_color[0], _color[1], _color[2], _alpha);

            g.drawLine(-radio,0, radio, 0);

            g.restore();
        }
    }

    //Dibujamos las vidas
    private void drawLifes(Graphics g){
        int radio = (_tam - (_tam/ 3)) / 2;

        int spaceBetween = 10;
        for(int i = 1; i <= _totalLifes; i++) {
            g.save();

            double[] posLife = new double[]{(Utils._WIDTH - 20) - radio - ((spaceBetween + (radio * 2)) * (i - 1)), 20};

            double[] scaled = g.getScaledPosition(posLife);
            int transX = (int)scaled[0];
            int transY = (int)scaled[1];
            double scale = scaled[2];

            g.translate(transX, transY);
            g.scale(scale * _ownScale, scale * _ownScale);

            // dibujar las vidas actuales
            if((_totalLifes - _lifes) < i){
                g.setColor(_color[0], _color[1], _color[2], _alpha);

                g.drawLine(-radio, -radio, radio, -radio);
                g.drawLine(radio, -radio, radio, radio);
                g.drawLine(radio, radio, -radio, radio);
                g.drawLine(-radio, radio, -radio, -radio);
            }
            else {
                g.setColor(255, 0, 0, 255);

                g.drawLine(-radio, -radio, radio, radio);
                g.drawLine(radio, -radio, -radio, radio);
            }

            g.restore();
        }
    }

    //Cogemos la direccion de salto
    private void jump(){
        _jumping = true;

        float[] jumpDir = _paths.get(_pathID).get(_lineID).getJumpDir();
        if(jumpDir != null) {
            _dirJump[0] = jumpDir[0];
            _dirJump[1] = jumpDir[1];
        }
        else
            _dirJump = Utils.getPerpendicular(_myDir, _dir);

        _posBeforeJump[0] = _position[0];
        _posBeforeJump[1] = _position[1];
    }

    //Nos movemos mientras saltamos
    private void moveJump(double deltaTime) {
        _lastPos[0] = _position[0];
        _lastPos[1] = _position[1];

        _position[0] += (double) _jumpVel * deltaTime * (double) _dirJump[0];
        _position[1] += (double) _jumpVel * deltaTime * (double) _dirJump[1];
    }

    //Nos movemos sobre la linea
    private void moveInLine(double deltaTime){
        _lastPos[0] = _position[0];
        _lastPos[1] = _position[1];

        double _movementX = (double)_velocity * deltaTime * (double)_dir[0];
        double _movementY = (double)_velocity * deltaTime * (double)_dir[1];

        double[] newPos = changeLine(Math.abs(_movementX) + Math.abs(_movementY));

        _position[0] = newPos[0];
        _position[1] = newPos[1];
        if(_myDir) {
            double[] Avertex = _paths.get(_pathID).get(_lineID).getAVertex();
            _distInLine = Math.abs(Avertex[0] - _position[0]) + Math.abs(Avertex[1] - _position[1]);
        }
        else
        {
            double[] Bvertex = _paths.get(_pathID).get(_lineID).getBVertex();
            _distInLine = Math.abs(Bvertex[0] - _position[0]) + Math.abs(Bvertex[1] - _position[1]);
        }

    }

    //Comprobamos las colisiones con todos los objetos
    private void checkCollision() {
        checkCollisionEnemies();
        checkCollisionCoins();
        checkCollisionPaths();
        checkOutOfBounds();
    }
    //Colision con paths
    private void checkCollisionPaths() {

        boolean collision = false;
        int size = _paths.size();
        for(int i = 0; i < size && !collision; i++) {
            int pathSize = _paths.get(i).size();
            for (int j = 0; j < pathSize && !collision; j++) {
                double[] A = _paths.get(i).get(j).getAVertex();
                double[] B = _paths.get(i).get(j).getBVertex();

                double[] result = Utils.segmentsIntersection(_position[0], _position[1],
                        _lastPos[0], _lastPos[1],
                        A[0], A[1], B[0], B[1], true);

                if (result != null && Math.abs(result[0] - _posBeforeJump[0]) + Math.abs(result[1] - _posBeforeJump[1]) > 3) {
                    boolean realColission = false;
                    if (_pathID == i) {
                        if (_lineID != j) {
                            float[] dirJump = _paths.get(i).get(j).getJumpDir();
                            float[] previousDirJump = _paths.get(i).get(j).getJumpDir();
                            if (dirJump == null)
                                realColission = true;
                            else if (dirJump[0] != previousDirJump[0] || dirJump[1] != previousDirJump[1])
                                realColission = true;
                        }
                    } else
                        realColission = true;

                    if (realColission) {
                        collision = true;
                        _jumping = false;
                        _pathID = i;
                        _lineID = j;
                        float[] lineDir = _paths.get(_pathID).get(_lineID).getDir();
                        float diff1 = Math.abs(_dir[0] - lineDir[0]) + Math.abs(_dir[1] - lineDir[1]);
                        float diff2 = Math.abs(_dir[0] - (-lineDir[0])) + Math.abs(_dir[1] - (-lineDir[1]));

                        _position[0] = result[0];
                        _position[1] = result[1];
                        _lastPos[0] = result[0];
                        _lastPos[1] = result[1];

                        if (diff1 < diff2) {
                            _dir[0] = lineDir[0];
                            _dir[1] = lineDir[1];
                            _myDir = true;
                        } else {
                            _dir[0] = -lineDir[0];
                            _dir[1] = -lineDir[1];
                            _myDir = false;

                        }
                    }
                }
            }
        }
    }
    //Colision con monedas
    private void checkCollisionCoins() {

        boolean collision = false;
        int size = _coins.size();
        ArrayList<Integer> toDelete = new ArrayList<Integer>();

        for(int i = 0; i < size && !collision; i++) {
            double[]coinPos = _coins.get(i).getPosition();
            if(Utils.sqrDistancePointSegment(_position[0],_position[1], _lastPos[0], _lastPos[1], coinPos[0], coinPos[1]) <= _distCol)
            {
                //collision = true;
                _coins.get(i).die();
                toDelete.add(i);
            }
        }

        for(int i = toDelete.size() - 1; i >= 0; i--)
        {
            _coins.remove(toDelete.get(i).intValue());
        }
    }
    //Colision con enemigos
    private void checkCollisionEnemies() {

        boolean collision = false;
        int size = _enemies.size();

        for(int i = 0; i < size && !collision; i++) {
            double[][] vertexRotated = Utils.rotate(_enemies.get(i), _enemies.get(i).getAngle());
            double[] A = vertexRotated[0];
            double[] B = vertexRotated[1];

            double[] result = Utils.segmentsIntersection(_position[0], _position[1],
                    _lastPos[0], _lastPos[1],
                    A[0], A[1], B[0], B[1], false);
            if(result != null) {
                collision = true;
                manageDeath();
            }
        }

    }

    //Comprobamos si nos hemos salido del mapa
    private void checkOutOfBounds(){
        int margen = _tam + 5;
        if(_position[0] < (0 - margen) || _position[0] > (Utils._WIDTH + margen) ||
        _position[1] < (0 - margen) || _position[1] > (Utils._HEIGHT + margen)) {
            manageDeath();
        }
    }

    //Hacemos lo necesario al morir
    private void manageDeath(){
        die();
        _lifes--;
        setInfoLinesDeath();
    }

    //Comprobamos si tenemos que cambiar de linea
    private double[] changeLine(double distToMake){

        while(distToMake >= 0.0) {
            double actualLineLenght = _paths.get(_pathID).get(_lineID).getLenght();

            double distToEndLine = actualLineLenght - _distInLine;

            if (distToEndLine > distToMake) {
                return new double[]{_position[0] + (distToMake * _dir[0]), _position[1] + (distToMake * _dir[1])};
            } else {
                if (_myDir) {
                    _lineID++;
                    if (_paths.get(_pathID).size() <= _lineID)
                        _lineID = 0;
                } else {
                    _lineID--;
                    if (_lineID < 0)
                        _lineID = _paths.get(_pathID).size() - 1;
                }
                _distInLine = 0;
                distToMake -= distToEndLine;
                setDirPos(_myDir);
            }
        }

        return _position;
    }

    //Cambiamos la direccion
    private void changeDir(){
        float[] dirLine = _paths.get(_pathID).get(_lineID).getDir();
        if(!_myDir)
        {
            _dir[0] = dirLine[0] * -1;
            _dir[1] = dirLine[1] * -1;
        }
        else
        {
            _dir[0] = dirLine[0];
            _dir[1] = dirLine[1];
        }

    }

    //Cambiamos la direccion y ponemos la posición inicial
    private void setDirPos(boolean b) {
        changeDir();

        double[] init;
        if(b)
            init = _paths.get(_pathID).get(_lineID).getAVertex();
        else
            init = _paths.get(_pathID).get(_lineID).getBVertex();

        setPosition(init[0], init[1]);
    }

    //Reseteamos al jugador
    private void resetPlayer(){
        _pathID = 0;
        _lineID = 0;
        _myDir = true;
        _jumping = false;

        setDirPos(_myDir);
        _lastPos = new double[]{_position[0], _position[1]};
    }
    public void setPaths(ArrayList<ArrayList<Line>> p){
        _paths = p;
        resetPlayer();
    }
    public void setCoins(ArrayList<Coin> p){
        _coins = p;
    }
    public void setEnemies(ArrayList<Enemy> p){
        _enemies = p;
    }

    public boolean isDead(){return _dead;}
    public boolean isDeath(){return (_died && _cont >= _waitCont);}
    public boolean passLevel(){ return (_passLevel && !_died); }
}
