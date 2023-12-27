import com.raylib.Jaylib;
import com.raylib.Raylib;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static com.raylib.Raylib.IsKeyPressed;
import static com.raylib.Raylib.Vector2Distance;

public class Player extends Entity {
    public static int PSPD;
    public static int PJSPD;
    public StringBuilder facing;
    public Map<String, Boolean> state = new HashMap();
    public Jaylib.Rectangle rect;
    public Raylib.Color color;
    private final Jaylib.Vector2 br_v = new Jaylib.Vector2();
    private final Jaylib.Vector2 tr_v = new Jaylib.Vector2();
    private final Jaylib.Vector2 bl_v = new Jaylib.Vector2();
    private final Jaylib.Vector2 tl_v = new Jaylib.Vector2();
    private static int nbPlayer = 0;

    public Player(Jaylib.Vector2 position, float speed, Raylib.Color color, int tile_size, int world_scale) {
        super(position, speed);
        this.speed = speed;
        this.state.put("falling", true);
        this.state.put("jumping", false);
        this.state.put("grounded", false);
        this.state.put("ideal", false);
        this.color = color;
        this.Update_Vector(tile_size, world_scale);
    }

    public void AddControls(String name, int key) {
        super.AddControls(name, key);
    }

    public void Update_Vector(int tile_size, int world_scale) {
        float offset = (float)(tile_size / 2) * (float)world_scale;
        this.br_v.x(this.position.x() + offset);
        this.br_v.y(this.position.y() + offset);
        this.tr_v.x(this.position.x() + offset);
        this.tr_v.y(this.position.y() - offset);
        this.bl_v.x(this.position.x() - offset);
        this.bl_v.y(this.position.y() + offset);
        this.tl_v.x(this.position.x() - offset);
        this.tl_v.y(this.position.y() - offset);
    }

    public void DrawVectors() {
        Jaylib.DrawRectangleRec(new Jaylib.Rectangle(this.br_v.x(), this.br_v.y(), 2.0F, 2.0F), Jaylib.RED);
        Jaylib.DrawRectangleRec(new Jaylib.Rectangle(this.tr_v.x(), this.tr_v.y(), 2.0F, 2.0F), Jaylib.GREEN);
        Jaylib.DrawRectangleRec(new Jaylib.Rectangle(this.bl_v.x(), this.bl_v.y(), 2.0F, 2.0F), Jaylib.BLACK);
        Jaylib.DrawRectangleRec(new Jaylib.Rectangle(this.tl_v.x(), this.tl_v.y(), 2.0F, 2.0F), Jaylib.YELLOW);
    }

    public static void Update_Position(Player player, ArrayList<Tile> tiles, int tile_size, int world_scale, int gravity, float delta) {
        int PSPD = 200;
        int PJSPD = 350;

        if (IsKeyDown(player.Controls.get("Left"))) {
            player.position.x(player.position.x() - (PSPD * delta));
        }
        if (IsKeyDown(player.Controls.get("Right"))) {
            player.position.x(player.position.x() + (PSPD * delta));
        }
        if (IsKeyPressed(player.Controls.get("Jump")) && player.state.get("grounded")) {
            player.speed = -PJSPD;
            player.state.put("jumping", true);
            player.state.put("grounded", false);
        }

        boolean falling;
        falling = !player.state.get("jumping");

        for (Tile tile : tiles) {
            if (Vector2Distance(tile.position, player.position) <= (tile_size * 1.5) * world_scale) {
                // Tile Above Col (top):
                if (
                        tile.aboveCol &&
                                player.position.x() < tile.rectangle.x() + (tile_size * world_scale) &&
                                player.position.x() >= tile.rectangle.x() &&
                                player.position.y() < tile.rectangle.y() &&
                                player.position.y() >= tile.rectangle.y() - ((float) tile_size / 2 * world_scale)
                ) {

                    if (!IsKeyPressed(player.Controls.get("Jump")) || player.state.get("falling")) {
                        player.speed = 0;
                        player.position.y(tile.rectangle.y() - ((float) tile_size / 2 * world_scale));
                        player.state.put("grounded", true);
                        player.state.put("falling", false);
                        player.state.put("jumping", false);
                        tile.debug = player.color;
                    }
                    falling = false;
                }


                // Side Collision:
                if (
                        tile.sideCol &&
                                !tile.aboveCol &&
                                player.position.y() >= tile.rectangle.y() &&
                                player.position.y() <= tile.rectangle.y() + ((float) tile_size* world_scale)
                ){
                    // Left Side:
                    if (
                            player.position.x() > tile.rectangle.x() - ((float) tile_size / 2 * world_scale) &&
                                    player.position.x() < tile.rectangle.x()
                    ){
                        player.position.x(tile.rectangle.x() - ((float) tile_size / 2 * world_scale));
                        tile.debug = player.color;
                        // Right Side:
                    } else if (
                            player.position.x() > tile.rectangle.x() + (tile_size * world_scale) &&
                                    player.position.x() <= tile.rectangle.x() + (tile_size * 1.5 * world_scale)
                    ) {
                        player.position.x(tile.rectangle.x() + (float) (tile_size * 1.5 * world_scale));
                        tile.debug = player.color;
                    }
                }

                // Corner Collision left:
                if (
                        tile.sideCol &&
                                tile.aboveCol &&
                                player.position.y() >= tile.rectangle.y() - (tile_size / 2 * world_scale) &&
                                (player.position.y() < tile.rectangle.y() + (tile_size * world_scale) && !player.state.get("grounded")) &&
                                player.position.x() > tile.rectangle.x() - (tile_size / 2 * world_scale) &&
                                player.position.x() < tile.rectangle.x()
                ){
                    player.position.x(tile.rectangle.x() - ((float) tile_size / 2 * world_scale));
                    tile.debug = player.color;
                }
            }
        }

        if (falling){
            player.state.put("grounded", false);
        }
        player.state.put("falling", falling);

        if (player.state.get("jumping") || player.state.get("falling")) {
            player.position.y(player.position.y() + player.speed * delta);
            if (player.speed > 0) {
                player.state.put("falling", true);
                player.state.put("jumping", false);
                player.speed += (gravity * 2) * delta * world_scale;
            } else {
                player.speed += gravity * delta * world_scale;
            }
        }
    }

    public void setRectangle(Jaylib.Rectangle rect) {
        this.rect = rect;
    }
}
