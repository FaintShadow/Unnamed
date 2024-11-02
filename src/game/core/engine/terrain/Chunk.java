package game.core.engine.terrain;

import java.util.ArrayList;
import java.util.List;

public class Chunk {
    private List<Tile> tiles;

    public Chunk() {
        this.tiles = new ArrayList<>();
    }

    public void addTile(Tile tile) {
        this.tiles.add(tile);
    }

    public List<Tile> getTiles() {
        return tiles;
    }

}
