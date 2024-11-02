package game.generation.noise;

import com.raylib.Jaylib;
import game.core.engine.camera.AdvCamera2D;
import game.core.engine.position.Position;
import game.exceptions.IllegalMethodUsage;

import static com.raylib.Jaylib.*;

import static game.common.utils.Variables.*;
import static game.core.world.type.BaseWorld.TILESCALEDSIZE;

public class Visualizer {

    private static Perlin1D perlinNoise = new Perlin1D(0.5, 8);

    public static void drawPerlinPoints(){
        double graphX = 0;
        double incrementX = 0.0007;
        double currentHeight = 0;
        double prevHeight = currentHeight;
        for (int x = -GAMEWIDTH; x < GAMEWIDTH; x++) {
            graphX += incrementX;

            prevHeight = currentHeight;
            currentHeight = perlinNoise.getHeight(graphX) * (TILESCALEDSIZE * 27);

            //DrawLine(x - 1, (int) prevHeight, x, (int) currentHeight, BLACK);
            DrawPixel(x, (int) currentHeight, RED);
        }
    }

    public static void main(String[] args) throws IllegalMethodUsage, NoSuchMethodException {
        InitWindow(GAMEWIDTH, GAMEHEIGHT, "Unnamed - Perlin Noise Visualizer");
        AdvCamera2D camera = new AdvCamera2D();
        camera.target(new Jaylib.Vector2(0, 0));
        camera.offset(new Jaylib.Vector2((float) GAMEWIDTH / 2, (float) GAMEHEIGHT / 2));
        camera.rotation(0);
        camera.zoom(1);

        Position tilepos = new Position(0, 0, W_TILE);
        Position temp = tilepos.toTile();

        while (!WindowShouldClose()) {
            float deltaTime = GetFrameTime();

            BeginDrawing();
            ClearBackground(LIGHTGRAY);
            BeginMode2D(camera);

            if (IsKeyDown(KEY_KP_ADD)){
                camera.zoom((float) (camera.zoom() + (1 * deltaTime)));
            }
            if (IsKeyDown(KEY_KP_SUBTRACT)){
                camera.zoom((float) (camera.zoom() - (1 * deltaTime)));
            }

            drawPerlinPoints();

            EndMode2D();
            EndDrawing();

        }
        CloseWindow();

    }

}
