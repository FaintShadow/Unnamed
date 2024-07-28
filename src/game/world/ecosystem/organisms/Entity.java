package game.world.ecosystem.organisms;

import com.raylib.Jaylib;
import game.utilities.Position;
import game.utilities.interfaces.Controllable;
import game.world.ecosystem.objects.Tile;

import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.IsKeyDown;
import static game.utilities.Variables.CHUNKSIZE;
import static game.world.type.BaseWorld.TILESCALEDSIZE;

public class Entity implements Controllable {
    private Map<String, Integer> controls = new HashMap<>();
    private Position position;
    private float speed;

    public Entity(Position position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    public void updatePosition(float delta){
        // Current Speed:
        int cspd = 100;
        // Current Up Speed:
        int cuspd = 100;

        if (IsKeyDown(controls.get("left"))) {
            position.x(position.x() - (cspd * delta));
        }
        if (IsKeyDown(controls.get("right"))) {
            position.x(position.x() + (cspd * delta));
        }
        if (IsKeyDown(controls.get("up"))) {
            position.y(position.y() - (cuspd * delta));
        }
        if (IsKeyDown(controls.get("down"))){
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
        String chunkIndex = xCD + ":" + yCD;

        return map.get(chunkIndex);
    }

    public Map<String, Integer> getControls() {
        return controls;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
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
                "X=" + position.x() +
                ", Y=" + position.y() +
                ", Speed=" + speed +
                ", Controls=" + controls +
                "}";
    }
}
