package es.ucm.gdv.logic;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import es.ucm.gdv.engine.Engine;

public class ReadJSON {
    private Engine _engine;
    private String _fontDir = "data/fonts/";

    public ReadJSON(Engine engine){
        _engine = engine;
    }

    //Leemo el nivel
    public void readLevel(int level, Player p, Mode mode, ArrayList<GameObject> gameObjects, State gameManager){
        InputStream read = _engine.openInputStream("data/levels.json");
        JSONParser jsonParser = new JSONParser();
        JSONArray jsonArray = null;
        try {
            jsonArray = (JSONArray) jsonParser.parse(
                    new InputStreamReader(read, "UTF-8"));
        }catch (java.io.IOException | org.json.simple.parser.ParseException e)
        {
            System.out.println("ERROR: " + e.getMessage());
        }
        JSONObject mapLevel = (JSONObject)jsonArray.get(level);

        //---------------- TEXTO ---------------//
        String nameLevel = mapLevel.get("name").toString();
        nameLevel = "Level " + (level + 1) + " - " + nameLevel;
        TextObject textObject = new TextObject(nameLevel, new double[]{5.0, 25.0},new int[]{255, 255, 255}, gameManager);
        textObject.createFont(_engine.getGraphics(), _fontDir + "BungeeHairline-Regular.ttf", 20, false);
        gameObjects.add(textObject);

        //---------------- MAPA ---------------//
        ArrayList<ArrayList<Line>> paths = new ArrayList<ArrayList<Line>>();

        JSONArray pathsArray = (JSONArray) mapLevel.get("paths");
        Iterator<JSONObject> iterator = pathsArray.iterator();

        while (iterator.hasNext()) {
            createPath(paths, iterator.next(), gameObjects, gameManager);
        }

        //---------------- MONEDAS ---------------//
        ArrayList<Coin> coins = new ArrayList<Coin>();
        JSONArray itemsArray = (JSONArray) mapLevel.get("items");
        createItems(itemsArray, gameObjects, gameManager, coins);

        //---------------- ENEMIGOS ---------------//
        ArrayList<Enemy> enemies = new ArrayList<Enemy>();
        JSONArray enemiesArray = (JSONArray) mapLevel.get("enemies");
        if(enemiesArray != null)
            createEnemies(enemiesArray, gameObjects, gameManager, enemies);

        //---------------- JUGADOR ---------------//
        p.setPaths(paths);
        p.setCoins(coins);
        p.setEnemies(enemies);
        gameObjects.add(p);

    }

    //Creamos los paths
    private void createPath(ArrayList<ArrayList<Line>> paths, JSONObject mapLevel, ArrayList<GameObject> gameObjects, State gameManager){
        ArrayList<Line> newPath = new ArrayList<Line>();

        JSONArray vertexArray = (JSONArray) mapLevel.get("vertices");
        Iterator<JSONObject> iterator = vertexArray.iterator();

        JSONObject firstVertex = iterator.next();
        JSONObject otherVertex = null;
        JSONObject lastVertex = firstVertex;

        boolean dir_exist = false;
        JSONArray dirArray = (JSONArray) mapLevel.get("directions");
        Iterator<JSONObject> dir_iterator = null;
        if(dirArray != null) {
            dir_exist = true;
            dir_iterator = dirArray.iterator();
        }

        while (iterator.hasNext()) {
            otherVertex = iterator.next();

            Line l = createLine(lastVertex, otherVertex, gameManager);
            if(dir_exist)
                createDir(dir_iterator, l);

            newPath.add(l);
            gameObjects.add(l);

            lastVertex = otherVertex;
        }

        Line l = createLine(lastVertex, firstVertex, gameManager);
        if(dir_exist)
            createDir(dir_iterator, l);

        newPath.add(l);
        gameObjects.add(l);

        paths.add(newPath);
    }

