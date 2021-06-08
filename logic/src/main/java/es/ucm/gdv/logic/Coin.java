package es.ucm.gdv.logic;

public class Coin extends CubeObject {
    // controlar muerte
    private double _timeToDie;
    private double _cont;

    // opcional
    private double _radius;
    private double _speed;
    private double _angle;
    private double[] _rotatePosition;

    public Coin(State gameManager){
        super(8, 2, gameManager);
        setTag("coin");

        setColor(255, 255, 0);

        _timeToDie = 0.5;
        _cont = 0;

        _radius = Integer.MAX_VALUE;
        _sound = _gameManager.getSoundManager().createSound("data/sounds/itempick.wav", false);
    }

    public void setValues(double radius, double speed, double angle) {
        _radius = radius;
        _speed = -speed;
        _angle = angle;

        double angleRad = Math.toRadians(_angle);
        _rotatePosition = new double[]{_position[0], _position[1]};
        setPosition(_position[0] + _radius * Math.cos(angleRad), _position[1] + _radius * Math.sin(angleRad));
    }

    //Rotamos y movemos las monedas, si mueren borramos la moneda
    @Override
    public void update(double deltaTime){
        super.update(deltaTime);
        if(_radius != Integer.MAX_VALUE){
            _angle += _speed * deltaTime;
            _angle = _angle % 360;

            double angleRad = Math.toRadians(_angle);
            _position[0] = _rotatePosition[0] + _radius * Math.cos(angleRad);
            _position[1] = _rotatePosition[1] + _radius * Math.sin(angleRad);
        }

        if (_died) {
            if(_cont >= _timeToDie) {
                _gameManager.getSoundManager().releaseSound(_sound);
                _gameManager.deleteGameObject(this);
            }
            else{
                _ownScale += deltaTime * 2;
                _cont+=deltaTime;
            }
        }
    }
}
