package game;

import java.util.*;

import game.common.utils.Utils;
import game.core.engine.position.Position;
import game.core.world.type.BaseWorld;
import game.exceptions.ChunkGenerationException;
import game.exceptions.IllegalMethodUsage;
import game.core.world.ecosystem.organisms.Player;
import game.core.engine.camera.AdvCamera2D;
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;

import static com.raylib.Jaylib.*;
import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.*;

/**
 * TODO: Add JAVA DOC
 */
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

        player.addControl(W_JUMP, KEY_W);
        player.addControl(W_LEFT, KEY_D);
        player.addControl(W_DOWN, KEY_S);
        player.addControl(W_RIGHT, KEY_A);
        player.addControl(W_PAUSE, KEY_X);
        player.addControl(W_RESET, KEY_R);
    }

    public static void setCameraProps(AdvCamera2D camera) {
        camera.cTV(playerList);
        camera.target(camera.getTargetPosition().getVector2());
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(3);
    }

    public static void main(String[] args) throws ChunkGenerationException {
        // Init Game:
        InitWindow(GAMEWIDTH, GAMEHEIGHT, GAME_TITLE);
        BaseWorld overWorld = new BaseWorld();
        Position spawnPosition = new Position(0, 0, W_WORLD);
        AdvCamera2D camera = new AdvCamera2D();
        // -------------------------------------------------------------------------

        // Init Default Values:
        camera.defaultControls();
        Jaylib.Color skyColor = Utils.hexColor("90e0ef");
        // -------------------------------------------------------------------------

        // Init Custom Values:
        initPlayers(spawnPosition);
        setCameraProps(camera);
        // -------------------------------------------------------------------------

        // Game Loop:
        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

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
            ClearBackground(skyColor);
            BeginMode2D(camera);

            camera.cameraController(deltaTime);

            overWorld.generateChunks(camera);
            overWorld.render(camera);

            EndMode2D();
            EndDrawing();
        }
        CloseWindow();
    }
}