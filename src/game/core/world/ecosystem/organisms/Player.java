package game.core.world.ecosystem.organisms;

import game.core.engine.position.Position;
import game.core.engine.terrain.Tile;
import com.raylib.Jaylib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static com.raylib.Raylib.IsKeyPressed;
import static com.raylib.Raylib.Vector2Distance;
import static game.common.utils.Variables.*;

public class Player extends Entity {

    // Facing = true : right
    // Facing = false : left
    public boolean facing = true;

    public Map<String, Boolean> state = new HashMap();
    public Jaylib.Rectangle rect;

    public Player(Position position) {
        super(position);
        this.state.put(W_FALLING, true);
        this.state.put(W_JUMPING, false);
        this.state.put(W_GROUNDED, false);
        this.state.put(W_IDLING, false);
    }

    public void updatePosition(Player player, List<Tile> chunk, int tileSize, int worldScale, int gravity, float delta) {
        if (IsKeyDown(player.getControls().get(W_LEFT))) {
            player.getPosition().x((int) (player.getPosition().x() - (player.getsPD() * delta)));
        }
        if (IsKeyDown(player.getControls().get(W_RIGHT))) {
            player.getPosition().x((int) (player.getPosition().x() + (player.getsPD() * delta)));
        }
        if (IsKeyPressed(player.getControls().get(W_JUMP)) && Boolean.TRUE.equals(player.state.get(W_GROUNDED))) {
            player.setsPD(-player.getuF());
            player.state.put(W_JUMPING, true);
            player.state.put(W_GROUNDED, false);
        }

        boolean falling = !player.state.get(W_JUMPING);

        if (chunk != null){
            for (Tile tile:
                    chunk) {
                if (Vector2Distance(tile.getPosition().getVector2(), player.getPosition().getVector2()) <= (tileSize * 1.5) * worldScale) {
                    // assets.Tile Above Col (top):
                    if (
                            tile.isCollidable() && player.getPosition().x() < tile.getPosition().x() + (tileSize * worldScale) &&
                                    player.getPosition().x() >= tile.getPosition().x() &&
                                    player.getPosition().y() < tile.getPosition().y() &&
                                    player.getPosition().y() >= tile.getPosition().y() - (tileSize / 2 * worldScale)
                    ) {

                        if (!IsKeyPressed(player.getControls().get(W_JUMP)) || Boolean.TRUE.equals(player.state.get(W_FALLING))) {
                            player.setsPD(0);
                            player.getPosition().y(tile.getPosition().y() - ( tileSize / 2 * worldScale));
                            player.state.put(W_GROUNDED, true);
                            player.state.put(W_FALLING, false);
                            player.state.put(W_JUMPING, false);
                        }
                        falling = false;
                    }

                    // Side Collision:
                    if (
                            tile.isCollidable() && tile.isCollidable() && !tile.isCollidable() &&
                            player.getPosition().y() >= tile.getPosition().y() &&
                            player.getPosition().y() <= tile.getPosition().y() + (tileSize * worldScale)
                    ){
                        // Left Side:
                        if (
                                player.getPosition().x() > tile.getPosition().x() - ( tileSize / 2 * worldScale) &&
                                        player.getPosition().x() < tile.getPosition().x()
                        ){
                            player.getPosition().x(tile.getPosition().x() - ( tileSize / 2 * worldScale));
                            // Right Side:
                        } else if (
                                player.getPosition().x() > tile.getPosition().x() + (tileSize * worldScale) &&
                                        player.getPosition().x() <= tile.getPosition().x() + (tileSize * 1.5 * worldScale)
                        ) {
                            player.getPosition().x((int) (tile.getPosition().x() + (tileSize * 1.5 * worldScale)));
                        }
                    }

                    // Corner Collision left:
                    if (
                            tile.isCollidable() && tile.isCollidable() && tile.isCollidable() &&
                            player.getPosition().y() >= tile.getPosition().y() - ( tileSize / 2 * worldScale) &&
                            (player.getPosition().y() < tile.getPosition().y() + ( tileSize / 2 * worldScale) && !player.state.get("grounded")) &&
                            player.getPosition().x() > tile.getPosition().x() - ( tileSize / 2 * worldScale) &&
                            player.getPosition().x() < tile.getPosition().x()
                    ){
                        player.getPosition().x(tile.getPosition().x() - ( tileSize / 2 * worldScale));
                    }

                    // Corner Collision Right:
                    if (
                            tile.isCollidable() && tile.isCollidable() && tile.isCollidable() &&
                            player.getPosition().y() >= tile.getPosition().y() - ( tileSize / 2 * worldScale) &&
                            (player.getPosition().y() < tile.getPosition().y() + ( tileSize / 2 * worldScale) && !player.state.get("grounded")) &&
                            player.getPosition().x() <= tile.getPosition().x() + ( tileSize / 2 * worldScale) &&
                            player.getPosition().x() > tile.getPosition().x()
                    ){
                        player.getPosition().x(tile.getPosition().x() - ( tileSize / 2 * worldScale));
                    }
                }
            }
        }

        if (falling){
            player.state.put(W_GROUNDED, false);
        }
        player.state.put(W_FALLING, falling);

        if (player.state.get(W_JUMPING) || player.state.get(W_FALLING)) {
            player.getPosition().y((int) (player.getPosition().y() + player.getsPD() * delta));
            if (player.getsPD() > 0) {
                player.state.put(W_FALLING, true);
                player.state.put(W_JUMPING, false);
                player.setsPD(player.getsPD() + (gravity * 2) * delta * worldScale);
            } else {
                player.setsPD(player.getsPD() + gravity * delta * worldScale);
            }
        }
    }

    public void setRectangle(Jaylib.Rectangle rect) {
        this.rect = rect;
    }

    public Jaylib.Rectangle getRect() {
        return rect;
    }

    public void updatePosition(float deltaTime){
        if (IsKeyDown(getControls().get(W_LEFT))) {
            getPosition().x((int) (getPosition().x() - (getsPD() * deltaTime)));
        }
        if (IsKeyDown(getControls().get(W_RIGHT))) {
            getPosition().x((int) (getPosition().x() + (getsPD() * deltaTime)));
        }
        if (IsKeyPressed(getControls().get(W_JUMP)) && Boolean.TRUE.equals(state.get(W_GROUNDED))) {
            setsPD(-getuF());
            state.put(W_JUMPING, true);
            state.put(W_GROUNDED, false);
        }
    }
}

