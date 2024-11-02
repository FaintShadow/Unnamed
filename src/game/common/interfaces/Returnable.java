package game.common.interfaces;

/**
 * Interface for objects that can produce copies of themselves and utilize method chaining.
 * Methods returning void are designed to return the implementing class instance (this) instead.
 * @param <T> The type of the implementing class
 */
interface Returnable<T> {
    default void noReturn(){}
    T copy();
}
