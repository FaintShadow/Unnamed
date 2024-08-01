package game.utilities;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.errors.IllegalPositioningSystemArgument;

import java.util.Arrays;

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
    // Screen conversion is still unfinished in all of these methods.
    // TODO: Finish conversion to screen on all the methods below.
    public Position toWorld() {
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
                return screen();
            }
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return world();
    }

    public Position toTile() {
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
                return screen();
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return tile();
    }

    public Position toChunk() {
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
                break;
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return chunk();
    }
    // ----------------------------------------------------------
}
