// Java:
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

// Types:
import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import com.raylib.Raylib.Texture;

// Libs:
import static com.raylib.Jaylib.*;

public class Main {
    public static int world_scale = 1;
    public static int game_width = 800*2;
    public static int game_height = 450*2;
    public static int gravity = 400 / world_scale;
    public static int tile_size = 24;
    public static boolean debug = false;
    public static String[] debuginfo;
    public static int debfontsize = 32;

    public static void DebugInfo(Player player){
        debuginfo = new String[]{
                "FPS: " + GetFPS(),
                "",
                "XY:" + String.valueOf((int) player.position.x()) + " , " + String.valueOf((int) player.position.y()),
                "Speed:" + player.speed,
                "Facing:" + player.facing,
                "State:" + player.state
        };
        int i = 0;
        for (String msg : debuginfo){
            DrawText(msg, 10, debfontsize * (i+1), debfontsize, BLACK);
            i++;
        }
    }

    public static ArrayList<int[]> read_csv(String FilePath) throws FileNotFoundException {
        File world_file = new File("lvl1.csv");
        Scanner world = new Scanner(world_file);
        ArrayList<int[]> map = new ArrayList<int[]>();

        while (world.hasNextLine()) {
            String[] data = world.nextLine().split(",");
            int[] int_array = new int[data.length];
            for (int i = 0; i < int_array.length; i++) {
                int_array[i] = Integer.parseInt(data[i]);
            }
            map.add(int_array);
        }
        return map;
    }

