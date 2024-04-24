package game.textures;

import com.raylib.Raylib;
import game.assets.Id;
import game.assets.Tile;

import static com.raylib.Raylib.LoadTexture;
import static com.raylib.Raylib.UnloadTexture;

public class TextureManager {
    private Raylib.Texture sprites;
    private int spriteHeight;
    private int spriteWidth;
    private int spriteNumber;

    public TextureManager(String texture, int spriteHeight, int spriteWidth) {
        this.sprites = LoadTexture(texture);
        this.spriteHeight = spriteHeight;
        this.spriteWidth = spriteWidth;
    }

    public TextureManager(String texture, int spriteSize) {
        this.sprites = LoadTexture(texture);
        this.spriteHeight = spriteSize;
        this.spriteWidth = spriteSize;
    }

    public void unload() {
        UnloadTexture(sprites);
    }


    public static void getTexture(Id tileID){

    }

    public static void getTexture(Tile tile){
        return getTexture(tile.getId());
    }

    public static void getTexture(int Id, int subId){

    }
}
