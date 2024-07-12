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

    public Identifier<Integer, Integer> getId() {
        return identifier;
    }

    public void setId(Identifier<Integer, Integer> identifier) {
        this.identifier = identifier;
    }

    public Map<String, Boolean> getCollision(){
        return this.collision;
    }

    public Tile(Jaylib.Vector2 position, Raylib.Color color, Boolean collision) {
        this.position = position;
        this.color = color;

        this.collision.put(TOP, collision);
        this.collision.put(RIGHT, collision);
        this.collision.put(LEFT, collision);
        this.collision.put(BOTTOM, collision);
    }
}
