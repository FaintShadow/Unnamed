package game.world.type;

import game.utilities.Position;
import game.utilities.errors.ChunkGenerationException;
import game.utilities.errors.InvalidIdentifierFormat;
import game.utilities.interfaces.Chunkable;
import game.world.ecosystem.objects.Tile;
import game.camera.AdvancedCamera2D;
import com.raylib.Jaylib;

import java.util.List;
import java.util.Map;

import static com.raylib.Jaylib.*;
import static game.utilities.Variables.*;

public abstract class BaseWorld implements Chunkable<Tile> {

    public static final int WORLDSCALE = 1;
    public static final int TILESIZE = (int) Math.pow(2, 5);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;
    public static final int GRAVITY = 400;

    public void renderChunks(AdvancedCamera2D camera, Jaylib.Vector2 cameraAncher, Map<String, List<Tile>> map) {
        int ccY = (int) ((GAMEHEIGHT * (2 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) + 1;
        int ccX = (int) ((GAMEWIDTH * (3 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) - 1;

        for (int y = 0; y < ccY; y++) {
            for (int x = 0; x < ccX; x++) {
                camera.getCorner(1, cameraAncher);
                int targetX = (int) (x + (cameraAncher.x() - (CHUNKSIZE * TILESCALEDSIZE)) / (CHUNKSIZE * TILESCALEDSIZE));
                int targetY = (int) (y + (cameraAncher.y() / (CHUNKSIZE * TILESCALEDSIZE)));
                int xCD = targetX * (CHUNKSIZE * TILESCALEDSIZE);
                int yCD = targetY * (CHUNKSIZE * TILESCALEDSIZE);
                String chunkIndex = xCD + ":" + yCD;

                map.computeIfAbsent(chunkIndex, k -> {
                    try {
                        return genChunk(new Position(xCD, yCD, W_CHUNK));
                    } catch (ChunkGenerationException e) {
                        throw new RuntimeException(e);
                    }
                });

                for (Tile tile : map.get(chunkIndex)) {
                    DrawTextureRec(TileTextureManager.getTexture(), TileTextureManager.getTextureRec(tile), tile.getPosition().getVector2(), WHITE);
                }

            }
        }
    }

    @Override
    public List<Tile> genChunk(Position position) throws ChunkGenerationException {
        return List.of();
    }
}
