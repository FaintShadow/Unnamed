package game.core.world.ecosystem.organisms;

import game.core.engine.position.Position;
import game.common.interfaces.Controllable;

import java.util.HashMap;
import java.util.Map;

public class Entity  implements Controllable<String,Integer> {
    private Map<String, Integer> controls = new HashMap<>();
    private Position position;

    // Entity Speed
    private float sPD = 200;

    // Entity Up Force (Jump)
    private float uF = 350;

    public Entity(Position position, float sPD, float uF) {
        this.position = position;
        this.sPD = sPD;
        this.uF = uF;
    }

    public Entity(Position position) {
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public float getsPD() {
        return sPD;
    }

    public void setsPD(float sPD) {
        this.sPD = sPD;
    }

    public float getuF() {
        return uF;
    }

    public void setuF(float uF) {
        this.uF = uF;
    }

    public Map<String, Integer> getControls() {
        return controls;
    }

    @Override
    public void addControls(String name, Integer key) {
        controls.put(name, key);
    }
}
