package game.utilities;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.errors.IllegalPositioningSystemArgument;

import static game.utilities.Variables.CHUNKSIZE;

public class Positioning {
    private int x;
    private int y;
    private String system;

    public Positioning(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Positioning(int x, int y, String system) throws IllegalPositioningSystemArgument {
        this.x = x;
        this.y = y;
        this.system = system.toLowerCase();

        if (!system.equalsIgnoreCase("world") && !system.equalsIgnoreCase("chunk") && !system.equalsIgnoreCase("screen")){
            throw new IllegalPositioningSystemArgument(system);
        }
    }

    // Getters & Setters:
    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Raylib.Vector2 getVector2(){
        return new Jaylib.Vector2(x, y);
    }
    // ----------------------------------------------------------

    // Set positioning system:
    public Positioning world(){
        setSystem("world");
        return this;
    }

    public Positioning chunk(){
        setSystem("chunk");
        return this;
    }

    public Positioning screen(){
        setSystem("screen");
        return this;
    }
    // ----------------------------------------------------------

    // Stream conversion:
    public Positioning toWorld(){
        if (getSystem().equals("world")){
            return this;
        }
        if (getSystem().equals("chunk")){
            setX(x * CHUNKSIZE);
            setY(y * CHUNKSIZE);
            return this;
        }
        if (getSystem().equals("screen")){
            return null;
        }
        throw new IllegalPositioningSystemArgument(system);
    }
    // ----------------------------------------------------------

    // Static position conversion:
    public static Raylib.Vector2 chunkToWorld(int chunkX, int chunkZ){
        return null;
    }

    public static Jaylib.Vector2 worldToChunk(int worldX, int worldZ){
        return null;
    }

    public static Raylib.Vector2 screenToWorld(int screenX, int screenZ){
        return null;
    }

    public static Raylib.Vector2 worldToScreen(int worldX, int worldZ){
        return null;
    }
    // ----------------------------------------------------------
}
