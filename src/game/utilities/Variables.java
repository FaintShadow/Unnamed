package game.utilities;

import game.texture.Manager;

public class Variables {
    public static final String W_RESET = "Reset";
    public static final String W_PAUSE = "Pause";

    public static final String W_UP = "Up";
    public static final String W_DOWN = "Down";
    public static final String W_RIGHT = "Right";
    public static final String W_LEFT = "Left";

    public static final String W_TOP = "Top";
    public static final String W_BOTTOM = "Bottom";

    public static final String W_JUMP = "Jump";

    public static final String W_TILE = "Tile";
    public static final String W_CHUNK = "Chunk";
    public static final String W_WORLD = "World";
    public static final String W_SCREEN = "Screen";

    public static final int GAMEWIDTH = 800 * 2;
    public static final int GAMEHEIGHT = 450 * 2;

    public static final int CHUNKSIZE = 8;
    public static final int DEBUGFONTSIZE = 16;
    protected static final String[] POSITIONSYSTEMS = new String[]{
            W_TILE,
            W_CHUNK,
            W_WORLD,
            W_SCREEN
    };

    public static final Manager TileTextureManager = new Manager("textures/assets/tiles.png", 16);

    private Variables(){}
}
