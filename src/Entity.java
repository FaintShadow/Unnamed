import com.raylib.Jaylib;

public class Entity extends Controls {
    public Jaylib.Vector2 position;
    public float speed;

    public Entity(Jaylib.Vector2 position, float speed) {
        this.position = position;
        this.speed = speed;
    }
}
