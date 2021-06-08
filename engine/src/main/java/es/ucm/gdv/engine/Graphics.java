package es.ucm.gdv.engine;

// Proporciona las funcionalidades gráficas mínimas sobre la ventana de la
// aplicación.
public interface Graphics {

    void calculateLogicSize();
    double[] getScaledPosition(double[] position);


    void prepare();
    void present();
    // Crea una nueva fuente del tamaño especificado a partir de un fichero .ttf.
    // Se indica si se desea o no fuente en negrita.
    Font newFont(String filename, int size, boolean isBold);

    // Borra el contenido completo de la ventana, rellenándolo con un color
    // recibido como parámetro.
    void clear(int r, int g, int b);

    // Métodos de control de la transformación sobre el canvas.
    // Las operaciones de dibujado se verán afectadas por la transformación establecida.
    void translate(int x, int y);
    void scale(double x, double y);
    void rotate(float angle);
    void save();
    void restore();

    // Establece el color a utilizar en las operaciones de dibujado posteriores.
    void setColor(int r, int g, int b, int a);

    // Dibuja una linea.
    void drawLine(int x1, int y1, int x2, int y2);

    // Dibuja un rectangulo relleno.
    void fillRect(int x1, int y1, int x2, int y2);

    // Establece la fuente a utilizar en las operaciones de escritura posteriores.
    void setFont(Font font);

    // Escribe el texto con la fuente y color activos.
    void drawText(String text, int x, int y);

    // Devuelven el tamaño de la ventana.
    int getWidth();
    int getHeight();
}
