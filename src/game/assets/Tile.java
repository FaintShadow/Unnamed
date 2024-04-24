package game.assets;

import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.Map;

public class Tile {
    private Jaylib.Vector2 position;
    private Raylib.Color color;
    private Map<String, Boolean> collision = new HashMap<>();
    private Id id;

    public Jaylib.Vector2 getPosition() {
        return position;
    }

    public void setPosition(Jaylib.Vector2 position) {
        this.position = position;
    }

    public Raylib.Color getColor() {
        return color;
    }

    public void setColor(Raylib.Color color) {
        this.color = color;
    }

    public void setCollision(Map<String, Boolean> collision) {
        this.collision = collision;
    }

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    public Map<String, Boolean> getCollision(){
        return this.collision;
    }

    public Tile(Jaylib.Vector2 position, boolean top, boolean sides, boolean bottom, Raylib.Color color) {
        this.position = position;
        this.color = color;
        this.collision.put("top", top);
        this.collision.put("right", sides);
        this.collision.put("left", sides);
        this.collision.put("bottom", bottom);
    }

    public Tile(Jaylib.Vector2 position, Raylib.Color color, Boolean collision) {
        this.position = position;
        this.color = color;
        this.collision.put("top", collision);
        this.collision.put("right", collision);
        this.collision.put("left", collision);
        this.collision.put("bottom", collision);
    }
}
