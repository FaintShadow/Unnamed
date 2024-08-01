package game.world.type;

import game.noise.Perlin1D;
import game.utilities.Identifier;
import game.utilities.Position;
import game.utilities.errors.ChunkGenerationException;
import game.utilities.errors.InvalidIdentifierFormat;
import game.world.ecosystem.objects.Tile;

import java.util.ArrayList;
import java.util.List;

import static game.utilities.Variables.CHUNKSIZE;

public class OverWorld extends BaseWorld {
    private static final Perlin1D noise = new Perlin1D(0.5, 8);

    public List<Tile> genChunk(Position position) throws ChunkGenerationException {
        ArrayList<Tile> chunkTiles = new ArrayList<>();
        Identifier<Integer, Integer> tempId = new Identifier<>();

        for (int yp = 0; yp < CHUNKSIZE; yp++) {
            for (int xp = 0; xp < CHUNKSIZE; xp++) {
                // Get the targeted chunk's X & Y:
                float targetX = position.x() + ((float) xp * TILESCALEDSIZE);
                float targetY = position.y() + ((float) yp * TILESCALEDSIZE);

                // Jaylib.Rectangle rec = new Jaylib.Rectangle(targetX, targetY, TILESCALEDSIZE, TILESCALEDSIZE);
                Tile tile = new Tile();
                tile.setPosition(new Position(targetX, targetY));
                double height = noise.perlinNoise1D(targetX) * 100;
                if (targetY > height) {
                    tempId.setParent(1);
                    tempId.setChild(4);
                    tile.setId(tempId);
                    tile.setCollision(true);
                } else if (targetY + TILESCALEDSIZE > height) {
                    tempId.setParent(8);
                    tempId.setChild(4);

                    tile.setId(tempId);
                    tile.setCollision(true);
                }
                if (tile.getId() != null) {
                    chunkTiles.add(tile);
                }
            }
        }
        return chunkTiles;
    }
}
