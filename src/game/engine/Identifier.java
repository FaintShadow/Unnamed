package game.engine;

import game.engine.concerns.InvalidIdentifierFormat;

/**
 * The Identifier class creates a compound identifier consisting of two parts: 'Parent' and 'Child'.
 * These parts are separated by a specified separator. For instance, an "Identifier<Integer, Integer>(1,0,':')"
 * will yield the string "1:0", where "1" is the 'Parent', "0" is the 'Child', and ":" is the separator.
 * @author FaintShadow
 * @param <T> The type of the Parent
 * @param <R> The type of the Child
 */
public class Identifier<T, R> {
    private T parent;
    private R child;
    private String separator = ":";

    public Identifier() {}

    public Identifier(T parent) throws InvalidIdentifierFormat {
        if (parent.toString().contains(separator)){
            throw new InvalidIdentifierFormat(parent.toString(), separator);
        }
        this.parent = parent;
    }

    public Identifier(T parent, R child) throws InvalidIdentifierFormat {
        new Identifier<T, R>(parent);
        // TODO: change the if statement later
        if (child.toString().contains(separator)){
            throw new InvalidIdentifierFormat(child.toString(), separator);
        }
        this.child = child;
    }

    public Identifier(T parent, String separator) throws InvalidIdentifierFormat {
        new Identifier<T, R>(parent);
        this.separator = separator;
    }

    public Identifier(T parent, R child, String separator) throws InvalidIdentifierFormat {
        new Identifier<T, R>(parent, child);
        this.separator = separator;
    }

    public void set(T parent, R child) throws InvalidIdentifierFormat {
        if (parent.toString().contains(separator)){
            throw new InvalidIdentifierFormat(parent.toString(), separator);
        }
        if (child.toString().contains(separator)){
            throw new InvalidIdentifierFormat(child.toString(), separator);
        }
        this.parent = parent;
        this.child = child;

    }

    public T getParent() {
        return parent;
    }

    public void setParent(T parent) {
        this.parent = parent;
    }

    public R getChild() {
        return child;
    }

    public void setChild(R child) {
        this.child = child;
    }

    // Overrides:
    @Override
    public String toString(){
        return parent + separator + child;
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof Identifier && obj.toString().equals(toString()));
    }
}
