package game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Types:
import game.assets.Tile;
import game.entities.Entity;
import game.entities.Player;
import game.camera.AdvancedCamera2D;
import game.utilities.World;
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import com.raylib.Raylib.Texture;

// Libs:
import static com.raylib.Jaylib.*;
import static java.lang.Math.exp;
import static java.lang.Math.sin;

public class Unnamed {
    public static final int WORLDSCALE = 1;
    public static final int CHUNKSIZE = 8;
    public static final int DEBUGFONTSIZE = 16;
    public static final String RESET = "Reset";
    public static final String PAUSE = "Pause";
    public static final String RIGHT = "Right";
    public static final String LEFT = "LEFT";
    public static final int GAMEWIDTH = 800 * 2;
    public static final int GAMEHEIGHT = 450 * 2;
    public static final int TILESIZE = (int) Math.pow(2, 5);
    public static final int TILESCALEDSIZE = TILESIZE * WORLDSCALE;
    public static final int GRAVITY = 400;

    private static boolean debug = false;
    private static List<Player> pL = new ArrayList<>(4);
    private static HashMap<String, HashMap<String, Texture>> textures = new HashMap<>();

    public static void debugInfo() {
        String[] ldebuginfo = new String[]{
                "FPS: " + GetFPS(),
                "Camera X/Y:",
                "Loaded Chunks: ",
        };
        int i = 0;
        for (String msg : ldebuginfo) {
            DrawText(msg, 10, DEBUGFONTSIZE * (i), DEBUGFONTSIZE, BLACK);
            i++;
        }
        i = 0;

        String[] rdebuginfo = new String[]{
                GetCurrentMonitor() + " : Monitor",
                GAMEWIDTH + " : Game Width",
                GAMEHEIGHT + " : Game Height",
                GetFrameTime() + " : Frame Time"
        };

        for (String msg : rdebuginfo) {
            DrawText(msg, GAMEWIDTH - msg.length() * (DEBUGFONTSIZE / 2), DEBUGFONTSIZE * (i), DEBUGFONTSIZE, BLACK);
            i++;
        }
    }

    public static List<Tile> genChunk(int x, int y, Texture underGround, Texture groundGrass) {
        ArrayList<Tile> chunkTiles = new ArrayList<>();

        for (int yp = 0; yp < CHUNKSIZE; yp++) {
            for (int xp = 0; xp < CHUNKSIZE; xp++) {
                float targetY = y + ((float) yp * TILESCALEDSIZE);
                float targetX = x + ((float) xp * TILESCALEDSIZE);
                double height = 0.3 * ( -3.2 * sin(-0.7 * x+xp) - 0.3 * sin(-1.7 * exp(x+xp)) + 1.9 * sin(0.7 * PI * x+xp));
                Rectangle rec = new Rectangle(targetX, targetY, TILESCALEDSIZE, TILESCALEDSIZE);
                Tile tile = null;
                if (targetY > 300) {
                    tile = new Tile(rec, false, false, false, RED, underGround);
                } else if (targetY >= 266) {
                    tile = new Tile(rec, true, false, false, PINK, groundGrass);
                }
                if (tile != null) {
                    chunkTiles.add(tile);
                }
            }
        }
        return chunkTiles;
    }

