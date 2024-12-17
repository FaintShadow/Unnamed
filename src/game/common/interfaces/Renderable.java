package game.common.interfaces;

import game.core.engine.camera.AdvCamera2D;
import game.exceptions.RenderingException;

/**
 * Interface for objects that can be rendered.
 * @author FaintShadow
 */
public interface Renderable {
    /**
     * Render the object.
     * @param advCamera2D The camera to render the object with.
     * @throws RenderingException If an error occurs while rendering.
     */
    void render(AdvCamera2D advCamera2D) throws RenderingException;

    /**
     * Render the object for debugging purposes <i>(usually in debug mode)</i>.
     * @throws RenderingException If an error occurs while rendering.
     */
    default void debugRender() throws RenderingException{};
}
