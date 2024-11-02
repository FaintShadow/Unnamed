package game.core.engine.position;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.core.engine.camera.AdvCamera2D;
import game.core.engine.common.Identifier;
import game.common.interfaces.Returnable;
import game.common.utils.Utils;
import game.exceptions.IllegalMethodUsage;
import game.exceptions.IllegalPositioningSystemArgument;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.raylib.Raylib.GetScreenToWorld2D;
import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.TILESIZE;

public class Position implements Returnable<Position> {
    private Jaylib.Vector2 vector = new Jaylib.Vector2();
    private String system;

    public Position() {
        vector = new Jaylib.Vector2();
    }

    public Position(int x, int y) {
        this.vector.x(x);
        this.vector.y(y);
    }

    public Position(int x, int y, String system) throws IllegalPositioningSystemArgument {
        this.vector.x(x);
        this.vector.y(y);
        this.system = system;

        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
    }

    public Position(Identifier<Integer, Integer> pos, String system) throws IllegalPositioningSystemArgument {
        this.vector.x(pos.getParent());
        this.vector.y(pos.getChild());
        this.system = system;

        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
    }

    // Getters & Setters:
    public int x() {
        return (int) vector.x();
    }

    public void x(int x) {
        vector.x(x);
    }

    public int y() {
        return (int) vector.y();
    }

    public void y(int y) {
        vector.y(y);
    }

    public String getSystem() {
        return system;
    }

    public Position setSystem(String system) {
        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
        this.system = system;
        return this;
    }

    public Jaylib.Vector2 getVector2() {
        return vector;
    }
    // ----------------------------------------------------------

    // Set positioning system:
    public Position world() {
        setSystem(W_WORLD).noReturn();
        return this;
    }

    public Position tile() {
        setSystem(W_TILE).noReturn();
        return this;
    }

    public Position chunk() {
        setSystem(W_CHUNK).noReturn();
        return this;
    }

    public Position screen() {
        setSystem(W_SCREEN).noReturn();
        return this;
    }
    // ----------------------------------------------------------

    // Stream conversion:
    // Screen conversion is still unfinished in all of methods below.
    // TODO: Finish conversion to screen on all the methods below.

    /**
     * Used to make a duplicate of the current position, Use this when you want to convert a position without changing it
     * @return Duplicate of the same position
     */
    @Override
    public Position copy(){
        return new Position(this.x(), this.y(), this.getSystem());
    }

    /**
     * Stops any method from returning the current position object.
     */
    public void noReturn(){
        // This method is used when you finish converting without needing to put the new value in a new variable
    }

    public Position toWorld() throws IllegalMethodUsage {
        switch (getSystem()) {
            case W_WORLD:
                break;
            case W_TILE: {
                x(x() * TILESIZE );
                y(y() * TILESIZE );
                break;
            }
            case W_CHUNK: {
                x(x() * (TILESIZE * CHUNKSIZE) );
                y(y() * (TILESIZE * CHUNKSIZE) );
                break;
            }
            case W_SCREEN: {
                Method method = Object.class
                        .getEnclosingMethod();
                throw new IllegalMethodUsage(method, "converting ONLY tile and chunk positions to world positions");
            }
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return world();
    }

    /**
     * This method is only used to convert screen position to world position, if you're converting another
     * position type please use the other toWorld method
     * @param camera world camera
     * @return A world position
     * @throws IllegalMethodUsage thrown when the method is used for purposes that it shouldn't perform
     */
    public Position toWorld(AdvCamera2D camera) throws IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x((int) pos.x());
            y((int) pos.y());
            return world();
        }
        Method method = Object.class
                .getEnclosingMethod();
        throw new IllegalMethodUsage(method, system);
    }

    public Position toTile() throws IllegalMethodUsage {
        switch (getSystem()) {
            case W_TILE:
                break;
            case W_CHUNK: {
                x( x() * CHUNKSIZE );
                y( y() * CHUNKSIZE );
                break;
            }
            case W_WORLD: {
                x( Utils.positionDivision( x(), TILESIZE ) );
                y( Utils.positionDivision( y(), TILESIZE ) );
                break;
            }
            case W_SCREEN:
                Method method = Object.class
                        .getEnclosingMethod();
                throw new IllegalMethodUsage(method, "converting ONLY tile and chunk positions to world positions");
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return tile();
    }

    /**
     * This method is only used to convert screen position to tile position, if you're converting another
     * position type please use the other toTile method
     * @param camera The game rendering camera
     * @return A Tile position of the screen position
     * @throws IllegalMethodUsage Thrown when using the wrong positioning system
     */
    public Position toTile(AdvCamera2D camera) throws IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x((int) pos.x());
            y((int) pos.y());
            return toTile();
        }
        Method method = Object.class
                .getEnclosingMethod();
        throw new IllegalMethodUsage(method, "converting ONLY screen positions to chunk positions");
    }

    public Position toChunk() throws IllegalMethodUsage {
        switch (getSystem()) {
            case W_CHUNK:
                break;
            case W_TILE: {
                x( x() * CHUNKSIZE );
                y( y() * CHUNKSIZE );
                break;
            }
            case W_WORLD: {
                x( Utils.positionDivision(x(), TILESIZE * CHUNKSIZE) );
                y( Utils.positionDivision(y(), TILESIZE * CHUNKSIZE) );
                break;
            }
            case W_SCREEN:
                Method method = Object.class
                        .getEnclosingMethod();
                throw new IllegalMethodUsage(method, "converting ONLY tile and world positions to chunk positions");
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return chunk();
    }

    /**
     * This method is only used to convert screen position to chunk position, if you're converting another
     * position type please use the other toChunk method
     * @param camera The game rendering camera
     * @return A chunk position of the screen position
     * @throws IllegalMethodUsage
     */
    public Position toChunk(AdvCamera2D camera) throws IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x((int) pos.x());
            y((int) pos.y());
            return toChunk();
        }
        Method method = Object.class
                .getEnclosingMethod();
        throw new IllegalMethodUsage(method, "converting ONLY screen positions to chunk positions");
    }
    // ----------------------------------------------------------


    @Override
    public String toString() {
        return vector.x() + ":" + vector.y();
    }
}
