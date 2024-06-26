import Animation.Flash;
import Animation.ScoreBoard.JdbcConnecter;
import Setting.LevelConstants;
import Setting.Settings;
import Setting.SizeConstants;
import Tetris.*;
import Tetris.ItemForm;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import static Tetris.ItemController.currentTextSetUserData;


public class ItemBattleApplication extends Application {
    private HBox hbox;
    private AnimationTimer timer;
    public static String item = "";
    public static String item2 = "";
    public static int itemRotate = 0;
    public static int itemRotate2 = 0;
    public static int itemCounter = 0;
    public static int itemCounter2 = 0;
    private Text scoretext1, scoretext2;
    private Text linetext1,linetext2;
    public boolean running;
    private static ItemForm object;
    private static ItemForm object2;
    private static Pane group1 = new Pane();
    private static Pane group2 = new Pane();
    private static Scene scene;
    private static int top;
    private static int top2;
    private static boolean game = true;
    private static char difficultylevel = LevelConstants.difficultyLevel;
    private ItemForm nextObj, nextObj2;
    private ItemForm waitObj, waitObj2;
    private final int[][] MESH, MESH2;
    private final int[][] miniMESH, miniMESH2;
    private static int linesNo = 0;
    private int linesNo2 = 0;
    private Button restartButton;
    private Button exitButton;
    private Button terminateButton;
    private static double scoreMultiplier = 1.0;
    private static double scoreMultiplier2 = 1.0;
    private static double frameMultiplier = 0.8;
    private JdbcConnecter scoreboardDataInserter;
    private static int winner = 0;

    private static int miniMeshLineCounter = 0;
    private static int miniMeshLineCounter2 = 0;

    private static int meshTop = 0;
    private static int meshTop2 = 0;
    private static double fontSize;

    private final ItemController itemController;
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

    boolean WeightMove = true;

    int LineClearY = -1;
    int LineClearY2 = -1;

    private int score, score2;
    private double Frame;
    public ItemBattleApplication(SizeConstants sizeConstants, Settings settings, ItemController itemController){
        this.itemController = itemController;
        this.score = 0;
        this.score2 = 0;
        this.running = true;
        this.wKey = settings.getP2upKey();
        this.aKey = settings.getP2leftKey();
        this.sKey = settings.getP2downKey();
        this.dKey = settings.getP2rightKey();
        this.shiftKey = settings.getShiftKey();
        this.rightKey = settings.getP1rightKey();
        this.leftKey = settings.getP1leftKey();
        this.upKey = settings.getP1upKey();
        this.downKey = settings.getP1downKey();
        this.spaceKey  = settings.getSpaceKey();


        this.MOVE = sizeConstants.getMOVE();
        this.SIZE = sizeConstants.getSIZE();
        this.XMAX = sizeConstants.getXMAX();
        this.YMAX = sizeConstants.getYMAX();


        this.waitObj = itemController.waitingTextMake(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);
        this.nextObj = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);

