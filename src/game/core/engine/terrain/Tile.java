package game.core.engine.terrain;

import com.raylib.Jaylib;
import game.core.engine.common.Identifier;
import game.core.engine.position.Position;

public class Tile {
    private Identifier<Integer, Integer> identifier;
    private Position position;
    private Jaylib.Color color;
    private Boolean collision;

    public Tile(){}

    public Tile(Identifier<Integer, Integer> id, Position position, Boolean collision) {
        this.identifier = id;
        this.position = position;
        this.collision = collision;
    }

    // Getters & Setters:

    public Identifier<Integer, Integer> getIdentifier() {
        return identifier;
    }

    public void setIdentifier(Identifier<Integer, Integer> identifier) {
        this.identifier = identifier;
    }

    public Position getPosition() {
        return position;
    }

    public Tile setPosition(Position position) {
        this.position = position;
        return this;
    }

    public Jaylib.Color getColor() {
        return color;
    }

    public void setColor(Jaylib.Color color) {
        this.color = color;
    }

    public Boolean getCollision() {
        return collision;
    }

    public void setCollision(Boolean collision) {
        this.collision = collision;
    }

    // ----------------------------------------------------------------------
}
