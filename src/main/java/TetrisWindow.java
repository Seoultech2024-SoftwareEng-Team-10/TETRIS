
import ScoreBoard.ScoreBoard;
import ScoreBoard.ScoreBoardWindow;
import ScoreBoard.ScoreRecord;
import ScoreBoard.ScoreboardConnector;
import Setting.SettingsWindow;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;



public class TetrisWindow extends Application {

    @Override
    public void start(Stage primaryStage) {

        BorderPane root = new BorderPane();

        Text logoText = new Text("TETRIS");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 50));
        logoText.setFill(Color.BLACK);

        // 로고를 담을 BorderPane 생성
        BorderPane logoPane = new BorderPane();
        logoPane.setCenter(logoText);
        BorderPane.setAlignment(logoText, Pos.CENTER);

        // Scene 생성
        Scene scene2 = new Scene(logoPane, 400, 300);
        scene2.setFill(Color.BLACK);

        // Stage 설정
        primaryStage.setTitle("TETRIS Logo");
        primaryStage.setScene(scene2);
        primaryStage.show();

        // 버튼 생성
        VBox buttonPane = new VBox(20);
        buttonPane.setAlignment(Pos.CENTER);
        Button gameStartButton = new Button("게임시작");
        Button scoreBoardButton = new Button("스코어보드");
        Button settingsButton = new Button("설정");
        Button exitButton = new Button("게임종료");
        gameStartButton.setPrefSize(1000, 50);
        scoreBoardButton.setPrefSize(1000,50);
        settingsButton.setPrefSize(1000, 50);
        exitButton.setPrefSize(1000, 50);
        Font font = Font.font("Arial", FontWeight.BOLD, 40);
        gameStartButton.setFont(font);
        scoreBoardButton.setFont(font);
        settingsButton.setFont(font);
        exitButton.setFont(font);




        buttonPane.getChildren().addAll(gameStartButton, scoreBoardButton, settingsButton, exitButton);

        // 설정 창 생성
        SettingsWindow settingsWindow = new SettingsWindow(primaryStage);
        settingsButton.setOnAction(event -> settingsWindow.show());

        // 게임 종료 버튼 동작 설정
        exitButton.setOnAction(event -> primaryStage.close());

        // 버튼에 대한 포커스 설정
        /*
        gameStartButton.setFocusTraversable(true);
        scoreBoardButton.setFocusTraversable(true);
        settingsButton.setFocusTraversable(true);
        exitButton.setFocusTraversable(true);
        이부분 CSS로 동작하게 수정했습니다 0326
        */

        //스코어보드 버튼 동작 설정
        scoreBoardButton.setOnAction(event -> {
            ScoreboardConnector scoreboardConnector;
            List<ScoreRecord> scoreboardData = ScoreboardConnector.fetchData(1);
            ScoreBoard scoreBoard = new ScoreBoard(scoreboardData);
            ScoreBoardWindow window = new ScoreBoardWindow(scoreBoard);
            window.show();

        });

        // 엔터 키로 버튼 선택하기
        gameStartButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                gameStartButton.fire();
            }
        });

        gameStartButton.setOnAction(event -> {
            try {
                // HelloApplication 인스턴스 생성
                HelloApplication helloApp = new HelloApplication();

                // 새 Stage 생성
                Stage gameStage = new Stage();

                // HelloApplication의 start 메소드 호출
                helloApp.start(gameStage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        scoreBoardButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                scoreBoardButton.fire();
            }
        });

        settingsButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                settingsButton.fire();
            }
        });

        exitButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                exitButton.fire();
            }
        });


        root.setTop(logoPane);
        root.setCenter(buttonPane);

        Scene scene = new Scene(root, 800, 600);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN && event.getCode() != KeyCode.ENTER) {
                // 방향키 또는 엔터키가 아닌 키를 입력한 경우 알림창 띄우기
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("키 입력 안내");
                alert.setHeaderText(null);
                alert.setContentText("이동은 방향키 위/아래, 선택은 엔터키입니다!");
                alert.showAndWait();
            }
        });

        // Css파일 로드
        URL cssFile = getClass().getResource("styles.css");
        if (cssFile != null) {
            scene.getStylesheets().add(cssFile.toExternalForm());
        } else {
            System.err.println("Could not find styles.css");
        }

        // 각 버튼에 CSS 적용
        gameStartButton.getStyleClass().add("button");
        scoreBoardButton.getStyleClass().add("button");
        settingsButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button");

        gameStartButton.getStyleClass().add("game-start-button");
        scoreBoardButton.getStyleClass().add("score-board-button");
        settingsButton.getStyleClass().add("settings-button");
        exitButton.getStyleClass().add("exit-button");

        primaryStage.setScene(scene);
        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
        //test주석
    }
}