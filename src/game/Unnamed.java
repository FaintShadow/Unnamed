package game;

import java.util.*;

// Types:
import com.raylib.Raylib;
import game.utilities.errors.InvalidIdentifierFormat;
import game.world.ecosystem.objects.Tile;
import game.world.ecosystem.organisms.Entity;
import game.world.ecosystem.organisms.Player;
import game.camera.AdvancedCamera2D;
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import game.world.type.OverWorld;

// Libs:
import static com.raylib.Jaylib.*;
import static game.utilities.Variables.*;
import static game.world.type.BaseWorld.*;

public class Unnamed {

    private static boolean debug = false;
    private static List<Player> playerList = new ArrayList<>(4);

    public static void initPlayers(Raylib.Vector2 spawnPosition) {
        playerList.add(
                new Player(
                        new Jaylib.Vector2(spawnPosition.x() + (TILESCALEDSIZE%2), spawnPosition.y() - TILESCALEDSIZE),
                        0,
                        TILESIZE,
                        WORLDSCALE
                )
        );
        Player player = playerList.getFirst();
        player.setRectangle(
                new Rectangle(
                        (player.getPosition().x() - ((float) TILESIZE / 2)) * 2,
                        (player.getPosition().y() - ((float) TILESIZE / 2)) * 2,
                        WORLDSCALE * (float) TILESIZE, WORLDSCALE * (float) TILESIZE
                )
        );
        player.addControls(player.getControls(), W_LEFT, KEY_A);
        player.addControls(player.getControls(), W_RIGHT, KEY_D);
        player.addControls(player.getControls(), W_JUMP, KEY_W);
        player.addControls(player.getControls(), W_PAUSE, KEY_X);
        player.addControls(player.getControls(), W_RESET, KEY_R);
    }

    public static void setCameraProps(AdvancedCamera2D camera) {
        camera.addControls(camera.getControls(), W_UP, KEY_UP);
        camera.addControls(camera.getControls(), W_DOWN, KEY_DOWN);
        camera.addControls(camera.getControls(), W_LEFT, KEY_LEFT);
        camera.addControls(camera.getControls(), W_RIGHT, KEY_RIGHT);
        camera.addControls(camera.getControls(), "zoom_in", KEY_KP_ADD);
        camera.addControls(camera.getControls(), "zoom_out", KEY_KP_SUBTRACT);

        camera.cTV(playerList);
        camera.target(camera.getTargetVector());
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(1);
    }

    public static void updatePlayers(List<Player> playerList, Map<String, List<Tile>> map, float deltaTime) {
        playerList.forEach(player -> {
            player.updateCollision(player.getEntityCurrentChunk(map), TILESIZE, WORLDSCALE, GRAVITY, deltaTime);
            player.updateVector(TILESIZE, WORLDSCALE);
        });
    }

    public static void main(String[] args) throws InvalidIdentifierFormat {
        // Init Window:
        InitWindow(GAMEWIDTH, GAMEHEIGHT, "Unnamed");

        // Init Overworld:
        int spawnX = new java.util.Random().nextInt();
        int spawnY = new java.util.Random().nextInt();

        Jaylib.Vector2 spawnPosition = OverWorld.genChunk(spawnX * 1000, spawnY * 1000).getFirst().getPosition();
        Map<String, List<Tile>> map = new HashMap<>();

        initPlayers(spawnPosition);
        AdvancedCamera2D camera = new AdvancedCamera2D();
        setCameraProps(camera);

        // =========================================================================

        Jaylib.Vector2 cameraAncher = new Jaylib.Vector2();
        // Game Loop:
        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

            camera.cTV(playerList);
            AdvancedCamera2D.sUCCSF(camera, playerList.getFirst(), GAMEWIDTH, GAMEHEIGHT, deltaTime);

            for (Entity entity : playerList) {
                if (IsKeyPressed(entity.getControls().get(W_RESET))) {
                    entity.getPosition().x(400);
                    entity.getPosition().y(280);
                    entity.setSpeed(0.0f);
                }
            }

            BeginDrawing();
            ClearBackground(WHITE);
            BeginMode2D(camera);

            OverWorld.renderChunks(camera, cameraAncher, map);

            // Entities.Player:
            playerList.forEach(player -> {
                player.getPosition().x(player.getPosition().x() - (((float) TILESIZE / 2) * WORLDSCALE));
                player.getPosition().y(player.getPosition().y() - (((float) TILESIZE / 2) * WORLDSCALE));
                DrawRectangleRec(player.getRect(), RED);
            });

            EndMode2D();
            //updatePlayers(playerList, map, deltaTime);
            EndDrawing();
        }
        CloseWindow();
    }
}