        this.waitObj2 = itemController.waitingTextMake(BlockColor.colorBlindMode, difficultylevel, item2, itemRotate2, this.XMAX);
        this.nextObj2 = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item2, itemRotate2, this.XMAX);


        this.fontSize = sizeConstants.getFontSize();
        this.MESH = new int[10][21];
        this.MESH2 = new int[10][21];
        this.miniMESH = new int[10][11];
        this.miniMESH2 = new int[10][11];
        this.meshTop = 0;
        this.meshTop2 = 0;
        this.winner = 0;

        this.running = true;

        this.linesNo = 0;
        this.linesNo2 = 0;
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

    public Text styleLineText(int Pos){
        Text level = new Text("LINES: ");//scoretext,level userdata추가
        level.setUserData("level");
        level.setStyle("-fx-font: 20 Lato;");
        level.setY(350);
        level.setX(XMAX + 30);
        level.setFill(Color.GREEN);
        return level;
    }
    @Override
    public void start(Stage stage) throws IOException {
        stage.close();
        if(LevelConstants.getLevel()=='E'){
            frameMultiplier = 0.8;
            scoreMultiplier = 1.0;
            scoreMultiplier2 = 1.0;
        }
        else if(LevelConstants.getLevel()=='N'){
            frameMultiplier = 1.0;
            scoreMultiplier = 1.2;
            scoreMultiplier2 = 1.2;
        }
        else{
            frameMultiplier = 1.2;
            scoreMultiplier = 1.4;
            scoreMultiplier2 = 1.4;
        }


        group1 = new Pane();
        group2 = new Pane();
        hbox = new HBox(2);
        hbox.setStyle("-fx-background-color: black;"); // CSS를 사용하여 배경색 설정
        hbox.getChildren().addAll(group2,group1);
        scene = new Scene(hbox,XMAX+XMAX+XMAX , YMAX - SIZE);
        running = true;
        group1.getChildren().clear();
        group2.getChildren().clear();

        for (int[] a : MESH) {
            Arrays.fill(a, 0);
        }
        drawGridLines(group1);
        drawGridLines(group2);
        Line line = new Line(XMAX, 0, XMAX, YMAX);
        Line margin = new Line(0,100,XMAX+XMAX*0.5,100);

        scoretext1 = styleScoretext(XMAX+30);
        scoretext2 = styleScoretext(XMAX+30);

        linetext1 = styleLineText(XMAX+30);
        linetext2 = styleLineText(XMAX+30);

        ItemForm wait = waitObj;
        ItemForm wait2 = waitObj2;
        group1.getChildren().addAll(scoretext1, line,margin, linetext1, wait.a, wait.b, wait.c, wait.d);
        group1.setStyle("-fx-background-color: black;");
        group2.getChildren().addAll(scoretext2, line,margin, linetext2, wait2.a, wait2.b, wait2.c, wait2.d);
        group2.setStyle("-fx-background-color: black;");
        ItemForm a = nextObj;
        ItemForm b = nextObj2;
        group1.getChildren().addAll(a.a, a.b, a.c, a.d);
        group2.getChildren().addAll(b.a, b.b, b.c, b.d);

        moveOnKeyPress(a,b);
        object = a;
        object2 = b;


        nextObj = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item, itemRotate, this.XMAX);
        nextObj2 = itemController.makeText(BlockColor.colorBlindMode, difficultylevel, item2, itemRotate2, this.XMAX);
        currentTextSetUserData(a);
        currentTextSetUserData(b);

        restartButton = new Button("게임 재시작");
        restartButton.setLayoutX(0);
        restartButton.setLayoutY(YMAX/2);
        restartButton.setVisible(false); // 초기에는 보이지 않게 설정

        exitButton = new Button("메뉴화면");
        exitButton.setLayoutX(0);
        exitButton.setLayoutY(YMAX/2+30);
        exitButton.setVisible(false); // 초기에는 보이지 않게 설정

        terminateButton = new Button("게임 나가기");
        terminateButton.setLayoutX(0);
        terminateButton.setLayoutY(YMAX / 2 + 60);
        terminateButton.setVisible(false);


        // 버튼 이벤트 핸들러 설정
        restartButton.setOnAction(e -> {startAnimation(hbox);});
        exitButton.setOnAction(e -> GameStopped(stage));
        terminateButton.setOnAction(e->System.exit(0));

        group1.getChildren().addAll(restartButton,exitButton, terminateButton);

        stage.setScene(scene);
        stage.setTitle("T E T R I S");
        stage.show();

        timer = new AnimationTimer() {
            private long lastUpdate = 0;

            @Override
            public void handle(long now) {
                if (running) {
                    if (now - lastUpdate >= Frame) { // 1초마다 실행
                        group1.getChildren().removeIf(node -> node.getUserData() == "effectText");//ㄴ임시로 넣어둠 이펙트텍스트 지우기
                        group2.getChildren().removeIf(node -> node.getUserData() == "effectText");//ㄴ임시로 넣어둠 이펙트텍스트 지우기
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
                        if (object2.a.getY() == 0 || object2.b.getY() == 0 || object2.c.getY() == 0 || object2.d.getY() == 0)
                            top2++;
                        else
                            top2 = 0;

                        if (top == 2 || top2 == 2) {
                            if(top==2){
                                winner = 1;
                            }else if(top2 == 2){
                                winner = 2;
                            }
                            running = false;
                            GameOver();
                        }
                        // Exit
                        if (top == 15 || top2==15) {
                            running = false;
                            GameOver();
                            stage.close();
                        }

                        if (game) {
                            MoveDown(object, MESH, group1, true);
                            MoveDown(object2, MESH2, group2, false);
                            scoretext1.setText("Score: " + score);
                            scoretext2 .setText("Score: " + score2);
                            linetext1.setText("Lines: " + linesNo);
                            linetext2.setText("Lines: " +linesNo2);
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

    private void moveOnKeyPress(ItemForm form, ItemForm form2) {
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                String pressedKey = event.getCode().toString();
                if(running) {
                    if (pressedKey.equals(rightKey)) {
                        if (form.getItem() == "Inverse") {
                            itemController.MoveLeft(form,MESH);
                        } else {
                            itemController.MoveRight(form,MESH);
                        }
                    } else if (pressedKey.equals(downKey)) {
                        if(!(form.getItem()=="Weight"&&!(WeightMove))){
                            MoveDown(form, MESH, group1, true);
                            scoretext1.setText("Score: " + score);
                        }
                        scoretext1.setText("Score: " + score);
                    } else if (pressedKey.equals(leftKey)) {
                        if (form.getItem() == "Inverse") {
                            itemController.MoveRight(form,MESH);
                        } else {
                            itemController.MoveLeft(form,MESH);
                        }
                    } else if (pressedKey.equals(upKey)) {
                        if(!(form.getItem()=="Weight"))
                            MoveTurn(form,false);
                    } else if (pressedKey.equals(spaceKey)) {
                        if (form.getItem() == "Fixed") {
                            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
                            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
                            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
                            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
                            RemoveRows(group1,MESH,0);
                            // 새 블록 생성
                            ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
                            group1.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                            waitObj = itemController.waitingTextMake(true, difficultylevel, item, itemRotate,XMAX);
                            object = a;
                            currentTextSetUserData(a);
                            group1.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                            moveOnKeyPress(a,object2);
                            item = "";
                            itemRotate = 0;
                        } else {
                            DirectMoveDown(form,form2,group1,true,MESH);
                        }
                        scoretext1.setText("Score: " + score);
                    } else if (pressedKey.equals("ESCAPE")) {
                        stopAnimation(hbox);
                    } else if (pressedKey.equals(dKey)) {
                        if (form2.getItem() == "Inverse") {
                            itemController.MoveLeft(form2,MESH2);
                        } else {
                            itemController.MoveRight(form2,MESH2);
                        }
                    } else if (pressedKey.equals(sKey)) {
                        if(!(form2.getItem()=="Weight"&&!(WeightMove))){
                            MoveDown(form2, MESH2, group2, false);
                            scoretext2.setText("Score: " + score);
                        }
                        scoretext2.setText("Score: " + score);
                    } else if (pressedKey.equals(aKey)) {
                        if (form2.getItem() == "Inverse") {
                            itemController.MoveRight(form2,MESH2);
                        } else {
                            itemController.MoveLeft(form2,MESH2);
                        }
                    } else if (pressedKey.equals(wKey)) {
                        if(!(form2.getItem()=="Weight"))
                            MoveTurn(form2,true);
                    } else if (pressedKey.equals(shiftKey)) {
                        if (form2.getItem() == "Fixed") {
                            MESH2[(int) form2.a.getX() / SIZE][(int) form2.a.getY() / SIZE] = 1;
                            MESH2[(int) form2.b.getX() / SIZE][(int) form2.b.getY() / SIZE] = 1;
                            MESH2[(int) form2.c.getX() / SIZE][(int) form2.c.getY() / SIZE] = 1;
                            MESH2[(int) form2.d.getX() / SIZE][(int) form2.d.getY() / SIZE] = 1;
                            RemoveRows(group2,MESH2,1);
                            // 새 블록 생성
                            ItemForm a = itemController.makeText(waitObj2.getName(), true, waitObj2.getItem(), waitObj2.getItemRotate());
                            group2.getChildren().removeAll(waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
                            waitObj2 = itemController.waitingTextMake(true, difficultylevel, item2, itemRotate2,XMAX);
                            object2 = a;
                            currentTextSetUserData(a);
                            group2.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
                            moveOnKeyPress(object,a);
                            item2 = "";
                            itemRotate2 = 0;
                        } else {
                            DirectMoveDown(form2,form,group2,false,MESH2);
                        }
                        scoretext2.setText("Score: " + score);
                    }
                }
                else{
                    if(event.getCode() == KeyCode.ESCAPE) {
                        startAnimation(hbox);
                    }
                }
            }
        });
    }


    private void MoveTurn(ItemForm form,boolean player) {
        int f = form.form;
        Text a = form.a;
        Text b = form.b;
        Text c = form.c;
        Text d = form.d;//Rectangle - >Text
        if(!player) {
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
                    if (f == 2) {
                        if (cB(a, -1, -1) && cB(c, -1, 1) && cB(d, -2, 2)) {
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
                        if (cB(a, 1, -1) && cB(c, 1, 1) && cB(b, 2, 0) && cB(d, 0, 2)) {
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
                    if (f == 4) {
                        if (cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 2, -2)) {
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
                        if (cB(a, -1, 1) && cB(b, -2, 0) && cB(c, -1, -1) && cB(d, 0, -2)) {
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
                        if (cB(a, 0, -1) && cB(c, 0, 1) && cB(b, 1, 2) && cB(d, -1, 0)) {
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
                        if (cB(a, -1, -1) && cB(b, 2, -2) && cB(c, 1, -1)) {
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
                        if (cB(a, -2, -1) && cB(b, 1, -2) && cB(c, 0, -1) && cB(d, -1, 0)) {
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
                    if (f == 3) {
                        if (cB(a, -1, 1) && cB(c, -1, -1) && cB(b, -2, -2)) {
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
                        if (cB(a, 0, 1) && cB(c, 0, -1) && cB(b, -1, -2) && cB(d, 1, 0)) {
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
                    if (f == 4) {
                        if (cB(a, 1, 1) && cB(b, -2, 2) && cB(c, -1, 1)) {
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
                        if (cB(a, 2, 1) && cB(b, -1, 2) && cB(c, 0, 1) && cB(d, 1, 0)) {
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
                    if (f == 2) {
                        if (cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                            MoveUp(form.a);
                            MoveRight(form.a);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveDown(form.d);
                            MoveDown(form.d);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, 0, 1) && cB(b, -1, 0) && cB(c, 0, -1) && cB(d, -1, -2)) {
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
                    if (f == 4) {
                        if (cB(a, 1, 1) && cB(c, 1, -1) && cB(d, 0, -2)) {
                            MoveUp(form.a);
                            MoveRight(form.a);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveDown(form.d);
                            MoveDown(form.d);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, 0, 1) && cB(b, -1, 0) && cB(c, 0, -1) && cB(d, -1, -2)) {
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
                    if (f == 2) {
                        if (cB(a, 1, -1) && cB(d, -1, 1) && cB(c, 1, 1)) {
                            MoveRight(form.a);
                            MoveDown(form.a);
                            MoveLeft(form.d);
                            MoveUp(form.d);
                            MoveUp(form.c);
                            MoveRight(form.c);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, 0, -1) && cB(b, -1, 0) && cB(d, -2, 1) && cB(c, 0, 1)) {
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
                    if (f == 4) {
                        if (cB(a, -1, 1) && cB(d, 1, -1) && cB(c, -1, -1)) {
                            MoveLeft(form.a);
                            MoveUp(form.a);
                            MoveRight(form.d);
                            MoveDown(form.d);
                            MoveDown(form.c);
                            MoveLeft(form.c);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, 0, 1) && cB(b, 1, 0) && cB(d, 2, -1) && cB(c, 0, -1)) {
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
                    if (f == 2) {
                        if (cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                            MoveDown(form.b);
                            MoveLeft(form.b);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveRight(form.d);
                            MoveRight(form.d);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, -1, 0) && cB(b, -2, -1) && cB(c, 0, -1) && cB(d, 1, 0)) {
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
                    if (f == 4) {
                        if (cB(b, -1, -1) && cB(c, 1, -1) && cB(d, 2, 0)) {
                            MoveDown(form.b);
                            MoveLeft(form.b);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveRight(form.d);
                            MoveRight(form.d);
                            form.changeForm();
                            break;
                        }
                        if (cB(a, -1, 0) && cB(b, -2, -1) && cB(c, 0, -1) && cB(d, 1, 0)) {
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
                    if (f == 2) {
                        if (cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
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
                        if (cB(a, -3, -2) && cB(b, -2, -1) && cB(c, -1, 0) && cB(d, 0, 1)) {
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
                        if (cB(a, 0, -2) && cB(b, 1, -1) && cB(c, 2, 0) && cB(d, 3, 1)) {
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
                    if (f == 4) {
                        if (cB(a, -2, -2) && cB(b, -1, -1) && cB(d, 1, 1)) {
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
                        if (cB(a, -3, -2) && cB(b, -2, -1) && cB(c, -1, 0) && cB(d, 0, 1)) {
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
                        if (cB(a, 0, -2) && cB(b, 1, -1) && cB(c, 2, 0) && cB(d, 3, 1)) {
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
        }else{
            switch (form.getName()) {
                case "j":
                    if (f == 1) {
                        if (cB2(a, 1, -1) && cB2(c, -1, -1) && cB2(d, -2, -2)) {
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
                        if( cB2(a, -1, -1) && cB2(c, -1, 1) && cB2(d, -2, 2)) {
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
                        if( cB2(a, 1, -1) && cB2(c, 1, 1) && cB2(b,2,0) && cB2(d, 0, 2)) {
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
                    if (f == 3 && cB2(a, -1, 1) && cB2(c, 1, 1) && cB2(d, 2, 2)) {
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
                        if( cB2(a, 1, 1) && cB2(c, 1, -1) && cB2(d, 2, -2)) {
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
                        if( cB2(a, -1, 1) && cB2(b,-2,0) && cB2(c, -1, -1) && cB2(d, 0, -2)) {
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
                        if (cB2(a, 1, -1) && cB2(c, 1, 1) && cB2(b, 2, 2)) {
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
                        if (cB2(a, 0, -1) && cB2(c, 0, 1) && cB2(b, 1, 2) && cB2(d,-1,0)) {
                            MoveDown(form.a);
                            MoveUp(form.c);
                            MoveUp(form.b);
                            MoveUp(form.b);
                            MoveRight(form.b);
                            MoveLeft(form.d);
                            form.changeForm();
                            break;
                        }
                        if (cB2(a, 1, -1) && cB2(c, 1, 1) && cB2(b, 2, 2)) {
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
                        if(cB2(a, -1, -1) && cB2(b, 2, -2) && cB2(c, 1, -1)) {
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
                        if(cB2(a, -2, -1) && cB2(b, 1, -2) && cB2(c, 0, -1) && cB2(d,-1,0)) {
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
                        if( cB2(a, -1, 1) && cB2(c, -1, -1) && cB2(b, -2, -2)) {
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
                        if( cB2(a, 0, 1) && cB2(c, 0, -1) && cB2(b, -1, -2) && cB2(d,1,0)) {
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
                        if( cB2(a, 1, 1) && cB2(b, -2, 2) && cB2(c, -1, 1)) {
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
                        if( cB2(a, 2, 1) && cB2(b, -1, 2) && cB2(c, 0, 1) && cB2(d,1,0)) {
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
                    if (f == 1 && cB2(a, -1, -1) && cB2(c, -1, 1) && cB2(d, 0, 2)) {
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
                        if( cB2(a, 1, 1) && cB2(c, 1, -1) && cB2(d, 0, -2)) {
                            MoveUp(form.a);
                            MoveRight(form.a);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveDown(form.d);
                            MoveDown(form.d);
                            form.changeForm();
                            break;
                        }
                        if( cB2(a, 0, 1) && cB2(b,-1,0) && cB2(c, 0, -1) && cB2(d, -1, -2)) {
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
                    if (f == 3 && cB2(a, -1, -1) && cB2(c, -1, 1) && cB2(d, 0, 2)) {
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
                        if(cB2(a, 1, 1) && cB2(c, 1, -1) && cB2(d, 0, -2)) {
                            MoveUp(form.a);
                            MoveRight(form.a);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveDown(form.d);
                            MoveDown(form.d);
                            form.changeForm();
                            break;
                        }
                        if( cB2(a, 0, 1) && cB2(b,-1,0) && cB2(c, 0, -1) && cB2(d, -1, -2)) {
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
                    if (f == 1 && cB2(a, 1, 1) && cB2(d, -1, -1) && cB2(c, -1, 1)) {
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
                        if(cB2(a, 1, -1) && cB2(d, -1, 1) && cB2(c, 1, 1)) {
                            MoveRight(form.a);
                            MoveDown(form.a);
                            MoveLeft(form.d);
                            MoveUp(form.d);
                            MoveUp(form.c);
                            MoveRight(form.c);
                            form.changeForm();
                            break;
                        }
                        if(cB2(a, 0, -1) && cB2(b,-1,0) && cB2(d, -2, 1) && cB2(c, 0, 1)) {
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
                    if (f == 3 && cB2(a, -1, -1) && cB2(d, 1, 1) && cB2(c, 1, -1)) {
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
                        if(cB2(a, -1, 1) && cB2(d, 1, -1) && cB2(c, -1, -1)) {
                            MoveLeft(form.a);
                            MoveUp(form.a);
                            MoveRight(form.d);
                            MoveDown(form.d);
                            MoveDown(form.c);
                            MoveLeft(form.c);
                            form.changeForm();
                            break;
                        }
                        if(cB2(a, 0, 1) && cB2(b,1,0) && cB2(d, 2, -1) && cB2(c, 0, -1)) {
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
                    if (f == 1 && cB2(b, 1, 1) && cB2(c, -1, 1) && cB2(d, -2, 0)) {
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
                        if(cB2(b, -1, -1) && cB2(c, 1, -1) && cB2(d, 2, 0)) {
                            MoveDown(form.b);
                            MoveLeft(form.b);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveRight(form.d);
                            MoveRight(form.d);
                            form.changeForm();
                            break;
                        }
                        if(cB2(a,-1,0) && cB2(b, -2, -1) && cB2(c, 0, -1) && cB2(d, 1, 0)) {
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
                    if (f == 3 && cB2(b, 1, 1) && cB2(c, -1, 1) && cB2(d, -2, 0)) {
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
                        if(cB2(b, -1, -1) && cB2(c, 1, -1) && cB2(d, 2, 0)) {
                            MoveDown(form.b);
                            MoveLeft(form.b);
                            MoveRight(form.c);
                            MoveDown(form.c);
                            MoveRight(form.d);
                            MoveRight(form.d);
                            form.changeForm();
                            break;
                        }
                        if(cB2(a,-1,0) && cB2(b, -2, -1) && cB2(c, 0, -1) && cB2(d, 1, 0)) {
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
                        if (cB2(a, 2, 2) && cB2(b, 1, 1) && cB2(d, -1, -1)) {
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
                        if( cB2(a, -2, -2) && cB2(b, -1, -1) && cB2(d, 1, 1)){
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
                        if(cB2(a, -3, -2) && cB2(b, -2, -1) &&cB2(c,-1,0) && cB2(d, 0, 1)){
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
                        if( cB2(a, 0, -2) && cB2(b, 1, -1) && cB2(c, 2, 0)&&cB2(d,3,1)){
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
                    if (f == 3 && cB2(a, 2, 2) && cB2(b, 1, 1) && cB2(d, -1, -1)) {
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
                        if( cB2(a, -2, -2) && cB2(b, -1, -1) && cB2(d, 1, 1))
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
                        if(cB2(a, -3, -2) && cB2(b, -2, -1) &&cB2(c,-1,0) && cB2(d, 0, 1)){
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
                        if( cB2(a, 0, -2) && cB2(b, 1, -1) && cB2(c, 2, 0)&&cB2(d,3,1)){
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
    }

    private void RemoveRows(Pane pane ,int [][] MESH,int groupnumber) {
        ArrayList<Node> texts = new ArrayList<Node>();
        ArrayList<Integer> lines = new ArrayList<Integer>();
        ArrayList<Node> newtexts = new ArrayList<Node>();
        boolean removeCheck = false; //지워야될 라인이 있나
        int miniMeshCountController = 0;
        int constClearLineSize = 0; //한번에 지워지는 라인 수
        int full = 0;
        for (int i = 0; i < MESH[0].length; i++) {
            for (int j = 0; j < MESH.length; j++) {
                if (MESH[j][i] == 1) {
                    full++;
                    if(groupnumber ==0){
                        if(meshTop!=0){
                            continue;
                        }else{
                            meshTop = i;
                        }
                    }else{
                        if(meshTop2!=0){
                            continue;
                        }else{
                            meshTop2 = i;
                        }
                    }
                }
                if(i == LineClearY || i ==LineClearY2)
                    full = MESH.length;

            }
            if (full == MESH.length) {
                lines.add(i);
                removeCheck = true;
            }
            //lines.add(i + lines.size());
            full = 0;
        }
        if(groupnumber ==0)
            LineClearY = -1;
        else
            LineClearY2 = -1;
        constClearLineSize = lines.size();
        miniMeshCountController = lines.size() - 1;
        if(groupnumber == 0)
            meshTop -= lines.size();
        else
            meshTop2 -= lines.size();
        if (lines.size() > 0)
            do {
                for (Node node : pane.getChildren()) {
                    if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                            node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                            node.getUserData() == "waitc" || node.getUserData() == "waitd" ||
                            node.getUserData() =="mini" || node.getUserData()=="effectText")//예외설정
                        continue;
                    if (node instanceof Text)
                        texts.add(node);
                }
                if(groupnumber == 0) {
                    if (Frame > 200000000) {
                        Frame -= 50000000 * frameMultiplier;
                        scoreMultiplier++;
                    }
                    score += 50 * scoreMultiplier;
                    linesNo++;
                    itemCounter++;
                }else{
                    if (Frame > 150000000) {
                        Frame -= 50000000 * frameMultiplier;//이거 프레임도 2붙여야 하나요..?
                        scoreMultiplier2++;
                    }
                    score2 += 50 * scoreMultiplier2;
                    linesNo2++;
                    itemCounter2++;
                }
                for (Node node : texts) {
                    Text a = (Text) node;

                    if (a.getY() == lines.get(0) * SIZE) {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                        if(groupnumber == 0) {
                            if((constClearLineSize>=2) && (miniMeshLineCounter2 < 10)) {
                                if (node.getUserData() != "current") {
                                    miniMESH2[(int) a.getX() / SIZE][(int) ((YMAX / SIZE)/2) - miniMeshCountController - miniMeshLineCounter2] = 1;
                                    Text c = new Text("X");
                                    c.setX(XMAX + a.getX() * 0.5);
                                    c.setY(YMAX - SIZE - ((miniMeshCountController + miniMeshLineCounter2) * SIZE) * 0.6);
                                    c.setUserData("mini");
                                    c.setFont(Font.font(fontSize * 0.65));
                                    c.setFill(Color.rgb(226, 226, 226));
                                    group2.getChildren().add(c);
                                }
                            }
                        }else{
                            if((constClearLineSize>=2) && (miniMeshLineCounter < 10)) {
                                if (node.getUserData() != "current") {
                                    miniMESH[(int) a.getX() / SIZE][(int) ((YMAX / SIZE)/2) - miniMeshCountController - miniMeshLineCounter] = 1;


                                    Text c = new Text("X");
                                    c.setX(XMAX + a.getX() * 0.5);
                                    c.setY(YMAX - SIZE - ((miniMeshCountController+miniMeshLineCounter) * SIZE) * 0.6);
                                    c.setUserData("mini");
                                    c.setFont(Font.font(fontSize * 0.65));
                                    c.setFill(Color.rgb(226, 226, 226));
                                    group1.getChildren().add(c);
                                }
                            }
                        }


                        //////효과
                        Text effectText = new Text("O");
                        effectText.setX(a.getX());
                        effectText.setY(a.getY());
                        effectText.setUserData("effectText");
                        effectText.setFill(Color.WHITE);
                        effectText.setFont(Font.font(fontSize));
                        pane.getChildren().add(effectText);
                        new Flash(effectText).play();
                        ///////효과
                        pane.getChildren().remove(node);
                    } else
                        newtexts.add(node);
                }

                for (Node node : newtexts) {
                    if(node.getUserData()=="effectText")
                        continue;
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
                    if(node.getUserData()=="effectText")
                        continue;
                    if (node instanceof Text)
                        texts.add(node);
                }
                for (Node node : texts) {
                    if(node.getUserData()=="effectText")
                        continue;
                    Text a = (Text) node;
                    try {
                        MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 1;
                    } catch (ArrayIndexOutOfBoundsException e) {
                    }
                }
                texts.clear();
                miniMeshCountController--;
            } while (lines.size() > 0);//size->0


        if(constClearLineSize>1){
            if(groupnumber == 0)
                miniMeshLineCounter2+=constClearLineSize;
            else
                miniMeshLineCounter+=constClearLineSize;
        }


        for(Node node:pane.getChildren()){
            if(node.getUserData()=="current"){
                node.setUserData(null);
            }
        }

        if(groupnumber == 0 && (miniMeshLineCounter>0)){
            //미니메쉬만큼 올라오는 기능필요
            if(((int)(YMAX/SIZE)-meshTop) + miniMeshLineCounter >=20){//meshtop 제일상단의 블록 mesh y값
                top = 2;
            }else if(miniMeshLineCounter > 0){
                ArrayList<Node> currentMeshText = new ArrayList<>();
                for (Node node : pane.getChildren()) {
                    if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                            node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                            node.getUserData() == "waitc" || node.getUserData() == "waitd" ||
                            node.getUserData() =="mini" || node.getUserData()=="effectText")//예외설정
                        continue;
                    if (node instanceof Text)
                        currentMeshText.add(node);
                }

                for (Node node : currentMeshText) {
                    Text a = (Text) node;
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                }
                for (Node node : currentMeshText) {
                    Text a = (Text) node;
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE - miniMeshLineCounter] = 1;
                    a.setY(a.getY() - SIZE*miniMeshLineCounter);
                }
                //minimesh초기화
                ArrayList<Node> removeMiniTexts = new ArrayList<>();
                for (int i = 0; i < miniMESH[0].length; i++) {
                    for (int j = 0; j < miniMESH.length; j++) {
                        if(miniMESH[j][i]==1){
                            MESH[j][i+10] = 1;
                            Text soloText=new Text(j*SIZE,(i+10)*SIZE,"O");
                            soloText.setFont(Font.font(fontSize));
                            soloText.setFill(Color.GRAY);
                            group1.getChildren().add(soloText);
                        }
                        miniMESH[j][i] = 0;
                    }
                }
                for (Node node : pane.getChildren()) {
                    if (node instanceof Text) {
                        if (node.getUserData() == "mini")
                            removeMiniTexts.add(node);
                    }
                }
                for (Node node : removeMiniTexts) {
                    pane.getChildren().remove(node);
                }
                miniMeshLineCounter = 0;
                removeCheck = false;
                //^^^^^^^^^^^^^^^^^^^^^^minimesh초기화
            }
        }if(groupnumber == 1 && (miniMeshLineCounter2>0)){
            //미니메쉬만큼 올라오는 기능필요
            if ((((int)(YMAX/SIZE)-meshTop2) + miniMeshLineCounter2) >= 20) {//meshtop 제일상단의 블록 mesh y값
                top2 = 2;
            } else if(miniMeshLineCounter2>0){
                ArrayList<Node> currentMeshText = new ArrayList<>();
                for (Node node : pane.getChildren()) {
                    if (node.getUserData() == "scoretext" || node.getUserData() == "level" ||
                            node.getUserData() == "waita" || node.getUserData() == "waitb" ||
                            node.getUserData() == "waitc" || node.getUserData() == "waitd" ||
                            node.getUserData() == "mini" || node.getUserData() == "effectText")//예외설정
                        continue;
                    if (node instanceof Text)
                        currentMeshText.add(node);
                }

                for (Node node : currentMeshText) {
                    Text a = (Text) node;
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE] = 0;
                }
                for (Node node : currentMeshText) {
                    Text a = (Text) node;
                    MESH[(int) a.getX() / SIZE][(int) a.getY() / SIZE - miniMeshLineCounter2] = 1;
                    a.setY(a.getY() - (SIZE * miniMeshLineCounter2));
                }
                ArrayList<Node> removeMiniTexts = new ArrayList<>();
                for (int i = 0; i < miniMESH2[0].length; i++) {
                    for (int j = 0; j < miniMESH2.length; j++) {
                        if(miniMESH2[j][i]==1){
                            MESH[j][i+10] = 1;
                            Text soloText=new Text(j*SIZE,(i+10)*SIZE,"O");
                            soloText.setFont(Font.font(fontSize));
                            soloText.setFill(Color.GRAY);
                            group2.getChildren().add(soloText);
                        }
                        miniMESH2[j][i] = 0;
                    }
                }
                for (Node node : pane.getChildren()) {
                    if (node instanceof Text) {
                        if (node.getUserData() == "mini")
                            removeMiniTexts.add(node);
                    }
                }
                for (Node node : removeMiniTexts) {
                    pane.getChildren().remove(node);
                }
                miniMeshLineCounter2 = 0;
                removeCheck = false;
            }
        }
    }

    private void WeightRemoveRows(Pane pane ,ItemForm form) {
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

    private void BombRemoveRows(Pane pane,int[][] MESH, ItemForm form) {
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



    private boolean MoveDown(ItemForm form,int [][] MESH, Pane group, boolean isGroupOne) {
        boolean moved = false; // 이동 여부를 추적하는 변수입니다.
        if (form.getItem() == "Weight") {
            if (!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                    || form.d.getY() == YMAX - SIZE)) {
                MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE + 1] = 0;
                MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE + 1] = 0;
                MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE + 1] = 0;
                MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE + 1] = 0;
                if(isGroupOne)
                    WeightRemoveRows(group1, form);
                else
                    WeightRemoveRows(group2,form);
            }
        }
        if (form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form, MESH) || moveB(form, MESH) || moveC(form, MESH) || moveD(form, MESH)) {
            // 여기서는 블록이 다음 위치로 이동할 수 없으므로, 현재 위치를 고정하고 새로운 블록을 생성합니다.
            MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
            MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
            MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
            MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
            if (isGroupOne){
                RemoveRows(group1,MESH,0);
                WeightMove = true;
                if (form.getItem() == "LineClear") {
                    switch (form.getItemRotate()) {
                        case 1: LineClearY = (int) form.a.getY() / SIZE; break;
                        case 2: LineClearY = (int) form.b.getY() / SIZE; break;
                        case 3: LineClearY = (int) form.c.getY() / SIZE; break;
                        case 4: LineClearY = (int) form.d.getY() / SIZE; break;
                    }
                    RemoveRows(group1,MESH,0);  // 즉시 행 삭제
                }
                if (form.getItem() == "Bomb") {
                    BombRemoveRows(group1, MESH, form);
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
                currentTextSetUserData(a);
                group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
                moveOnKeyPress(a,object2);
                moved = false; // 이 경우에는 이동하지 않으므로 false
                item = "";
                itemRotate = 0;

                // 이 경우에는 이동하지 않으므로 false
            }
            else{
                RemoveRows(group2,MESH,1);
                WeightMove = true;
                if (form.getItem() == "LineClear") {
                    switch (form.getItemRotate()) {
                        case 1: LineClearY2 = (int) form.a.getY() / SIZE; break;
                        case 2: LineClearY2 = (int) form.b.getY() / SIZE; break;
                        case 3: LineClearY2 = (int) form.c.getY() / SIZE; break;
                        case 4: LineClearY2 = (int) form.d.getY() / SIZE; break;
                    }
                    RemoveRows(group2,MESH,1);  // 즉시 행 삭제
                }
                if (form.getItem() == "Bomb") {
                    BombRemoveRows(group2, MESH, form);
                }
                ItemForm a = itemController.makeText(waitObj2.getName(), true, waitObj2.getItem(), waitObj2.getItemRotate());
                group.getChildren().removeAll(waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
                if (itemCounter2 >= 10) {
                    int itemNumber = (int) (Math.random() * 100);
                    int itemRotateNumber = (int) (Math.random() * 100);
                    if (itemNumber < 20)
                        item2 = "LineClear";
                    else if (itemNumber < 40)
                        item2 = "Weight";
                    else if (itemNumber < 60)
                        item2 = "Inverse";
                    else if (itemNumber < 80)
                        item2 = "Bomb";
                    else
                        item2 = "Fixed";
                    if (itemRotateNumber < 25)
                        itemRotate2 = 1;
                    else if (itemRotateNumber < 50)
                        itemRotate2 = 2;
                    else if (itemRotateNumber < 75)
                        itemRotate2 = 3;
                    else
                        itemRotate2 = 4;
                    itemCounter2 = 0;
                }
                waitObj2 = itemController.waitingTextMake(true, difficultylevel, item2, itemRotate2, XMAX);
                object2 = a;
                currentTextSetUserData(a);
                group.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
                moveOnKeyPress(object,a);
                moved = false; // 이 경우에는 이동하지 않으므로 false
                item2 = "";
                itemRotate2 = 0;
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
            if(isGroupOne) {
                score += scoreMultiplier;
                top = 0;
            }
            else {
                score2 += scoreMultiplier;
                top2 = 0;
            }
        }
        return moved; // 이동 여부를 반환
    }


    private void DirectMoveDown(ItemForm form,ItemForm form2, Pane group, boolean isGroupOne, int [][] MESH) {
        while (!(form.a.getY() == YMAX - SIZE || form.b.getY() == YMAX - SIZE || form.c.getY() == YMAX - SIZE
                || form.d.getY() == YMAX - SIZE || moveA(form, MESH) || moveB(form, MESH) || moveC(form, MESH) || moveD(form, MESH))){
            form.a.setY(form.a.getY() + MOVE);
            form.b.setY(form.b.getY() + MOVE);
            form.c.setY(form.c.getY() + MOVE);
            form.d.setY(form.d.getY() + MOVE);
            // 실제로 이동했으므로 true로 설정
            if(isGroupOne) {
                score += scoreMultiplier;
                top = 0;
            }
            else {
                score2 += scoreMultiplier;
                top2 = 0;
            }

            //directmovedown 호출시 object 겹침 버그 방지용
        }
        MESH[(int) form.a.getX() / SIZE][(int) form.a.getY() / SIZE] = 1;
        MESH[(int) form.b.getX() / SIZE][(int) form.b.getY() / SIZE] = 1;
        MESH[(int) form.c.getX() / SIZE][(int) form.c.getY() / SIZE] = 1;
        MESH[(int) form.d.getX() / SIZE][(int) form.d.getY() / SIZE] = 1;
        if (isGroupOne){
            RemoveRows(group1,MESH,0);
            if (form.getItem() == "LineClear") {
                switch (form.getItemRotate()) {
                    case 1: LineClearY = (int) form.a.getY() / SIZE; break;
                    case 2: LineClearY = (int) form.b.getY() / SIZE; break;
                    case 3: LineClearY = (int) form.c.getY() / SIZE; break;
                    case 4: LineClearY = (int) form.d.getY() / SIZE; break;
                }
                RemoveRows(group1,MESH,0);  // 즉시 행 삭제
            }
            if (form.getItem() == "Bomb") {
                BombRemoveRows(group1, MESH , form);
            }
            ItemForm a = itemController.makeText(waitObj.getName(), true, waitObj.getItem(), waitObj.getItemRotate());
            group1.getChildren().removeAll(waitObj.a, waitObj.b, waitObj.c, waitObj.d);
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
            currentTextSetUserData(a);
            group1.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj.a, waitObj.b, waitObj.c, waitObj.d);
            moveOnKeyPress(a,form2);
            item = "";
            itemRotate = 0;
        }
        else{
            RemoveRows(group2,MESH,1);
            if (form.getItem() == "LineClear") {
                switch (form.getItemRotate()) {
                    case 1: LineClearY2 = (int) form.a.getY() / SIZE; break;
                    case 2: LineClearY2 = (int) form.b.getY() / SIZE; break;
                    case 3: LineClearY2 = (int) form.c.getY() / SIZE; break;
                    case 4: LineClearY2 = (int) form.d.getY() / SIZE; break;
                }
                RemoveRows(group2,MESH,1);  // 즉시 행 삭제
            }
            if (form.getItem() == "Bomb") {
                BombRemoveRows(group2, MESH , form);
            }
            ItemForm a = itemController.makeText(waitObj2.getName(), true, waitObj2.getItem(), waitObj2.getItemRotate());
            group2.getChildren().removeAll(waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
            if (itemCounter2 >= 10) {
                int itemNumber = (int) (Math.random() * 100);
                int itemRotateNumber = (int) (Math.random() * 100);
                if (itemNumber < 20)
                    item2 = "LineClear";
                else if (itemNumber < 40)
                    item2 = "Weight";
                else if (itemNumber < 60)
                    item2 = "Inverse";
                else if (itemNumber < 80)
                    item2 = "Bomb";
                else
                    item2 = "Fixed";
                if (itemRotateNumber < 25)
                    itemRotate2 = 1;
                else if (itemRotateNumber < 50)
                    itemRotate2 = 2;
                else if (itemRotateNumber < 75)
                    itemRotate2 = 3;
                else
                    itemRotate2 = 4;
                itemCounter2 = 0;
            }
            waitObj2 = itemController.waitingTextMake(true, difficultylevel, item2, itemRotate2, XMAX);
            object2 = a;
            currentTextSetUserData(a);
            group2.getChildren().addAll(a.a, a.b, a.c, a.d, waitObj2.a, waitObj2.b, waitObj2.c, waitObj2.d);
            moveOnKeyPress(form2,a);
            item2 = "";
            itemRotate2 = 0;
        }
    }

    private boolean moveA(ItemForm form, int [][] MESH) {
        return (MESH[(int) form.a.getX() / SIZE][((int) form.a.getY() / SIZE) +1] == 1);
    }

    private boolean moveB(ItemForm form, int [][] MESH) {
        return (MESH[(int) form.b.getX() / SIZE][((int) form.b.getY() / SIZE) +1] == 1);
    }

    private boolean moveC(ItemForm form, int [][] MESH) {
        return (MESH[(int) form.c.getX() / SIZE][((int) form.c.getY() / SIZE) +1] == 1);
    }

    private boolean moveD(ItemForm form, int [][] MESH) {
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

    private boolean cB2(Text text, int x, int y) {
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
        return xb && yb && MESH2[((int) text.getX() / SIZE) + x][((int) text.getY() / SIZE) - y] == 0;
    }//Text로 변경


    public void stopAnimation(HBox hbox) {
        running=false;
        applyGrayscaleEffect(hbox);
        for (Node node : group1.getChildren()) {
            if (node instanceof Button) {
                node.setVisible(true);
            }
        }
        bringButtonsToFront();
    }

    public void startAnimation(HBox hbox) {
        running = true;

        // 게임 재시작 및 종료 버튼 숨김
        // 흑백 효과 해제
        clearGrayscaleEffect(hbox);
        for (Node node : group1.getChildren()) {
            if (node instanceof Button) {
                node.setVisible(false);
            }
        }
        bringButtonsToFront();
    }

    public void applyGrayscaleEffect(HBox hbox) {
        ColorAdjust colorAdjust = new ColorAdjust(); //Color Adjust 조절
        colorAdjust.setSaturation(-1);
        colorAdjust.setBrightness(-0.3);
        hbox.setEffect(colorAdjust); // 전체 그룹에 흑백 효과 적용
    }

    public void GameOver(){
        applyGrayscaleEffect(hbox);  // HBox에 흑백 효과 적용
        running = false;


        Label scoreLabel = new Label("score: " + score + "\n" + "player " +winner + "WIN!");
        Label scoreLabel2 = new Label("score: " + score2 + "\n" + "player " + winner + "WIN!");


        if (exitButton != null)
            exitButton.toFront();
        exitButton.setVisible(true);

        if (terminateButton != null)
            terminateButton.toFront();
        terminateButton.setVisible(true);

        scoreLabel.setLayoutX(XMAX/4 - 10);
        scoreLabel.setLayoutY(YMAX/4);
        scoreLabel2.setLayoutX(XMAX/4 - 10);
        scoreLabel2.setLayoutY(YMAX/4);

        scoreLabel.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-background-color: black;");
        scoreLabel2.setStyle("-fx-font-size: 20px; -fx-text-fill: red; -fx-background-color: black;");

        // 색상 조정 효과 다시 적용
        ColorAdjust resetColorAdjust = new ColorAdjust();
        resetColorAdjust.setSaturation(3.0); // 채도를 조금 증가시켜 원래 색상에 가깝게 조정
        resetColorAdjust.setBrightness(0.2); // 명도를 살짝 높여주어 밝기 조정
        resetColorAdjust.setContrast(0.2);   // 대비를 살짝 높여주어 색상의 구분을 명확히

        scoreLabel.setEffect(resetColorAdjust);
        scoreLabel2.setEffect(resetColorAdjust);

        group1.getChildren().add(scoreLabel);
        group2.getChildren().add(scoreLabel2);
        scoreLabel.setVisible(true);
        scoreLabel2.setVisible(true);
    }

    private void GameStopped(Stage stage){
        timer.stop();
        stage.close();
    }

    public void clearGrayscaleEffect(HBox hbox) {
        hbox.setEffect(null); // 흑백 효과 해제
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