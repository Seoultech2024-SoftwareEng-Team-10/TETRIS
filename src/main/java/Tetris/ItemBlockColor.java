package Tetris;

public class ItemBlockColor {
    public static boolean colorBlindMode;

    public static javafx.scene.paint.Color getItemColor(String name) {
        if (colorBlindMode) {
            switch (name) {
                case "j":
                    return javafx.scene.paint.Color.GRAY;
                case "l":
                    return javafx.scene.paint.Color.GREEN;
                case "o":
                    return javafx.scene.paint.Color.BLUE;
                case "s":
                    return javafx.scene.paint.Color.ORANGE;
                case "t":
                    return javafx.scene.paint.Color.PINK;
                case "z":
                    return javafx.scene.paint.Color.YELLOW;
                case "i":
                    return javafx.scene.paint.Color.SKYBLUE;
                default:
                    return null;
            }
        } else {
            switch (name) {
                case "j":
                    return javafx.scene.paint.Color.SLATEGRAY;
                case "l":
                    return javafx.scene.paint.Color.DARKGOLDENROD;
                case "o":
                    return javafx.scene.paint.Color.INDIANRED;
                case "s":
                    return javafx.scene.paint.Color.FORESTGREEN;
                case "t":
                    return javafx.scene.paint.Color.CADETBLUE;
                case "z":
                    return javafx.scene.paint.Color.HOTPINK;
                case "i":
                    return javafx.scene.paint.Color.SANDYBROWN;
                default:
                    return null;
            }
        }
    }

    public static void setColorBlindMode(boolean mode) {
        colorBlindMode = mode;
    }
}