    //Creamos las lineas
    private Line createLine(JSONObject AVertex, JSONObject BVertex, State gameManager)
    {
        double aX = 0;
        double aY = 0;
        double bX = 0;
        double bY = 0;


        Number temp = (Number)AVertex.get("x");
        aX = temp.doubleValue();

        temp = (Number)AVertex.get("y");
        aY = temp.doubleValue();

        temp = (Number)BVertex.get("x");
        bX = temp.doubleValue();

        temp = (Number)BVertex.get("y");
        bY = temp.doubleValue();

        return new Line((Utils._WIDTH / 2) + aX, (Utils._HEIGHT / 2) - aY,
                (Utils._WIDTH / 2) + bX, (Utils._HEIGHT / 2) - bY,
                255 , 255 ,255, gameManager);
    }

    //Creamos las direcciones
    private void createDir(Iterator<JSONObject> it, Line line){
        JSONObject dir = it.next();
        Number temp = (Number) dir.get("x");
        float x = temp.floatValue();

        temp = (Number) dir.get("y");
        float y = temp.floatValue();
        line.setJumpDir(x, -y);
    }

    //Creamos los items
    private void createItems(JSONArray itemsArray, ArrayList<GameObject> gameObjects, State gameManager, ArrayList<Coin> coins)
    {
        Iterator<JSONObject> iterator = itemsArray.iterator();

        while (iterator.hasNext()) {
            Coin c = new Coin(gameManager);
            coins.add(c);

            JSONObject obj = iterator.next();

            Number temp = (Number)obj.get("x");
            int x = (int)(temp.longValue());

            temp = (Number)obj.get("y");
            int y = (int)(temp.longValue());

            c.setPosition((Utils._WIDTH / 2) + x,(Utils._HEIGHT / 2) - y);

            temp = (Number)obj.get("radius");
            double radius = 0;
            if (temp != null) {
                radius = temp.doubleValue();

                temp = (Number) obj.get("speed");
                double speed = temp.doubleValue();

                temp = (Number) obj.get("angle");
                double angle = temp.doubleValue();

                c.setValues(radius, speed, angle);
            }

            gameObjects.add(c);
        }
    }

    //Creamos los enemigos
    private void createEnemies(JSONArray itemsArray, ArrayList<GameObject> gameObjects, State gameManager, ArrayList<Enemy> enemies)
    {
        Iterator<JSONObject> iterator = itemsArray.iterator();

        while (iterator.hasNext()) {
            JSONObject obj = iterator.next();

            Number temp = (Number)obj.get("x");
            int x = (int)(temp.longValue());

            temp = (Number)obj.get("y");
            int y = (int)(temp.longValue());

            temp = (Number)obj.get("length");
            double length = (temp.doubleValue());

            Enemy e = new Enemy((Utils._WIDTH / 2) + x - length/2 , (Utils._HEIGHT / 2) - y,
                    (Utils._WIDTH / 2) + x + length/2 , (Utils._HEIGHT / 2) - y, 255, 0 ,0, gameManager);
            enemies.add(e);

            temp = (Number)obj.get("angle");
            int angle = (int)(temp.longValue());

            temp = (Number)obj.get("speed");
            int speed = 0;
            if (temp != null)
                speed = (int)(temp.longValue());

            JSONObject offset = (JSONObject)obj.get("offset");
            int of1 = Integer.MAX_VALUE; int of2=Integer.MAX_VALUE;
            if (offset != null)
            {
                Number temp2 = (Number)offset.get("x");
                of1 = (int)(temp2.longValue());

                temp2 = (Number)offset.get("y");
                of2 = -(int)(temp2.longValue());
            }
            temp = (Number)obj.get("time1");
            float t1 = Float.MAX_VALUE;
            if (temp != null)
                t1 = temp.floatValue();

            temp = (Number)obj.get("time2");
            float t2 = Float.MAX_VALUE;
            if (temp != null)
                t2 = temp.floatValue();

            e.setValues(-angle, speed, of1, of2, t1, t2, length);
            gameObjects.add(e);
        }
    }
}
