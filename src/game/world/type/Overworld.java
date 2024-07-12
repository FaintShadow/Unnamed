package game.world.type;

import com.raylib.Jaylib;
import game.noise.Perlin1D;
import game.texture.Manager;
import game.world.World;
import game.world.ecosystem.objects.Tile;

import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.List;

import static com.raylib.Jaylib.PINK;
import static com.raylib.Jaylib.RED;
import static game.utilities.Variables.CHUNKSIZE;

public class Overworld extends World {
    private static final Perlin1D noise = new Perlin1D(0.5, 8);

    public Overworld() { /* TODO document why this constructor is empty */ }

    public static List<Tile> genChunk(int x, int y) {
        ArrayList<Tile> chunkTiles = new ArrayList<>();

        for (int yp = 0; yp < CHUNKSIZE; yp++) {
            for (int xp = 0; xp < CHUNKSIZE; xp++) {
                // Get the targeted chunk's X & Y:
                float targetX = x + ((float) xp * TILESCALEDSIZE);
                float targetY = y + ((float) yp * TILESCALEDSIZE);

                Jaylib.Rectangle rec = new Jaylib.Rectangle(targetX, targetY, TILESCALEDSIZE, TILESCALEDSIZE);
                Tile tile = null;
                double height = noise.perlinNoise1D(targetX) * 100;
                if (targetY > height) {
                    tile = new Tile(rec, false, false, false, RED, Manager.getTexture(0, 0));
                } else if (targetY + TILESCALEDSIZE > height) {
                    tile = new Tile(rec, true, false, false, PINK, Manager.getTexture(1,0));
                }
                if (tile != null) {
                    chunkTiles.add(tile);
                }
            }
        }
        return chunkTiles;
    }
}
