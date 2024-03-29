package entities;

import assets.Tile;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.List;
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

    public Player(Jaylib.Vector2 position, float speed, Raylib.Color color, int tileSize, int worldScale) {
        super(position, speed);
        this.speed = speed;
        this.state.put("falling", true);
        this.state.put("jumping", false);
        this.state.put("grounded", false);
        this.state.put("ideal", false);
        this.color = color;
        this.Update_Vector(tileSize, worldScale);
    }

    public void Update_Vector(int tileSize, int worldScale) {
        float offset = (float)(tileSize / 2) * (float)worldScale;
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
        Raylib.DrawRectangleRec(new Jaylib.Rectangle(this.br_v.x(), this.br_v.y(), 2.0F, 2.0F), Jaylib.RED);
        Raylib.DrawRectangleRec(new Jaylib.Rectangle(this.tr_v.x(), this.tr_v.y(), 2.0F, 2.0F), Jaylib.GREEN);
        Raylib.DrawRectangleRec(new Jaylib.Rectangle(this.bl_v.x(), this.bl_v.y(), 2.0F, 2.0F), Jaylib.BLACK);
        Raylib.DrawRectangleRec(new Jaylib.Rectangle(this.tl_v.x(), this.tl_v.y(), 2.0F, 2.0F), Jaylib.YELLOW);
    }

    public static void updatePosition(Player player, List<Tile> chunk, int tileSize, int worldScale, int gravity, float delta) {
        int PSPD = 200;
        int PJSPD = 350;

        if (IsKeyDown(player.keys.get("Left"))) {
            player.position.x(player.position.x() - (PSPD * delta));
        }
        if (IsKeyDown(player.keys.get("Right"))) {
            player.position.x(player.position.x() + (PSPD * delta));
        }
        if (IsKeyPressed(player.keys.get("Jump")) && Boolean.TRUE.equals(player.state.get("grounded"))) {
            player.speed = -PJSPD;
            player.state.put("jumping", true);
            player.state.put("grounded", false);
        }

        boolean falling = !player.state.get("jumping");

        if (chunk != null){
            for (Tile tile:
                    chunk) {
                if (Vector2Distance(tile.position, player.position) <= (tileSize * 1.5) * worldScale) {
                    // assets.Tile Above Col (top):
                    if (
                            tile.aboveCol && player.position.x() < tile.rectangle.x() + (tileSize * worldScale) &&
                                    player.position.x() >= tile.rectangle.x() &&
                                    player.position.y() < tile.rectangle.y() &&
                                    player.position.y() >= tile.rectangle.y() - ((float) tileSize / 2 * worldScale)
                    ) {

                        if (!IsKeyPressed(player.keys.get("Jump")) || Boolean.TRUE.equals(player.state.get("falling"))) {
                            player.speed = 0;
                            player.position.y(tile.rectangle.y() - ((float) tileSize / 2 * worldScale));
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
                                    player.position.y() <= tile.rectangle.y() + ((float) tileSize * worldScale)
                    ){
                        // Left Side:
                        if (
                                player.position.x() > tile.rectangle.x() - ((float) tileSize / 2 * worldScale) &&
                                        player.position.x() < tile.rectangle.x()
                        ){
                            player.position.x(tile.rectangle.x() - ((float) tileSize / 2 * worldScale));
                            tile.debug = player.color;
                            // Right Side:
                        } else if (
                                player.position.x() > tile.rectangle.x() + (tileSize * worldScale) &&
                                        player.position.x() <= tile.rectangle.x() + (tileSize * 1.5 * worldScale)
                        ) {
                            player.position.x(tile.rectangle.x() + (float) (tileSize * 1.5 * worldScale));
                            tile.debug = player.color;
                        }
                    }

                    // Corner Collision left:
                    if (
                            tile.sideCol && tile.aboveCol &&
                                    player.position.y() >= tile.rectangle.y() - ((float) tileSize / 2 * worldScale) &&
                                    (player.position.y() < tile.rectangle.y() + ((float) tileSize / 2 * worldScale) && !player.state.get("grounded")) &&
                                    player.position.x() > tile.rectangle.x() - ((float) tileSize / 2 * worldScale) &&
                                    player.position.x() < tile.rectangle.x()
                    ){
                        player.position.x(tile.rectangle.x() - ((float) tileSize / 2 * worldScale));
                        tile.debug = player.color;
                    }

                    // Corner Collision Right:
                    if (
                            tile.sideCol && tile.aboveCol &&
                                    player.position.y() >= tile.rectangle.y() - ((float) tileSize / 2 * worldScale) &&
                                    (player.position.y() < tile.rectangle.y() + ((float) tileSize / 2 * worldScale) && !player.state.get("grounded")) &&
                                    player.position.x() <= tile.rectangle.x() + ((float) tileSize / 2 * worldScale) &&
                                    player.position.x() > tile.rectangle.x()
                    ){
                        player.position.x(tile.rectangle.x() - ((float) tileSize / 2 * worldScale));
                        tile.debug = player.color;
                    }
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
                player.speed += (gravity * 2) * delta * worldScale;
            } else {
                player.speed += gravity * delta * worldScale;
            }
        }
    }

    public void setRectangle(Jaylib.Rectangle rect) {
        this.rect = rect;
    }
}
