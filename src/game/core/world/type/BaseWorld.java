package game.core.world.type;

import game.common.interfaces.Renderable;
import game.core.engine.common.Identifier;
import game.core.engine.position.Position;
import game.core.engine.texture.Manager;
import game.exceptions.ChunkGenerationException;
import game.exceptions.IllegalMethodUsage;
import game.common.interfaces.Chunkable;
import game.core.engine.terrain.Chunk;
import game.core.engine.terrain.Tile;
import game.core.engine.camera.AdvCamera2D;
import game.exceptions.RenderingException;

import java.util.HashMap;

import static game.common.utils.Variables.*;

/**
 * Base world class for generating and rendering chunks of tiles.
 * @see Chunk
 * @see Tile
 * @see Manager
 * @see Renderable
 * @see Chunkable
 * @see Identifier
 * @see Position
 * @author FaintShadow
 */
public class BaseWorld implements Chunkable<Chunk>, Renderable {
    public static final int WORLDSCALE = 1;
    public static final int TILESIZE = (int) Math.pow(2, 4);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;

    private HashMap<String, Chunk> worldMap = new HashMap<>();

    public BaseWorld() {
        // Todo: Fix file Manager
        // Replace `game.core.engine.files.Manager.textureAssets("tiles.png")` with absolute path to `tiles.png`
        SHARED_TEXTURE_MANAGER.loadTexture("tiles", game.core.engine.files.Manager.textureAssets("tiles.png"));
        SHARED_TEXTURE_MANAGER.registerSpriteSheet("tiles",
                new Manager.SpriteSheetConfig(16, 16, 16, 16)
        );
    }

    @Override
    public void render(AdvCamera2D advCamera2D) throws RenderingException {
        for (Chunk chunk : worldMap.values()) {
            chunk.render(advCamera2D);
        }
    }

    @Override
    public Chunk generateChunk(Position position) throws ChunkGenerationException {
        Chunk chunk = new Chunk();
        for (int x = 0; x < CHUNKSIZE; x++) {
            for (int y = 0; y < CHUNKSIZE; y++) {
                if (position.y() * (CHUNKSIZE * TILESCALEDSIZE) + y >= 0){
                    Position tilePos = new Position(
                            position.x() * CHUNKSIZE + x,
                            position.y() * CHUNKSIZE + y,
                            W_TILE
                    );
                    Tile tile = new Tile();
                    tile.setPosition(tilePos);
                    tile.setCollision(false);
                    tile.setIdentifier(Identifier.<Integer, Integer>create()
                            .parent(position.y() * (CHUNKSIZE * TILESCALEDSIZE) + y == 0 ? 4 : 3)
                            .child(1)
                            .build()
                    );
                    chunk.addTile(tile);
                }
            }
        }
        return chunk;
    }

    @Override
    public void generateChunks(AdvCamera2D advCamera2D) throws ChunkGenerationException {
        try {
            Position topLeft = advCamera2D.getScreenCornerWorldPosition(1).toChunk();
            Position bottomRight = advCamera2D.getScreenCornerWorldPosition(3).toChunk();

            for (int x = topLeft.x(); x <= bottomRight.x(); x++) {
                for (int y = topLeft.y(); y <= bottomRight.y(); y++) {
                    Position chunkPos = new Position(x, y, W_CHUNK);
                    worldMap.computeIfAbsent(chunkPos.toString(), k -> {
                        try {
                            return generateChunk(chunkPos);
                        } catch (ChunkGenerationException e) {
                            throw new RuntimeException(e);
                        }
                    });
                }
            }
        } catch (IllegalMethodUsage e) {
            throw new ChunkGenerationException(e);
        }
    }
}