import com.raylib.Jaylib;
import com.raylib.Raylib;

public class RectangleColored extends Jaylib.Rectangle {
    private Raylib.Color color;
    public RectangleColored(float v, float v1, float v2, float v3, Raylib.Color color) {
        super(v, v1, v2, v3);
        this.color = color;
    }

    @Override
    public String toString() {
        return "RectangleColored{" +
                "rectangle=[" + x() + "," + y() + "," + height() + "," + width() + "]" +
                ", color=" + color +
                '}';
    }

    public RectangleColored(Jaylib.Rectangle rectangle, Raylib.Color color) {
        super(rectangle);
        this.color = color;
    }

    public Raylib.Color getColor(){
        return this.color;
    }

    public void setColor(Raylib.Color color) {
        this.color = color;
    }
}
