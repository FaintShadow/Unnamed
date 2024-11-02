package game.core.engine.camera;

import game.core.engine.position.Position;
import game.exceptions.IllegalMethodUsage;
import game.common.interfaces.Controllable;
import game.core.engine.terrain.Tile;
import game.core.world.ecosystem.organisms.Entity;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static game.common.utils.Variables.*;

/**
 * Advanced 2D camera system that extends Raylib's Camera2D with additional
 * features for entity tracking, smooth following, and boundary management.
 */
public class AdvCamera2D extends Raylib.Camera2D implements Controllable<String, Integer> {
    private final Map<String, Integer> controls = new HashMap<>();

    private Position targetPosition = new Position();

    /**
     * @return The current control key mappings
     */
    public Map<String, Integer> getControls() {
        return controls;
    }

    /**
     * @return The current target position of the camera
     */
    public Position getTargetPosition() {
        return targetPosition;
    }

    /**
     * Sets the target position for the camera
     * @param pos The new target position
     */
    public void setTargetPosition(Position pos) {
        this.targetPosition = pos;
    }

    /**
     * Calculates and updates the camera target vector based on nearby entities.
     * Only considers entities within 400 units of the current target position.
     *
     * @param el List of entities to consider for target calculation
     */
    public void cTV(List<? extends Entity> el) {
        float totX = 0;
        float totY = 0;
        float ignoreDistance = 400;
        int i = 0;

        for (Entity E : el) {
            if (Raylib.Vector2Distance(E.getPosition().getVector2(), this.targetPosition.getVector2()) < ignoreDistance) {
                totX += E.getPosition().x();
                totY += E.getPosition().y();
                ++i;
            }
        }

        int avgX = Math.divideExact((int) totX, i);
        int avgY = Math.divideExact((int) totY, i);

        this.targetPosition.x(avgX);
        this.targetPosition.y(avgY);
    }

    // Single Position Camera Center
    /**
     * Centers the camera on a single entity.
     *
     * @param width Game width
     * @param height Game height
     * @param entity The entity to center on
     */
    public void sPCC(int width, int height, Entity entity) {
        super.offset(new Jaylib.Vector2(width / 2.0F, height / 2.0F));
        super.target(entity.getPosition().getVector2());
    }

