package Setting;

public class SizeConstants {
    // 450 x 600 (기본값)
    // 450 x 600 (기본값)
    private static final int MOVE_450x600 = 30;
    private static final int SIZE_450x600 = 30;
    private static final int XMAX_450x600 = SIZE_450x600 * 10;
    private static final int YMAX_450x600 = SIZE_450x600 * 21;
    private static final double FONTSIZE_450x600 = SIZE_450x600 * 1.4;

    // 300 x 400
    private static final int MOVE_300x400 = 20;
    private static final int SIZE_300x400 = 20;
    private static final int XMAX_300x400 = SIZE_300x400 * 10;
    private static final int YMAX_300x400 = SIZE_300x400 * 21;
    private static final double FONTSIZE_300x400 = SIZE_300x400 * 1.4;

    // 600 x 800
    private static final int MOVE_600x800 = 39;
    private static final int SIZE_600x800 = 39;
    private static final int XMAX_600x800 = SIZE_600x800 * 10;
    private static final int YMAX_600x800 = SIZE_600x800 * 21;
    private static final double FONTSIZE_600x800 = SIZE_600x800 * 1.4;
    // 현재 사용중인 크기 상수들
    public static int MOVE = MOVE_450x600;
    public static int SIZE = SIZE_450x600;
    public static int XMAX = XMAX_450x600;
    public static int YMAX = YMAX_450x600;
    public static double fontSize = FONTSIZE_450x600;
    public static int[][] MESH = new int[XMAX / SIZE][YMAX / SIZE + 1];

    public static int getMOVE() {
        return MOVE;
    }

    public static int getSIZE() {
        return SIZE;
    }

    public static int getXMAX() {
        return XMAX;
    }

    public static int getYMAX() {
        return YMAX;
    }

    public static double getFontSize() {
        return fontSize;
    }

    public static int[][] getMESH() {
        return MESH;
    }

    public static void setSize(int width, int height) {
        if (width == 450 && height == 600) {
            MOVE = MOVE_450x600;
            SIZE = SIZE_450x600;
            XMAX = XMAX_450x600;
            YMAX = YMAX_450x600;
            fontSize = FONTSIZE_450x600;
        } else if (width == 300 && height == 400) {
            MOVE = MOVE_300x400;
            SIZE = SIZE_300x400;
            XMAX = XMAX_300x400;
            YMAX = YMAX_300x400;
            fontSize = FONTSIZE_300x400;
        } else if (width == 600 && height == 800) {
            MOVE = MOVE_600x800;
            SIZE = SIZE_600x800;
            XMAX = XMAX_600x800;
            YMAX = YMAX_600x800;
            fontSize = FONTSIZE_600x800;
        }
        MESH = new int[XMAX / SIZE][YMAX / SIZE + 1];
    }
}