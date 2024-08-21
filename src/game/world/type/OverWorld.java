package game.world.type;

import game.utilities.concerns.IllegalMethodUsage;
import game.engine.noise.Perlin1D;
import game.engine.Identifier;
import game.engine.Position;
import game.engine.concerns.ChunkGenerationException;
import game.world.ecosystem.objects.Chunk;
import game.world.ecosystem.objects.Tile;

import static game.utilities.Variables.CHUNKSIZE;

public class OverWorld extends BaseWorld {
    private Perlin1D perlinNoise = new Perlin1D(0.5, 8);

    @Override
    public Chunk generateChunk(Position chunkPosition) throws ChunkGenerationException {
        Chunk chunk = new Chunk();

        try{
            Position chunkStartingTile = chunkPosition.toTile();

            for (int yp = 0; yp < CHUNKSIZE; yp++) {
                for (int xp = 0; xp < CHUNKSIZE; xp++) {

                    // Get the targeted chunk's X & Y:
                    int targetX = chunkStartingTile.x() + xp;
                    int targetY = chunkStartingTile.y() + yp;

                    // Jaylib.Rectangle rec = new Jaylib.Rectangle(targetX, targetY, TILESCALEDSIZE, TILESCALEDSIZE);

                    Identifier<Integer, Integer> tileId = new Identifier<>();
                    Tile tile = new Tile();
                    tile.setPosition(new Position(targetX, targetY));

                    double height = perlinNoise.getHeight(perlinNoise.getIncremental() * targetX);

                    if (targetY <= height) {
                        tileId.setParent(1);
                        tileId.setChild(4);
                    } else if (targetY + TILESCALEDSIZE > height) {
                        tileId.setParent(8);
                        tileId.setChild(4);
                    }

                    if (targetY > height || targetY + TILESCALEDSIZE > height) {
                        tile.setIdentifier(tileId);
                        tile.setCollision(true);
                        chunk.addTile(tile);
                    }
                }
            }
        } catch (IllegalMethodUsage e){
            System.out.println(e.getMessage());
        }

        return chunk;
    }

    public Perlin1D getPerlinNoise() {
        return perlinNoise;
    }

    public void setPerlinNoise(Perlin1D perlinNoise) {
        this.perlinNoise = perlinNoise;
    }
}
