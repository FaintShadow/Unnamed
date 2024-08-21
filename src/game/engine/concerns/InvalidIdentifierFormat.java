package game.engine.concerns;

public class InvalidIdentifierFormat extends Exception{
    public InvalidIdentifierFormat(String part, String separator) {
        super("Invalid identifier format:" + part + "\nThe identifier must not contain '" + separator + "'");
    }
}
