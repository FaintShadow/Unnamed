package camera;

import assets.Tile;
import entities.Entity;
import com.raylib.Jaylib;
import com.raylib.Raylib;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;

public class AdvancedCamera2D extends Raylib.Camera2D{

    public Map<String, Integer> keys = new HashMap<>();

    public Jaylib.Vector2 targetV = new Jaylib.Vector2();

    public AdvancedCamera2D() {
        // just create a camera, and then you can add what you want using the provided methods.
    }

    public void AddControls(String name, int key) {
        this.keys.put(name, key);
    }

    public void setTargetVector(Jaylib.Vector2 v) {
        this.targetV = v;
    }

    public Jaylib.Vector2 getTargetVector() {
        return this.targetV;
    }

    // Camera Target Vector:
    public void CTV(List<?extends Entity> EL) {
        float totX = 0.0F;
        float totY = 0.0F;
        float ignoredistance = 400.0F;
        int i = 0;

        for (Entity E : EL) {
            if (Raylib.Vector2Distance(E.position, this.targetV) < ignoredistance) {
                totX += E.position.x();
                totY += E.position.y();
                ++i;
            }
        }
        if (i == 0){
            totX = EL.get(0).position.x();
            totY = EL.get(0).position.y();
            i++;
        }

        float avgX = totX / i;
        float avgY = totY / i;
        this.targetV.x(avgX);
        this.targetV.y(avgY);
    }

    // Single U? Camera Center:
    public void SUCC(int width, int height, Entity entity) {
        super.offset(new Jaylib.Vector2(width / 2.0F, height / 2.0F));
        super.target(entity.position);
    }

    // Single U? Camera Center:
    public void MUCC(int width, int height) {
        super.offset(new Jaylib.Vector2(width / 2.0F, height / 2.0F));
        super.target(targetV);
    }

    // Single U? Camera Center Smooth Follow
    public static void SUCCSF(AdvancedCamera2D camera, Entity entity, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;
        camera.offset(new Jaylib.Vector2((width / 2), (height / 2)));
        Raylib.Vector2 Difference = Raylib.Vector2Subtract(entity.position, camera.target());
        float length = Raylib.Vector2Length(Difference);
        if (length > min_eff_length) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(Difference, speed * delta / length)));
        }

    }

    // Multi U? Camera Center Smooth Follow:
    public static void MUCCSF(AdvancedCamera2D camera, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((width / 2));
        camera.offset().y((height / 2));
        Raylib.Vector2 Difference = Raylib.Vector2Subtract(camera.targetV, camera.target());

        float length = Raylib.Vector2Length(Difference);
        if (length > min_eff_length) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(Difference, speed * delta / length)));
        }

    }

    // Multi Camera Center
    public static void MUCCIM(AdvancedCamera2D camera, List<Tile> tiles, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((width / 2));
        camera.offset().y((height / 2));
        Raylib.Vector2 Difference = Raylib.Vector2Subtract(camera.targetV, camera.target());

        float length = Raylib.Vector2Length(Difference);
        float minX;
        if (length > min_eff_length) {
            minX = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(Difference, minX * delta / length)));
        }

        minX = 0.0F;
        float minY = 0.0F;
        float maxX = 0.0F;
        float maxY = 0.0F;

        for (Tile tile : tiles) {
            minX = Math.min(tile.rectangle.x(), minX);
            maxX = Math.max(tile.rectangle.x(), maxX);
            minY = Math.min(tile.rectangle.y(), minY);
            maxY = Math.max(tile.rectangle.y(), maxY);
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

    public void cameraController(float delta){
        int CSPD = 300;
        int CUSPD = 300;

        if (IsKeyDown(keys.get("left"))) {
            targetV.x(targetV.x() - (CSPD * delta));
        }
        if (IsKeyDown(keys.get("right"))) {
            targetV.x(targetV.x() + (CSPD * delta));
        }
        if (IsKeyDown(keys.get("up"))) {
            targetV.y(targetV.y() - (CUSPD * delta));
        }
        if (IsKeyDown(keys.get("down"))){
            targetV.y(targetV.y() + (CUSPD * delta));
        }
    }

    public void getCorner(int id, Raylib.Vector2 vector){

        //
        //  Top Left Corner : 1
        //  Top Right Corner : 2
        //  Bottom Left Corner : 3
        //  Bottom Right Corner : 4
        //
        //  Get XY Screen Corners Based On Zoom level:
        //  X = (camera.target.x -/+ ( camera.offset.x / 3 ))
        //  Y = (camera.target.x -/+ ( camera.offset.y / 3 ))
        //
        // ((target().x() - (offset().x() / zoom())), (target().y() - (offset().y() / zoom())))

        switch (id){
            case 1:
                vector.x(target().x() - (offset().x() / zoom()));
                vector.y(target().y() - (offset().y() / zoom()));
                break;
            case 2:
                vector.x(target().x() + (offset().x() / zoom()));
                vector.y(target().y() - (offset().y() / zoom()));
                break;
            case 3:
                vector.x(target().x() - (offset().x() / zoom()));
                vector.y(target().y() + (offset().y() / zoom()));
                break;
            case 4:
                vector.x(target().x() + (offset().x() / zoom()));
                vector.y(target().y() + (offset().y() / zoom()));
                break;
            default:
                break;
        }
    }
}

