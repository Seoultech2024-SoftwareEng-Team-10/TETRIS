package Tetris;

public class ItemBlockColor {
    public static boolean colorBlindMode;

    public static javafx.scene.paint.Color getItemColor(String name) {
        if (!colorBlindMode) {
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
                    return javafx.scene.paint.Color.WHITE;
                case "l":
                    return javafx.scene.paint.Color.rgb(37, 37, 37);
                case "o":
                    return javafx.scene.paint.Color.rgb(217, 43, 110);
                case "s":
                    return javafx.scene.paint.Color.rgb(156, 247, 99);
                case "t":
                    return javafx.scene.paint.Color.rgb(115, 183, 255);
                case "z":
                    return javafx.scene.paint.Color.rgb(255, 145, 61);
                case "i":
                    return javafx.scene.paint.Color.rgb(117, 0, 235);
                default:
                    return null;
            }
        }
    }

    public static void setColorBlindMode(boolean mode) {
        colorBlindMode = mode;
    }
}
