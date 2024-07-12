package game.texture;

import com.raylib.Raylib;
import game.utilities.Identifier;
import game.world.ecosystem.objects.Tile;

import static com.raylib.Raylib.LoadTexture;
import static com.raylib.Raylib.UnloadTexture;

/**
 * The Following class is the texture manager, It is used to manage sprite textures
 */
public class Manager {
    private Raylib.Texture sprite;
    private int spriteHeight;
    private int spriteWidth;

    public Manager(String texture) {
        this.sprite = LoadTexture(texture);
        this.spriteHeight = sprite.height();
        this.spriteWidth = sprite.width();
    }

    public void unload() {
        UnloadTexture(sprite);
    }


    public static void getTexture(Identifier tileIdentifier){

    }

    public static void getTexture(Tile tile){
        return getTexture(tile.getId());
    }

    public static void getTexture(int Id, int subId){

    }
}
