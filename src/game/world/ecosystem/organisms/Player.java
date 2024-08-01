package game.world.ecosystem.organisms;

import game.utilities.Position;
import game.world.ecosystem.objects.Tile;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static com.raylib.Raylib.IsKeyPressed;
import static com.raylib.Raylib.Vector2Distance;
import static game.utilities.Variables.*;
import static game.world.type.BaseWorld.*;

public class Player extends Entity {
    private static int spd;
    private static int jspd;

    private final Map<String, Boolean> state = new HashMap<>();
    private Jaylib.Rectangle rect;

    public Player(Position position, float speed, int tileSize, int worldScale) {
        super(position, speed);

        this.state.put(W_FALLING, true);
        this.state.put(W_JUMPING, false);
        this.state.put(W_GROUNDED, false);
        this.state.put(W_IDLING, false);
    }

    @Override
    public void updatePosition(float delta){
        if (IsKeyDown(getControls().get("Left"))) {
            getPosition().x(getPosition().x() - (spd * delta));
        }
        if (IsKeyDown(getControls().get("Right"))) {
            getPosition().x(getPosition().x() + (spd * delta));
        }
        if (IsKeyPressed(getControls().get("Jump")) && Boolean.TRUE.equals(state.get(W_GROUNDED))) {
            super.setSpeed(-jspd);
            state.put(W_JUMPING, true);
            state.put(W_GROUNDED, false);
        }
    }

    /**
     * Check if the player is on top of the provided tile
     * or to put it in other words check if there is a tile
     * underneath the player
     */
    public void checkBottomCollision(Tile tile){
        if (
                Boolean.TRUE.equals(tile.getCollision().get("top")) &&
                        getPosition().x() < tile.getPosition().x() + (TILESCALEDSIZE) &&
                        getPosition().x() >= tile.getPosition().x() &&
                        getPosition().y() < tile.getPosition().y() &&
                        getPosition().y() >= tile.getPosition().y() - ((float) TILESIZE / 2 * WORLDSCALE)
        ) {
            if (!IsKeyPressed(getControls().get("Jump")) || Boolean.TRUE.equals(state.get(W_FALLING))) {
                setSpeed(0);
                getPosition().y(tile.getPosition().y() - ((float) TILESIZE / 2 * WORLDSCALE));
                state.put(W_GROUNDED, true);
                state.put(W_FALLING, false);
                state.put(W_JUMPING, false);
            }
            setState(W_FALLING, false);
        }
    }

    public void updateCollision(List<Tile> chunk, int tileSize, int worldScale, int gravity, float delta) {
        updatePosition(delta);

        boolean falling = !state.get(W_JUMPING);

        if (chunk != null) {
            for (Tile tile : chunk) {
                if (Vector2Distance(tile.getPosition().getVector2(), getPosition().getVector2()) <= (tileSize * 1.5) * worldScale) {

                    // Check side collisions
                    if (
                            Boolean.TRUE.equals(tile.getCollision().get("left")) ||
                            Boolean.TRUE.equals(tile.getCollision().get("right"))
                    ) {
                        if (
                                getPosition().y() >= tile.getPosition().y() &&
                                getPosition().y() <= tile.getPosition().y() + ((float) tileSize * worldScale)
                        ) {
                            // Left side collision
                            if (
                                    Boolean.TRUE.equals(tile.getCollision().get("left")) &&
                                    getPosition().x() > tile.getPosition().x() - ((float) tileSize / 2 * worldScale) &&
                                    getPosition().x() < tile.getPosition().x()
                            ) {
                                getPosition().x(tile.getPosition().x() - ((float) tileSize / 2 * worldScale));
                            }
                            // Right side collision
                            if (
                                    Boolean.TRUE.equals(tile.getCollision().get("right")) &&
                                    getPosition().x() > tile.getPosition().x() + (tileSize * worldScale) &&
                                    getPosition().x() <= tile.getPosition().x() + (tileSize * 1.5 * worldScale)
                            ) {
                                getPosition().x(tile.getPosition().x() + (float) (tileSize * 1.5 * worldScale));
                            }
                        }
                    }
                }
            }
        }

        if (falling) {
            state.put(W_GROUNDED, false);
        }
        state.put(W_FALLING, falling);

        if (state.get("jumping") || state.get("falling")) {
            getPosition().y(getPosition().y() + getSpeed() * delta);
            if (getSpeed() > 0) {
                state.put("falling", true);
                state.put("jumping", false);
                setSpeed(getSpeed() + (gravity * 2) * delta * worldScale );
            } else {
                setSpeed(getSpeed() + gravity * delta * worldScale);
            }
        }
    }

    /*public void updatePositionOld(float delta){
        int PSPD = 200;
        int PJSPD = 350;

        if (IsKeyDown(keys.get("Left"))) {
            position.x(position.x() - (PSPD * delta));
        }
        if (IsKeyDown(keys.get("Right"))) {
            position.x(position.x() + (PSPD * delta));
        }
        if (IsKeyPressed(keys.get("Jump")) && Boolean.TRUE.equals(state.get(GROUNDED))) {
            speed = -PJSPD;
            state.put("jumping", true);
            state.put(GROUNDED, false);
        }
    }

    public static void updateCollisionOld(Player player, List<Tile> chunk, int tileSize, int worldScale, int gravity, float delta) {
        player.updatePosition(delta);

        boolean falling = !player.state.get("jumping");

        if (chunk != null){
            for (Tile tile:
                    chunk) {
                if (Vector2Distance(tile.position, player.position) <= (tileSize * 1.5) * worldScale) {
                    // Game.assets.Tile Above Col (top):
                    if (
                            tile.aboveCol && player.position.x() < tile.rectangle.x() + (tileSize * worldScale) &&
                                    player.position.x() >= tile.rectangle.x() &&
                                    player.position.y() < tile.rectangle.y() &&
                                    player.position.y() >= tile.rectangle.y() - ((float) tileSize / 2 * worldScale)
                    ) {

                        if (!IsKeyPressed(player.keys.get("Jump")) || Boolean.TRUE.equals(player.state.get("falling"))) {
                            player.speed = 0;
                            player.position.y(tile.rectangle.y() - ((float) tileSize / 2 * worldScale));
                            player.state.put(GROUNDED, true);
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
                                    (player.position.y() < tile.rectangle.y() + ((float) tileSize / 2 * worldScale) && !player.state.get(GROUNDED)) &&
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
                                    (player.position.y() < tile.rectangle.y() + ((float) tileSize / 2 * worldScale) && !player.state.get(GROUNDED)) &&
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
            player.state.put(GROUNDED, false);
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
    }*/

    // SETTERS & GETTERS:
    public Map<String, Boolean> getState() {
        return state;
    }

    public void setState(String state, Boolean status){
        this.state.computeIfPresent(state, (k, v) -> v = status);
    }

    public void setRectangle(Jaylib.Rectangle rect) {
        this.rect = rect;
    }

    public Jaylib.Rectangle getRect() {
        return rect;
    }
}
