package game.assets;

import com.raylib.Jaylib;
import com.raylib.Jaylib.Rectangle;
import com.raylib.Raylib;
import com.raylib.Raylib.Texture;

public class Tile {
    public Rectangle rectangle;
    public Jaylib.Vector2 position;
    public boolean aboveCol;
    public boolean sideCol;
    public boolean belowCol;
    public Raylib.Color color;
    public Texture texture;
    public Raylib.Color debug;

    public Tile(Rectangle rect, boolean aboveCol, boolean sideCol, boolean belowCol, Raylib.Color color, Texture texture) {
        this.rectangle = rect;
        this.aboveCol = aboveCol;
        this.sideCol = sideCol;
        this.belowCol = belowCol;
        this.color = color;
        this.debug = null;
        this.texture = texture;
        this.position = new Jaylib.Vector2(rect.x(), rect.y());
    }

    public Tile(){}
}
