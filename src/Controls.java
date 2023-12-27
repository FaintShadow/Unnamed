import java.util.HashMap;
import java.util.Map;

public abstract class Controls {
    public Map<String, Integer> Controls = new HashMap<>();

    public void AddControls(String name, int key) {
        this.Controls.put(name, key);
    }

}