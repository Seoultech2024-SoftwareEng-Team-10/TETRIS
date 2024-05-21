package Setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SizeConstants {
    private int MOVE;
    private int SIZE;
    private int XMAX;
    private int YMAX;
    private double fontSize;
    private int[][] MESH;

    public SizeConstants(int width, int height) {
        if (width == 450 && height == 600) {
            this.MOVE = MOVE_450x600;
            this.SIZE = SIZE_450x600;
            this.XMAX = XMAX_450x600;
            this.YMAX = YMAX_450x600;
            this.fontSize = FONTSIZE_450x600;
        } else if (width == 300 && height == 400) {
            this.MOVE = MOVE_300x400;
            this.SIZE = SIZE_300x400;
            this.XMAX = XMAX_300x400;
            this.YMAX = YMAX_300x400;
            this.fontSize = FONTSIZE_300x400;
        } else if (width == 600 && height == 800) {
            this.MOVE = MOVE_600x800;
            this.SIZE = SIZE_600x800;
            this.XMAX = XMAX_600x800;
            this.YMAX = YMAX_600x800;
            this.fontSize = FONTSIZE_600x800;
        }
        MESH = new int[XMAX / SIZE][YMAX / SIZE + 1];
    }
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


    public void setSize(int width, int height) {
        if (width == 450 && height == 600) {
            this.MOVE = MOVE_450x600;
            this.SIZE = SIZE_450x600;
            this.XMAX = XMAX_450x600;
            this.YMAX = YMAX_450x600;
            this.fontSize = FONTSIZE_450x600;
        } else if (width == 300 && height == 400) {
            this.MOVE = MOVE_300x400;
            this.SIZE = SIZE_300x400;
            this.XMAX = XMAX_300x400;
            this.YMAX = YMAX_300x400;
            this.fontSize = FONTSIZE_300x400;
        } else if (width == 600 && height == 800) {
            this.MOVE = MOVE_600x800;
            this.SIZE = SIZE_600x800;
            this.XMAX = XMAX_600x800;
            this.YMAX = YMAX_600x800;
            this.fontSize = FONTSIZE_600x800;
        }
        MESH = new int[XMAX / SIZE][YMAX / SIZE + 1];
    }

}