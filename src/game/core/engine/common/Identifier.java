package game.core.engine.common;

import game.common.utils.Utils;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.logging.Level;

/**
 * A compound identifier consisting of two parts: 'Parent' and 'Child'.
 * These parts are separated by a specified separator.
 * <br>
 * Example: new Identifier(1, 0, ":") creates "1:0"
 * @param <P> The type of the Parent
 * @param <C> The type of the Child
 * @author FaintShadow
 */
public class Identifier<P, C> {
    private static final String DEFAULT_SEPARATOR = ":";

    private P parent;
    private C child;
    private String separator;

    private Identifier() {
        this.separator = DEFAULT_SEPARATOR;
    }

    /**
     * Builder class for creating an Identifier instance.
     * @param <P> The type of the Parent
     * @param <C> The type of the Child
     */
    public static class Builder<P, C> {
        private final Identifier<P, C> instance = new Identifier<>();

        public Builder<P, C> parent(P parent) {
            instance.parent = Objects.requireNonNull(parent, "Parent cannot be null");
            return this;
        }

        public Builder<P, C> child(C child) {
            instance.child = child;
            return this;
        }

        public Builder<P, C> separator(String separator) {
            instance.separator = Optional.ofNullable(separator)
                    .filter(s -> !s.isEmpty())
                    .orElse(DEFAULT_SEPARATOR);
            return this;
        }

        public Identifier<P, C> build() {
            validateParts();
            return instance;
        }

        private void validateParts() {
            if (containsSeparator(instance.parent)) {
                Utils.Logger.warning("Parent contains separator: potential formatting issue");
            }
            if (instance.child != null && containsSeparator(instance.child)) {
                Utils.Logger.warning("Child contains separator: potential formatting issue");
            }
        }

        private boolean containsSeparator(Object value) {
            return value.toString().contains(instance.separator);
        }
    }

    /**
     * Used to create a new Identifier instance.
     * @return Builder instance
     * @param <P> The type of the Parent
     * @param <C> The type of the Child
     */
    public static <P, C> Builder<P, C> create() {
        return new Builder<>();
    }

    // Getters
    public Optional<P> getParent() {
        return Optional.ofNullable(parent);
    }

    public Optional<C> getChild() {
        return Optional.ofNullable(child);
    }

    public String getSeparator() {
        return separator;
    }

    @Override
    public String toString() {
        return child == null
                ? parent.toString()
                : parent + separator + child;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Identifier<?, ?>)) return false;
        return Objects.equals(this.toString(), obj.toString());
    }

    @Override
    public int hashCode() {
        return Objects.hash(parent, child, separator);
    }

    /**
     * Parses a string into an Identifier instance.
     * @param identifier The string to parse
     * @param separator The separator used to split the string
     * @param parentParser Function to parse the parent part
     * @param childParser Function to parse the child part
     * @param <P> The type of the Parent
     * @param <C> The type of the Child
     * @return Optional containing the Identifier instance if parsing was successful
     */
    public static <P, C> Optional<Identifier<P, C>> parse(
            String identifier,
            String separator,
            Function<String, P> parentParser,
            Function<String, C> childParser
    ) {
        try {
            String[] parts = identifier.split(Pattern.quote(separator));
            if (parts.length != 2) {
                Utils.Logger.warning("Invalid identifier format: " + identifier);
                return Optional.empty();
            }

            return Optional.of(
                    Identifier.<P, C>create()
                            .parent(parentParser.apply(parts[0]))
                            .child(childParser.apply(parts[1]))
                            .separator(separator)
                            .build()
            );
        } catch (Exception e) {
            Utils.Logger.log(Level.WARNING, "Error parsing identifier");
            return Optional.empty();
        }
    }
}