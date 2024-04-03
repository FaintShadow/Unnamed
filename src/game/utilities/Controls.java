package game.utilities;

import java.util.HashMap;
import java.util.Map;

public abstract class Controls {
    public Map<String, Integer> keys = new HashMap<>();

    public void addControls(String name, int key) {
        this.keys.put(name, key);
    }

    @Override
    public String toString() {
        return "Game.utilities.Controls{" +
                "Game.utilities.Controls=" + keys +
                '}';
    }
}