package game;

import java.util.*;

import game.engine.Position;
import game.utilities.concerns.IllegalMethodUsage;
import game.engine.concerns.InvalidIdentifierFormat;
import game.engine.noise.Perlin1D;
import game.world.ecosystem.organisms.Entity;
import game.world.ecosystem.organisms.Player;
import game.engine.AdvancedCamera2D;
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import game.world.type.OverWorld;
import org.jetbrains.annotations.NotNull;

import static com.raylib.Jaylib.*;
import static game.utilities.Variables.*;
import static game.world.type.BaseWorld.*;

public class Unnamed {

    private static final List<Player> playerList = new ArrayList<>(4);

    public static void initPlayers(Position spawnPosition) {
        playerList.add(
                new Player(
                        new Position(spawnPosition.x() + (TILESCALEDSIZE%2), spawnPosition.y() - TILESCALEDSIZE, W_WORLD)
                )
        );

        Player player = playerList.getFirst();

        player.setRectangle(
                new Rectangle(
                        (player.getPosition().x() - ((float) TILESCALEDSIZE / 2)) * 2,
                        (player.getPosition().y() - ((float) TILESCALEDSIZE / 2)) * 2,
                        TILESCALEDSIZE,
                        TILESCALEDSIZE
                )
        );

        player.addControls(W_JUMP, KEY_W);
        player.addControls(W_LEFT, KEY_D);
        player.addControls(W_DOWN, KEY_S);
        player.addControls(W_RIGHT, KEY_A);
        player.addControls(W_PAUSE, KEY_X);
        player.addControls(W_RESET, KEY_R);
    }

    public static void setCameraProps(@NotNull AdvancedCamera2D camera) {
        camera.cTV(playerList);
        camera.target(camera.getTargetPosition().getVector2());
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(1);
    }

    public static Position getSpawnPoint(int randomX, @NotNull Perlin1D noise) throws IllegalMethodUsage {
        double height = noise.getHeight(noise.getIncremental() * ((double) randomX / TILESCALEDSIZE));
        return new Position(randomX, (int) (height * 256), W_TILE).toWorld();
    }

    public static void main(String[] args) throws InvalidIdentifierFormat, IllegalMethodUsage {
        // Init Window:
        InitWindow(GAMEWIDTH, GAMEHEIGHT, "Unnamed");

        // Init Overworld:
        int spawnX = new java.util.Random().nextInt();

        OverWorld overWorld = new OverWorld();

        Position spawnPosition = getSpawnPoint(spawnX, overWorld.getPerlinNoise());

        initPlayers(spawnPosition);

        AdvancedCamera2D camera = new AdvancedCamera2D();
        setCameraProps(camera);
        // =========================================================================

        // Game Loop:
        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

            camera.cTV(playerList);
            camera.sUCC(GAMEWIDTH, GAMEHEIGHT, playerList.getFirst());

            for (Entity entity : playerList) {
                if (IsKeyPressed(entity.getControls().get(W_RESET))) {
                    entity.getPosition().x(400);
                    entity.getPosition().y(280);
                    entity.setsPD(0);
                }
            }

            if (IsKeyDown(KEY_KP_ADD)) {
                camera.zoom(camera.zoom() + 0.04f);
            }
            if (IsKeyDown(KEY_KP_SUBTRACT)) {
                camera.zoom(camera.zoom() - 0.04f);
            }
            if (camera.zoom() > 3.0) {
                camera.zoom(3.0f);
            } else if (camera.zoom() < 0.25f) {
                camera.zoom(0.25f);
            }

            BeginDrawing();
            ClearBackground(WHITE);
            BeginMode2D(camera);

            overWorld.renderChunks(camera);

            playerList.forEach(player -> DrawRectangleRec(player.getRect(), RED));

            EndMode2D();
            EndDrawing();
            playerList.forEach(player -> player.updatePosition(deltaTime));
        }
        CloseWindow();
    }
}