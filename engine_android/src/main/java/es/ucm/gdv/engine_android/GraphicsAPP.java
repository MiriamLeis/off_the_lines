package es.ucm.gdv.engine_android;

import android.content.res.AssetManager;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.view.SurfaceView;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.Font;
import es.ucm.gdv.engine.Graphics;

public class GraphicsAPP extends AbstractGraphics {
    private SurfaceView _view;
    private Canvas _canvas;
    private Paint _paint;
    private AssetManager _assetManager = null;

    public GraphicsAPP(int logic_width, int logic_height, SurfaceView view, AssetManager assetManager) {
        super(logic_width, logic_height);
        _view = view;
        _paint = new Paint();
        _assetManager = assetManager;
    }

    //Esperamos a que el surface sea valido y hacemos lock del canvas en ese moemento
    @Override
    public void prepare(){
        while(!_view.getHolder().getSurface().isValid())
            ;
        _canvas = _view.getHolder().lockCanvas();
    }

    //Hacemos unlock del canvas
    @Override
    public void present(){
        _view.getHolder().unlockCanvasAndPost(_canvas);
    }

    //Devuelve una nueva fuente
    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        return new FontAPP(filename, isBold, size, _assetManager);
    }

    //Pintamos la pantalla de un color específico
    @Override
    public void clear(int r, int g, int b) {
        setColor(r, g, b, 255);
        _canvas.drawColor(_paint.getColor());
    }

    //Trasladamos el canvas a una posicion
    @Override
    public void translate(int x, int y) {
        _canvas.translate(x, y);
    }

    //Escalamos el canvas
    @Override
    public void scale(double x, double y) {
        _canvas.scale((float)x, (float)y);
    }

    //Rotamos el canvas
    @Override
    public void rotate(float angle) {
        _canvas.rotate(angle);
    }

    //Guardamos el estado del canvas
    @Override
    public void save() {
        _canvas.save();
    }

    //Restauramos el estado guardado del canvas
    @Override
    public void restore() {
        _canvas.restore();
    }

    //Pasamos de tener 4 variables para el color a solo una
    private int getIntFromColor(int r, int g, int b, int a){
        a = (a << 24) & 0xFF000000;
        r = (r << 16) & 0x00FF0000;
        g = (g << 8) & 0x0000FF00;
        b = b & 0x000000FF;

        return a | r | g | b;
    }

    //Elegimos el color con el que se realizarán las próximas acciones
    @Override
    public void setColor(int r, int g, int b, int a) {
        _paint.setColor(getIntFromColor(r, g, b, a));
    }

    //Dibujamos una línea
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        _canvas.drawLine(x1, y1, x2, y2, _paint);
    }

    //Rellenamos un rectángulo
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        _canvas.drawRect(x1, y1, x2, y2, _paint);
    }

    //Seleccionamos la fuente
    @Override
    public void setFont(Font font){
        FontAPP fontAPP = (FontAPP) font;

        _paint.setTypeface(fontAPP.getFont());
        _paint.setTextSize(fontAPP.getSize());
        _paint.setFakeBoldText(fontAPP.isBold());
    }

    //Dibujamos texto
    @Override
    public void drawText(String text, int x, int y) {
        _canvas.drawText(text, x, y, _paint);
    }

    @Override
    public int getWidth() {
        return _canvas.getWidth();
    }

    @Override
    public int getHeight() {
        return _canvas.getHeight();
    }
}
