package game.common.interfaces;

/**
 * When implemented, The class's method that return 'void' and are not static should return their own class.
 * For an example, you can check out the class Position
 * @param <T> Any Class
 */
public interface Returnable<T> {
    void noReturn();
    T copy();
}
