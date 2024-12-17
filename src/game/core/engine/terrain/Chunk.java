package game.core.engine.terrain;

import game.common.interfaces.Renderable;
import game.core.engine.camera.AdvCamera2D;

import java.util.Arrays;

import static game.common.utils.Variables.CHUNKSIZE;

/**
 * A chunk of tiles that make up a portion of the terrain.
 *
 * @see Tile
 * @author FaintShadow
 */
public class Chunk implements Renderable {
    private final Tile[] tiles = new Tile[CHUNKSIZE * CHUNKSIZE];
    private int tileCount = 0;

    public void addTile(Tile tile) {
        tiles[tileCount++] = tile;
    }

    public Iterable<Tile> getTiles() {
        return Arrays.asList(tiles).subList(0, tileCount);
    }

    @Override
    public void render(AdvCamera2D advCamera2D) {
        for (Tile tile : getTiles()) {
            tile.render(advCamera2D);
        }
    }
}
