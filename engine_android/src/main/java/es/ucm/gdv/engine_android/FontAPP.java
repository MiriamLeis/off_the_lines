package es.ucm.gdv.engine_android;

import android.content.res.AssetManager;
import android.graphics.Typeface;

import es.ucm.gdv.engine.Font;

public class FontAPP implements Font {
    private Typeface _font;
    private String _name = "";
    private boolean _bold = false;
    private int _size = 0;

    //Creamos la fuente
    public FontAPP(String filename, boolean bold, int size, AssetManager assetManager){
        _font = Typeface.createFromAsset(assetManager, filename);

        _bold = bold;
        _size = size;
    }

    Typeface getFont(){return _font;}
    boolean isBold(){return _bold;}
    int getSize(){return _size;}
}
