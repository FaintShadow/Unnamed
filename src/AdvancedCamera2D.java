import com.raylib.Jaylib;
import com.raylib.Raylib;
import java.util.ArrayList;
import java.util.Iterator;

public class AdvancedCamera2D extends Raylib.Camera2D{
    public Jaylib.Vector2 targetV = new Jaylib.Vector2();

    public AdvancedCamera2D() {}

    public void setTargetVector(Jaylib.Vector2 v) {
        this.targetV = v;
    }

    public Jaylib.Vector2 getTargetVector() {
        return this.targetV;
    }

    public void CTV(ArrayList<Entity> EL) {
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

        float avgX = totX / (float)i;
        float avgY = totY / (float)i;
        this.targetV.x(avgX);
        this.targetV.y(avgY);
    }

    public void SUCC(int width, int height, Entity entity) {
        super.offset(new Jaylib.Vector2((float)width / 2.0F, (float)height / 2.0F));
        super.target(entity.position);
    }

    public static void SUCCSF(AdvancedCamera2D camera, Entity entity, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;
        camera.offset(new Jaylib.Vector2((float)(width / 2), (float)(height / 2)));
        Raylib.Vector2 Difference = Raylib.Vector2Subtract(entity.position, camera.target());
        float length = Raylib.Vector2Length(Difference);
        if (length > min_eff_length) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(Difference, speed * delta / length)));
        }

    }

    public static void MUCCSF(AdvancedCamera2D camera, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;
        camera.offset().x((float)(width / 2));
        camera.offset().y((float)(height / 2));
        Raylib.Vector2 Difference = Raylib.Vector2Subtract(camera.targetV, camera.target());
        float length = Raylib.Vector2Length(Difference);
        if (length > min_eff_length) {
            float speed = Math.max(fractionSPD * length, minSPD);
            camera.target(Raylib.Vector2Add(camera.target(), Raylib.Vector2Scale(Difference, speed * delta / length)));
        }

    }

    public static void MUCCIM(AdvancedCamera2D camera, ArrayList<Tile> tiles, int width, int height, float delta) {
        float minSPD = 30.0F;
        float min_eff_length = 10.0F;
        float fractionSPD = 0.8F;

        camera.offset().x((float)(width / 2));
        camera.offset().y((float)(height / 2));
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
        if (wmax.x() < (float)width) {
            camera.offset().x((float)width - (wmax.x() - (float)(width / 2)));
        }

        if (wmax.y() < (float)height) {
            camera.offset().y((float)height - (wmax.y() - (float)(height / 2)));
        }

        if (wmin.x() > 0.0F) {
            camera.offset().x((float)(width / 2) - wmin.x());
        }

        if (wmin.y() > 0.0F) {
            camera.offset().y((float)(height / 2) - wmin.y());
        }

    }
}
