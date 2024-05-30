
import ScoreBoard.JdbcConnecter;
import ScoreBoard.ScoreBoardWindow;
import Setting.LevelConstants;
import Setting.Settings;
import Setting.SizeConstants;
import Tetris.*;
import User.User;
import User.SessionManager;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;





public class ItemHelloApplication extends Application {

    public static String item = "";
    public static int itemRotate = 0;
    public static int itemCounter = 0;

    private static ItemForm object;

    private AnimationTimer timer;
    public boolean running;
    private static Pane group = new Pane();
    private static Scene scene;
    public int score;
    private static int top;
    private static boolean game = true;
    private ColorAdjust colorAdjust = new ColorAdjust(); //Color Adjust 조절
    private static char difficultylevel = LevelConstants.difficultyLevel;

    private static int linesNo = 0;
    private Button restartButton;
    private Button exitButton;
    private Button terminateButton;
    private double Frame = 1000000000;
    private static double scoreMultiplier = 1.0;
    private static double frameMultiplier = 0.8;
    private JdbcConnecter scoreboardDataInserter;
    private Text scoretext;
    //private final User user;
    private final int MOVE;
    private final int SIZE;
    private final int XMAX;
    private final int YMAX;

    private static ItemForm nextObj;//makeRect->makeText
    private static ItemForm waitObj;
    private final int[][] MESH;
    private final String rightKey;
    private final String leftKey;
    private final String spaceKey;
    private final String upKey;
    private final String downKey;
    private ItemController itemController;

    boolean WeightMove = true;
    int LineClearY = -1;

