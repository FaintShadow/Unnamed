package game.core.engine.texture;

import com.raylib.Jaylib;
import com.raylib.Raylib;
import game.core.engine.common.Identifier;
import game.core.engine.terrain.Tile;

import java.util.HashMap;
import java.util.Map;

import static com.raylib.Raylib.*;

/**
 * Texture Manager for handling multiple sprite sheets.
 * @author FaintShadow
 */
public class Manager {
    // Texture storage with support for multiple sprite sheets
    private final Map<String, Raylib.Texture> textures = new HashMap<>();

    // Sprite sheet configuration
    private final Map<String, SpriteSheetConfig> spriteSheetConfigs = new HashMap<>();

    /**
     * Configuration for a sprite sheet.
     */
    public static class SpriteSheetConfig {
        private final int tileWidth;
        private final int tileHeight;
        private final int columns;
        private final int rows;

        /**
         * Create a sprite sheet configuration.
         *
         * @param tileWidth Width of a single tile/sprite
         * @param tileHeight Height of a single tile/sprite
         * @param columns Number of columns in the sprite sheet
         * @param rows Number of rows in the sprite sheet
         */
        public SpriteSheetConfig(int tileWidth, int tileHeight, int columns, int rows) {
            this.tileWidth = tileWidth;
            this.tileHeight = tileHeight;
            this.columns = columns;
            this.rows = rows;
        }

        public int getTileWidth() {
            return tileWidth;
        }

        public int getTileHeight() {
            return tileHeight;
        }

        public int getColumns() {
            return columns;
        }

        public int getRows() {
            return rows;
        }
    }

    /**
     * Load a texture from a file path.
     *
     * @param key Unique identifier for the texture
     * @param spritePath Path to the texture file
     * @return Loaded texture
     */
    public Raylib.Texture loadTexture(String key, String spritePath) {
        // Unload existing texture if it exists
        if (textures.containsKey(key)) {
            UnloadTexture(textures.get(key));
        }

        // Load and store the new texture
        Raylib.Texture texture = LoadTexture(spritePath);
        textures.put(key, texture);
        return texture;
    }

    /**
     * Register a sprite sheet configuration.
     *
     * @param key Unique identifier for the sprite sheet
     * @param config Sprite sheet configuration
     */
    public void registerSpriteSheet(String key, SpriteSheetConfig config) {
        spriteSheetConfigs.put(key, config);
    }

    /**
     * Get a texture rectangle for a specific tile in a sprite sheet.
     *
     * @param spriteSheetKey Key of the registered sprite sheet
     * @param x X coordinate of the tile (1-based indexing)
     * @param y Y coordinate of the tile (1-based indexing)
     * @return Rectangle representing the tile in the sprite sheet
     */
    public Jaylib.Rectangle getTextureRec(String spriteSheetKey, int x, int y) {
        SpriteSheetConfig config = spriteSheetConfigs.get(spriteSheetKey);
        if (config == null) {
            throw new IllegalArgumentException("No sprite sheet configuration found for key: " + spriteSheetKey);
        }

        // Adjust for 1-based indexing
        return new Jaylib.Rectangle(
                ((float) x - 1) * config.getTileWidth(),
                ((float) y - 1) * config.getTileHeight(),
                config.getTileWidth(),
                config.getTileHeight()
        );
    }

    /**
     * Get a texture rectangle for a tile using an Identifier.
     *
     * @param spriteSheetKey Key of the registered sprite sheet
     * @param identifier Identifier with x and y coordinates
     * @return Rectangle representing the tile in the sprite sheet
     */
    public Jaylib.Rectangle getTextureRec(String spriteSheetKey, Identifier<Integer, Integer> identifier) {
        return getTextureRec(
                spriteSheetKey,
                identifier.getParent().orElseThrow(() -> new RuntimeException("Parent is empty")),
                identifier.getChild().orElseThrow(() -> new RuntimeException("Child is empty"))
        );
    }

    /**
     * Get a texture rectangle for a specific tile.
     *
     * @param spriteSheetKey Key of the registered sprite sheet
     * @param tile Tile with an identifier
     * @return Rectangle representing the tile in the sprite sheet
     */
    public Jaylib.Rectangle getTextureRec(String spriteSheetKey, Tile tile) {
        return getTextureRec(
                spriteSheetKey,
                tile.getIdentifier().getParent().orElseThrow(() -> new RuntimeException("Parent is empty")),
                tile.getIdentifier().getChild().orElseThrow(() -> new RuntimeException("Child is empty"))
        );
    }

    /**
     * Get a loaded texture by its key.
     *
     * @param key Unique identifier for the texture
     * @return Loaded texture
     */
    public Raylib.Texture getTexture(String key) {
        Raylib.Texture texture = textures.get(key);
        if (texture == null) {
            throw new IllegalArgumentException("No texture found with key: " + key);
        }
        return texture;
    }

    /**
     * Unload a specific texture.
     *
     * @param key Unique identifier for the texture to unload
     */
    public void unloadTexture(String key) {
        Raylib.Texture texture = textures.remove(key);
        if (texture != null) {
            UnloadTexture(texture);
        }
    }

    /**
     * Unload all managed textures.
     */
    public void unloadAllTextures() {
        textures.values().forEach(Raylib::UnloadTexture);
        textures.clear();
        spriteSheetConfigs.clear();
    }

    /**
     * Check if a texture is loaded.
     * @param key Unique identifier for the texture
     * @return True if the texture is loaded
     */
    public boolean hasTexture(String key) {
        return textures.containsKey(key);
    }

    // Cleanup method to be called when the game is closing
    public void cleanup() {
        unloadAllTextures();
    }
}