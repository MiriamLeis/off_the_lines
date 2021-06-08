package es.ucm.gdv.engine_pc;

import java.awt.FontFormatException;
import java.io.FileInputStream;
import java.io.IOException;

import es.ucm.gdv.engine.Font;

public class FontDSK implements Font {
    private java.awt.Font _font;

    //Creamos una fuente
    public FontDSK(String filename, boolean bold, float size) {
        try {
            _font = java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, new FileInputStream(filename));
            _font = _font.deriveFont(size);
            _font = _font.deriveFont(bold? java.awt.Font.BOLD : java.awt.Font.PLAIN);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
    }

    public java.awt.Font getFont(){return _font;}
}
