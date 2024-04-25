package Setting;

public class LevelConstants {
    public static char difficultyLevel = 'N'; // 기본값은 Normal

    public static void setLevel(char level) {
        difficultyLevel = level;
    }

    public static char getLevel() {
        return difficultyLevel;
    }
}
