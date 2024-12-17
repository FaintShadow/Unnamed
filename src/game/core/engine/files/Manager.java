package game.core.engine.files;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Manages file paths for the game.
 * @author FaintShadow
 */
public class Manager {
    private static final String PROJECT_SOURCE_ROOT = System.getProperty("user.dir") + File.separator + "src";
    private static final String ASSETS_ROOT = PROJECT_SOURCE_ROOT + File.separator + "game" + File.separator + "core" + File.separator + "texture" + File.separator + "assets";

    /**
     * Retrieves the absolute path for a file under the src directory.
     *
     * @param relativePath Path relative to the src directory
     * @return Absolute path to the file
     */
    public static String getPath(String relativePath) {
        String normalizedPath = relativePath.replace("/", File.separator).replace("\\", File.separator);
        Path fullPath = Paths.get(PROJECT_SOURCE_ROOT, normalizedPath);
        return fullPath.toString();
    }

    /**
     * Retrieves the absolute path for a texture asset.
     *
     * @param assetName Name of the asset file (e.g., "tiles.png")
     * @return Absolute path to the texture asset
     */
    public static String textureAssets(String assetName) {
        return getPath("game/core/texture/assets/" + assetName);
    }

    /**
     * Lists all files in the assets directory.
     *
     * @return Array of File objects representing texture assets
     */
    public static File[] listTextureAssets() {
        File assetsDir = new File(ASSETS_ROOT);
        return assetsDir.listFiles();
    }

    /**
     * Checks if a given file exists.
     *
     * @param path Path to the file
     * @return true if file exists, false otherwise
     */
    public static boolean fileExists(String path) {
        return new File(path).exists();
    }
}