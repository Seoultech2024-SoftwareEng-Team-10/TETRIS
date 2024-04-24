package Tetris;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class Form {

    public Text a, b, c, d;
    private Color color;
    private String name;
    public int form = 1;
    private boolean colorBlindMode; // 색맹 모드 여부 추가

    public Form(Text a, Text b, Text c, Text d, String name, boolean colorBlindMode) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;
        this.colorBlindMode = colorBlindMode;
        this.form = 1;
        if(colorBlindMode){
            switch (name) {
                case "j":
                    this.a.setFill(Color.RED);
                    this.b.setFill(Color.RED);
                    this.c.setFill(Color.RED);
                    this.d.setFill(Color.RED);
                    break;
                case "l":
                    this.a.setFill(Color.GREEN);
                    this.b.setFill(Color.GREEN);
                    this.c.setFill(Color.GREEN);
                    this.d.setFill(Color.GREEN);
                    break;
                case "o":
                    this.a.setFill(Color.BLUE);
                    this.b.setFill(Color.BLUE);
                    this.c.setFill(Color.BLUE);
                    this.d.setFill(Color.BLUE);
                    break;
                case "s":
                    a.setFill(Color.ORANGE);
                    b.setFill(Color.ORANGE);
                    c.setFill(Color.ORANGE);
                    d.setFill(Color.ORANGE);
                    break;
                case "t":
                    a.setFill(Color.PURPLE);
                    b.setFill(Color.PURPLE);
                    c.setFill(Color.PURPLE);
                    d.setFill(Color.PURPLE);
                    break;
                case "z":
                    a.setFill(Color.YELLOW);
                    b.setFill(Color.YELLOW);
                    c.setFill(Color.YELLOW);
                    d.setFill(Color.YELLOW);
                    break;
                case "i":
                    a.setFill(Color.BLACK);
                    b.setFill(Color.BLACK);
                    c.setFill(Color.BLACK);
                    d.setFill(Color.BLACK);
                    break;
            }
        }
        else{
            updateColor();
        }
    }

    private void updateColor() {
        if (colorBlindMode) {
            switch (name) {
                case "j":
                    color = Color.RED;
                    break;
                case "l":
                    color = Color.GREEN;
                    break;
                case "o":
                    color = Color.BLUE;
                    break;
                case "s":
                    color = Color.ORANGE;
                    break;
                case "t":
                    color = Color.PURPLE;
                    break;
                case "z":
                    color = Color.YELLOW;
                    break;
                case "i":
                    color = Color.BLACK;
                    break;
            }
        } else {
            switch (name) {
                case "j":
                    color = Color.SLATEGRAY;
                    break;
                case "l":
                    color = Color.DARKGOLDENROD;
                    break;
                case "o":
                    color = Color.INDIANRED;
                    break;
                case "s":
                    color = Color.FORESTGREEN;
                    break;
                case "t":
                    color = Color.CADETBLUE;
                    break;
                case "z":
                    color = Color.HOTPINK;
                    break;
                case "i":
                    color = Color.SANDYBROWN;
                    break;
            }

        }
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }


    // 색맹 모드 설정 메서드
    public void setColorBlindMode(boolean colorBlindMode) {
        this.colorBlindMode = colorBlindMode;
        if(colorBlindMode){
            switch (name) {
                case "j":
                    this.a.setFill(Color.RED);
                    this.b.setFill(Color.RED);
                    this.c.setFill(Color.RED);
                    this.d.setFill(Color.RED);
                    break;
                case "l":
                    this.a.setFill(Color.GREEN);
                    this.b.setFill(Color.GREEN);
                    this.c.setFill(Color.GREEN);
                    this.d.setFill(Color.GREEN);
                    break;
                case "o":
                    this.a.setFill(Color.BLUE);
                    this.b.setFill(Color.BLUE);
                    this.c.setFill(Color.BLUE);
                    this.d.setFill(Color.BLUE);
                    break;
                case "s":
                    a.setFill(Color.ORANGE);
                    b.setFill(Color.ORANGE);
                    c.setFill(Color.ORANGE);
                    d.setFill(Color.ORANGE);
                    break;
                case "t":
                    a.setFill(Color.PURPLE);
                    b.setFill(Color.PURPLE);
                    c.setFill(Color.PURPLE);
                    d.setFill(Color.PURPLE);
                    break;
                case "z":
                    a.setFill(Color.YELLOW);
                    b.setFill(Color.YELLOW);
                    c.setFill(Color.YELLOW);
                    d.setFill(Color.YELLOW);
                    break;
                case "i":
                    a.setFill(Color.BLACK);
                    b.setFill(Color.BLACK);
                    c.setFill(Color.BLACK);
                    d.setFill(Color.BLACK);
                    break;
            }
        }
        else{
            updateColor();
        }
    }

    public String getName() {
        return name;
    }

    public void changeForm() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }

}
