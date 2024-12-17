package game.common.interfaces;

/**
 * Interface for objects that support method chaining and deep copying.
 *
 * @param <T> The type of the implementing class
 * @author FaintShadow
 */
public interface Chainable<T extends Chainable<T>> extends Cloneable {
    /**
     * Creates a deep copy of the current object.
     * Implementing classes should override this to provide a true deep copy.
     *
     * @return A new instance with the same state as the current object
     */
    T copy();

    /**
     * Creates a shallow copy of the current object.
     * Implementing classes should override this to provide a true shallow copy.
     *
     * @return A new instance with the same state as the current object
     * @see Cloneable
     */
    T clone() throws CloneNotSupportedException;

    /**
     * Placeholder method for method chaining when no return is needed.
     * Allows for consistent method chaining even for void-like operations.
     */
    default void noReturn() {}

    /**
     * Provides a convenient way to create a deep copy with potential modifications.
     *
     * @param modifier A function to modify the copied object
     * @return A modified copy of the current object
     */
    default T copyWith(java.util.function.Function<T, T> modifier) {
        T copiedObject = copy();
        return modifier.apply(copiedObject);
    }
}