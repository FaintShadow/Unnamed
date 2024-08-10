package game.utilities;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.camera.AdvancedCamera2D;
import game.utilities.errors.IllegalMethodUsage;
import game.utilities.errors.IllegalPositioningSystemArgument;

import java.lang.reflect.Method;
import java.util.Arrays;

import static com.raylib.Raylib.GetScreenToWorld2D;
import static game.utilities.Variables.*;
import static game.world.type.BaseWorld.TILESIZE;

public class Position {
    private Jaylib.Vector2 position;
    private String system;

    public Position() {
        position = new Jaylib.Vector2();
    }

    public Position(float x, float y) {
        position = new Jaylib.Vector2();
        this.position.x(x);
        this.position.y(y);
    }

    public Position(float x, float y, String system) throws IllegalPositioningSystemArgument {
        new Position(x, y);
        this.system = system.toLowerCase();

        if (!Arrays.asList(POSITIONSYSTEMS).contains(system)) {
            throw new IllegalPositioningSystemArgument(system);
        }

        position = new Jaylib.Vector2();
    }

    // Getters & Setters:
    public float x() {
        return position.x();
    }

    public void x(float x) {
        position.x(x);
    }

    public float y() {
        return position.y();
    }

    public void y(float y) {
        position.y(y);
    }

    public String getSystem() {
        return system;
    }

    public void setSystem(String system) {
        this.system = system;
    }

    public Jaylib.Vector2 getVector2() {
        return position;
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
    public Position duplicate(){
        return new Position(position.x(), position.y(), system);
    }

    public Position toWorld() throws NoSuchMethodException, IllegalMethodUsage {
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
                throw new IllegalMethodUsage(Position.class.getMethod("toWorld"), "converting ONLY tile and chunk positions to world positions");
            }
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return world();
    }

    /**
     * This method is only used to convert screen position to world position, if you're converting another
     * position type please use the other toWorld method
     * @param camera
     * @return A world position
     * @throws NoSuchMethodException
     * @throws IllegalMethodUsage
     */
    public Position toWorld(AdvancedCamera2D camera) throws NoSuchMethodException, IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x( pos.x() );
            y( pos.y() );
            return screen();
        }
        throw new IllegalMethodUsage(Position.class.getMethod("toWorld"), system);
    }

    public Position toTile() throws NoSuchMethodException, IllegalMethodUsage {
        switch (getSystem()) {
            case W_TILE:
                break;
            case W_CHUNK: {
                x( Utils.positionDivision( (int) x(), TILESIZE * CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE * CHUNKSIZE ) );
                break;
            }
            case W_WORLD: {
                x( Utils.positionDivision( (int) x(), TILESIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE ) );
                break;
            }
            case W_SCREEN:
                throw new IllegalMethodUsage(Position.class.getMethod("toTile"), "converting ONLY tile and chunk positions to world positions");
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
    public Position toTile(AdvancedCamera2D camera) throws NoSuchMethodException, IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x( pos.x() );
            y( pos.y() );
            return toTile();
        }
        Method method = new Object() {}
                .getClass()
                .getEnclosingMethod();
        throw new IllegalMethodUsage(method, "converting ONLY screen positions to chunk positions");
    }

    public Position toChunk() throws NoSuchMethodException, IllegalMethodUsage {
        switch (getSystem()) {
            case W_CHUNK:
                break;
            case W_TILE: {
                x( Utils.positionDivision( (int) x(), CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), CHUNKSIZE ) );
                break;
            }
            case W_WORLD: {
                x( Utils.positionDivision( (int) x(), TILESIZE + CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE + CHUNKSIZE ) );
                break;
            }
            case W_SCREEN:
                throw new IllegalMethodUsage(Position.class.getMethod("toChunk"), "converting ONLY tile and world positions to chunk positions");
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
     * @throws NoSuchMethodException
     * @throws IllegalMethodUsage
     */
    public Position toChunk(AdvancedCamera2D camera) throws NoSuchMethodException, IllegalMethodUsage {
        if (getSystem().equals(W_SCREEN)) {
            Raylib.Vector2 pos = GetScreenToWorld2D(getVector2(), camera);
            x( pos.x() );
            y( pos.y() );
            return toChunk();
        }
        throw new IllegalMethodUsage(Position.class.getMethod("toChunk"), "converting ONLY screen positions to chunk positions");
    }
    // ----------------------------------------------------------
}
