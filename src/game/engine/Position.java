package game.engine;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.Utils;
import game.utilities.concerns.IllegalMethodUsage;
import game.engine.concerns.IllegalPositioningSystemArgument;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.raylib.Raylib.GetScreenToWorld2D;
import static game.utilities.Variables.*;
import static game.world.type.BaseWorld.TILESIZE;

public class Position {
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
        new Position(x,y);
        this.system = system;

        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
    }

    public Position(Identifier<Integer, Integer> pos, String system) throws IllegalPositioningSystemArgument {
        new Position(pos.getParent(),pos.getChild());
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

    public void setSystem(String system) {
        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
        this.system = system;
    }

    public Jaylib.Vector2 getVector2() {
        return vector;
    }
    // ----------------------------------------------------------

    // Set positioning system:
    public Position world() {
        setSystem(W_WORLD);
        return this;
    }

    public Position tile() {
        setSystem(W_TILE);
        return this;
    }

    public Position chunk() {
        setSystem(W_CHUNK);
        return this;
    }

    public Position screen() {
        setSystem(W_SCREEN);
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
    public Position newPosition(){
        return new Position((int) vector.x(), (int) vector.y(), system);
    }

    /**
     * Usually used when you convert without using 'newPosition()'. Makes converting return no value
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
    public Position toWorld(AdvancedCamera2D camera) throws IllegalMethodUsage {
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
                x( Utils.positionDivision( x(), TILESIZE * CHUNKSIZE ) );
                y( Utils.positionDivision( y(), TILESIZE * CHUNKSIZE ) );
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
     * @param camera
     * @return A Tile position of the screen position
     * @throws NoSuchMethodException
     * @throws IllegalMethodUsage
     */
    public Position toTile(AdvancedCamera2D camera) throws IllegalMethodUsage {
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
                x( Utils.positionDivision( x(), CHUNKSIZE ) );
                y( Utils.positionDivision( y(), CHUNKSIZE ) );
                break;
            }
            case W_WORLD: {
                x( Utils.positionDivision( x(), TILESIZE + CHUNKSIZE ) );
                y( Utils.positionDivision( y(), TILESIZE + CHUNKSIZE ) );
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
     * @param camera
     * @return A chunk position of the screen position
     * @throws IllegalMethodUsage
     */
    public Position toChunk(AdvancedCamera2D camera) throws IllegalMethodUsage {
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
}
