package game.common.interfaces;


public interface Controllable<T, R> {
    void addControls(T name, R key);
}