    //private JdbcConnecter scoreboardDataInserter;
    //private Text scoretext;
    public ItemHelloApplication(SizeConstants sizeConstants, Settings settings, ItemController itemController) {
        this.itemController = itemController;
        this.score = 0;
        this.running = true;
        this.rightKey = settings.getP1rightKey();
        this.leftKey = settings.getP1leftKey();
        this.upKey = settings.getP1upKey();
        this.downKey = settings.getP1downKey();
        this.spaceKey  = settings.getSpaceKey();
        this.MOVE = sizeConstants.getMOVE();
        this.SIZE = sizeConstants.getSIZE();
        this.XMAX = sizeConstants.getXMAX();
        this.YMAX = sizeConstants.getYMAX();
        this.MESH = sizeConstants.getMESH();
        this.waitObj = itemController.waitingTextMake(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);
        this.nextObj = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);
        this.group = new Pane();
        this.scene = new Scene(group, XMAX + 150, YMAX - SIZE);
        this.running = true;
        this.linesNo = 0;
    }

    @Override
    public void start(Stage stage) throws IOException {
        User currentUser = SessionManager.getCurrentUser();
        System.out.println(currentUser.getNickname());
        stage.close();
        Frame = 1000000000;
        running = true;
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


        group = new Pane();
        scene = new Scene(group, XMAX + 150, YMAX - SIZE);//Mesh 시점 맞추기 임시 y 에 - size
        running = true;


        group.getChildren().clear();

        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }
        drawGridLines();
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        scoretext = new Text("SCORE: ");
        scoretext.setUserData("scoretext");
        scoretext.setFill(Color.WHITE);
        scoretext.setStyle("-fx-font: 20  Lato;");
        scoretext.setY(50);
        scoretext.setX(XMAX + 30);
        scoretext.setY(300);
        Text level = new Text("LINES: ");//scoretext,level userdata추가
        level.setUserData("level");
        level.setStyle("-fx-font: 20 Lato;");
        level.setY(350);
        level.setX(XMAX + 30);
        level.setFill(Color.GREEN);
        ItemForm wait = waitObj;

        group.getChildren().addAll(scoretext, line, level, wait.a, wait.b, wait.c, wait.d);
        group.setStyle("-fx-background-color: black;");
        ItemForm a = nextObj;
        group.getChildren().addAll(a.a, a.b, a.c, a.d);
        moveOnKeyPress(a);
        object = a;
        nextObj = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);
        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();


        // 흑백 효과 초기 설정
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(-0.3);

        // 게임 재시작 및 종료 버튼 추가
        restartButton = new Button("게임 재시작");
        restartButton.setLayoutX(XMAX/2);
        restartButton.setLayoutY(YMAX/2);
        restartButton.setVisible(false); // 초기에는 보이지 않게 설정

        exitButton = new Button("메뉴화면");
        exitButton.setLayoutX(XMAX/2);
        exitButton.setLayoutY(YMAX/2+30);
        exitButton.setVisible(false); // 초기에는 보이지 않게 설정

        terminateButton = new Button("게임 나가기");
        terminateButton.setLayoutX(XMAX / 2);
        terminateButton.setLayoutY(YMAX / 2 + 60);
        terminateButton.setVisible(false);


        // 버튼 이벤트 핸들러 설정
        restartButton.setOnAction(e -> {startAnimation();});
        exitButton.setOnAction(e -> GameStopped(stage));
        terminateButton.setOnAction(e->System.exit(0));

        // 그룹에 버튼 추가
        group.getChildren().addAll(restartButton, exitButton,terminateButton);
        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {

                if (running) {
                    if (now - lastUpdate >= Frame) { // 1초마다 실행

                        stage.setOnCloseRequest(event -> {
                            timer.stop();
                            group.getChildren().clear();
                        });
                        lastUpdate = now;

                        if (object.a.getY() == 0 || object.b.getY() == 0 || object.c.getY() == 0 || object.d.getY() == 0)
                            top++;
                        else
                            top = 0;

                        if (top == 2) {
                            GameOver();

                        }
                        // Exit
                        if (top == 15) {
                            GameOver();
                            stage.close();
                        }

                        if (game) {
                            MoveDown(object);
                            scoretext.setText("Score: " + score);
                            level.setText("Lines: " + linesNo);
                        }

                    }
                }
            }
        };
        timer.start();
    }



    private void drawGridLines() {
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

    private void moveOnKeyPress(ItemForm form) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String pressedKey = event.getCode().toString();
                if(running) {
                    if (pressedKey.equals(rightKey)) {
                        if (form.getItem() == "Inverse") {
                            itemController.MoveLeft(form);
                        } else {
                            itemController.MoveRight(form);
                        }
                    } else if (pressedKey.equals(downKey)){
                        if(!(form.getItem()=="Weight"&&!(WeightMove))){
                            MoveDown(form);
                            scoretext.setText("Score: " + score);
                        }
                    } else if (pressedKey.equals(leftKey)) {
                        if (form.getItem() == "Inverse") {
                            itemController.MoveRight(form);
                        } else {
                            itemController.MoveLeft(form);
                        }
                    } else if (pressedKey.equals(upKey)) {
                        if(!(form.getItem()=="Weight"))
                            MoveTurn(form);
                    } else if (pressedKey.equals(spaceKey)) {
                        if (form.getItem() == "Fixed") {
                            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
                            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
                            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
                            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
                            RemoveRows(group);
                            // 새 블록 생성
                            ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
                            group.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                            waitObj = itemController.waitingTextMake(true, difficultylevel, item, itemRotate,XMAX);
                            object = a;
                            group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                            moveOnKeyPress(a);
                            item = "";
                            itemRotate = 0;
                        } else {
                            DirectMoveDown(form);
                        }
                    } else if (pressedKey.equals("ESCAPE")) {
                        stopAnimation();
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


    private void MoveTurn(ItemForm form) {
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
                if(i == LineClearY)
                    full = MESH.length;
            }
            if (full == MESH.length) {
                lines.add(i);
            }
            //lines.add(i + lines.size());
            full = 0;

        }
        LineClearY = -1;
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
                if (Frame > 200000000) {
                    Frame -= 50000000 * frameMultiplier;
                    scoreMultiplier++;
                }
                score += 50 * scoreMultiplier;
                linesNo++;
                itemCounter++;

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
    private void WeightRemoveRows(Pane pane,ItemForm form) {
        ArrayList<Node> texts = new ArrayList<Node>();
        for (Node node : pane.getChildren()) {
            if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                    node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                    node.getUserData() == "waitc" || node.getUserData() == "waitd")//예외설정
                continue;
            if (node instanceof Text)
                texts.add(node);
        }
        for (Node node : texts) {
            Text a = (Text) node;
            if ((a.getY() == form.a.getY()+SIZE&&a.getX()==form.a.getX())||
                    (a.getY() == form.b.getY()+SIZE&&a.getX()==form.b.getX())||
                    (a.getY() == form.c.getY()+SIZE&&a.getX()==form.c.getX())||
                    (a.getY() == form.d.getY()+SIZE&&a.getX()==form.d.getX())) {
                //MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                pane.getChildren().remove(node);
                WeightMove = false;
            }
        }
    }

    private void BombRemoveRows(Pane pane,ItemForm form) {
        ArrayList<Node> texts = new ArrayList<Node>();
        for (Node node : pane.getChildren()) {
            if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                    node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                    node.getUserData() == "waitc" || node.getUserData() == "waitd")//예외설정
                continue;
            if (node instanceof Text)
                texts.add(node);
        }
        for (Node node : texts) {
            Text a = (Text) node;
            if(form.getItemRotate() == 1) {
                if ((a.getY() == form.a.getY() - SIZE && a.getX() == form.a.getX() - SIZE) ||
                        (a.getY() == form.a.getY() - SIZE && a.getX() == form.a.getX()) ||
                        (a.getY() == form.a.getY() - SIZE && a.getX() == form.a.getX() + SIZE) ||
                        (a.getY() == form.a.getY() && a.getX() == form.a.getX() - SIZE)||
                        (a.getY() == form.a.getY() && a.getX() == form.a.getX())||
                        (a.getY() == form.a.getY() && a.getX() == form.a.getX() + SIZE)||
                        (a.getY() == form.a.getY() + SIZE && a.getX() == form.a.getX() - SIZE)||
                        (a.getY() == form.a.getY() + SIZE && a.getX() == form.a.getX())||
                        (a.getY() == form.a.getY() + SIZE && a.getX() == form.a.getX() + SIZE)) {
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                    pane.getChildren().remove(node);
                }
            }
            if(form.getItemRotate() == 2) {
                if ((a.getY() == form.b.getY() - SIZE && a.getX() == form.b.getX() - SIZE) ||
                        (a.getY() == form.b.getY() - SIZE && a.getX() == form.b.getX()) ||
                        (a.getY() == form.b.getY() - SIZE && a.getX() == form.b.getX() + SIZE) ||
                        (a.getY() == form.b.getY() && a.getX() == form.b.getX() - SIZE)||
                        (a.getY() == form.b.getY() && a.getX() == form.b.getX())||
                        (a.getY() == form.b.getY() && a.getX() == form.b.getX() + SIZE)||
                        (a.getY() == form.b.getY() + SIZE && a.getX() == form.b.getX() - SIZE)||
                        (a.getY() == form.b.getY() + SIZE && a.getX() == form.b.getX())||
                        (a.getY() == form.b.getY() + SIZE && a.getX() == form.b.getX() + SIZE)) {
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                    pane.getChildren().remove(node);
                }
            }
            if(form.getItemRotate() == 3) {
                if ((a.getY() == form.c.getY() - SIZE && a.getX() == form.c.getX() - SIZE) ||
                        (a.getY() == form.c.getY() - SIZE && a.getX() == form.c.getX()) ||
                        (a.getY() == form.c.getY() - SIZE && a.getX() == form.c.getX() + SIZE) ||
                        (a.getY() == form.c.getY() && a.getX() == form.c.getX() - SIZE)||
                        (a.getY() == form.c.getY() && a.getX() == form.c.getX())||
                        (a.getY() == form.c.getY() && a.getX() == form.c.getX() + SIZE)||
                        (a.getY() == form.c.getY() + SIZE && a.getX() == form.c.getX() - SIZE)||
                        (a.getY() == form.c.getY() + SIZE && a.getX() == form.c.getX())||
                        (a.getY() == form.c.getY() + SIZE && a.getX() == form.c.getX() + SIZE)) {
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                    pane.getChildren().remove(node);
                }
            }
            if(form.getItemRotate() == 4) {
                if ((a.getY() == form.d.getY() - SIZE && a.getX() == form.d.getX() - SIZE) ||
                        (a.getY() == form.d.getY() - SIZE && a.getX() == form.d.getX()) ||
                        (a.getY() == form.d.getY() - SIZE && a.getX() == form.d.getX() + SIZE) ||
                        (a.getY() == form.d.getY() && a.getX() == form.d.getX() - SIZE)||
                        (a.getY() == form.d.getY() && a.getX() == form.d.getX())||
                        (a.getY() == form.d.getY() && a.getX() == form.d.getX() + SIZE)||
                        (a.getY() == form.d.getY() + SIZE && a.getX() == form.d.getX() - SIZE)||
                        (a.getY() == form.d.getY() + SIZE && a.getX() == form.d.getX())||
                        (a.getY() == form.d.getY() + SIZE && a.getX() == form.d.getX() + SIZE)) {
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                    pane.getChildren().remove(node);
                }
            }
        }
    }

    private void MoveDown(Text text) {
        scoretext.setText("Score: " + score);
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
    }//move명령어들 Text로 변경함

    /*private boolean MoveDown(ItemForm form) {
        boolean moved = false; // 이동 여부를 추적하는 변수입니다.
        if(form.getItem()=="Weight"){
            if(!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                    || form.d.getY() == YMAX - SIZE)){
                MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE + 1] = 0;
                MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE + 1] = 0;
                MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE + 1] = 0;
                MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE + 1] = 0;
                WeightRemoveRows(group,form);
            }
        }
        if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
            // 여기서는 블록이 다음 위치로 이동할 수 없으므로, 현재 위치를 고정하고 새로운 블록을 생성합니다.
            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            RemoveRows(group);
            WeightMove = true;
            if(form.getItem()=="LineClear"){
                switch (form.getItemRotate()){
                    case 1: LineClearY = (int)form.a.getY()/SIZE;break;
                    case 2: LineClearY = (int)form.b.getY()/SIZE;break;
                    case 3: LineClearY = (int)form.c.getY()/SIZE;break;
                    case 4: LineClearY = (int)form.d.getY()/SIZE;break;
                }
            }if(form.getItem()=="Bomb"){
                BombRemoveRows(group,form);
            }
            // 새 블록 생성
            ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
            group.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            if(itemCounter>=10){
                int itemNumber = (int)(Math.random()*100);
                int itemRotateNumber = (int)(Math.random()*100);
                if(itemNumber<20)
                    item="LineClear";
                else if(itemNumber<40)
                    item="Weight";
                else if(itemNumber<60)
                    item="Inverse";
                else if(itemNumber<80)
                    item = "Bomb";
                else
                    item = "Fixed";
                if(itemRotateNumber<25)
                    itemRotate = 1;
                else if(itemRotateNumber<50)
                    itemRotate = 2;
                else if(itemRotateNumber<75)
                    itemRotate = 3;
                else
                    itemRotate = 4;
                itemCounter = 0;
            }
            waitObj = itemController.waitingTextMake(true,difficultylevel,item,itemRotate,XMAX);
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            moveOnKeyPress(a);
            moved = false; // 이 경우에는 이동하지 않으므로 false
            item = "";
            itemRotate = 0;
        }
        if (form.a.getY() + MOVE < YMAX && form.b.getY() + MOVE < YMAX && form.c.getY() + MOVE < YMAX
                && form.d.getY() + MOVE < YMAX && !(moveA(form) || moveB(form) || moveC(form) || moveD(form))) {
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            moved = true; // 실제로 이동했으므로 true로 설정
            score += scoreMultiplier;
        }



        return moved; // 이동 여부를 반환
    }*/
    private boolean MoveDown(ItemForm form) {
        boolean moved = false; // 이동 여부를 추적하는 변수입니다.
        if (form.getItem() == "Weight") {
            if (!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                    || form.d.getY() == YMAX - SIZE)) {
                MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE + 1] = 0;
                MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE + 1] = 0;
                MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE + 1] = 0;
                MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE + 1] = 0;
                WeightRemoveRows(group, form);
            }
        }
        if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form)) {
            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            RemoveRows(group);
            WeightMove = true;
            if (form.getItem() == "LineClear") {
                switch (form.getItemRotate()) {
                    case 1: LineClearY = (int) form.a.getY() / SIZE; break;
                    case 2: LineClearY = (int) form.b.getY() / SIZE; break;
                    case 3: LineClearY = (int) form.c.getY() / SIZE; break;
                    case 4: LineClearY = (int) form.d.getY() / SIZE; break;
                }
                RemoveRows(group);  // 즉시 행 삭제
            }
            if (form.getItem() == "Bomb") {
                BombRemoveRows(group, form);
            }
            ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
            group.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            if (itemCounter >= 10) {
                int itemNumber = (int) (Math.random() * 100);
                int itemRotateNumber = (int) (Math.random() * 100);
                if (itemNumber < 20)
                    item = "LineClear";
                else if (itemNumber < 40)
                    item = "Weight";
                else if (itemNumber < 60)
                    item = "Inverse";
                else if (itemNumber < 80)
                    item = "Bomb";
                else
                    item = "Fixed";
                if (itemRotateNumber < 25)
                    itemRotate = 1;
                else if (itemRotateNumber < 50)
                    itemRotate = 2;
                else if (itemRotateNumber < 75)
                    itemRotate = 3;
                else
                    itemRotate = 4;
                itemCounter = 0;
            }
            waitObj = itemController.waitingTextMake(true, difficultylevel, item, itemRotate, XMAX);
            object = a;
            group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            moveOnKeyPress(a);
            moved = false; // 이 경우에는 이동하지 않으므로 false
            item = "";
            itemRotate = 0;
        }
        if (form.a.getY() + MOVE < YMAX && form.b.getY() + MOVE < YMAX && form.c.getY() + MOVE < YMAX
                && form.d.getY() + MOVE < YMAX && !(moveA(form) || moveB(form) || moveC(form) || moveD(form))) {
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            moved = true; // 실제로 이동했으므로 true로 설정
            score += scoreMultiplier;
        }

        return moved; // 이동 여부를 반환
    }

    private void DirectMoveDown(ItemForm form) {
        while (!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form) || moveB(form) || moveC(form) || moveD(form))) {
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            // 실제로 이동했으므로 true로 설정
            score += scoreMultiplier;
            top = 0; // directmovedown 호출시 object 겹침 버그 방지용
        }
        MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
        MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
        MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
        MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
        RemoveRows(group);
        if (form.getItem() == "LineClear") {
            switch (form.getItemRotate()) {
                case 1: LineClearY = (int) form.a.getY() / SIZE; break;
                case 2: LineClearY = (int) form.b.getY() / SIZE; break;
                case 3: LineClearY = (int) form.c.getY() / SIZE; break;
                case 4: LineClearY = (int) form.d.getY() / SIZE; break;
            }
            RemoveRows(group);  // 즉시 행 삭제
        }
        if (form.getItem() == "Bomb") {
            BombRemoveRows(group, form);
        }
        ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
        group.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
        if (itemCounter >= 10) {
            int itemNumber = (int) (Math.random() * 100);
            int itemRotateNumber = (int) (Math.random() * 100);
            if (itemNumber < 20)
                item = "LineClear";
            else if (itemNumber < 40)
                item = "Weight";
            else if (itemNumber < 60)
                item = "Inverse";
            else if (itemNumber < 80)
                item = "Bomb";
            else
                item = "Fixed";
            if (itemRotateNumber < 25)
                itemRotate = 1;
            else if (itemRotateNumber < 50)
                itemRotate = 2;
            else if (itemRotateNumber < 75)
                itemRotate = 3;
            else
                itemRotate = 4;
            itemCounter = 0;
        }
        waitObj = itemController.waitingTextMake(true, difficultylevel, item, itemRotate, XMAX);
        object = a;
        group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
        moveOnKeyPress(a);
        item = "";
        itemRotate = 0;
    }


    private boolean moveA(ItemForm form) {
        return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) +1] == 1);
    }

    private boolean moveB(ItemForm form) {
        return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) +1] == 1);
    }

    private boolean moveC(ItemForm form) {
        return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) +1] == 1);
    }

    private boolean moveD(ItemForm form) {
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
        for (Node node : group.getChildren()) {
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
        for (Node node : group.getChildren()) {
            if (node instanceof Button) {
                node.setVisible(false);
            }
        }
        bringButtonsToFront();
    }


    public void GameOver(){
        running = false;
        User user = SessionManager.getCurrentUser();
        applyGrayscaleEffect();
        Label scoreLabel = new Label("score: " + score);
        scoreLabel.setLayoutX(XMAX/2 - 10);
        scoreLabel.setLayoutY(YMAX/2);
        scoreLabel.setStyle("-fx-font-size: XMAX/3; -fx-text-fill: red; -fx-background-color: blue;");
        group.getChildren().addAll(scoreLabel);

        scoreLabel.setVisible(true);
        TextArea nicknameTextArea = new TextArea(user.getNickname());
        nicknameTextArea.setLayoutX(XMAX / 2);
        nicknameTextArea.setLayoutY(YMAX / 3);
        nicknameTextArea.setPrefWidth(XMAX / 4);
        nicknameTextArea.setPrefHeight(XMAX / 10);

        Label NameLabel = new Label("점수를 저장하시겠습니까?");
        NameLabel.setLayoutX(XMAX/2 - 10);
        NameLabel.setLayoutY(YMAX/2 - 40);
        NameLabel.setStyle("-fx-font-size: XMAX; -fx-text-fill: red; -fx-background-color: blue;");
        group.getChildren().addAll(nicknameTextArea, NameLabel);

        NameLabel.setVisible(true);

        Button yesButton = new Button("Yes");
        yesButton.setLayoutX(XMAX / 2 - 50); //
        yesButton.setLayoutY(YMAX / 2 + 50); //
        exitButton.setLayoutX(XMAX/2+50);
        exitButton.setLayoutY(YMAX/2+50);
        if (exitButton != null)
            exitButton.toFront();
        exitButton.setVisible(true);
        String newNickname = nicknameTextArea.getText();
        Date date = new Date();
        long now = date.getTime();
        yesButton.setOnAction(e -> {
            try {
                JdbcConnecter.insertData(user.getLoginId(), newNickname, score, 1, LevelConstants.getLevel(), linesNo, now);
                yesButton.setVisible(false);
                System.out.println(newNickname);
                int pageNo = JdbcConnecter.fetchPageOfUser(newNickname, now);
                System.out.println("PAGENO: "+pageNo);
                ScoreBoardWindow window = new ScoreBoardWindow(pageNo, newNickname,now);
                window.show();
            } catch (Exception ex) {
                System.out.println("jdbc error");
            }
        });
        group.getChildren().addAll(yesButton);

    }
    private void GameStopped(Stage stage){
        timer.stop();
        stage.close();
    }
    public void applyGrayscaleEffect() {
        group.setEffect(colorAdjust); // 전체 그룹에 흑백 효과 적용
    }

    public void clearGrayscaleEffect() {
        group.setEffect(null); // 흑백 효과 해제
    }
    private void bringButtonsToFront() {
        if (restartButton != null) restartButton.toFront();
        if (exitButton != null) exitButton.toFront();
        if (terminateButton != null) terminateButton.toFront();
    }



    public void main(String[] args) {
        launch();
    }
}