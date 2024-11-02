package game;

import java.util.*;

import game.core.engine.position.Position;
import game.core.world.type.BaseWorld;
import game.exceptions.ChunkGenerationException;
import game.exceptions.IllegalMethodUsage;
import game.exceptions.InvalidIdentifierFormat;
import game.generation.noise.Perlin1D;
import game.core.world.ecosystem.organisms.Entity;
import game.core.world.ecosystem.organisms.Player;
import game.core.engine.camera.AdvCamera2D;
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import game.core.world.type.OverWorld;

import static com.raylib.Jaylib.*;
import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.*;

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

    public static void setCameraProps(AdvCamera2D camera) {
        camera.cTV(playerList);
        camera.target(camera.getTargetPosition().getVector2());
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(3);
    }

    public static void main(String[] args) throws InvalidIdentifierFormat, IllegalMethodUsage, ChunkGenerationException {
        // Init Window:
        InitWindow(GAMEWIDTH, GAMEHEIGHT, "Unnamed");

        // Init Overworld:
        BaseWorld overWorld = new BaseWorld();
        Position spawnPosition = new Position(0, 0, W_WORLD);
        initPlayers(spawnPosition);

        AdvCamera2D camera = new AdvCamera2D();
        setCameraProps(camera);
        // =========================================================================
        // Game Loop:
        while (!WindowShouldClose()) {


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

            overWorld.generateChunks(camera);
            overWorld.renderChunks(camera);

            EndMode2D();
            EndDrawing();
        }
        CloseWindow();
    }
}