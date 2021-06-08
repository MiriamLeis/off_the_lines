package es.ucm.gdv.engine_pc;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.Input;

public class InputDSK implements Input, MouseListener, MouseMotionListener, KeyListener {

    private List<TouchEvent> list;

    public InputDSK(){list = new ArrayList<TouchEvent>();}

    //Devolvemos una copia de la lista de eventos y creamos una nueva
    @Override
    public List<TouchEvent> getTouchEvents() {
        List<TouchEvent> copy;
        synchronized (this) {
            copy = list;
            list = new ArrayList<TouchEvent>();
        }
        return copy;
    }

    //------------- MOUSE LISTENER -------------//
    @Override
    public void mousePressed(MouseEvent mouseEvent) {
        synchronized (this) {
            list.add(new TouchEvent(Type.PRESSED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID()));
        }
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
        synchronized (this) {
            list.add(new TouchEvent(Type.RELEASED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID()));
        }
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {}

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {}

    @Override
    public void mouseExited(MouseEvent mouseEvent) {}

    //------------- MOUSE MOTION LISTENER -------------//
    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
        synchronized (this) {
            list.add(new TouchEvent(Type.DISPLACED, mouseEvent.getX(), mouseEvent.getY(), mouseEvent.getID()));
        }
    }
    @Override
    public void mouseMoved(MouseEvent mouseEvent) {}

    //------------- KEY LISTENER -------------//
    @Override
    public void keyPressed(KeyEvent keyEvent) {
        synchronized (this) {
            list.add(new TouchEvent(Type.PRESSED, -1.0f, -1.0f, keyEvent.getKeyCode()));
        }
    }

    @Override
    public void keyReleased(KeyEvent keyEvent) {
        synchronized (this) {
            list.add(new TouchEvent(Type.RELEASED, -1.0f, -1.0f, keyEvent.getKeyCode()));
        }
    }

    @Override
    public void keyTyped(KeyEvent keyEvent) {}
}
