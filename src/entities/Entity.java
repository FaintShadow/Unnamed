package entities;

import com.raylib.Jaylib;
import utilities.Controls;

import static com.raylib.Raylib.IsKeyDown;

public class Entity extends Controls {
    public Jaylib.Vector2 position;
    public float speed;

    public Entity(Jaylib.Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
    }

    public void updatePosition(float delta){
        int cspd = 100;
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
