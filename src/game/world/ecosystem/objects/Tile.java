package game.world.ecosystem.objects;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.Identifier;

import java.util.HashMap;
import java.util.Map;

import static game.utilities.Variables.*;

public class Tile {
    private Identifier<Integer, Integer> identifier;
    private Jaylib.Vector2 position;
    private Raylib.Color color;
    private Map<String, Boolean> collision = new HashMap<>();

    public Tile(){}

    public Tile(Identifier<Integer, Integer> id, Jaylib.Vector2 position, Boolean collision) {
        this.identifier = id;
        this.position = position;
        this.collision.put(W_TOP, collision);
        this.collision.put(W_RIGHT, collision);
        this.collision.put(W_LEFT, collision);
        this.collision.put(W_BOTTOM, collision);
    }

    // Getters & Setters:
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

    public void setCollision(boolean collision) {
        this.collision.put(W_TOP, collision);
        this.collision.put(W_RIGHT, collision);
        this.collision.put(W_LEFT, collision);
        this.collision.put(W_BOTTOM, collision);
    }

    public Map<String, Boolean> getCollision(){
        return this.collision;
    }

    public Identifier<Integer, Integer> getId() {
        return identifier;
    }

    public void setId(Identifier<Integer, Integer> identifier) {
        this.identifier = identifier;
    }
    // ----------------------------------------------------------------------
}
