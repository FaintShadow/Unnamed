package game.entities;

import com.raylib.Jaylib;
import game.assets.Tile;
import game.utilities.Controls;

import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static game.Unnamed.CHUNKSIZE;
import static game.Unnamed.TILESCALEDSIZE;

public class Entity extends Controls {
    public Jaylib.Vector2 position;
    public float speed;

    public Entity(Jaylib.Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    public void updatePosition(float delta){
        // Current Speed:
        int cspd = 100;
        // Current Up Speed:
        int cuspd = 100;

        if (IsKeyDown(keys.get("left"))) {
            position.x(position.x() - (cspd * delta));
        }
        if (IsKeyDown(keys.get("right"))) {
            position.x(position.x() + (cspd * delta));
        }
        if (IsKeyDown(keys.get("up"))) {
            position.y(position.y() - (cuspd * delta));
        }
        if (IsKeyDown(keys.get("down"))){
            position.y(position.y() + (cuspd * delta));
        }
    }

    public List<Tile> getEntityCurrentChunk(Map<String, List<Tile>> map) {
        int xCD = 0;
        int yCD = 0;
        int targetX = 0;
        int targetY = 0;
        if (position.x() < 0) {
            targetX = (int) ((position.x() - (CHUNKSIZE * TILESCALEDSIZE)) / (CHUNKSIZE * TILESCALEDSIZE));
            targetY = (int) (position.y() / (CHUNKSIZE * TILESCALEDSIZE));
        } else {
            targetX = (int) (position.x() / (CHUNKSIZE * TILESCALEDSIZE));
            targetY = (int) (position.y() / (CHUNKSIZE * TILESCALEDSIZE));
        }

        xCD = targetX * (CHUNKSIZE * TILESCALEDSIZE);
        yCD = targetY * (CHUNKSIZE * TILESCALEDSIZE);
        String chunkIndex = xCD + ";" + yCD;

        return map.get(chunkIndex);
    }

    @Override
    public String toString() {
        return "Entities.Entity{" +
                "X=" + position.x() +
                ", Y=" + position.y() +
                ", Speed=" + speed +
                ", Controls=" + keys +
                "}";
    }
}
