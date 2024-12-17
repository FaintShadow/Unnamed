package game.core.engine.position;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.core.engine.camera.AdvCamera2D;
import game.core.engine.common.Identifier;
import game.common.interfaces.Chainable;
import game.common.utils.Utils;
import game.exceptions.IllegalMethodUsage;
import game.exceptions.IllegalPositioningSystemArgument;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.raylib.Raylib.GetScreenToWorld2D;
import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.TILESIZE;

public class Position implements Chainable<Position> {
    private final Jaylib.Vector2 vector = new Jaylib.Vector2();
    private String system;

    public Position() {}

    /**
     * Creates a position in the default coordinate system
     *
     * @param x X coordinate
     * @param y Y coordinate
     */
    public Position(int x, int y) {
        this.vector.x(x);
        this.vector.y(y);
    }

    /**
     * Creates a position in a specified coordinate system
     *
     * @param x X coordinate
     * @param y Y coordinate
     * @param system The coordinate system to use (World, Tile, Chunk, Screen)
     * @throws IllegalPositioningSystemArgument If the system is not recognized
     */
    public Position(int x, int y, String system) throws IllegalPositioningSystemArgument {
        this.vector.x(x);
        this.vector.y(y);
        this.system = system;

        if (!Arrays.asList(getPositionSystems()).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
    }

    /**
     * Creates a position from an identifier in a specified coordinate system
     *
     * @param pos Identifier containing x,y coordinates as parent,child
     * @param system The coordinate system to use
     * @throws IllegalPositioningSystemArgument If the system is not recognized
     */
    public Position(Identifier<Integer, Integer> pos, String system) throws IllegalPositioningSystemArgument {
        this.vector.x(pos.getParent().orElseThrow(() -> new RuntimeException("Parent is empty")));
        this.vector.y((pos.getChild().orElseThrow(() -> new RuntimeException("Child is empty"))));
        this.system = system;

        if (!Arrays.asList(getPositionSystems()).contains(system)) {
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
        if (!Arrays.asList(getPositionSystems()).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }
        this.system = system;
        return this;
    }

    public Jaylib.Vector2 getVector2() {
        return vector;
    }
    // ----------------------------------------------------------

    /**
     * convert to World coordinate system
     * @return This position instance for chaining
     */
    public Position world() {
        setSystem(W_WORLD).noReturn();
        return this;
    }

    /**
     * convert to Tile coordinate system
     * @return This position instance for chaining
     */
    public Position tile() {
        setSystem(W_TILE).noReturn();
        return this;
    }

    /**
     * convert to Tile coordinate system
     * @return This position instance for chaining
     */
    public Position chunk() {
        setSystem(W_CHUNK).noReturn();
        return this;
    }

    /**
     * convert to Screen coordinate system
     * @return This position instance for chaining
     */
    public Position screen() {
        setSystem(W_SCREEN).noReturn();
        return this;
    }
    // ----------------------------------------------------------

    /**
     * Converts current position to World coordinates, except Screen position
     * @throws IllegalMethodUsage If converting from Screen coordinates
     * @return This position instance for chaining
     */
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
     * Converts Screen coordinates to World coordinates
     * @param camera Camera for coordinate transformation
     * @return This position instance for chaining
     * @throws IllegalMethodUsage If not converting from Screen coordinates
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


    /**
     * Converts current position to Tile coordinates, except Screen position
     * @throws IllegalMethodUsage If converting from Screen coordinates
     * @return This position instance for chaining
     */
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
     * Converts Screen coordinates to Tile coordinates
     * @param camera Camera for coordinate transformation
     * @return This position instance for chaining
     * @throws IllegalMethodUsage If not converting from Screen coordinates
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

    /**
     * Converts current position to Chunk coordinates, except Screen position
     * @throws IllegalMethodUsage If converting from Screen coordinates
     * @return This position instance for chaining
     */
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
     * Converts Screen coordinates to Chunk coordinates
     * @param camera Camera for coordinate transformation
     * @return This position instance for chaining
     * @throws IllegalMethodUsage If not converting from Screen coordinates
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

    // Static Methods:
    public static int convert(int pos, String from, String to) throws IllegalMethodUsage {
        return switch (from) {
            case W_WORLD -> switch (to) {
                case W_CHUNK -> pos / (TILESIZE * CHUNKSIZE);
                case W_TILE -> Utils.positionDivision(pos, TILESIZE);
                case W_WORLD -> pos;
                default -> throw new IllegalStateException("Unexpected target coordinate system: " + to);
            };
            case W_CHUNK -> switch (to) {
                case W_WORLD -> pos * (TILESIZE * CHUNKSIZE);
                case W_TILE -> pos * CHUNKSIZE;
                case W_CHUNK -> pos;
                default -> throw new IllegalStateException("Unexpected target coordinate system: " + to);
            };
            case W_TILE -> switch (to) {
                case W_WORLD -> pos * TILESIZE;
                case W_CHUNK -> pos / CHUNKSIZE;
                case W_TILE -> pos;
                default -> throw new IllegalStateException("Unexpected target coordinate system: " + to);
            };
            default -> throw new IllegalStateException("Unexpected source coordinate system: " + from);
        };
    }
    // ----------------------------------------------------------

    // Overrides:
    @Override
    public String toString() {
        return vector.x() + ":" + vector.y();
    }

    @Override
    public Position copy(){
        return new Position(this.x(), this.y(), this.getSystem());
    }

    @Override
    public Position clone() throws CloneNotSupportedException {
        return (Position) super.clone();
    }
    // ----------------------------------------------------------
}
