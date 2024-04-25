package Tetris;

import javafx.scene.text.Text;

public class Form {

    public Text a, b, c, d;
    private String name;
    public int form = 1;

    public Form(Text a, Text b, Text c, Text d, String name) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;
        this.form = 1;
        updateColor();
    }

    private void updateColor() {
        javafx.scene.paint.Color color = BlockColor.getColor(name);
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
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