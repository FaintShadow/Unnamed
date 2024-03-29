package utilities;

import java.util.HashMap;
import java.util.Map;

public abstract class Controls {
    public Map<String, Integer> keys = new HashMap<>();

    public void addControls(String name, int key) {
        this.keys.put(name, key);
    }

    @Override
    public String toString() {
        return "utilities.Controls{" +
                "utilities.Controls=" + keys +
                '}';
    }
}