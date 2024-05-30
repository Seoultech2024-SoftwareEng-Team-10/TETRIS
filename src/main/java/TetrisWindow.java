import ScoreBoard.ScoreBoard;
import ScoreBoard.ScoreBoardWindow;
import ScoreBoard.ScoreRecord;
import ScoreBoard.JdbcConnecter;
import Setting.Settings;
import Setting.SettingsWindow;
import Setting.SizeConstants;
import Tetris.Controller;
import User.User;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import User.SessionManager;


public class TetrisWindow extends Application {
    private Label userInfoLabel;
    public static User user;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Settings settings = new Settings();
        settings.printSettings();
        SizeConstants sizeConstants = new SizeConstants(settings.getWindowWidth(), settings.getWindowHeight());
        Controller controller = new Controller(sizeConstants.getMOVE(), sizeConstants.getXMAX(),sizeConstants.getYMAX(), sizeConstants.getSIZE(), sizeConstants.getFontSize() ,sizeConstants.getMESH());
        HelloApplication helloApp = new HelloApplication(sizeConstants, controller);

        primaryStage.setTitle("TETRIS GAME");
        BorderPane root = new BorderPane();
        root.setStyle("-fx-background-color: rgb(211, 211, 211);");
        //ItemHelloApplication itemHelloApp = new ItemHelloApplication();

        Pane bottomPane = new Pane();
        bottomPane.setPrefSize(800, 200);
        root.setBottom(bottomPane);

        // 로고 생성
        Text textT = new Text("T ");
        textT.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textT.setFill(javafx.scene.paint.Color.web("#BC7566"));

        Text textE = new Text("E ");
        textE.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textE.setFill(javafx.scene.paint.Color.web("#DEB242"));

        Text textSecondT = new Text("T ");
        textSecondT.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textSecondT.setFill(javafx.scene.paint.Color.web("#BC7566"));

        Text textR = new Text("R ");
        textR.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textR.setFill(javafx.scene.paint.Color.web("#5BB23C"));

        Text textI = new Text("I ");
        textI.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textI.setFill(javafx.scene.paint.Color.web("4192CD"));

        Text textS = new Text("S");
        textS.setFont(Font.font("Arial", FontWeight.BOLD, 60));
        textS.setFill(javafx.scene.paint.Color.web("464AB4"));


        HBox logoPane = new HBox(textT, textE, textSecondT, textR, textI, textS);
        logoPane.setAlignment(Pos.CENTER);
        logoPane.setPadding(new Insets(20, 10, 20, 10));
        logoPane.setStyle("-fx-background-color: white;");



        // 버튼 생성
        userInfoLabel = new Label("로그인이 필요합니다");
        VBox leftButtonPane = new VBox(15);
        leftButtonPane.setAlignment(Pos.CENTER);
        leftButtonPane.setTranslateY(80);
        leftButtonPane.setPadding(new Insets(30, 50, 20, 50));
        Button scoreBoardButton = new Button("스코어보드");
        Button settingsButton = new Button("설정");
        Button exitButton = new Button("게임종료");
        Button loginButton = new Button("로그인");
        Button signUpButton = new Button("회원가입");

        leftButtonPane.getChildren().addAll(
                scoreBoardButton,
                settingsButton,
                exitButton,
                signUpButton,
                loginButton,
                userInfoLabel
        );

        VBox centerButtonPane = new VBox(15);
        centerButtonPane.setPadding(new Insets(30, 50, 20, 50));
        centerButtonPane.setAlignment(Pos.CENTER);
        centerButtonPane.setTranslateY(60);
        Button singleButton = new Button("싱글\n모드");
        Button battleModeButton = new Button("배틀\n모드");

        centerButtonPane.getChildren().addAll(
                singleButton,
                battleModeButton
        );

        VBox rightButtonPane = new VBox(15);
        rightButtonPane.setPadding(new Insets(30, 50, 20, 50));
        rightButtonPane.setAlignment(Pos.CENTER);
        rightButtonPane.setTranslateY(60);
        Button gameStartButton = new Button("일반모드");
        Button itemgameButton = new Button("아이템모드");

        rightButtonPane.getChildren().addAll(
                gameStartButton,
                itemgameButton
        );

