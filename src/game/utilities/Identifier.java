package game.utilities;

import game.utilities.errors.InvalidIdentifierFormat;

/**
 * A class used to make an Identifier that has 2 parts
 * 'main' which is the first one and 'sub' which is the second one.
 * Example of an "Identified<Integer, Integer>(1,0,':')" will make
 * the following "1:0" where 1 is the main part, 0 is the sub part
 * and ':' a separator between them.
 * @author FaintShadow
 */
public class Identifier<T, R> {
    private T parent;
    private R child;
    private String separator = ":";

    public Identifier(T parent) throws InvalidIdentifierFormat {
        if (parent.toString().contains(separator)){
            throw new InvalidIdentifierFormat(parent.toString(), separator);
        }
        this.parent = parent;
    }

    public Identifier(T parent, R child) throws InvalidIdentifierFormat {
        new Identifier<T, R>(parent);
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

    @Override
    public String toString(){
        return parent + separator + child;
    }
}
