package game.world.type;

import com.raylib.Raylib;
import game.engine.Identifier;
import game.engine.Position;
import game.engine.concerns.ChunkGenerationException;
import game.utilities.concerns.IllegalMethodUsage;
import game.engine.concerns.InvalidIdentifierFormat;
import game.engine.interfaces.Chunkable;
import game.world.ecosystem.objects.Chunk;
import game.world.ecosystem.objects.Tile;
import game.engine.AdvancedCamera2D;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Jaylib.*;
import static game.utilities.Variables.*;

public abstract class BaseWorld implements Chunkable<Chunk> {

    public static final int WORLDSCALE = 1;
    public static final int TILESIZE = (int) Math.pow(2, 5);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;
    public static final int GRAVITY = 400;
    HashMap<Identifier<Integer, Integer>, Chunk> map = new HashMap<>();
    private final List<Raylib.Color> listC = Arrays.asList(GREEN, RED, PINK, BLACK);

    public void renderChunks(AdvancedCamera2D camera) throws InvalidIdentifierFormat, IllegalMethodUsage {
        // cc: chunk count
        int ccY = (int) ((GAMEHEIGHT * (2 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) + 1;
        int ccX = (int) ((GAMEWIDTH * (3 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) - 1;

        // TODO: fix the NULL error in the following line: Position cornerChunk = camera.getScreenCorner(1).toChunk(camera);
        Position cornerChunk = camera.getScreenCorner(1).toChunk();

        for (int y = 0; y < ccY; y++) {
            for (int x = 0; x < ccX; x++) {
                cornerChunk.x(cornerChunk.x() + 1);
                cornerChunk.y(cornerChunk.y() + 1);

                Identifier<Integer, Integer> chunkId = new Identifier<>();
                chunkId.setParent((int) cornerChunk.x());
                chunkId.setChild((int) cornerChunk.y());
                DrawRectangle(cornerChunk.newPosition().toWorld().x(), cornerChunk.newPosition().toWorld().y(), TILESIZE, TILESIZE, RED);
                map.computeIfAbsent(chunkId, k -> {
                    try {
                        return generateChunk(new Position(chunkId, W_CHUNK));
                    } catch (ChunkGenerationException e) {
                        throw new RuntimeException(e);
                    }
                });

                for (Tile tile : map.get(chunkId).getTiles()) {
                    //DrawTextureRec(TileTextureManager.getTexture(), TileTextureManager.getTextureRec(tile), tile.getPosition().getVector2(), WHITE);
                    DrawRectangle((int) tile.getPosition().x(), (int) tile.getPosition().y(), TILESCALEDSIZE / 2, TILESCALEDSIZE / 2, GRAY);
                }

            }
        }
    }

    public Map<Identifier<Integer, Integer>, Chunk> getMap() {
        return map;
    }

    @Override
    public Chunk generateChunk(Position position) throws ChunkGenerationException {
        return null;
    }
}