        //battle-button
        battleModeButton.setOnAction(event ->BattleButton.show(sizeConstants, controller));
        battleModeButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                battleModeButton.fire();
            }
        });
        battleModeButton.getStyleClass().add("button");
        battleModeButton.getStyleClass().add("battle-button");


        // 설정 창 생성
        SettingsWindow settingsWindow = new SettingsWindow(primaryStage, settings,sizeConstants);
        settingsButton.setOnAction(event -> settingsWindow.show());

        // 게임 종료 버튼 동작 설정
        exitButton.setOnAction(event -> primaryStage.close());


        //로그인 버튼 동작 설정
        loginButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                loginButton.fire();
            }
        });
        signUpButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행
                signUpButton.fire();
            }
        });

        loginButton.setOnAction(event -> showLoginDialog(primaryStage));
        //회원가입 버튼 동작 생성
        signUpButton.setOnAction(event -> showSignUpDialog(primaryStage));

        //스코어보드 버튼 동작 설정
        scoreBoardButton.setOnAction(event -> {
            List<ScoreRecord> scoreboardData = JdbcConnecter.fetchData(1);
            System.out.println(scoreboardData.getClass());
            ScoreBoard scoreBoard = new ScoreBoard(scoreboardData);
            System.out.println(scoreBoard);
            ScoreBoardWindow window = new ScoreBoardWindow(scoreBoard);
            System.out.println(window);
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
                if(user!=null) {
                    // 새 Stage 생성
                    Stage gameStage = new Stage();
                    helloApp.start(gameStage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // 엔터 키로 버튼 선택하기
        itemgameButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {

                itemgameButton.fire();
            }
        });

        itemgameButton.setOnAction(event -> {
            try {
                if(user!=null) {
                    // 새 Stage 생성
                    Stage gameStage = new Stage();

                    // HelloApplication의 start 메소드 호출
                    //itemHelloApp.start(gameStage);
                }
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

        Scene scene = new Scene(root, 800, 600);

        root.setTop(logoPane);
        root.setLeft(leftButtonPane);
        root.setCenter(centerButtonPane);
        root.setRight(rightButtonPane);

        scene.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
            if (event.getCode() != KeyCode.UP && event.getCode() != KeyCode.DOWN && event.getCode() != KeyCode.ENTER && event.getCode() != KeyCode.RIGHT && event.getCode() != KeyCode.LEFT) {
                // 방향키 또는 엔터키가 아닌 키를 입력한 경우 알림창 띄우기
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("키 입력 안내");
                alert.setHeaderText(null);
                alert.setContentText("이동은 방향키 위/아래/좌/우, 선택은 엔터키입니다!");
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
        itemgameButton.getStyleClass().add("button");
        scoreBoardButton.getStyleClass().add("button");
        settingsButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button");
        battleModeButton.getStyleClass().add("button");
        singleButton.getStyleClass().add("button");

        gameStartButton.getStyleClass().add("game-start-button");
        itemgameButton.getStyleClass().add("item-game-button");
        scoreBoardButton.getStyleClass().add("score-board-button");
        settingsButton.getStyleClass().add("settings-button");
        exitButton.getStyleClass().add("exit-button");
        battleModeButton.getStyleClass().add("battle-button");
        singleButton.getStyleClass().add("single-button");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void showLoginDialog(Stage parentStage) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("로그인");
        dialog.setHeaderText("사용자 이름과 비밀번호를 입력하세요.");

        ButtonType loginButtonType = new ButtonType("로그인", ButtonBar.ButtonData.OK_DONE);
        ButtonType guestButtonType = new ButtonType("게스트", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType,guestButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField username = new TextField();
        username.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*")) { // 영어와 숫자만 허용
                username.setText(newValue.replaceAll("[^A-Za-z0-9]", "")); // 비영어 문자 제거
            }
        });
        username.setPromptText("사용자 이름");

        PasswordField password = new PasswordField();
        password.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*")) { // 영어와 숫자만 허용
                password.setText(newValue.replaceAll("[^A-Za-z0-9]", "")); // 비영어 문자 제거
            }
        });
        password.setPromptText("비밀번호");

        grid.add(new Label("사용자 이름:"), 0, 0);
        grid.add(username, 1, 0);
        grid.add(new Label("비밀번호:"), 0, 1);
        grid.add(password, 1, 1);

        dialog.getDialogPane().setContent(grid);
        Platform.runLater(username::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new String[] {username.getText(), password.getText()};
            }
            else if (dialogButton == guestButtonType){
                return new String[] {"Guest", "abc123"};
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            Alert alert = new Alert(AlertType.CONFIRMATION);
            try{
                System.out.println(username.getText());
                if (username.getText().equals("")){
                    alert.setContentText("게스트유저님 환영합니다");
                    user = new User("Guest", "Guest","abc123");
                }
                else{
                    user = JdbcConnecter.SuccessLogin(username.getText(), password.getText());
                }

                if (user != null ) {
                    alert.setContentText(user.getNickname() + "님 환영합니다");
                    SessionManager.setCurrentUser(user);
                    alert.showAndWait();
                    userInfoLabel.setText(user.getNickname() + "님 환영합니다");
                }
            }
            catch (Exception e){
                System.out.println(e);
            }
        });
    }

    private void showSignUpDialog(Stage parentStage) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("회원가입");
        dialog.setHeaderText("ID, 비밀번호, 닉네임을 입력하세요.");

        ButtonType signUpButtonType = new ButtonType("회원가입", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(signUpButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        Label idLabel = new Label("ID (6-20자, 영문+숫자)");
        TextField idField = new TextField();
        idField.setPromptText("ID");
        idField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*") || newValue.length() > 20) { // 영문과 숫자만 허용하며 최대 20자까지
                idField.setText(oldValue); // 잘못된 입력 또는 20자 초과 입력은 무시
            }
        });

        Label passwordLabel = new Label("비밀번호 (8-20자, 영문+숫자)");
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("비밀번호");
        passwordField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*") || newValue.length() > 20) { // 영문과 숫자만 허용하며 최대 20자까지
                passwordField.setText(oldValue); // 잘못된 입력 또는 20자 초과 입력은 무시
            }
        });

        Label passwordConfirmLabel = new Label("비밀번호 확인 (8-20자, 영문+숫자)");
        PasswordField passwordConfirmField = new PasswordField();
        passwordConfirmField.setPromptText("비밀번호 확인");
        passwordConfirmField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*") || newValue.length() > 20) { // 영문과 숫자만 허용하며 최대 20자까지
                passwordConfirmField.setText(oldValue); // 잘못된 입력 또는 20자 초과 입력은 무시
            }
        });

        Label nicknameLabel = new Label("닉네임 (4-20자, 영문+숫자)");
        TextField nicknameField = new TextField();
        nicknameField.setPromptText("닉네임");
        nicknameField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("[A-Za-z0-9]*") || newValue.length() > 20) { // 영문과 숫자만 허용하며 최대 20자까지
                nicknameField.setText(oldValue); // 잘못된 입력 또는 20자 초과 입력은 무시
            }
        });

        grid.add(idLabel, 0, 0);
        grid.add(idField, 1, 0);
        grid.add(passwordLabel, 0, 1);
        grid.add(passwordField, 1, 1);
        grid.add(passwordConfirmLabel, 0, 2);
        grid.add(passwordConfirmField, 1, 2);
        grid.add(nicknameLabel, 0, 3);
        grid.add(nicknameField, 1, 3);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(idField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == signUpButtonType) {
                if (idField.getText().length() >= 6 && passwordField.getText().length() >= 8 && nicknameField.getText().length() >= 4 &&
                        idField.getText().length() <= 20 && passwordField.getText().length() <= 20 && nicknameField.getText().length() <= 20 &&
                        passwordField.getText().equals(passwordConfirmField.getText())) {
                    return new String[] {idField.getText(), passwordField.getText(), nicknameField.getText()};
                } else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setContentText("입력 조건을 확인하세요.");
                    alert.showAndWait();
                    return null;
                }
            }
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(idPasswordNickname -> {
            User user = new User(idPasswordNickname[0], idPasswordNickname[2], idPasswordNickname[1]);
            String message = JdbcConnecter.CreateUser(user);
            Alert alert = new Alert(AlertType.CONFIRMATION);
            if (message.equals("Success")) {
                alert.setContentText("회원가입 성공");
            } else {
                alert.setContentText(message);
            }
            alert.showAndWait();
        });
    }


    public static void main(String[] args) {
        launch(args);
    }
}