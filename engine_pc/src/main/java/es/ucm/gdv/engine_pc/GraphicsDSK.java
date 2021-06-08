package es.ucm.gdv.engine_pc;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;

import es.ucm.gdv.engine.AbstractGraphics;
import es.ucm.gdv.engine.Font;
import es.ucm.gdv.engine.Graphics;

public class GraphicsDSK extends AbstractGraphics {

    private JFrame _jFrame;
    private java.awt.image.BufferStrategy _strategy;
    private java.awt.Graphics _graphics;

    private  AffineTransform _old_canvas;

    //Inicializamos to do lo relacionado con jFrame
    public GraphicsDSK(boolean fullScreen, int logic_width, int logic_height, String titulo, JFrame jFrame){
        super(logic_width, logic_height);
        _jFrame = jFrame;
        _jFrame.setTitle(titulo);

        _jFrame.setSize(_logic_width, _logic_height);

        if(fullScreen) {
            _jFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
            _jFrame.setUndecorated(true);
        }

        _jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        _jFrame.setVisible(true);

        _jFrame.createBufferStrategy(2);
        _strategy = _jFrame.getBufferStrategy();
    }

    //Hacemos dispose del graphics
    public void dispose(){
        _graphics.dispose();
    }

    //Cogemos el DrawGraphics y si hay exepcion realmente no pasa nada
    @Override
    public void prepare(){
        try {
            _graphics = _strategy.getDrawGraphics();
        } catch (IllegalStateException e) {
            System.out.println("no pasa nada");
        }
    }

    //Le pedimos al bufferStrategy que presente en pantalla, de nuevo si hay excepcion realmente no pasa nada
    @Override
    public void present(){
        try {
        _strategy.show();
        } catch (IllegalStateException e) {
            System.out.println("no pasa nada");
        }
    }

    //Devolvemos una nueva fuente
    @Override
    public Font newFont(String filename, int size, boolean isBold) {
        return new FontDSK(filename, isBold, size);
    }

    //Rellenamos la pantalla de un color
    @Override
    public void clear(int r, int g, int b) {
        setColor(r, g, b, 255);
        fillRect(0, 0, getWidth(), getHeight());
    }

    //Trasladamos el canvas
    @Override
    public void translate(int x, int y) {
        _graphics.translate(x, y);
    }

    //Escalamos el canvas
    @Override
    public void scale(double x, double y) {
        Graphics2D g2d = (Graphics2D)_graphics;
        g2d.scale(x, y);
    }

    //Rotamos el canvas
    @Override
    public void rotate(float angle) {
        Graphics2D g2d = (Graphics2D)_graphics;
        g2d.rotate(Math.toRadians(angle));
    }

    //Guardamos el estado del canvas
    @Override
    public void save() {
        Graphics2D g2d = (Graphics2D)_graphics;
        _old_canvas = g2d.getTransform();
    }

    //Restauramos el estado del canvas
    @Override
    public void restore() {
        Graphics2D g2d = (Graphics2D)_graphics;
        g2d.setTransform(_old_canvas);
    }

    //Especificamos el color con el que se realizarán las próximas acciones
    @Override
    public void setColor(int r, int g, int b, int a) {
        _graphics.setColor(new Color(r, g, b, a));
    }

    //Dibujamos una linea
    @Override
    public void drawLine(int x1, int y1, int x2, int y2) {
        _graphics.drawLine(x1, y1, x2, y2);
    }

    //Dibujamos un rectángulo
    @Override
    public void fillRect(int x1, int y1, int x2, int y2) {
        _graphics.fillRect(x1, y1, x2 - x1, y2 - y1);
    }

    //Seleccionamos la fuente para el texto que vayamos a escribir
    @Override
    public void setFont(Font font){
        _graphics.setFont(((FontDSK)font).getFont());
    }

    //Escribimos texto
    @Override
    public void drawText(String text, int x, int y) {
        _graphics.drawString(text, x, y);
    }

    @Override
    public int getWidth() {
        return _jFrame.getWidth();
    }

    @Override
    public int getHeight() {
        return _jFrame.getHeight();
    }
}
