package game.camera;

import game.utilities.Position;
import game.utilities.interfaces.Controllable;
import game.world.ecosystem.objects.Tile;
import game.world.ecosystem.organisms.Entity;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;

public class AdvancedCamera2D extends Raylib.Camera2D implements Controllable {
    private Map<String, Integer> controls = new HashMap<>();
    private Position target = new Position();

    public Map<String, Integer> getControls() {
        return controls;
    }

    public Position getTarget() {
        return target;
    }

    public void setTarget(Position pos) {
        this.target = pos;
    }

    // Camera Target Vector:
    public void cTV(List<? extends Entity> el) {
        float totX = 0.0F;
        float totY = 0.0F;
        float ignoreDistance = 400.0F;
        int i = 0;

        for (Entity E : el) {
            if (Raylib.Vector2Distance(E.getPosition().getVector2(), this.target.getVector2()) < ignoreDistance) {
                totX += E.getPosition().x();
                totY += E.getPosition().y();
                ++i;
            }
        }

        float avgX = totX / i;
        float avgY = totY / i;
        this.target.x(avgX);
        this.target.y(avgY);
    }

    // Single U? Camera Center:
    public void sUCC(int width, int height, Entity entity) {
        super.offset(new Jaylib.Vector2(width / 2.0F, height / 2.0F));
        super.target(entity.getPosition().getVector2());
    }

    // Single U? Camera Center:
    public void mUCC(int width, int height) {
        super.offset(new Jaylib.Vector2(width / 2.0F, height / 2.0F));
        super.target(target.getVector2());
    }

    // Single U? Camera Center Smooth Follow
    public static void sUCCSF(AdvancedCamera2D camera, Entity entity, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;
        //camera.offset(new Jaylib.Vector2((width / 2), (height / 2)));
        camera.offset().x(Math.floorMod(width, 2));
        camera.offset().y(Math.floorMod(height, 2));
        Raylib.Vector2 difference = Raylib.Vector2Subtract(entity.getPosition().getVector2(), camera.target());
        float length = Raylib.Vector2Length(difference);
        if (length > minEffLength) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(difference, speed * delta / length)));
        }

    }

    // Multi U? Camera Center Smooth Follow:
    public static void mUCCSF(AdvancedCamera2D camera, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x(width / 2);
        camera.offset().y(height / 2);
        Raylib.Vector2 difference = Raylib.Vector2Subtract(camera.target.getVector2(), camera.target());

        float length = Raylib.Vector2Length(difference);
        if (length > minEffLength) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(difference, speed * delta / length)));
        }

    }

    // Multi ? Camera Center
    public static void mUCCIM(AdvancedCamera2D camera, List<Tile> tiles, int width, int height, float delta) {
        float minSPD = 30.0F;
        float minEffLength = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((width / 2));
        camera.offset().y((height / 2));
        Raylib.Vector2 difference = Raylib.Vector2Subtract(camera.target.getVector2(), camera.target());

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

    public void cameraController(float delta) {
        int cspd = 300;
        int cuspd = 300;

        if (IsKeyDown(controls.get("left"))) {
            target.x(target.x() - (cspd * delta));
        }
        if (IsKeyDown(controls.get("right"))) {
            target.x(target.x() + (cspd * delta));
        }
        if (IsKeyDown(controls.get("up"))) {
            target.y(target.y() - (cuspd * delta));
        }
        if (IsKeyDown(controls.get("down"))) {
            target.y(target.y() + (cuspd * delta));
        }
    }

    /**
     * Get the XY of the screen's corners
     * <table>
     *     <tr>
     *         <th>Corner</th>
     *         <th>Id</th>
     *     </tr>
     *     <tr>
     *         <td>Top left</td>
     *         <td>1</td>
     *     </tr>
     *     <tr>
     *         <td>Top right</td>
     *         <td>2</td>
     *     </tr>
     *     <tr>
     *         <td>Bottom left</td>
     *         <td>3</td>
     *     </tr>
     *     <tr>
     *         <td>Bottom right</td>
     *         <td>4</td>
     *     </tr>
     * </table>
     *
     * @param corner The id of the screen corner you want.
     * @param vector The vector in which the XY position of the corner will be set.
     */
    public void getCorner(int corner, Raylib.Vector2 vector) {

        //  X = (Game.camera.target.x -/+ ( Game.camera.offset.x / 3 ))
        //  Y = (Game.camera.target.x -/+ ( Game.camera.offset.y / 3 ))
        //
        // ((target().x() - (offset().x() / zoom())), (target().y() - (offset().y() / zoom())))

        switch (corner) {
            case 1:
                vector.x(this.target().x() - (this.offset().x() / zoom()));
                vector.y(this.target().y() - (this.offset().y() / zoom()));
                break;
            case 2:
                vector.x(this.target().x() + (this.offset().x() / zoom()));
                vector.y(this.target().y() - (this.offset().y() / zoom()));
                break;
            case 3:
                vector.x(this.target().x() - (this.offset().x() / zoom()));
                vector.y(this.target().y() + (this.offset().y() / zoom()));
                break;
            case 4:
                vector.x(this.target().x() + (this.offset().x() / zoom()));
                vector.y(this.target().y() + (this.offset().y() / zoom()));
                break;
            default:
                break;
        }
    }
}

