package game.utilities;

import game.assets.Tile;
import game.camera.AdvancedCamera2D;
import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.noise.Perlin1D;
import game.textures.TextureManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.raylib.Jaylib.*;
import static game.Unnamed.*;
import static com.raylib.Raylib.DrawTextureEx;

public class World {
    private static Perlin1D noise = new Perlin1D(0.5, 8);

    public static void renderChunks(AdvancedCamera2D camera, Jaylib.Vector2 cameraAncher, Map<String, List<Tile>> map, Raylib.Texture underGround, Raylib.Texture groundGrass) {
        int ccY = (int) ((GAMEHEIGHT * (2 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) + 1;
        int ccX = (int) ((GAMEWIDTH * (3 / camera.zoom())) / (CHUNKSIZE * TILESCALEDSIZE)) - 1;

        for (int y = 0; y < ccY; y++) {
            for (int x = 0; x < ccX; x++) {
                camera.getCorner(1, cameraAncher);
                int targetX = (int) (x + (cameraAncher.x() - (CHUNKSIZE * TILESCALEDSIZE)) / (CHUNKSIZE * TILESCALEDSIZE));
                int targetY = (int) (y + (cameraAncher.y() / (CHUNKSIZE * TILESCALEDSIZE)));
                int xCD = targetX * (CHUNKSIZE * TILESCALEDSIZE);
                int yCD = targetY * (CHUNKSIZE * TILESCALEDSIZE);
                String chunkIndex = xCD + ";" + yCD;

                map.computeIfAbsent(chunkIndex, k -> genChunk(xCD, yCD));

                for (Tile tile : map.get(chunkIndex)) {
                    DrawTextureEx(tile.texture, tile.position, 0, 1, WHITE);
                }

            }
        }
    }

    public static List<Tile> genChunk(int x, int y) {
        ArrayList<Tile> chunkTiles = new ArrayList<>();

        for (int yp = 0; yp < CHUNKSIZE; yp++) {
            for (int xp = 0; xp < CHUNKSIZE; xp++) {
                float targetY = y + ((float) yp * TILESCALEDSIZE);
                float targetX = x + ((float) xp * TILESCALEDSIZE);
                Jaylib.Rectangle rec = new Jaylib.Rectangle(targetX, targetY, TILESCALEDSIZE, TILESCALEDSIZE);
                Tile tile = null;
                double height = noise.perlinNoise1D(x + xp) * 100;
                if (targetY > height) {
                    tile = new Tile(rec, false, false, false, RED, TextureManager.getTexture(0, 0));
                } else if (targetY + TILESCALEDSIZE > height) {
                    tile = new Tile(rec, true, false, false, PINK, TextureManager.getTexture(1,0));
                }
                if (tile != null) {
                    chunkTiles.add(tile);
                }
            }
        }
        return chunkTiles;
    }
}
