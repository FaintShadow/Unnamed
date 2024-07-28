package game.utilities;

public class Utils {
    private Utils(){}

    public static Integer positionDivision(int a, int b){
        if (a % b == 0){
            return a / b;
        }
        return (a / b) + 1;
    }
}
