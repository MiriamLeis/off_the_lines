package es.ucm.gdv.desktop;

import javax.swing.JFrame;

import es.ucm.gdv.engine_pc.EngineDSK;
import es.ucm.gdv.logic.GameLogic;

public class Main {
    //Creamos el engine y el GameLogic
    public static void main(String[] args){
        boolean fullScreen = false;
        int logic_width = 640; int logic_height = 480;
        EngineDSK engine = new EngineDSK(fullScreen, logic_width,  logic_height, "Off the lines", new JFrame(), new GameLogic());
        engine.getGame().run();
    }
}
