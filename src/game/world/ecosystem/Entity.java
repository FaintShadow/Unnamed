package game.world.ecosystem;

import com.raylib.Jaylib;
import game.world.ecosystem.objects.Tile;
import game.utilities.Controls;

import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static game.Unnamed.CHUNKSIZE;
import static game.Unnamed.TILESCALEDSIZE;
import static game.world.World.TILESCALEDSIZE;

public class Entity extends Controls {
    private Jaylib.Vector2 posiion;
    private float speed;

    public Entity(Jaylib.Vector2 position, float speed) {
        this.posiion = position;
        this.speed = speed;
    }

    public void updatePosition(float delta){
        // Current Speed:
        int cspd = 100;
        // Current Up Speed:
        int cuspd = 100;

        if (IsKeyDown(keys.get("left"))) {
            posiion.x(posiion.x() - (cspd * delta));
        }
        if (IsKeyDown(keys.get("right"))) {
            posiion.x(posiion.x() + (cspd * delta));
        }
        if (IsKeyDown(keys.get("up"))) {
            posiion.y(posiion.y() - (cuspd * delta));
        }
        if (IsKeyDown(keys.get("down"))){
            posiion.y(posiion.y() + (cuspd * delta));
        }
    }

    public List<Tile> getEntityCurrentChunk(Map<String, List<Tile>> map) {
        int xCD = 0;
        int yCD = 0;
        int targetX = 0;
        int targetY = 0;
        if (posiion.x() < 0) {
            targetX = (int) ((posiion.x() - (CHUNKSIZE * TILESCALEDSIZE)) / (CHUNKSIZE * TILESCALEDSIZE));
            targetY = (int) (posiion.y() / (CHUNKSIZE * TILESCALEDSIZE));
        } else {
            targetX = (int) (posiion.x() / (CHUNKSIZE * TILESCALEDSIZE));
            targetY = (int) (posiion.y() / (CHUNKSIZE * TILESCALEDSIZE));
        }

        xCD = targetX * (CHUNKSIZE * TILESCALEDSIZE);
        yCD = targetY * (CHUNKSIZE * TILESCALEDSIZE);
        String chunkIndex = xCD + ":" + yCD;

        return map.get(chunkIndex);
    }

    public Jaylib.Vector2 getPosition() {
        return posiion;
    }

    public void setPosition(Jaylib.Vector2 position) {
        this.posiion = position;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return "Entities.Entity{" +
                "X=" + posiion.x() +
                ", Y=" + posiion.y() +
                ", Speed=" + speed +
                ", Controls=" + keys +
                "}";
    }
}
