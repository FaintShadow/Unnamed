package game.core.engine.common;

import game.exceptions.InvalidIdentifierFormat;

import java.util.Objects;
import java.util.function.Function;
import java.util.regex.Pattern;

/**
 * A compound identifier consisting of two parts: 'Parent' and 'Child'.
 * These parts are separated by a specified separator.
 * <br>
 * Example: new Identifier<Integer, Integer>(1, 0, ":") creates "1:0"
 * <hr>
 * @param <P> The type of the Parent
 * @param <C> The type of the Child
 * @author FaintShadow
 */
public class Identifier<P, C> {
    private P parent;
    private C child;
    private final String separator;  // Made final since it shouldn't change after construction

    // Default separator as a constant
    private static final String DEFAULT_SEPARATOR = ":";

    public Identifier() {
        this.separator = DEFAULT_SEPARATOR;
    }

    public Identifier(P parent) throws InvalidIdentifierFormat {
        this(parent, null, DEFAULT_SEPARATOR);
    }

    public Identifier(P parent, C child) throws InvalidIdentifierFormat {
        this(parent, child, DEFAULT_SEPARATOR);
    }

    /**
     * Main constructor that all other constructors delegate to
     */
    public Identifier(P parent, C child, String separator) throws InvalidIdentifierFormat {
        validateNotNull(parent, "Parent");
        validateSeparator(separator);

        this.separator = separator;
        setParent(parent);  // Using setters for validation
        if (child != null) {
            setChild(child);
        }
    }

    /**
     * Sets both parent and child values atomically
     * @throws InvalidIdentifierFormat if either value contains the separator
     * @throws IllegalArgumentException if parent is null
     */
    public void set(P parent, C child) throws InvalidIdentifierFormat {
        validateNotNull(parent, "Parent");
        validateNotContainsSeparator(parent);
        if (child != null) {
            validateNotContainsSeparator(child);
        }

        this.parent = parent;
        this.child = child;
    }

    public P getParent() {
        return parent;
    }

    public void setParent(P parent) throws InvalidIdentifierFormat {
        validateNotNull(parent, "Parent");
        validateNotContainsSeparator(parent);
        this.parent = parent;
    }

    public C getChild() {
        return child;
    }

    public void setChild(C child) throws InvalidIdentifierFormat {
        if (child != null) {
            validateNotContainsSeparator(child);
        }
        this.child = child;
    }

    public String getSeparator() {
        return separator;
    }

    // Private validation methods
    private void validateNotContainsSeparator(Object value) throws InvalidIdentifierFormat {
        if (value.toString().contains(separator)) {
            throw new InvalidIdentifierFormat(value.toString(), separator);
        }
    }

    private void validateSeparator(String separator) {
        if (separator == null || separator.isEmpty()) {
            throw new IllegalArgumentException("Separator cannot be null or empty");
        }
    }

    private void validateNotNull(Object value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be null");
        }
    }

    @Override
    public String toString() {
        if (child == null) {
            return parent.toString();
        }
        return parent + separator + child;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Identifier<?, ?>)) return false;

        Identifier<?, ?> other = (Identifier<?, ?>) obj;
        return Objects.equals(this.toString(), other.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child, separator);
    }

    /**
     * Creates a new Identifier by parsing a string with the given separator
     * @throws InvalidIdentifierFormat if the string format is invalid
     */
    public static <P, C> Identifier<P, C> parse(String identifier, String separator, Function<String, P> parentParser, Function<String, C> childParser) throws InvalidIdentifierFormat {
        String[] parts = identifier.split(Pattern.quote(separator));
        if (parts.length != 2) {
            throw new InvalidIdentifierFormat(identifier, separator);
        }
        return new Identifier<>(parentParser.apply(parts[0]), childParser.apply(parts[1]), separator);
    }
}
