package Setting;

public class LevelConstants {
    public static char difficultyLevel = 'N';
    public static void setLevel(char level){
        if(level=='E')
            difficultyLevel = 'E';
        else if(level == 'H')
            difficultyLevel = 'H';
        else if(level =='N')
            difficultyLevel = 'N';
    }
}
