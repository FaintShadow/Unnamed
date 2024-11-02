package game.exceptions;

import java.lang.reflect.Method;

public class IllegalMethodUsage extends Exception{
    public IllegalMethodUsage(Method method, String message) {
        super("'" + method.toGenericString() + "' is used for " + message);
    }

    public IllegalMethodUsage(String message) {
        super(message);
    }
}
