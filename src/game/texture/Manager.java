package game.texture;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.utilities.Identifier;
import game.world.ecosystem.objects.Tile;

import static com.raylib.Raylib.LoadTexture;
import static com.raylib.Raylib.UnloadTexture;

/**
 * Texture manager, this class is used to manage a 2D texture sprite.
 */
public class Manager {
    private final Raylib.Texture sprite;
    private final int tileHeight;
    private final int tileWidth;

    /**
     * @param spritePath the path to the image file that contains the textures
     * @param tileSize the size of the tile in pixels, this is used if both the width and height have the save value
     */
    public Manager(String spritePath, int tileSize) {
        this.sprite = LoadTexture(spritePath);
        this.tileHeight = tileSize;
        this.tileWidth = tileSize;
    }

    /**
     * @param spritePath the path to the tile sheet image file
     * @param tileHeight the height of the tile in pixels
     * @param tileWidth the width of the tile in pixels
     */
    public Manager(String spritePath, int tileHeight, int tileWidth) {
        this.sprite = LoadTexture(spritePath);
        this.tileHeight = tileHeight;
        this.tileWidth = tileWidth;
    }

    public void unload() {
        UnloadTexture(sprite);
    }

    public Raylib.Texture getTexture() {
        return sprite;
    }

    public Jaylib.Rectangle getTextureRec(int x, int y){
        return new Jaylib.Rectangle((x - 1) * tileWidth, (y - 1) * tileHeight, tileWidth, tileHeight);
    }

    public Jaylib.Rectangle getTextureRec(Identifier<Integer, Integer> identifier){
        return getTextureRec(identifier.getParent(), identifier.getChild());
    }

    public Jaylib.Rectangle getTextureRec(Tile tile){
        return getTextureRec(tile.getId().getParent(), tile.getId().getChild());
    }
}
