package game.utilities.errors;

public class IllegalPositioningSystemArgument extends IllegalArgumentException {
    public IllegalPositioningSystemArgument(String system) {
        super("Invalid positioning system: '" + system + "'\nThe system must be one of the following: 'world', 'chunk' or 'screen'");
    }
}
