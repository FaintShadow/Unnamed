package game.exceptions;

import java.lang.reflect.Method;

public class IllegalMethodUsage extends Exception{
    public IllegalMethodUsage(Method method, String message) {
        super("'" + method.toGenericString() + "' is used for " + message);
        // TODO: Upgrade to JVM 22+

        /* String genericMethod = method.toGenericString();
        while (genericMethod.lastIndexOf(" ") > 0 || genericMethod.indexOf(" ") > 0){
            genericMethod = genericMethod.substring(0, genericMethod.lastIndexOf(" "));
            genericMethod = genericMethod.substring(genericMethod.indexOf(" ") + 1);
        } */
    }
}
