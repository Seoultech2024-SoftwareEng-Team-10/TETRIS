package Tetris;

import javafx.scene.text.Text;

public class ItemForm {

    public Text a, b, c, d;
    private String name;
    public int form = 1;
    String item;
    int itemRotate;

    public ItemForm(Text a, Text b, Text c, Text d, String name, boolean colorBlindMode,String item,int itemRotate) {
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.name = name;
        this.form = 1;
        updateItemColor();
        this.item = item;
        this.itemRotate = itemRotate;
    }


    private void updateItemColor() {
        javafx.scene.paint.Color color = ItemBlockColor.getItemColor(name);
        this.a.setFill(color);
        this.b.setFill(color);
        this.c.setFill(color);
        this.d.setFill(color);
    }


    public String getName() {
        return name;
    }
    public String getItem(){ return item; }
    public int getItemRotate(){ return itemRotate; }
    public void changeForm() {
        if (form != 4) {
            form++;
        } else {
            form = 1;
        }
    }

}