    public static void main(String[] args) throws FileNotFoundException {
        boolean gravity_out = false;
        boolean gravity_out_target;

        // Init Window:
        InitWindow(game_width, game_height, "Unnamed");
        SetTargetFPS(60);

        //Init Player, world and textures
        Texture UnderGround = LoadTexture("textures/UnderGround.png");
        Texture UnderGround2 = LoadTexture("textures/UnderGround2.png");
        Texture GroundGrass = LoadTexture("textures/GroundGrass.png");
        Texture GroundDead = LoadTexture("textures/GroundDead.png");

        Texture SideLeft = LoadTexture("textures/Side_left.png");
        Texture SideRight = LoadTexture("textures/Side_right.png");
        Texture CornerRightGrass = LoadTexture("textures/Corner_right_Grass.png");
        Texture CornerLeftGrass = LoadTexture("textures/Corner_left_Grass.png");
        Texture CornerRightDead = LoadTexture("textures/Corner_right_Dead.png");
        Texture CornerLeftDead = LoadTexture("textures/Corner_left_Dead.png");

        // Load Level
        ArrayList<int[]> level_file = read_csv("lvl1.csv");

        // Generate Tile Map:
        ArrayList<Tile> level_map = new ArrayList<Tile>();

        for (int y = 0; y < level_file.size(); y++) {
            int[] row = level_file.get(y);
            for (int x = 0; x < row.length; x++) {
                int col = level_file.get(y)[x];
                Rectangle rec = new Rectangle(x * tile_size * world_scale, y * tile_size * world_scale, tile_size * world_scale, tile_size * world_scale);
                if (col != -1){
                    Tile atile = new Tile();
                    if (row.length - 1 > x && x >= 1 && y > 0){
                        if (level_file.get(y - 1)[x] == -1){
                            // AboveCol Only:
                            atile = new Tile(rec, true, false, false, PINK, GroundGrass);
                            // Top and Sides:
                            if (level_file.get(y)[x + 1] == -1){
                                atile.sideCol = true;
                                atile.color = LIME;
                                atile.texture = CornerRightGrass;
                            } else if (level_file.get(y)[x - 1] == -1) {
                                atile.sideCol = true;
                                atile.color = LIME;
                                atile.texture = CornerLeftGrass;
                            }
                        } // Side Col Only:
                        else if (level_file.get(y)[x + 1] == -1) {
                            atile = new Tile(rec, false, true, false, ORANGE, SideRight);
                        } else if (level_file.get(y)[x - 1] == -1) {
                            atile = new Tile(rec, false, true, false, ORANGE, SideLeft);
                        } else {
                            atile = new Tile(rec, false, false, false, RED, UnderGround);
                        }
                    } else if (x == 0) {
                        atile = new Tile(rec, false, false, false, PURPLE, UnderGround);
                        if (level_file.get(y - 1)[x] == -1){
                            atile.color = PINK;
                            atile.texture = GroundGrass;
                        }
                    }
                    if (atile.rectangle != null){
                        level_map.add(atile);
                    }
                }

            }
        }

        // Main Init:
        ArrayList<Entity> PL = new ArrayList<Entity>(4);
        // - PL1:
        PL.add(new Player(new Jaylib.Vector2(400, 200), 0, WHITE,tile_size, world_scale));
        Player player = (Player) PL.get(0);
        player.setRectangle(new Rectangle((player.position.x() - (float)(tile_size / 2)) * 2, (player.position.y() - (float)(tile_size / 2)) * 2, world_scale * tile_size, world_scale * tile_size));
        player.AddControls("Left", KEY_A);
        player.AddControls("Right", KEY_D);
        player.AddControls("Jump", KEY_W);
        player.AddControls("Pause", KEY_X);
        player.AddControls("Reset", KEY_R);
        // - PL2:
        PL.add(new Player(new Jaylib.Vector2(450, 200), 0, YELLOW, tile_size, world_scale));
        player = (Player) PL.get(1);
        player.setRectangle(new Rectangle((player.position.x() - (float)(tile_size / 2)) * 2, (player.position.y() - (float)(tile_size / 2)) * 2, world_scale * tile_size, world_scale * tile_size));
        player.AddControls("Left", KEY_LEFT);
        player.AddControls("Right", KEY_RIGHT);
        player.AddControls("Jump", KEY_UP);
        player.AddControls("Pause", KEY_KP_0);
        player.AddControls("Reset", KEY_KP_3);
        // - PL3:
        PL.add(new Player(new Jaylib.Vector2(425, 220), 0, BLUE, tile_size, world_scale));
        player = (Player) PL.get(2);
        player.setRectangle(new Rectangle((player.position.x() - (float)(tile_size / 2)) * 2, (player.position.y() - (float)(tile_size / 2)) * 2, world_scale * tile_size, world_scale * tile_size));
        player.AddControls("Left", KEY_J);
        player.AddControls("Right", KEY_L);
        player.AddControls("Jump", KEY_I);
        player.AddControls("Pause", KEY_O);
        player.AddControls("Reset", KEY_U);

        AdvancedCamera2D camera = new AdvancedCamera2D();
        camera.setTargetVector(new Jaylib.Vector2(PL.get(0).position.x(), PL.get(0).position.y()));
        camera.target(camera.getTargetVector());

        camera.offset(new Jaylib.Vector2((float) game_width / 2, (float) game_height / 2));
        camera.rotation(0);
        camera.zoom(1.0f);

        boolean deb = debug;
        boolean pause = false;

        // Game Loop:
        while (!WindowShouldClose()){
            for (Entity entity: PL) {
                if (IsKeyPressed(entity.Controls.get("Pause"))){
                    pause = !pause;
                }
            }

            float delta_time = 0.0f;
            if (!pause){
                delta_time = GetFrameTime();
            }

            for (Entity entity: PL) {
                Player player_ = (Player) entity;
                Player.Update_Position(player_, level_map, tile_size, world_scale, gravity, delta_time);
                player_.Update_Vector(tile_size, world_scale);
            }

            camera.CTV(PL);
            //AdvancedCamera2D.MUCCIM(camera, level_map, game_width, game_height, delta_time);
            AdvancedCamera2D.MUCCSF(camera, game_width, game_height, delta_time);

            if (IsKeyDown(KEY_C)) camera.zoom(camera.zoom() + 0.03f);
            if (camera.zoom() > 3.0){
                camera.zoom(3.0f);
            } else if(camera.zoom() < 0.25) {
                camera.zoom(0.25f);
            }

            if (IsKeyPressed(KEY_F1)){
                deb = !deb;
            }

            for (Entity entity : PL) {
                if (IsKeyPressed(entity.Controls.get("Reset"))){
                    entity.position.x(400);
                    entity.position.y(280);
                    entity.speed = 0.0f;
                }
            }

            BeginDrawing();
            ClearBackground(LIGHTGRAY);
            BeginMode2D(camera);

            // Map Drawing:
            for (Tile tile : level_map) {
                    DrawTextureEx(tile.texture, tile.position, 0, (tile.rectangle.height() / tile.texture.width()), WHITE);
                if (deb){
                    if (tile.debug == null){
                        DrawRectangleRec(tile.rectangle, tile.color);
                    } else {
                        DrawRectangleRec(tile.rectangle, tile.debug);
                    }
                }
            }

            DrawPixelV(camera.targetV, GREEN);
            DrawRectangleV(camera.targetV, new Jaylib.Vector2(5, 5), GREEN);

            // Player:
            for (Entity entity :
                    PL) {
                Player player_ = (Player) entity;
                player_.rect.x(entity.position.x() - (float)((tile_size / 2) * world_scale));
                player_.rect.y(entity.position.y() - (float)((tile_size / 2) * world_scale));
                DrawRectangleRec(player_.rect, player_.color);
            }

            if(deb){
                for (Tile tile : level_map) {
                        if (tile.debug != null){
                            DrawRectangleRec(tile.rectangle, tile.debug);
                        }
                        tile.debug = null;
                }
                for (Entity entity :
                        PL) {
                    Player player_ = (Player) entity;
                    player_.DrawVectors();
                }
            }

            EndMode2D();
            if (deb){
                DebugInfo((Player) PL.get(0));
            }

            EndDrawing();
        }
        CloseWindow();
    }
}