    public static void initPlayers() {
        // - PL1:
        pL.add(new Player(new Jaylib.Vector2(400, 100), 0, WHITE, TILESIZE, WORLDSCALE));
        Player player = pL.get(0);
        player.setRectangle(new Rectangle((player.position.x() - ((float) TILESIZE / 2)) * 2, (player.position.y() - ((float) TILESIZE / 2)) * 2, WORLDSCALE * (float) TILESIZE, WORLDSCALE * (float) TILESIZE));
        player.addControls("Left", KEY_A);
        player.addControls(RIGHT, KEY_D);
        player.addControls("Jump", KEY_W);
        player.addControls(PAUSE, KEY_X);
        player.addControls(RESET, KEY_R);
        // - PL2:
        /*pL.add(new Player(new Jaylib.Vector2(450, 100), 0, YELLOW, TILESIZE, WORLDSCALE));
        player = pL.get(1);
        player.setRectangle(new Rectangle((player.position.x() - ((float) TILESIZE / 2)) * 2, (player.position.y() - ((float) TILESIZE / 2)) * 2, WORLDSCALE * (float) TILESIZE, WORLDSCALE * (float) TILESIZE));
        player.addControls("Left", KEY_LEFT);
        player.addControls(RIGHT, KEY_RIGHT);
        player.addControls("Jump", KEY_UP);
        player.addControls(PAUSE, KEY_KP_0);
        player.addControls(RESET, KEY_KP_3);*/
        // - PL3:
        /*pL.add(new Player(new Jaylib.Vector2(425, 100), 0, BLUE, TILESIZE, WORLDSCALE));
        player = pL.get(2);
        player.setRectangle(new Rectangle((player.position.x() - ((float) TILESIZE / 2)) * 2, (player.position.y() - ((float) TILESIZE / 2)) * 2, WORLDSCALE * (float) TILESIZE, WORLDSCALE * (float) TILESIZE));
        player.addControls("Left", KEY_J);
        player.addControls(RIGHT, KEY_L);
        player.addControls("Jump", KEY_I);
        player.addControls(PAUSE, KEY_O);
        player.addControls(RESET, KEY_U);*/
    }

    public static void setCameraProps(AdvancedCamera2D camera) {
        camera.AddControls("up", KEY_UP);
        camera.AddControls("down", KEY_DOWN);
        camera.AddControls("left", KEY_LEFT);
        camera.AddControls("right", KEY_RIGHT);
        camera.AddControls("zoom_in", KEY_KP_ADD);
        camera.AddControls("zoom_out", KEY_KP_SUBTRACT);

        camera.setTargetVector(new Jaylib.Vector2(pL.get(0).position.x(), pL.get(0).position.y()));
        camera.target(camera.getTargetVector());
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(1);
    }

    public static void updatePlayers(List<Player> playerList, Map<String, List<Tile>> map, float deltaTime) {
        for (Player player : playerList) {
            Player.updateCollision(player, player.getEntityCurrentChunk(map), TILESIZE, WORLDSCALE, GRAVITY, deltaTime);
            player.Update_Vector(TILESIZE, WORLDSCALE);
        }
    }

    public static void main(String[] args) {
        // Init Window:
        InitWindow(GAMEWIDTH, GAMEHEIGHT, "Unnamed");

        // Init:
        Texture underGround = LoadTexture("textures/UnderGround.png");
        Texture groundGrass = LoadTexture("textures/GroundGrass.png");
        Map<String, List<Tile>> map = new HashMap<>();
        initPlayers();
        AdvancedCamera2D camera = new AdvancedCamera2D();
        setCameraProps(camera);
        // =========================================================================

        Jaylib.Vector2 cameraAncher = new Jaylib.Vector2();
        // Game Loop:
        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

            camera.CTV(pL);
            AdvancedCamera2D.MUCCSF(camera, GAMEWIDTH, GAMEHEIGHT, deltaTime);

            if (IsKeyDown(camera.keys.get("zoom_in"))) {
                camera.zoom(camera.zoom() + deltaTime);
            }
            if (IsKeyDown(camera.keys.get("zoom_out"))) {
                camera.zoom(camera.zoom() - deltaTime);
            }
            if (camera.zoom() > 3.0) {
                camera.zoom(3.0f);
            } else if (camera.zoom() < 0.25f) {
                camera.zoom(0.25f);
            }

            for (Entity entity : pL) {
                if (IsKeyPressed(entity.keys.get(RESET))) {
                    entity.position.x(400);
                    entity.position.y(280);
                    entity.speed = 0.0f;
                }
            }

            BeginDrawing();
            ClearBackground(LIGHTGRAY);
            BeginMode2D(camera);

            World.renderChunks(camera, cameraAncher, map, underGround, groundGrass);

            // Entities.Player:
            for (Player player :
                    pL) {
                player.rect.x(player.position.x() - (((float) TILESIZE / 2) * WORLDSCALE));
                player.rect.y(player.position.y() - (((float) TILESIZE / 2) * WORLDSCALE));
                DrawRectangleRec(player.rect, player.color);
            }

            EndMode2D();
            DrawText(String.valueOf(GetFPS()), 0, 0, DEBUGFONTSIZE + 20, BLACK);
            updatePlayers(pL, map, deltaTime);
            EndDrawing();
        }
        CloseWindow();
    }
}