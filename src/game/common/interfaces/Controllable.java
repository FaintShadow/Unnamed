package game.common.interfaces;

/**
 * Interface for objects that support user controls.
 *
 * @param <T> The type of the control name
 * @param <R> The type of the control key
 * @author FaintShadow
 */
public interface Controllable<T, R> {

    /**
     * Adds a control to the object.
     *
     * @param name The name of the control
     * @param key The key for the control
     */
    void addControl(T name, R key);

    /**
     * Used for setting the default controls for the object.
     */
    void defaultControls();
}
