
import ScoreBoard.JdbcConnecter;
import Setting.LevelConstants;
import Setting.Settings;
import Setting.SizeConstants;
import Tetris.BlockColor;
import Tetris.Controller;
import Tetris.Form;
import User.User;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import User.SessionManager;


public class HelloApplication extends Application {
    private HBox hbox;
    private AnimationTimer timer;
    private Text scoretext1, scoretext2;
    public boolean running;
    private static Form object;
    private static Form object2;
    private static Pane group1 = new Pane();
    private static Pane group2 = new Pane();
    private static Scene scene;
    private static int top;
    private static boolean game = true;
    private ColorAdjust colorAdjust = new ColorAdjust(); //Color Adjust 조절
    private static char difficultylevel = LevelConstants.difficultyLevel;
    private Form nextObj, nextObj2;
    private Form waitObj, waitObj2;
    private final int[][] MESH, MESH2;
    private static int linesNo = 0;
    private Button restartButton;
    private Button exitButton;
    private Label scoreLabel;
    private static double scoreMultiplier = 1.0;
    private static double frameMultiplier = 0.8;
    private JdbcConnecter scoreboardDataInserter;

    //private final User user;
    private final Controller controller;
    private final int MOVE;
    private final int SIZE;
    private final int XMAX;
    private final int YMAX;
    private final String rightKey;
    private final String leftKey;
    private final String spaceKey;
    private final String upKey;
    private final String downKey;
    private final String dKey;
    private final String aKey;
    private final String shiftKey;
    private final String wKey;
    private final String sKey;

