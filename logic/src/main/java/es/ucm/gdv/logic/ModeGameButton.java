package es.ucm.gdv.logic;

public class ModeGameButton extends ButtonObject {
    private Mode _mode;
    private boolean _clicked;

    public ModeGameButton(Mode mode, double[] pos, int w, int h, State gameManager) {
        super(pos, w, h, gameManager);
        _mode = mode;
        _clicked = false;
    }

    @Override
    protected void onClick() {
        super.onClick();
        _clicked = true;
    }

    public Mode getMode(){return _mode;}
    public boolean isClicked(){return _clicked;}
}
