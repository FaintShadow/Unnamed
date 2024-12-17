package game.core.world.ecosystem.organisms;

import game.core.engine.position.Position;
import game.common.interfaces.Controllable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.raylib.Raylib.*;
import static com.raylib.Raylib.KEY_DOWN;
import static game.common.utils.Variables.*;
import static game.common.utils.Variables.W_DOWN;

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
    public void addControl(String name, Integer key) {
        controls.put(name, key);
    }

    @Override
    public void defaultControls() {
        List<String> keys = new ArrayList<>();
        keys.add(W_LEFT);
        keys.add(W_RIGHT);
        keys.add(W_UP);
        keys.add(W_DOWN);

        List<Integer> maps = new ArrayList<>();
        maps.add(KEY_A);
        maps.add(KEY_D);
        maps.add(KEY_W);
        maps.add(KEY_S);

        for(String key : keys) {
            controls.computeIfAbsent(key, k -> maps.get(keys.indexOf(key)));
        }
    }
}
