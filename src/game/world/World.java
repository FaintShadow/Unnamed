package game.world;

import game.world.ecosystem.objects.Tile;
import game.camera.AdvancedCamera2D;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.List;
import java.util.Map;

import static com.raylib.Jaylib.*;
import static com.raylib.Raylib.DrawTextureEx;
import static game.utilities.Variables.*;

public abstract class World {

    public static final int WORLDSCALE = 1;
    public static final int TILESIZE = (int) Math.pow(2, 5);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;
    public static final int GRAVITY = 400;

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
                String chunkIndex = xCD + ":" + yCD;

                map.computeIfAbsent(chunkIndex, k -> genChunk(xCD, yCD));

                for (Tile tile : map.get(chunkIndex)) {
                    DrawTextureEx(tile.texture, tile.getPosition(), 0, 1, WHITE);
                }

            }
        }
    }


    public static List<Tile> genChunk(int x, int y) {
        return null;
    }
}
