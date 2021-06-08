package es.ucm.gdv.engine;

import java.util.List;


// Proporciona las funcionalidades de entrada básicas. El juego no requiere un
// interfaz complejo, por lo que se utiliza únicamente la pulsación sobre la pantalla (o
// click con el ratón).
public interface Input {
    // Devuelve la lista de eventos recibidos desde la última invocación.
    List<TouchEvent> getTouchEvents();

    // Tipo de inputs.
    enum Type{
        PRESSED,
        RELEASED,
        DISPLACED
    }

    // Clase que representa la información de un toque sobre
    // la pantalla (o evento de ratón). Indicará el tipo (pulsación, liberación,
    // desplazamiento), la posición y el identificador del dedo (o boton).
    class TouchEvent {
        private Type _type;
        private float[] _position = new float[2];
        private int _id;

        public TouchEvent(Type type, float x, float y, int id) {
            _type = type;
            _position[0] = x;
            _position[1] = y;
            _id = id;
        }

        public Type getType() {
            return _type;
        }

        public float[] getPosition() {
            return _position;
        }

        public int getId() {
            return _id;
        }
    }
}
