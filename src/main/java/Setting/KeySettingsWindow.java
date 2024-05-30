package Setting;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class KeySettingsWindow extends Application {
    private SettingsWindow settingsWindow;
    private Settings settings;

    public static Label p1rightKeyLabel;
    public static Label p1downKeyLabel;
    public static Label p1leftKeyLabel;
    public static Label p1upKeyLabel;
    public static Label spaceKeyLabel;
    public static Label p2rightKeyLabel;
    public static Label p2downKeyLabel;
    public static Label p2leftKeyLabel;
    public static Label p2upKeyLabel;
    public static Label shiftKeyLabel;

    private KeyCode currentlyEditingKeyCode;

    public KeySettingsWindow(SettingsWindow settingsWindow, Settings settings) {
        this.settingsWindow = settingsWindow;
        this.settings = settings;
    }

    @Override
    public void start(Stage stage) {
        stage.setTitle("Key Settings");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        // 현재 설정된 키 정보를 표시하는 라벨 생성
        grid.add(new Label("P1 Right Key:"), 0, 0);
        p1rightKeyLabel = new Label(settings.getP1rightKey());
        p1rightKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP1rightKey())));
        grid.add(p1rightKeyLabel, 1, 0);

        grid.add(new Label("P1 Down Key:"), 0, 1);
        p1downKeyLabel = new Label(settings.getP1downKey());
        p1downKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP1downKey())));
        grid.add(p1downKeyLabel, 1, 1);

        grid.add(new Label("P1 Left Key:"), 0, 2);
        p1leftKeyLabel = new Label(settings.getP1leftKey());
        p1leftKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP1leftKey())));
        grid.add(p1leftKeyLabel, 1, 2);

        grid.add(new Label("P1 Up Key:"), 0, 3);
        p1upKeyLabel = new Label(settings.getP1upKey());
        p1upKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP1upKey())));
        grid.add(p1upKeyLabel, 1, 3);

        grid.add(new Label("Space Key:"), 0, 4);
        spaceKeyLabel = new Label(settings.getSpaceKey());
        spaceKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getSpaceKey())));
        grid.add(spaceKeyLabel, 1, 4);

        grid.add(new Label("P2 Right Key:"), 5, 0);
        p2rightKeyLabel = new Label(settings.getP2rightKey());
        p2rightKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP2rightKey())));
        grid.add(p2rightKeyLabel, 6, 0);

        grid.add(new Label("P2 Down Key:"), 5, 1);
        p2downKeyLabel = new Label(settings.getP2downKey());
        p2downKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP2downKey())));
        grid.add(p2downKeyLabel, 6, 1);

        grid.add(new Label("P2 Left Key:"), 5, 2);
        p2leftKeyLabel = new Label(settings.getP2leftKey());
        p2leftKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP2leftKey())));
        grid.add(p2leftKeyLabel, 6, 2);

        grid.add(new Label("P2 Up Key:"), 5, 3);
        p2upKeyLabel = new Label(settings.getP2upKey());
        p2upKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getP2upKey())));
        grid.add(p2upKeyLabel, 6, 3);

        grid.add(new Label("Shift Key:"), 5, 4);
        shiftKeyLabel = new Label(settings.getShiftKey());
        shiftKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.valueOf(settings.getShiftKey())));
        grid.add(shiftKeyLabel, 6, 4);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            if (isKeyCodeDuplicated()) {
                showDuplicateKeyAlert();
            } else {
                stage.close(); // 저장 후 창 닫기
            }
        });
        grid.add(saveButton, 0, 5, 2, 1); // 저장 버튼 GridPane에 추가

        Scene scene = new Scene(grid, 450, 200);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        scene.setOnKeyPressed(this::handleKeyPressed);

        stage.setOnCloseRequest(event -> {
            if (isKeyCodeDuplicated()) {
                showDuplicateKeyAlert();
                event.consume(); // 창 닫힘 이벤트를 소비하여 창이 닫히지 않도록 함
            } else {
                event.consume(); // 창 닫힘 이벤트를 소비하여 창이 닫히도록 함
            }
        });

        stage.showAndWait();
    }

    private void startEditingKey(KeyCode keyCode) {
        currentlyEditingKeyCode = keyCode;
        // 사용자에게 새로운 키 입력을 요청하는 UI 표시
        Stage stage = new Stage();
        stage.setTitle("Set " + getCurrentKeyLabel(keyCode) + " Key");
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));

        Label currentKeyLabel = new Label("Current " + getCurrentKeyLabel(keyCode) + " Key: " + keyCode.getName());
        grid.add(currentKeyLabel, 0, 0);

        Label newKeyLabel = new Label("New Key: ");
        grid.add(newKeyLabel, 0, 1);
        Label newKeyValueLabel = new Label();
        grid.add(newKeyValueLabel, 1, 1);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            KeyCode newKeyCode = newKeyValueLabel.getText().isEmpty() ? keyCode : KeyCode.valueOf(newKeyValueLabel.getText());
            updateKeySettings(currentlyEditingKeyCode, newKeyCode);
            updateKeyLabelText(currentlyEditingKeyCode, newKeyCode);
            currentlyEditingKeyCode = null;
            settings.printSettings();
            stage.close();
        });
        grid.add(saveButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);

        scene.setOnKeyPressed(event -> {
            KeyCode newKeyCode = event.getCode();
            newKeyValueLabel.setText(newKeyCode.getName());
        });
        stage.showAndWait();
    }

    private boolean isKeyCodeDuplicated() {
        Set<String> keyCodes = new HashSet<>(Arrays.asList(
                settings.getP1rightKey(), settings.getP1downKey(), settings.getP1leftKey(), settings.getP1upKey(), settings.getSpaceKey(),
                settings.getP2rightKey(), settings.getP2downKey(), settings.getP2leftKey(), settings.getP2upKey(), settings.getShiftKey()
        ));
        return keyCodes.size() != 10;
    }

    private void showDuplicateKeyAlert() {
        Alert alert = new Alert(AlertType.WARNING);
        alert.setTitle("Duplicate Keys");
        alert.setHeaderText(null);
        alert.setContentText("중복되는 키가 있습니다! 다시 확인해주세요!");
        alert.showAndWait();
    }

    private String getCurrentKeyLabel(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                return "P1 Up";
            case LEFT:
                return "P1 Left";
            case DOWN:
                return "P1 Down";
            case RIGHT:
                return "P1 Right";
            case SPACE:
                return "Space";
            case W:
                return "P2 Right";
            case A:
                return "P2 Left";
            case S:
                return "P2 Down";
            case D:
                return "P2 Right";
            case SHIFT:
                return "Shift";
            default:
                return "";
        }
    }

    private void handleKeyPressed(KeyEvent event) {
        KeyCode keyCode = event.getCode();
        if (currentlyEditingKeyCode != null) {
            updateKeySettings(currentlyEditingKeyCode, keyCode);
            updateKeyLabelText(currentlyEditingKeyCode, keyCode);
            currentlyEditingKeyCode = null;
        }
    }

    private void updateKeyLabelText(KeyCode oldKeyCode, KeyCode newKeyCode) {

        if (oldKeyCode == KeyCode.valueOf(settings.getP1upKey())) {
            p1upKeyLabel.setText(newKeyCode.getName());
            settings.setP1upKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP1leftKey())) {
            p1leftKeyLabel.setText(newKeyCode.getName());
            settings.setP1leftKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP1downKey())) {
            p1downKeyLabel.setText(newKeyCode.getName());
            settings.setP1downKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP1rightKey())) {
            p1rightKeyLabel.setText(newKeyCode.getName());
            settings.setP1rightKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getSpaceKey())) {
            spaceKeyLabel.setText(newKeyCode.getName());
            settings.setSpaceKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP2upKey())) {
            p2upKeyLabel.setText(newKeyCode.getName());
            settings.setP2upKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP2leftKey())) {
            p2leftKeyLabel.setText(newKeyCode.getName());
            settings.setP2leftKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP2downKey())) {
            p2downKeyLabel.setText(newKeyCode.getName());
            settings.setP2downKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getP2rightKey())) {
            p2rightKeyLabel.setText(newKeyCode.getName());
            settings.setP2rightKey(newKeyCode.toString());
        } else if (oldKeyCode == KeyCode.valueOf(settings.getShiftKey())) {
            shiftKeyLabel.setText(newKeyCode.getName());
            settings.setShiftKey(newKeyCode.toString());
        }
    }

    public void updateKeySettings(KeyCode oldKeyCode, KeyCode newKeyCode) {
        String oldKey = oldKeyCode.getName().toUpperCase();
        if (oldKey.equals(settings.getP1upKey())) {
            settings.setP1upKey(newKeyCode.toString()); // Settings 인스턴스 업데이트
            p1upKeyLabel.setText(settings.getP1upKey());
            settings.updateAndSaveKey("UP", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP1leftKey())) {
            settings.setP1leftKey(newKeyCode.toString());
            p1leftKeyLabel.setText(settings.getP1leftKey());
            settings.updateAndSaveKey("LEFT", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP1downKey())) {
            settings.setP1downKey(newKeyCode.toString());
            p1downKeyLabel.setText(settings.getP1downKey());
            settings.updateAndSaveKey("DOWN", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP1rightKey())) {
            settings.setP1rightKey(newKeyCode.toString());
            p1rightKeyLabel.setText(settings.getP1rightKey());
            settings.updateAndSaveKey("RIGHT", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getSpaceKey())) {
            settings.setSpaceKey(newKeyCode.toString());
            spaceKeyLabel.setText(settings.getSpaceKey());
            settings.updateAndSaveKey("SPACE", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP2upKey())) {
            settings.setP2upKey(newKeyCode.toString()); // Settings 인스턴스 업데이트
            p2upKeyLabel.setText(settings.getP2upKey());
            settings.updateAndSaveKey("W", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP2leftKey())) {
            settings.setP2leftKey(newKeyCode.toString());
            p2leftKeyLabel.setText(settings.getP2leftKey());
            settings.updateAndSaveKey("A", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP2downKey())) {
            settings.setP2downKey(newKeyCode.toString());
            p2downKeyLabel.setText(settings.getP2downKey());
            settings.updateAndSaveKey("S", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getP2rightKey())) {
            settings.setP2rightKey(newKeyCode.toString());
            p2rightKeyLabel.setText(settings.getP2rightKey());
            settings.updateAndSaveKey("D", String.valueOf(newKeyCode));
        } else if (oldKey.equals(settings.getShiftKey())) {
            settings.setShiftKey(newKeyCode.toString());
            shiftKeyLabel.setText(settings.getShiftKey());
            settings.updateAndSaveKey("SHIFT", String.valueOf(newKeyCode));
        }
    }
    public static void main(String[] args) {
        launch(args);
    }
}