    // Single Position Camera Center Smooth Follow
    /**
     * Implements smooth following behavior for a single position.
     * Uses minimum speed and distance thresholds for smooth movement.
     *
     * @param camera The camera instance
     * @param entity The entity to follow
     * @param width Game width
     * @param height Game height
     * @param delta Time elapsed since last frame
     */
    public static void sPCCSF(AdvCamera2D camera, Entity entity, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;
        camera.offset().x(Math.floorMod(width, 2));
        camera.offset().y(Math.floorMod(height, 2));
        Raylib.Vector2 difference = Raylib.Vector2Subtract(entity.getPosition().getVector2(), camera.target());
        float length = Raylib.Vector2Length(difference);
        if (length > minEffLength) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(difference, speed * delta / length)));
        }

    }

    // Multi Position Camera Center Smooth Follow:
    /**
     * Implements smooth following behavior for multiple entities.
     * Uses minimum speed and distance thresholds for smooth movement.
     *
     * @param camera The camera instance
     * @param width Game width
     * @param height Game height
     * @param delta Time elapsed since last frame
     */
    public static void mPCCSF(AdvCamera2D camera, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((float)(width) / 2);
        camera.offset().y((float)(height) / 2);
        Raylib.Vector2 difference = Raylib.Vector2Subtract(camera.targetPosition.getVector2(), camera.target());

        float length = Raylib.Vector2Length(difference);
        if (length > minEffLength) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(difference, speed * delta / length)));
        }

    }

    // Multi Position Camera Center With Boundary
    /**
     * Implements improved multi-entity camera centering with tile boundary constraints.
     * Prevents the camera from showing areas outside the tile boundaries.
     *
     * @param camera The camera instance
     * @param tiles List of tiles defining the boundary
     * @param width Game width
     * @param height Game height
     * @param delta Time elapsed since last frame
     */
    public static void mPCCWB(AdvCamera2D camera, List<Tile> tiles, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((width / 2));
        camera.offset().y((height / 2));
        Raylib.Vector2 difference = Raylib.Vector2Subtract(camera.targetPosition.getVector2(), camera.target());

        float length = Raylib.Vector2Length(difference);
        float minX;
        if (length > minEffLength) {
            minX = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(difference, minX * delta / length)));
        }

        minX = 0.0F;
        float minY = 0.0F;
        float maxX = 0.0F;
        float maxY = 0.0F;

        for (Tile tile : tiles) {
            minX = Math.min(tile.getPosition().x(), minX);
            maxX = Math.max(tile.getPosition().x(), maxX);
            minY = Math.min(tile.getPosition().y(), minY);
            maxY = Math.max(tile.getPosition().y(), maxY);
        }

        Raylib.Vector2 wmax = Raylib.GetWorldToScreen2D(new Jaylib.Vector2(maxX, maxY), camera);
        Raylib.Vector2 wmin = Raylib.GetWorldToScreen2D(new Jaylib.Vector2(minX, minY), camera);

        if (wmax.x() < width) {
            camera.offset().x(width - (wmax.x() - ((float) width / 2)));
        }

        if (wmax.y() < height) {
            camera.offset().y(height - (wmax.y() - ((float) height / 2)));
        }

        if (wmin.x() > 0.0F) {
            camera.offset().x(((float) width / 2) - wmin.x());
        }

        if (wmin.y() > 0.0F) {
            camera.offset().y(((float) height / 2) - wmin.y());
        }

    }

    /**
     * Handles keyboard input for manual camera control.
     *
     * @param delta Time elapsed since last frame
     */
    public void cameraController(float delta) {
        int cspd = 300;
        int cuspd = 300;

        if (IsKeyDown(controls.get(W_LEFT))) {
            targetPosition.x((int) (targetPosition.x() - (cspd * delta)));
        }
        if (IsKeyDown(controls.get(W_RIGHT))) {
            targetPosition.x((int) (targetPosition.x() + (cspd * delta)));
        }
        if (IsKeyDown(controls.get(W_UP))) {
            targetPosition.y((int) (targetPosition.y() - (cuspd * delta)));
        }
        if (IsKeyDown(controls.get(W_DOWN))) {
            targetPosition.y((int) (targetPosition.y() + (cuspd * delta)));
        }
    }

    /**
     * Get the XY of the screen's corners<br>
     * Corner mapping:
     * <table>
     *     <tr>
     *         <th></th>
     *         <th>Left</th>
     *         <th>Right</th>
     *     </tr>
     *     <tr>
     *         <td>Top</td>
     *         <td>1</td>
     *         <td>2</td>
     *     </tr>
     *     <tr>
     *         <td>Bottom</td>
     *         <td>4</td>
     *         <td>3</td>
     *     </tr>
     * </table>
     *
     * @param corner Corner identifier (1-4)
     * @return Position in world coordinates
     * @throws IllegalArgumentException if corner identifier is invalid
     * @throws IllegalMethodUsage if method usage is incorrect
     */
    public Position getScreenCornerWorldPosition(int corner) throws IllegalMethodUsage, IllegalArgumentException {
        return switch (corner) {
            case 1 -> new Position(0, 0, W_SCREEN).toWorld(this);
            case 2 -> new Position(GAMEWIDTH, 0, W_SCREEN).toWorld(this);
            case 3 -> new Position(GAMEWIDTH, GAMEHEIGHT, W_SCREEN).toWorld(this);
            case 4 -> new Position(0, GAMEHEIGHT, W_SCREEN).toWorld(this);
            default -> throw new IllegalArgumentException("Incorrect corner identifier, Must be 1-4");
        };
    }

    /**
     * Adds a new control key binding.
     *
     * @param name The name of the control
     * @param key The key code to bind
     */
    @Override
    public void addControls(String name, Integer key) {
        controls.put(name, key);
    }
}

