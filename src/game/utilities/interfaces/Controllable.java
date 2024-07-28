package game.utilities.interfaces;

import java.util.Map;

public interface Controllable {
    default void addControls(Map<String, Integer> controls, String name, int key) {
        controls.put(name, key);
    }
}
