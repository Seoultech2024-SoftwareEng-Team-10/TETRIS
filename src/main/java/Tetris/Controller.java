package Tetris;

import Setting.LevelConstants;
import Setting.SizeConstants;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import static Setting.SizeConstants.*;


public class Controller {
    // Getting the numbers and the MESH from HelloApplication


    public static char difficultyLevel = LevelConstants.difficultyLevel;


    public static void MoveRight(Form form) {
        if (form.a.getX() + MOVE <= XMAX - SIZE && form.b.getX() + MOVE <= XMAX - SIZE
                && form.c.getX() + MOVE <= XMAX - SIZE && form.d.getX() + MOVE <= XMAX - SIZE) {
            int movea = MESH[((int) form.a.getX() / SIZE) + 1][((int) form.a.getY() / SIZE)];
            int moveb = MESH[((int) form.b.getX() / SIZE) + 1][((int) form.b.getY() / SIZE)];
            int movec = MESH[((int) form.c.getX() / SIZE) + 1][((int) form.c.getY() / SIZE)];
            int moved = MESH[((int) form.d.getX() / SIZE) + 1][((int) form.d.getY() / SIZE)];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                form.a.setX(form.a.getX() + MOVE);
                form.b.setX(form.b.getX() + MOVE);
                form.c.setX(form.c.getX() + MOVE);
                form.d.setX(form.d.getX() + MOVE);
            }
        }
    }

    public static void MoveLeft(Form form) {
        if (form.a.getX() - MOVE >= 0 && form.b.getX() - MOVE >= 0 && form.c.getX() - MOVE >= 0
                && form.d.getX() - MOVE >= 0) {
            int movea = MESH[((int) form.a.getX() / SIZE) - 1][((int) form.a.getY() / SIZE)];
            int moveb = MESH[((int) form.b.getX() / SIZE) - 1][((int) form.b.getY() / SIZE)];
            int movec = MESH[((int) form.c.getX() / SIZE) - 1][((int) form.c.getY() / SIZE)];
            int moved = MESH[((int) form.d.getX() / SIZE) - 1][((int) form.d.getY() / SIZE)];
            if (movea == 0 && movea == moveb && moveb == movec && movec == moved) {
                form.a.setX(form.a.getX() - MOVE);
                form.b.setX(form.b.getX() - MOVE);
                form.c.setX(form.c.getX() - MOVE);
                form.d.setX(form.d.getX() - MOVE);
            }
        }
    }



    public static Form makeText(boolean colorBlindMode, char difficultyLevel) {
        int block = 0;
        if(difficultyLevel == 'E'){
            block = (int) (Math.random() * 72);

        }else if(difficultyLevel == 'H'){
            block = (int) (Math.random() * 68);

        }else{
            block = (int) (Math.random() * 70);
        }
        String name;
        Text a = new Text(0, 0, "O"), b = new Text(0, 0, "O"), c = new Text(0, 0, "O"), d = new Text(0, 0, "O");//Rectangle --> Text
        a.setFont(Font.font(fontSize));
        b.setFont(Font.font(fontSize));
        c.setFont(Font.font(fontSize));
        d.setFont(Font.font(fontSize));//fontsize설정
        if (block < 10) {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);
            name = "j";
        } else if (block < 20) {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);
            name = "l";
        } else if (block < 30) {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 - SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2);
            d.setY(SIZE);
            name = "o";
        } else if (block < 40) {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 - SIZE);
            d.setY(SIZE);
            name = "s";
        } else if (block < 50) {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            name = "t";
        } else if (block < 60) {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 + SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE + SIZE);
            d.setY(SIZE);
            name = "z";
        } else {
            a.setX(XMAX / 2 - SIZE - SIZE);
            b.setX(XMAX / 2 - SIZE);
            c.setX(XMAX / 2);
            d.setX(XMAX / 2 + SIZE);
            name = "i";
        }

        if (!colorBlindMode) {
            // 일반 모드
            switch (name) {
                case "j":
                    a.setFill(Color.GRAY);
                    b.setFill(Color.GRAY);
                    c.setFill(Color.GRAY);
                    d.setFill(Color.GRAY);
                    break;
                case "l":
                    a.setFill(Color.GREEN);
                    b.setFill(Color.GREEN);
                    c.setFill(Color.GREEN);
                    d.setFill(Color.GREEN);
                    break;
                case "o":
                    a.setFill(Color.BLUE);
                    b.setFill(Color.BLUE);
                    c.setFill(Color.BLUE);
                    d.setFill(Color.BLUE);
                    break;
                case "s":
                    a.setFill(Color.ORANGE);
                    b.setFill(Color.ORANGE);
                    c.setFill(Color.ORANGE);
                    d.setFill(Color.ORANGE);
                    break;
                case "t":
                    a.setFill(Color.PINK);
                    b.setFill(Color.PINK);
                    c.setFill(Color.PINK);
                    d.setFill(Color.PINK);
                    break;
                case "z":
                    a.setFill(Color.YELLOW);
                    b.setFill(Color.YELLOW);
                    c.setFill(Color.YELLOW);
                    d.setFill(Color.YELLOW);
                    break;
                case "i":
                    a.setFill(Color.SKYBLUE);
                    b.setFill(Color.SKYBLUE);
                    c.setFill(Color.SKYBLUE);
                    d.setFill(Color.SKYBLUE);
                    break;
            }
        } else {
            // 색맹 모드
            switch (name) {
                case "j":
                    a.setFill(Color.WHITE);
                    b.setFill(Color.WHITE);
                    c.setFill(Color.WHITE);
                    d.setFill(Color.WHITE);
                    break;
                case "l":
                    a.setFill(Color.rgb(37, 37, 37));
                    b.setFill(Color.rgb(37, 37, 37));
                    c.setFill(Color.rgb(37, 37, 37));
                    d.setFill(Color.rgb(37, 37, 37));
                    break;
                case "o":
                    a.setFill(Color.rgb(217, 43, 110));
                    b.setFill(Color.rgb(217, 43, 110));
                    c.setFill(Color.rgb(217, 43, 110));
                    d.setFill(Color.rgb(217, 43, 110));
                    break;
                case "s":
                    a.setFill(Color.rgb(156, 247, 99));
                    b.setFill(Color.rgb(156, 247, 99));
                    c.setFill(Color.rgb(156, 247, 99));
                    d.setFill(Color.rgb(156, 247, 99));
                    break;
                case "t":
                    a.setFill(Color.rgb(115, 183, 255));
                    b.setFill(Color.rgb(115, 183, 255));
                    c.setFill(Color.rgb(115, 183, 255));
                    d.setFill(Color.rgb(115, 183, 255));
                    break;
                case "z":
                    a.setFill(Color.rgb(255, 145, 61));
                    b.setFill(Color.rgb(255, 145, 61));
                    c.setFill(Color.rgb(255, 145, 61));
                    d.setFill(Color.rgb(255, 145, 61));
                    break;
                case "i":
                    a.setFill(Color.rgb(117, 0, 235));
                    b.setFill(Color.rgb(117, 0, 235));
                    c.setFill(Color.rgb(117, 0, 235));
                    d.setFill(Color.rgb(117, 0, 235));
                    break;
            }
        }

        return new Form(a, b, c, d, name);
    }
    public static Form makeText(String name, boolean colorBlindMode) {
        Text a = new Text(0, 0, "O"), b = new Text(0, 0, "O"), c = new Text(0, 0, "O"),
                d = new Text(0, 0, "O");//Rectangle --> Text
        a.setFont(Font.font(fontSize));
        b.setFont(Font.font(fontSize));
        c.setFont(Font.font(fontSize));
        d.setFont(Font.font(fontSize));//fontsize설정
        if (name=="j") {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);
        } else if (name=="l") {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2 - SIZE);
            b.setY(SIZE);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
            d.setY(SIZE);
        } else if (name=="o") {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 - SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2);
            d.setY(SIZE);
        } else if (name=="s") {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 - SIZE);
            d.setY(SIZE);
        } else if (name=="t") {
            a.setX(XMAX / 2 - SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE);
        } else if (name=="z") {
            a.setX(XMAX / 2 + SIZE);
            b.setX(XMAX / 2);
            c.setX(XMAX / 2 + SIZE);
            c.setY(SIZE);
            d.setX(XMAX / 2 + SIZE + SIZE);
            d.setY(SIZE);
        } else {
            a.setX(XMAX / 2 - SIZE - SIZE);
            b.setX(XMAX / 2 - SIZE);
            c.setX(XMAX / 2);
            d.setX(XMAX / 2 + SIZE);
        }

        if (!colorBlindMode) {
            // 일반 모드
            switch (name) {
                case "j":
                    a.setFill(Color.GRAY);
                    b.setFill(Color.GRAY);
                    c.setFill(Color.GRAY);
                    d.setFill(Color.GRAY);
                    break;
                case "l":
                    a.setFill(Color.GREEN);
                    b.setFill(Color.GREEN);
                    c.setFill(Color.GREEN);
                    d.setFill(Color.GREEN);
                    break;
                case "o":
                    a.setFill(Color.BLUE);
                    b.setFill(Color.BLUE);
                    c.setFill(Color.BLUE);
                    d.setFill(Color.BLUE);
                    break;
                case "s":
                    a.setFill(Color.ORANGE);
                    b.setFill(Color.ORANGE);
                    c.setFill(Color.ORANGE);
                    d.setFill(Color.ORANGE);
                    break;
                case "t":
                    a.setFill(Color.PINK);
                    b.setFill(Color.PINK);
                    c.setFill(Color.PINK);
                    d.setFill(Color.PINK);
                    break;
                case "z":
                    a.setFill(Color.YELLOW);
                    b.setFill(Color.YELLOW);
                    c.setFill(Color.YELLOW);
                    d.setFill(Color.YELLOW);
                    break;
                case "i":
                    a.setFill(Color.SKYBLUE);
                    b.setFill(Color.SKYBLUE);
                    c.setFill(Color.SKYBLUE);
                    d.setFill(Color.SKYBLUE);
                    break;
            }
        } else {
            // 색맹 모드
            switch (name) {
                case "j":
                    a.setFill(Color.WHITE);
                    b.setFill(Color.WHITE);
                    c.setFill(Color.WHITE);
                    d.setFill(Color.WHITE);
                    break;
                case "l":
                    a.setFill(Color.rgb(37, 37, 37));
                    b.setFill(Color.rgb(37, 37, 37));
                    c.setFill(Color.rgb(37, 37, 37));
                    d.setFill(Color.rgb(37, 37, 37));
                    break;
                case "o":
                    a.setFill(Color.rgb(217, 43, 110));
                    b.setFill(Color.rgb(217, 43, 110));
                    c.setFill(Color.rgb(217, 43, 110));
                    d.setFill(Color.rgb(217, 43, 110));
                    break;
                case "s":
                    a.setFill(Color.rgb(156, 247, 99));
                    b.setFill(Color.rgb(156, 247, 99));
                    c.setFill(Color.rgb(156, 247, 99));
                    d.setFill(Color.rgb(156, 247, 99));
                    break;
                case "t":
                    a.setFill(Color.rgb(115, 183, 255));
                    b.setFill(Color.rgb(115, 183, 255));
                    c.setFill(Color.rgb(115, 183, 255));
                    d.setFill(Color.rgb(115, 183, 255));
                    break;
                case "z":
                    a.setFill(Color.rgb(255, 145, 61));
                    b.setFill(Color.rgb(255, 145, 61));
                    c.setFill(Color.rgb(255, 145, 61));
                    d.setFill(Color.rgb(255, 145, 61));
                    break;
                case "i":
                    a.setFill(Color.rgb(117, 0, 235));
                    b.setFill(Color.rgb(117, 0, 235));
                    c.setFill(Color.rgb(117, 0, 235));
                    d.setFill(Color.rgb(117, 0, 235));
                    break;
            }
        }

        return new Form(a, b, c, d, name);
    }
    public static Form waitingTextMake(boolean colorBlindMode, char difficultyLevel){
        Form waitObj = makeText(colorBlindMode, difficultyLevel);

        waitObj.a.setX(waitObj.a.getX()+SIZE*7);
        waitObj.b.setX(waitObj.b.getX()+SIZE*7);
        waitObj.c.setX(waitObj.c.getX()+SIZE*7);
        waitObj.d.setX(waitObj.d.getX()+SIZE*7);
        waitObj.a.setY(waitObj.a.getY()+150);
        waitObj.b.setY(waitObj.b.getY()+150);
        waitObj.c.setY(waitObj.c.getY()+150);
        waitObj.d.setY(waitObj.d.getY()+150);
        waitObj.a.setUserData("waita");
        waitObj.b.setUserData("waitb");
        waitObj.c.setUserData("waitc");
        waitObj.d.setUserData("waitd");

        return new Form(waitObj.a, waitObj.b, waitObj.c, waitObj.d, waitObj.getName());
    }
}