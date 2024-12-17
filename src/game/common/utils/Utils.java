package game.common.utils;

import com.raylib.Jaylib;

import java.util.logging.Level;

import static game.common.utils.Variables.GAME_TITLE;

/**
 * Utility class providing various helper methods
 *
 * @author FaintShadow
 */
public class Utils {
    private Utils() {}

    /**
     * Custom logger class for logging messages with automatic caller class name detection.
     */
    public static class Logger {
        private Logger() {
        }

        private static final java.util.logging.Logger writer = java.util.logging.Logger.getLogger(GAME_TITLE + " Logger");

        /**
         * Log a message with a specified level and automatic caller class name detection.
         *
         * @param level   The logging level
         * @param message The message to log
         */
        public static void log(Level level, String message) {
            // Retrieve the name of the class that called this method
            String callingClassName = Thread.currentThread().getStackTrace()[2].getClassName();
            writer.log(level, () -> "[" + callingClassName + "] " + message);
        }

        /**
         * Log an info level message.
         *
         * @param message The message to log
         */
        public static void info(String message) {
            log(Level.INFO, message);
        }

        /**
         * Log a warning level message.
         *
         * @param message The message to log
         */
        public static void warning(String message) {
            log(Level.WARNING, message);
        }

        /**
         * Log a severe level message.
         *
         * @param message The message to log
         */
        public static void severe(String message) {
            log(Level.SEVERE, message);
        }
    }

    /**
     * Convert a hex string to a Jaylib color.
     *
     * @param hex The hex string to convert
     * @return The Jaylib color
     */
    public static Jaylib.Color hexColor(String hex) {
        hex = hex.startsWith("#") ? hex.substring(1) : hex;
        return new Jaylib.Color(
                Integer.parseInt(hex.substring(0, 2), 16),
                Integer.parseInt(hex.substring(2, 4), 16),
                Integer.parseInt(hex.substring(4, 6), 16),
                (hex.length() == 8) ? Integer.parseInt(hex.substring(6, 8), 16) : 255
        );
    }

    /**
     * Perform a division of two integers with specific rounding behavior.
     *
     * @param a The dividend
     * @param b The divisor
     * @return The result of the division
     */
    public static Integer positionDivision(int a, int b) {
        if (a % b == 0) {
            return a / b;
        }
        return a < 0 ? (a / b) - 1 : (a / b) + 1;
    }
}