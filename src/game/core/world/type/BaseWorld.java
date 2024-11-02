package game.core.world.type;

import game.core.engine.position.Position;
import game.exceptions.IllegalMethodUsage;
import game.common.interfaces.Chunkable;
import game.core.engine.terrain.Chunk;
import game.core.engine.terrain.Tile;
import game.core.engine.camera.AdvCamera2D;
import game.generation.noise.Perlin1D;

import java.util.HashMap;
import java.util.Map;

import static com.raylib.Jaylib.*;
import static game.common.utils.Variables.*;

public class BaseWorld implements Chunkable<Chunk> {

    public static final int WORLDSCALE = 1;
    public static final int TILESIZE = (int) Math.pow(2, 5);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;
    private Perlin1D perlinNoise = new Perlin1D(0.5, 8);
    HashMap<String, Chunk> map = new HashMap<>();


    public void renderChunks(AdvCamera2D camera) throws IllegalMethodUsage {
        // cc: count visible chunks
        int ccY = (int) ((GAMEHEIGHT * (2 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) + 1;
        int ccX = (int) ((GAMEWIDTH * (3 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) - 1;

        // TODO: fix the NULL error in the following line: Position cornerChunk = camera.getScreenCorner(1).toChunk(camera);
        Position cornerChunk = camera.getScreenCornerWorldPosition(1).copy().toChunk();

        // Loop through visible chunks
        for (int y = 0; y < ccY; y++) {
            for (int x = 0; x < ccX; x++) {
                Position currentChunk = new Position(cornerChunk.x() + x, cornerChunk.y() + y, W_CHUNK);
                map.get(currentChunk.toString()).getTiles().forEach(tile -> {
                    try {
                        DrawRectangle(tile.getPosition().copy().toWorld().x(), tile.getPosition().copy().toWorld().y(), TILESCALEDSIZE, TILESCALEDSIZE, GRAY);
                    } catch (IllegalMethodUsage e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        }
    }

    public void generateChunks(AdvCamera2D camera) {
        // cc: count visible chunks
        int ccY = (int) ((GAMEHEIGHT * (2 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) + 1;
        int ccX = (int) ((GAMEWIDTH * (3 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) - 1;
        try {
            Position cornerChunk = camera.getScreenCornerWorldPosition(1).copy().toChunk();
            for (int y = 0; y < ccY; y++) {
                for (int x = 0; x < ccX; x++) {
                    Position currentChunk = new Position(cornerChunk.x() + x, cornerChunk.y() + y, W_CHUNK);
                    map.put(currentChunk.toString(), generateChunk(currentChunk));
                }
            }
        } catch (IllegalMethodUsage e) {
            throw new RuntimeException(e);
        }
    }

    public Chunk generateChunk(Position chunkPos) {
        Chunk chunk = new Chunk();
        try {
            Position chunkTilePos = chunkPos.copy().toTile();

            for (int y = 0; y < CHUNKSIZE; y++) {
                for (int x = 0; x < CHUNKSIZE; x++) {
                    Tile tile = makeTile(chunkTilePos.x() + x, chunkTilePos.y() + y);
                    if (tile.getPosition().y() >= 0){
                        chunk.addTile(tile);
                    }
                }
            }
        } catch (IllegalMethodUsage e) {
            throw new RuntimeException(e);
        }
        return chunk;
    }

    public Tile makeTile(int x, int y) {
        Position tilePos = new Position(x, y, W_TILE);
        return new Tile().setPosition(tilePos);
    }

    public Map<String, Chunk> getMap() {
        return map;
    }

    public Perlin1D getPerlinNoise() {
        return perlinNoise;
    }

    public void setPerlinNoise(Perlin1D perlinNoise) {
        this.perlinNoise = perlinNoise;
    }
}
