
import ScoreBoard.ScoreBoard;
import ScoreBoard.ScoreBoardWindow;
import ScoreBoard.ScoreRecord;
import ScoreBoard.JdbcConnecter;
import Setting.SettingsWindow;
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

import java.net.URL;
import java.util.List;
import java.util.Optional;
import User.SessionManager;


public class TetrisWindow extends Application {
    public JdbcConnecter scoreboardConnector;
    private Label userInfoLabel;
    public static User user;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("TETRIS GAME");
        BorderPane root = new BorderPane();
        HelloApplication helloApp = new HelloApplication();

        // 로고 생성
        Text logoText = new Text("TETRIS");
        logoText.setFont(Font.font("Arial", FontWeight.BOLD, 24));
        logoText.setFill(Color.BLACK);
        VBox logoPane = new VBox(logoText);
        logoPane.setPadding(new Insets(10));
        logoPane.setStyle("-fx-background-color: #800080;");

        // 버튼 생성
        userInfoLabel = new Label("로그인이 필요합니다");
        VBox buttonPane = new VBox(10);
        buttonPane.setAlignment(Pos.CENTER);
        Button gameStartButton = new Button("일반모드");
        Button itemgameButton = new Button("아이템모드");
        Button scoreBoardButton = new Button("스코어보드");
        Button settingsButton = new Button("설정");
        Button exitButton = new Button("게임종료");
        Button loginButton = new Button("로그인");
        Button signUpButton = new Button("회원가입");




        buttonPane.getChildren().addAll(gameStartButton, itemgameButton, scoreBoardButton, settingsButton, exitButton, loginButton, signUpButton, userInfoLabel);

        // 설정 창 생성
        SettingsWindow settingsWindow = new SettingsWindow(primaryStage);
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
                // 새 Stage 생성
                Stage gameStage = new Stage();

                // HelloApplication의 start 메소드 호출
                if (user !=null){
                    helloApp.start(gameStage);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        itemgameButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                // 버튼에 대한 동작 수행

                itemgameButton.fire();
            }
        });

        itemgameButton.setOnAction(event -> {
            try {
                // 새 Stage 생성
                Stage gameStage = new Stage();
                HelloApplication.itemMode = true; // 아이템 모드 활성화

                // 아이템 모드 여부 확인
                Alert alert = new Alert(AlertType.CONFIRMATION);
                alert.setTitle("아이템 모드");
                alert.setHeaderText(null);
                alert.setContentText("아이템 모드로 진행하시겠습니까?");

                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.OK) {
                    helloApp.start(gameStage);
                } else {
                    HelloApplication.itemMode = false; // 아이템 모드 비활성화
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
        itemgameButton.getStyleClass().add("button");
        scoreBoardButton.getStyleClass().add("button");
        settingsButton.getStyleClass().add("button");
        exitButton.getStyleClass().add("button");

        gameStartButton.getStyleClass().add("game-start-button");
        itemgameButton.getStyleClass().add("item-game-button");
        scoreBoardButton.getStyleClass().add("score-board-button");
        settingsButton.getStyleClass().add("settings-button");
        exitButton.getStyleClass().add("exit-button");

        primaryStage.setScene(scene);
        primaryStage.show();

    }
    private void showLoginDialog(Stage parentStage) {
        Dialog<String[]> dialog = new Dialog<>();
        dialog.setTitle("로그인");
        dialog.setHeaderText("사용자 이름과 비밀번호를 입력하세요.");

        ButtonType loginButtonType = new ButtonType("로그인", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

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
            return null;
        });

        Optional<String[]> result = dialog.showAndWait();

        result.ifPresent(usernamePassword -> {
            user = JdbcConnecter.SuccessLogin(username.getText(), password.getText());
            Alert alert = new Alert(AlertType.CONFIRMATION);
            if (user != null) {
                alert.setContentText(user.getNickname() + "님 환영합니다");
                SessionManager.setCurrentUser(user);
                System.out.println("로그인성공");
                alert.showAndWait();
                userInfoLabel.setText(user.getNickname() + "님 환영합니다");
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
            User user = new User(idPasswordNickname[0], idPasswordNickname[1], idPasswordNickname[2]);
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