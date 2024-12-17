package game.core.engine.terrain;

import com.raylib.Jaylib;
import game.common.interfaces.Chainable;
import game.common.interfaces.Renderable;
import game.common.utils.Utils;
import game.core.engine.camera.AdvCamera2D;
import game.core.engine.common.Identifier;
import game.core.engine.position.Position;
import game.exceptions.IllegalMethodUsage;
import game.exceptions.RenderingException;

import static com.raylib.Jaylib.GRAY;
import static com.raylib.Jaylib.WHITE;
import static com.raylib.Raylib.DrawRectangle;
import static com.raylib.Raylib.DrawTextureRec;
import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.TILESCALEDSIZE;

/**
 * A single tile that makes up the terrain.
 *
 * @see Chunk
 * @author FaintShadow
 */
public class Tile implements Chainable<Tile>, Renderable {
    private Identifier<Integer, Integer> identifier;
    private Position position;
    private boolean collision;

    public Tile() {
    }

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

    public boolean isCollidable() {
        return collision;
    }

    public void setCollision(boolean collision) {
        this.collision = collision;
    }
    // ----------------------------------------------------------------------


    // Implementations:
    @Override
    public Tile copy() {
        return new Tile(identifier, position, collision);
    }

    @Override
    public Tile clone() throws CloneNotSupportedException {
        return (Tile) super.clone();
    }

    @Override
    public void render(AdvCamera2D advCamera2D) {
        try{
            DrawTextureRec(
                    SHARED_TEXTURE_MANAGER.getTexture("tiles"),
                    SHARED_TEXTURE_MANAGER.getTextureRec("tiles", identifier),
                    new Jaylib.Vector2(
                            Position.convert(position.x(), W_TILE, W_WORLD),
                            Position.convert(position.y(), W_TILE, W_WORLD)
                    ),
                    WHITE
            );
        } catch (IllegalMethodUsage e) {
            Utils.Logger.warning("Failed to render tile: " + e.getMessage());
        }
    }
    // ----------------------------------------------------------------------
}