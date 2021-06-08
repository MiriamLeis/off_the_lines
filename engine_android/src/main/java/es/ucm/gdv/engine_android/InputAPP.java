package es.ucm.gdv.engine_android;

import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import es.ucm.gdv.engine.Input;

public class InputAPP implements Input, View.OnTouchListener {

    private List<TouchEvent> list;

    public InputAPP(){

        list = new ArrayList<TouchEvent>();
    }

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

    //Métedo que detecta cuando se ha pulsado la pantalla
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
            synchronized (this){
                list.add(new TouchEvent(Type.PRESSED, motionEvent.getX(), motionEvent.getY(), motionEvent.getDeviceId()));
            }
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
            synchronized (this) {
                list.add(new TouchEvent(Type.RELEASED, motionEvent.getX(), motionEvent.getY(), motionEvent.getDeviceId()));
            }
        }
        else if(motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
            synchronized (this) {
                list.add(new TouchEvent(Type.DISPLACED, motionEvent.getX(), motionEvent.getY(), motionEvent.getDeviceId()));
            }
        }

        return true;
    }
}
