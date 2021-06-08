package es.ucm.gdv.android;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.SurfaceView;

import es.ucm.gdv.engine_android.EngineAPP;
import es.ucm.gdv.engine_android.GameAPP;
import es.ucm.gdv.logic.GameLogic;

public class MainActivity extends AppCompatActivity {
    EngineAPP _engine;

    //Creamos el engine y el GameLogic
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int logic_width = 640; int logic_height = 480;

        SurfaceView view = new SurfaceView(this);
        setContentView(view);
        _engine = new EngineAPP(logic_width, logic_height, view, new GameLogic(), this.getAssets());
    }

    //Reaccionamos a la llamada del sistema
    protected void onResume(){
        super.onResume();
        ((GameAPP)_engine.getGame()).resume();
    }
    //Reaccionamos a la llamada del sistema
    protected void onPause(){
        super.onPause();
        ((GameAPP)_engine.getGame()).pause();
    }
}