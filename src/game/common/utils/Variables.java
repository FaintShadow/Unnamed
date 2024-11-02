package game.common.utils;

import game.core.engine.texture.Manager;

public class Variables {
    public static final String GAME_TITLE = "Unnamed";
    public static final String W_RESET = "reset";
    public static final String W_PAUSE = "pause";

    public static final String W_UP = "up";
    public static final String W_DOWN = "down";
    public static final String W_RIGHT = "right";
    public static final String W_LEFT = "left";

    public static final String W_TOP = "top";
    public static final String W_BOTTOM = "bottom";

    public static final String W_JUMP = "jump";
    public static final String W_FALLING = "falling";
    public static final String W_JUMPING = "jumping";
    public static final String W_GROUNDED = "grounded";
    public static final String W_IDLING = "idling";

    public static final String W_TILE = "Tile";
    public static final String W_CHUNK = "Chunk";
    public static final String W_WORLD = "World";
    public static final String W_SCREEN = "Screen";
    protected static final String[] POSITIONSYSTEMS = new String[]{
            W_TILE,
            W_CHUNK,
            W_WORLD,
            W_SCREEN
    };

    public static final int GAMEWIDTH = 800 * 2;
    public static final int GAMEHEIGHT = 450 * 2;

    public static final int CHUNKSIZE = 8;
    public static final int DEBUGFONTSIZE = 16;

    public static final Manager TileTextureManager = new Manager("../engine/textures/assets/tiles.png", 16);

    private Variables(){}
    public static String[] getPositionSystems(){
        return POSITIONSYSTEMS;
    }
}
