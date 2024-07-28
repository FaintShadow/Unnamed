package game.utilities;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.errors.IllegalPositioningSystemArgument;

import java.util.Arrays;

import static game.utilities.Variables.CHUNKSIZE;
import static game.utilities.Variables.POSITIONSYSTEMS;
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

        if (Arrays.stream(POSITIONSYSTEMS).anyMatch(system::equalsIgnoreCase)) {
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

    public Raylib.Vector2 getVector2() {
        return position;
    }
    // ----------------------------------------------------------

    // Set positioning system:
    public Position world() {
        setSystem("world");
        return this;
    }

    public Position tile() {
        setSystem("tile");
        return this;
    }

    public Position chunk() {
        setSystem("chunk");
        return this;
    }

    public Position screen() {
        setSystem("screen");
        return this;
    }
    // ----------------------------------------------------------

    // Stream conversion:
    // Screen conversion is still unfinished in all of these methods.
    // TODO: Finish conversion to screen on all the methods below.
    public Position toWorld() {
        switch (getSystem()) {
            case "world":
                break;
            case "tile": {
                x(x() * TILESIZE );
                y(y() * TILESIZE );
                break;
            }
            case "chunk": {
                x(x() * (TILESIZE * CHUNKSIZE) );
                y(y() * (TILESIZE * CHUNKSIZE) );
                break;
            }
            case "screen": {
                return screen();
            }
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return world();
    }

    public Position toTile() {
        switch (getSystem()) {
            case "tile":
                break;
            case "chunk": {
                x( Utils.positionDivision( (int) x(), TILESIZE * CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE * CHUNKSIZE ) );
                break;
            }
            case "world": {
                x( Utils.positionDivision( (int) x(), TILESIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE ) );
                break;
            }
            case "screen":
                return screen();
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return tile();
    }

    public Position toChunk() {
        switch (getSystem()) {
            case "chunk":
                break;
            case "tile": {
                x( Utils.positionDivision( (int) x(), CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), CHUNKSIZE ) );
                break;
            }
            case "world": {
                x( Utils.positionDivision( (int) x(), TILESIZE + CHUNKSIZE ) );
                y( Utils.positionDivision( (int) y(), TILESIZE + CHUNKSIZE ) );
                break;
            }
            case "screen":
                break;
            default:
                throw new IllegalPositioningSystemArgument(system);
        }
        return chunk();
    }
    // ----------------------------------------------------------

    // Static position conversion:
    public static Raylib.Vector2 chunkToWorld(int chunkX, int chunkZ) {
        return null;
    }

    public static Jaylib.Vector2 worldToChunk(int worldX, int worldZ) {
        return null;
    }

    public static Raylib.Vector2 screenToWorld(int screenX, int screenZ) {
        return null;
    }

    public static Raylib.Vector2 worldToScreen(int worldX, int worldZ) {
        return null;
    }
    // ----------------------------------------------------------
}