    private int score;
    private double Frame;
    public HelloApplication(SizeConstants sizeConstants, Settings settings, Controller controller){
        this.controller = controller;
        this.score = 0;
        this.running = true;
        this.wKey = "W";
        this.aKey = "A";
        this.sKey = "S";
        this.shiftKey = "SHIFT";
        this.dKey = "D";
        this.rightKey = settings.getRightKey();
        this.leftKey = settings.getLeftKey();
        this.upKey = settings.getUpKey();
        this.downKey = settings.getDownKey();
        this.spaceKey  = settings.getSpaceKey();
        this.waitObj = controller.waitingTextMake(BlockColor.colorBlindMode, difficultylevel, 400);
        this.nextObj = controller.makeText(BlockColor.colorBlindMode, difficultylevel, 200);

        this.waitObj2 = controller.waitingTextMake(BlockColor.colorBlindMode, difficultylevel, 400);
        this.nextObj2 = controller.makeText(BlockColor.colorBlindMode, difficultylevel,200);

        this.MOVE = sizeConstants.getMOVE();
        this.SIZE = sizeConstants.getSIZE();
        this.XMAX = sizeConstants.getXMAX();
        this.YMAX = sizeConstants.getYMAX();
        this.MESH = new int[20][21];
        this.MESH2 = new int[20][21];
        this.running = true;
        //this.user = TetrisWindow.user;
        this.linesNo = 0;
        this.Frame = 1000000000;
    }
    public Text styleScoretext(int Pos){
        Text scoretext = new Text();
        scoretext = new Text("SCORE: ");
        scoretext.setUserData("scoretext");
        scoretext.setFill(Color.WHITE);
        scoretext.setStyle("-fx-font: 20  Lato;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 30);
        scoretext.setY(300);
        return scoretext;
    }
    @Override
    public void start(Stage stage) throws IOException {
        System.out.println(Frame);
        System.out.println(score);
        System.out.println(linesNo);
        //User currentUser = SessionManager.getCurrentUser();
        //System.out.println(currentUser.getNickname());
        stage.close();
        if(LevelConstants.getLevel()=='E'){
            frameMultiplier = 0.8;
            scoreMultiplier = 1.0;
        }
        else if(LevelConstants.getLevel()=='N'){
            frameMultiplier = 1.0;
            scoreMultiplier = 1.2;
        }
        else{
            frameMultiplier = 1.2;
            scoreMultiplier = 1.4;
        }


        group1 = new Pane();
        group2 = new Pane();
        hbox = new HBox(20);
        hbox.getChildren().addAll(group2,group1);
        scene = new Scene(hbox, 1500, YMAX-40);
        running = true;
        group1.getChildren().clear();
        group2.getChildren().clear();

        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }
        drawGridLines(group1);
        drawGridLines(group2);
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        scoretext1 = styleScoretext(XMAX+30);
        scoretext2 = styleScoretext(XMAX+30);

        Text level = new Text("LINES: ");//scoretext,level userdata추가
        level.setUserData("level");
        level.setStyle("-fx-font: 20 Lato;");
        level.setY(350);
        level.setX(XMAX + 30);
        level.setFill(Color.GREEN);
        Form wait = waitObj;
        Form wait2 = waitObj2;
        group1.getChildren().addAll(scoretext1, line, level, wait.a, wait.b, wait.c, wait.d);
        group1.setStyle("-fx-background-color: black;");
        group2.getChildren().addAll(scoretext2, line, level, wait2.a, wait2.b, wait2.c, wait2.d);
        group2.setStyle("-fx-background-color: black;");
        Form a = nextObj;
        Form b = nextObj2;
        group1.getChildren().addAll(a.a, a.b, a.c, a.d);
        group2.getChildren().addAll(b.a, b.b, b.c, b.d);
        moveOnKeyPress(a,b);
        object = a;
        object2 = b;
        nextObj = controller.makeText(true,difficultylevel, 200);
        nextObj2 = controller.makeText(true,difficultylevel, 200);
        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (running) {
                    if (now - lastUpdate >= Frame) { // 1초마다 실행
                        stage.setOnCloseRequest(event -> {
                            timer.stop();
                            group1.getChildren().clear();
                            group2.getChildren().clear();
                        });
                        lastUpdate = now;

                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 || object.d.getY() == 0)
                            top++;
                        else
                            top = 0;

                        if (top == 2) {
                            running = false;
                            GameOver();

                        }
                        // Exit
                        if (top == 15) {
                            running = false;
                            GameOver();
                            stage.close();
                        }

                        if (game) {
                            MoveDown(object, MESH, group1, true);
                            MoveDown(object2, MESH2, group2, false);
                            scoretext1.setText("Score: " + score);
                            scoretext2 .setText("Score: " + score);
                            level.setText("Lines: " + linesNo);
                        }

                    }
                }
            }
        };
        stage.setOnCloseRequest(event -> {
            // 게임 종료 시 실행되는 코드
            timer.stop(); // AnimationTimer 중지
            group1.getChildren().clear(); // 모든 노드 제거
            group2.getChildren().clear(); // 모든 노드 제거
            // 필요한 경우 추가적인 리소스 해제 코드 추가
        });
        timer.start();
    }



    private void drawGridLines(Pane group) {
        for (int x = 0; x <= XMAX / SIZE; x++) {
            Line line = new Line(x * SIZE, 0, x * SIZE, YMAX);
            line.setStroke(Color.DARKGRAY);
            line.setStrokeWidth(0.2);
            if (x == XMAX / SIZE) { // 마지막 열에 굵은 선 추가
                line.setStrokeWidth(5.0);
            }
            group.getChildren().add(line);
        }
        for (int y = 0; y <= YMAX / SIZE; y++) {
            Line line = new Line(0, y * SIZE, XMAX, y * SIZE);
            line.setStroke(Color.DARKGRAY);
            line.setStrokeWidth(0.2);
            group.getChildren().add(line);
        }
    }

    private void moveOnKeyPress(Form form, Form form2) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String pressedKey = event.getCode().toString();
                if(running) {
                    if (pressedKey.equals(rightKey)) {
                        controller.MoveRight(form);
                    } else if (pressedKey.equals(downKey)) {
                        MoveDown(form, MESH, group1, true);
                        scoretext1.setText("Score: " + score);
                    } else if (pressedKey.equals(leftKey)) {
                        controller.MoveLeft(form);
                    } else if (pressedKey.equals(upKey)) {
                        MoveTurn(form);
                    } else if (pressedKey.equals(spaceKey)) {
                        DirectMoveDown(form, form2, group1, true, MESH);
                        scoretext1.setText("Score: " + score);
                    } else if (pressedKey.equals("ESCAPE")) {
                        stopAnimation();
                    } else if (pressedKey.equals(dKey)) {
                        controller.MoveRight(form2);
                    } else if (pressedKey.equals(sKey)) {
                        MoveDown(form2, MESH2, group2, false);
                        scoretext1.setText("Score: " + score);
                    } else if (pressedKey.equals(aKey)) {
                        controller.MoveLeft(form2);
                    } else if (pressedKey.equals(wKey)) {
                        MoveTurn(form2);
                    } else if (pressedKey.equals(shiftKey)) {
                        DirectMoveDown(form2, form, group2, false, MESH2);
                        scoretext1.setText("Score: " + score);
                    }
                }
                else{
                    if(event.getCode() == KeyCode.ESCAPE) {
                        startAnimation();
                    }
                }
            }
        });
    }


    private void MoveTurn(Form form) {
        int f = form.form;
        Text a = form.a;
        Text b = form.b;
        Text c = form.c;
        Text d = form.d;//Rectangle - >Text
        switch (form.getName()) {
            case "j":
                if (f == 1) {
                    if (cB(a, 1, -1) && cB(c, -1, -1) && cB(d, -2, -2)) {
                        MoveRight(form.a);
                        MoveDown(form.a);
                        MoveDown(form.c);
                        MoveLeft(form.c);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        MoveLeft(form.d);
                        MoveLeft(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 2){
                    if( cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.c);
                        MoveUp(form.c);
                        MoveLeft(form.d);
                        MoveLeft(form.d);
                        MoveUp(form.d);
                        MoveUp(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 1, -1) && cB(c, 1, 1) && cB(b,2,0) && cB(d, 0, 2)) {
                        MoveDown(form.a);
                        MoveRight(form.a);
                        MoveRight(form.b);
                        MoveRight(form.b);
                        MoveRight(form.c);
                        MoveUp(form.c);
                        MoveUp(form.d);
                        MoveUp(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3 && cB(a, -1, 1) && cB(c, 1, 1) && cB(d, 2, 2)) {
                    MoveLeft(form.a);
                    MoveUp(form.a);
                    MoveUp(form.c);
                    MoveRight(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4){
                    if( cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, -1, 1) && cB(b,-2,0) && cB(c, -1, -1) && cB(d, 0, -2)) {
                        MoveUp(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.c);
                        MoveDown(form.c);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                }
                break;
            case "l":
                if (f == 1) {
                    if (cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                        MoveRight(form.a);
                        MoveDown(form.a);
                        MoveUp(form.c);
                        MoveRight(form.c);
                        MoveUp(form.b);
                        MoveUp(form.b);
                        MoveRight(form.b);
                        MoveRight(form.b);
                        form.changeForm();
                        break;
                    }
                    if (cB(a, 0, -1) && cB(c, 0, 1) && cB(b, 1, 2) && cB(d,-1,0)) {
                        MoveDown(form.a);
                        MoveUp(form.c);
                        MoveUp(form.b);
                        MoveUp(form.b);
                        MoveRight(form.b);
                        MoveLeft(form.d);
                        form.changeForm();
                        break;
                    }
                    if (cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 2)) {
                        MoveRight(form.a);
                        MoveDown(form.a);
                        MoveUp(form.c);
                        MoveRight(form.c);
                        MoveUp(form.b);
                        MoveUp(form.b);
                        MoveRight(form.b);
                        MoveRight(form.b);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 2) {
                    if(cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveRight(form.b);
                        MoveRight(form.b);
                        MoveDown(form.b);
                        MoveDown(form.b);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        form.changeForm();
                        break;
                    }
                    if(cB(a, -2, -1) && cB(b, 1, -2) && cB(c, 0, -1) && cB(d,-1,0)) {
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveRight(form.b);
                        MoveDown(form.b);
                        MoveDown(form.b);
                        MoveDown(form.c);
                        MoveLeft(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3){
                    if( cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
                        MoveLeft(form.a);
                        MoveUp(form.a);
                        MoveDown(form.c);
                        MoveLeft(form.c);
                        MoveDown(form.b);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 0, 1) && cB(c, 0, -1) && cB(b, -1, -2) && cB(d,1,0)) {
                        MoveUp(form.a);
                        MoveDown(form.c);
                        MoveDown(form.b);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 4){
                    if( cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveUp(form.b);
                        MoveUp(form.b);
                        MoveLeft(form.c);
                        MoveUp(form.c);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 2, 1) && cB(b, -1, 2) && cB(c, 0, 1) && cB(d,1,0)) {
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveRight(form.a);
                        MoveLeft(form.b);
                        MoveUp(form.b);
                        MoveUp(form.b);
                        MoveUp(form.c);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                break;
            case "o":
                break;
            case "s":
                if (f == 1 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2){
                    if( cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 0, 1) && cB(b,-1,0) && cB(c, 0, -1) && cB(d, -1, -2)) {
                        MoveUp(form.a);
                        MoveLeft(form.b);
                        MoveDown(form.c);
                        MoveLeft(form.d);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3 && cB(a, -1, -1) && cB(c, -1, 1) && cB(d, 0, 2)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveUp(form.d);
                    MoveUp(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4){
                    if(cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 0, 1) && cB(b,-1,0) && cB(c, 0, -1) && cB(d, -1, -2)) {
                        MoveUp(form.a);
                        MoveLeft(form.b);
                        MoveDown(form.c);
                        MoveLeft(form.d);
                        MoveDown(form.d);
                        MoveDown(form.d);
                        form.changeForm();
                        break;
                    }
                }
                break;
            case "t":
                if (f == 1 && cB(a, 1, 1) && cB(d, -1, -1) && cB(c, -1, 1)) {
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 2){
                    if(cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
                        MoveRight(form.a);
                        MoveDown(form.a);
                        MoveLeft(form.d);
                        MoveUp(form.d);
                        MoveUp(form.c);
                        MoveRight(form.c);
                        form.changeForm();
                        break;
                    }
                    if(cB(a, 0, -1) && cB(b,-1,0) && cB(d, -2, 1) && cB(c, 0, 1)) {
                        MoveLeft(form.b);
                        MoveDown(form.a);
                        MoveLeft(form.d);
                        MoveLeft(form.d);
                        MoveUp(form.d);
                        MoveUp(form.c);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3 && cB(a, -1, -1) && cB(d, 1, 1) && cB(c, 1, -1)) {
                    MoveDown(form.a);
                    MoveLeft(form.a);
                    MoveUp(form.d);
                    MoveRight(form.d);
                    MoveRight(form.c);
                    MoveDown(form.c);
                    form.changeForm();
                    break;
                }
                if (f == 4){
                    if(cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
                        MoveLeft(form.a);
                        MoveUp(form.a);
                        MoveRight(form.d);
                        MoveDown(form.d);
                        MoveDown(form.c);
                        MoveLeft(form.c);
                        form.changeForm();
                        break;
                    }
                    if(cB(a, 0, 1) && cB(b,1,0) && cB(d, 2, -1) && cB(c, 0, -1)) {
                        MoveUp(form.a);
                        MoveRight(form.b);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        MoveDown(form.d);
                        MoveDown(form.c);
                        form.changeForm();
                        break;
                    }
                }
                break;
            case "z":
                if (f == 1 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 2){
                    if(cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                    if(cB(a,-1,0) && cB(b, -2, -1) && cB(c, 0, -1) && cB(d, 1, 0)) {
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveDown(form.c);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3 && cB(b, 1, 1) && cB(c, -1, 1) && cB(d, -2, 0)) {
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveLeft(form.c);
                    MoveUp(form.c);
                    MoveLeft(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4){
                    if(cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveRight(form.c);
                        MoveDown(form.c);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                    if(cB(a,-1,0) && cB(b, -2, -1) && cB(c, 0, -1) && cB(d, 1, 0)) {
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveDown(form.c);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                break;
            case "i":
                if (f == 1) {
                    if (cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                        MoveUp(form.a);
                        MoveUp(form.a);
                        MoveRight(form.a);
                        MoveRight(form.a);
                        MoveUp(form.b);
                        MoveRight(form.b);
                        MoveDown(form.d);
                        MoveLeft(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 2){
                    if( cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)){
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveUp(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                    if(cB(a, -3, -2) && cB(b, -2, -1) &&cB(c,-1,0) && cB(d, 0, 1)){
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.c);
                        MoveUp(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 0, -2) && cB(b, 1, -1) && cB(c, 2, 0)&&cB(d,3,1)){
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveDown(form.b);
                        MoveRight(form.b);
                        MoveRight(form.c);
                        MoveRight(form.c);
                        MoveUp(form.d);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                if (f == 3 && cB(a, 2, 2) && cB(b, 1, 1) && cB(d, -1, -1)) {
                    MoveUp(form.a);
                    MoveUp(form.a);
                    MoveRight(form.a);
                    MoveRight(form.a);
                    MoveUp(form.b);
                    MoveRight(form.b);
                    MoveDown(form.d);
                    MoveLeft(form.d);
                    form.changeForm();
                    break;
                }
                if (f == 4){
                    if( cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1))
                    {
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveUp(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                    if(cB(a, -3, -2) && cB(b, -2, -1) &&cB(c,-1,0) && cB(d, 0, 1)){
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveLeft(form.a);
                        MoveDown(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.b);
                        MoveLeft(form.c);
                        MoveUp(form.d);
                        form.changeForm();
                        break;
                    }
                    if( cB(a, 0, -2) && cB(b, 1, -1) && cB(c, 2, 0)&&cB(d,3,1)){
                        MoveDown(form.a);
                        MoveDown(form.a);
                        MoveDown(form.b);
                        MoveRight(form.b);
                        MoveRight(form.c);
                        MoveRight(form.c);
                        MoveUp(form.d);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        MoveRight(form.d);
                        form.changeForm();
                        break;
                    }
                }
                break;
        }
    }

    private void RemoveRows(Pane pane) {
        ArrayList<Node> texts = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newtexts = new ArrayList<Node>();
        int full = 0;
        for (int i = 0; i < MESH[0].length; i++) {
            for (int j = 0; j < MESH.length; j++) {
                if (MESH[j][i] == 1)
                    full++;
            }
            if (full == MESH.length)
                lines.add(i);
            //lines.add(i + lines.size());
            full = 0;
        }
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                            node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                            node.getUserData() == "waitc" || node.getUserData() == "waitd")//예외설정
                        continue;
                    if (node instanceof Text)
                        texts.add(node);
                }
                if (Frame > 150000000) {
                    Frame -= 50000000 * frameMultiplier;
                    scoreMultiplier++;
                }
                score += 50 * scoreMultiplier;
                linesNo++;

                for (Node node : texts) {
                    Text a = (Text) node;
                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        pane.getChildren().remove(node);
                    } else
                        newtexts.add(node);
                }

                for (Node node : newtexts) {
                    Text a = (Text) node;
                    if (a.getY() < lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        a.setY(a.getY() + SIZE);
                    }//try-catch삭제
                }
                lines.remove(0);
                texts.clear();
                newtexts.clear();
                for (Node node : pane.getChildren()) {
                    if (node instanceof Text)
                        texts.add(node);
                }
                for (Node node : texts) {
                    Text a = (Text) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                texts.clear();
            } while (lines.size() > 0);//size->0
    }

    private void MoveDown(Text text) {
        if (text.getY() + MOVE < YMAX)
            text.setY(text.getY() + MOVE);

    }

    private void MoveRight(Text text) {
        if (text.getX() + MOVE <= XMAX - SIZE)
            text.setX(text.getX() + MOVE);
    }

    private void MoveLeft(Text text) {
        if (text.getX() - MOVE >= 0)
            text.setX(text.getX() - MOVE);
    }

    private void MoveUp(Text text) {
        if (text.getY() - MOVE > 0)
            text.setY(text.getY() - MOVE);
    }

    private boolean MoveDown(Form form,int [][] MESH, Pane group, boolean isGroupOne) {
        boolean moved = false; // 이동 여부를 추적하는 변수입니다.
        if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form, MESH) || moveB(form, MESH) || moveC(form, MESH) || moveD(form, MESH)) {
            // 여기서는 블록이 다음 위치로 이동할 수 없으므로, 현재 위치를 고정하고 새로운 블록을 생성합니다.
            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            if (isGroupOne){
                RemoveRows(group1);
                Form a = controller.makeText(waitObj.getName(), true);
                waitObj = controller.waitingTextMake(true,difficultylevel, 200);
                object = a;
                group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                moveOnKeyPress(a,object2);
                // 이 경우에는 이동하지 않으므로 false
            }
            else{
                RemoveRows(group2);
                Form a = controller.makeText(waitObj2.getName(), true);
                waitObj2 = controller.waitingTextMake(true,difficultylevel, 200);
                object2 = a;
                group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
                moveOnKeyPress(object,a);
                // 이 경우에는 이동하지 않으므로 false
            }
        }
        if (form.a.getY() + MOVE < YMAX && form.b.getY() + MOVE < YMAX && form.c.getY() + MOVE < YMAX
                && form.d.getY() + MOVE < YMAX && !(moveA(form, MESH) || moveB(form, MESH) || moveC(form, MESH) || moveD(form, MESH))) {
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            moved = true; // 실제로 이동했으므로 true로 설정
            score += scoreMultiplier;
        }
        return moved; // 이동 여부를 반환
    }

    private void DirectMoveDown(Form form,Form form2, Pane group, boolean isGroupOne, int [][] MESH) {
        while (!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form, MESH) || moveB(form, MESH) || moveC(form, MESH) || moveD(form, MESH))){
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            // 실제로 이동했으므로 true로 설정
            score += scoreMultiplier;
            top = 0;
            //directmovedown 호출시 object 겹침 버그 방지용
        }
        MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
        MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
        MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
        MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
        if (isGroupOne){
            RemoveRows(group1);
            Form a = controller.makeText(waitObj.getName(), true);
            waitObj = controller.waitingTextMake(true,difficultylevel, 200);
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            moveOnKeyPress(a,form2);
        }
        else{
            RemoveRows(group2);
            Form a = controller.makeText(waitObj2.getName(), true);
            waitObj2 = controller.waitingTextMake(true,difficultylevel, 200);
            object2 = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
            moveOnKeyPress(form2,a);
        }
    }


    private boolean moveA(Form form, int [][] MESH) {
        return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) +1] == 1);
    }

    private boolean moveB(Form form, int [][] MESH) {
        return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) +1] == 1);
    }

    private boolean moveC(Form form, int [][] MESH) {
        return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) +1] == 1);
    }

    private boolean moveD(Form form, int [][] MESH) {
        return (MESH[(int) form.d.getX() / SIZE][((int) form.d.getY() / SIZE) +1] == 1);
    }

    private boolean cB(Text text, int x, int y) {
        boolean xb = false;
        boolean yb = false;
        if (x >= 0)
            xb = text.getX() + x * MOVE <= XMAX - SIZE;
        if (x < 0)
            xb = text.getX() + x * MOVE >= 0;
        if (y >= 0)
            yb = text.getY() - y * MOVE > 0;
        if (y < 0)
            yb = text.getY() + y * MOVE < YMAX;
        return xb && yb && MESH[((int) text.getX() / SIZE) + x][((int) text.getY() / SIZE) - y] == 0;
    }//Text로 변경


    public void stopAnimation() {
        running = false;
        applyGrayscaleEffect();
        for (Node node : group1.getChildren()) {
            if (node instanceof Button) {
                node.setVisible(true);
            }
        }
        bringButtonsToFront();
    }

    public void startAnimation() {
        running = true;

        // 게임 재시작 및 종료 버튼 숨김
        // 흑백 효과 해제
        clearGrayscaleEffect();
        for (Node node : group1.getChildren()) {
            if (node instanceof Button) {
                node.setVisible(false);
            }
        }
        bringButtonsToFront();
    }


    public void GameOver(){
        running = false;
        //User user = SessionManager.getCurrentUser();
        applyGrayscaleEffect();
        scoreLabel = new Label("score: " + score);
        scoreLabel.setLayoutX(XMAX/2 - 10);
        scoreLabel.setLayoutY(YMAX/2);
        scoreLabel.setStyle("-fx-font-size: XMAX/3; -fx-text-fill: red; -fx-background-color: blue;");
        group1.getChildren().addAll(scoreLabel);

        scoreLabel.setVisible(true);
        TextArea nicknameTextArea = new TextArea("dafd");
        nicknameTextArea.setLayoutX(XMAX / 2);
        nicknameTextArea.setLayoutY(YMAX / 3);
        nicknameTextArea.setPrefWidth(XMAX / 4);
        nicknameTextArea.setPrefHeight(XMAX / 10);
        //1 코드정리와 함께UI 정리
        //2. 스코어보드 띄워놓을 부분 냄겨주기
        //3. GamePause에서 끄는거
        //4.
        Label NameLabel = new Label("점수를 저장하시겠습니까?");
        NameLabel.setLayoutX(XMAX/2 - 10);
        NameLabel.setLayoutY(YMAX/2 - 40);
        NameLabel.setStyle("-fx-font-size: XMAX; -fx-text-fill: red; -fx-background-color: blue;");
        group1.getChildren().addAll(nicknameTextArea, NameLabel);

        NameLabel.setVisible(true);
        Button yesButton = new Button("Yes");
        yesButton.setLayoutX(XMAX / 2 - 50); //
        yesButton.setLayoutY(YMAX / 2 + 50); //
        exitButton.setLayoutX(XMAX/2+50);
        exitButton.setLayoutY(YMAX/2+50);
        if (exitButton != null)
            exitButton.toFront();
        exitButton.setVisible(true);
        /*yesButton.setOnAction(e -> {
            String newNickname = nicknameTextArea.getText();
            try {
                JdbcConnecter.insertData(user.getLoginId(), newNickname, score, 0, LevelConstants.getLevel(), linesNo);
                yesButton.setVisible(false);

                //여기서 Scoreboard 보여주기
            } catch (Exception ex) {
                System.out.println("jdbc error");
            }
        });*/
        group1.getChildren().addAll(yesButton);
    }
    private void GameStopped(Stage stage){
        timer.stop();
        stage.close();
    }
    public void applyGrayscaleEffect() {
        group1.setEffect(colorAdjust); // 전체 그룹에 흑백 효과 적용
    }

    public void clearGrayscaleEffect() {
        group1.setEffect(null); // 흑백 효과 해제
    }
    private void bringButtonsToFront() {
        if (restartButton != null) restartButton.toFront();
        if (exitButton != null) exitButton.toFront();
    }

    public void main(String[] args) {
        launch();
    }
}