package game.common.utils;

public class Utils {
    private Utils(){}

    public static Integer positionDivision(int a, int b) {
        if (a % b == 0) {
            return a / b;
        }
        return a < 0 ? (a / b) - 1 : (a / b) + 1;
    }
}
