package Setting;


import ScoreBoard.JdbcConnecter;
import Tetris.BlockColor;
import Tetris.ItemBlockColor;
import User.SessionManager;
import User.User;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javafx.scene.control.Button;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;



public class SettingsWindow extends Stage {
    private final Settings settings;
    private ToggleButton colorBlindModeToggle;
    private MenuButton resizeMenuButton;
    private Stage mainWindow;
    private int isColorBlindModeOn;
    private Button[] buttons;
    private MenuButton levelButton;
    private LevelConstants levelConstants = new LevelConstants();
    private SizeConstants sizeConstants;
    private KeySettingsWindow keySettingsWindow;

    public SettingsWindow(Stage mainWindow, Settings settings, SizeConstants sizeConstants) {
        this.mainWindow = mainWindow;
        this.settings = settings;
        this.sizeConstants = sizeConstants;
        this.keySettingsWindow = new KeySettingsWindow(this,this.settings);
        setTitle("설정");
        setWidth(300);
        setHeight(400);

        BorderPane root = new BorderPane();

        VBox buttonBox = new VBox();
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.setSpacing(10);
        buttonBox.setPadding(new Insets(20));

        FlowPane modePanel = new FlowPane();
        modePanel.setAlignment(Pos.CENTER);
        modePanel.setHgap(10);
        Label colorBlindModeLabel = new Label("색맹 모드");
        colorBlindModeToggle = new ToggleButton("off");
        colorBlindModeToggle.setText(isColorBlindModeOn == 100 ? "off" : "on");
        BlockColor.setColorBlindMode(isColorBlindModeOn == 200); // BlockColor의 colorBlindMode 값 설정
        ItemBlockColor.setColorBlindMode(isColorBlindModeOn == 200); // ItemBlockColor의 colorBlindMode 값 설정
        colorBlindModeToggle.setPrefSize(50, 25);
        colorBlindModeToggle.setOnAction(event -> handleButtonClick(event));
        colorBlindModeToggle.setFocusTraversable(false);
        modePanel.getChildren().addAll(colorBlindModeLabel, colorBlindModeToggle);


        FlowPane levelPanel = new FlowPane();
        levelPanel.setAlignment(Pos.CENTER);
        levelPanel.setHgap(10);
        Label levelLable = new Label("난이도 조절");
        levelButton = new MenuButton();
        levelButton.setFocusTraversable(false);
        if (settings.getLevel() == 'E') {
            levelButton.setText("Easy");
        } else if (settings.getLevel() == 'H') {
            levelButton.setText("Hard");
        } else {
            levelButton.setText("Normal");
        }
        MenuItem leveItem1 = new MenuItem("Normal");
        leveItem1.setOnAction(event -> {
            levelButton.setText("Normal");
            LevelConstants.setLevel('N');
            settings.updateAndSaveKey("level", "N");
        });
        MenuItem leveItem2 = new MenuItem("Easy");
        leveItem2.setOnAction(event -> {
            levelButton.setText("Easy");
            LevelConstants.setLevel('E');
            settings.updateAndSaveKey("level", "E");
        });
        MenuItem leveItem3 = new MenuItem("Hard");
        leveItem3.setOnAction(event -> {
            levelButton.setText("Hard");
            LevelConstants.setLevel('H');
            settings.updateAndSaveKey("level", "H");
        });
        levelButton.getItems().addAll(leveItem1, leveItem2, leveItem3);
        levelPanel.getChildren().addAll(levelLable, levelButton);


        FlowPane resizePanel = new FlowPane();
        resizePanel.setAlignment(Pos.CENTER);
        resizePanel.setHgap(10);
        Label resizeLabel = new Label("화면 크기 조절");
        resizeMenuButton = new MenuButton();

        resizeMenuButton.setFocusTraversable(false); // 포커스 가능 여부 설정
        resizeMenuButton.setText(settings.getWindowWidth()+"x"+settings.getWindowHeight());
        MenuItem item1 = new MenuItem("450 x 600");
        item1.setOnAction(event -> {
            resizeMenuButton.setText("450 x 600");
            sizeConstants.setSize(450, 600);
            settings.updateAndSaveKey("windowWidth", "450");
            settings.updateAndSaveKey("windowHeight", "600");
        });
        MenuItem item2 = new MenuItem("300 x 400");
        item2.setOnAction(event -> {
            resizeMenuButton.setText("300 x 400");
            sizeConstants.setSize(300, 400);
            settings.updateAndSaveKey("windowWidth", "300");
            settings.updateAndSaveKey("windowHeight", "400");
        });
        MenuItem item3 = new MenuItem("600 x 800");
        item3.setOnAction(event -> {
            resizeMenuButton.setText("600 x 800");
            sizeConstants.setSize(600, 800);
            settings.updateAndSaveKey("windowWidth", "600");
            settings.updateAndSaveKey("windowHeight", "800");
        });
        resizeMenuButton.getItems().addAll(item1, item2, item3);

        resizePanel.getChildren().addAll(resizeLabel, resizeMenuButton);
        buttonBox.getChildren().addAll(modePanel, levelPanel, resizePanel);

        Button keySettingsButton = new Button("키 설정");
        Button resetScoreButton = new Button("기록 초기화");
        Button resetAllButton = new Button("기본 설정으로 되돌리기");
        Button backButton = new Button("뒤로가기");

        buttons = new Button[]{keySettingsButton, resetScoreButton, resetAllButton, backButton};

        for (Button button : buttons) {
            button.setOnAction(event -> handleButtonClick(event));
            button.setFocusTraversable(true); // 포커스 가능하게 설정
            button.setOnKeyPressed(event -> {
                if (event.getCode() == KeyCode.ENTER) {
                    handleButtonClick(new ActionEvent(button, button));
                }
            });
            buttonBox.getChildren().add(button);
        }


        // 초기 포커스 설정
        keySettingsButton.requestFocus();

        backButton.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                hide();
                mainWindow.show();
            }
        });

        root.setCenter(buttonBox);

        Scene scene = new Scene(root);
        setScene(scene);

        colorBlindModeToggle.setOnAction(event -> {
            isColorBlindModeOn = isColorBlindModeOn == 100 ? 200 : 100;
            colorBlindModeToggle.setText(isColorBlindModeOn == 100 ? "off" : "on");
            BlockColor.setColorBlindMode(isColorBlindModeOn == 200); // BlockColor의 colorBlindMode 값 설정
            ItemBlockColor.setColorBlindMode(isColorBlindModeOn == 200); // ItemBlockColor의 colorBlindMode 값 설정
        });
    }

    private void handleButtonClick(ActionEvent event) {
        Object source = event.getSource();
        if (source instanceof Button) {
            Button clickedButton = (Button) source;
            String buttonText = clickedButton.getText();
            if (buttonText.equals("키 설정")) {
                keySettingsWindow.start(new Stage());
            } else if (buttonText.equals("뒤로가기")) {
                hide();
                mainWindow.show();
            } else if (buttonText.equals("기본 설정으로 되돌리기")) {
                showResetConfirmation();
            } else if (buttonText.equals("기록 초기화")) {
                showScoreResetConfirmation();
            }
        }
    }


    private void showResetConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("설정 초기화");
        alert.setHeaderText(null);
        alert.setContentText("정말 기본 설정으로 되돌리시겠습니까?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                resetSettings();
            }
        });
    }

    private void showScoreResetConfirmation() {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("기록 초기화");
        alert.setHeaderText(null);
        User user = SessionManager.getCurrentUser();
        alert.setContentText("정말 "+user.getNickname()+"님의 모든 기록을 초기화하시겠습니까?");

        alert.showAndWait().ifPresent(result -> {
            if (result == ButtonType.OK) {
                resetScoreSettings();
            }
        });
    }

    private void resetSettings() {
        // 색맹 모드 초기화
        isColorBlindModeOn = 100;
        colorBlindModeToggle.setSelected(false);
        colorBlindModeToggle.setText("off");

        // 화면 크기 초기화

        resizeMenuButton.setText("450 x 600");
        sizeConstants.setSize(450, 600);
        settings.updateAndSaveKey("windowWidth", "450");
        settings.updateAndSaveKey("windowHeight", "600");

        // 키 설정 초기화
        settings.setP1rightKey("RIGHT");
        settings.updateAndSaveKey("RIGHT", "RIGHT");
        settings.setP1downKey("DOWN");
        settings.updateAndSaveKey("DOWN", "DOWN");
        settings.setP1leftKey("LEFT");
        settings.updateAndSaveKey("LEFT", "LEFT");
        settings.setP1upKey("UP");
        settings.updateAndSaveKey("UP", "UP");
        settings.setSpaceKey("SPACE");
        settings.updateAndSaveKey("SPACE", "SPACE");

        // 난이도 설정 초기화
        levelButton.setText("Normal");
        LevelConstants.setLevel('N');
        settings.updateAndSaveKey("level", "N");

        // 다른 설정들도 초기화하는 코드 추가
        System.out.println("기본 설정으로 되돌림");
        settings.printSettings();
        System.out.println(settings.getWindowWidth());
        System.out.println(settings.getWindowHeight());
        System.out.println(settings.getP1upKey());
        System.out.println(settings.getP1downKey());
        System.out.println(settings.getP1leftKey());
        System.out.println(settings.getP1rightKey());
        System.out.println(settings.getSpaceKey());
        System.out.println(settings.getLevel());
    }

    private void resetScoreSettings() {
        User user = SessionManager.getCurrentUser();
        String message = JdbcConnecter.deleteUserByNickname(user.getNickname());
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setHeaderText(message);
        alert.showAndWait();
    }
}