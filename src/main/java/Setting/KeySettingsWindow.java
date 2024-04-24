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

public class KeySettingsWindow extends Application {
    private SettingsWindow settingsWindow;

    private Label rightKeyLabel;
    private Label downKeyLabel;
    private Label leftKeyLabel;
    private Label upKeyLabel;
    private Label spaceKeyLabel;

    private KeyCode currentlyEditingKeyCode;

    public KeySettingsWindow(SettingsWindow settingsWindow) {
        this.settingsWindow = settingsWindow;
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
        grid.add(new Label("Right Key:"), 0, 0);
        rightKeyLabel = new Label(KeySettings.getRightKey());
        rightKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.RIGHT));
        grid.add(rightKeyLabel, 1, 0);

        grid.add(new Label("Down Key:"), 0, 1);
        downKeyLabel = new Label(KeySettings.getDownKey());
        downKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.DOWN));
        grid.add(downKeyLabel, 1, 1);

        grid.add(new Label("Left Key:"), 0, 2);
        leftKeyLabel = new Label(KeySettings.getLeftKey());
        leftKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.LEFT));
        grid.add(leftKeyLabel, 1, 2);

        grid.add(new Label("Up Key:"), 0, 3);
        upKeyLabel = new Label(KeySettings.getUpKey());
        upKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.UP));
        grid.add(upKeyLabel, 1, 3);

        grid.add(new Label("Space Key:"), 0, 4);
        spaceKeyLabel = new Label(KeySettings.getSpaceKey());
        spaceKeyLabel.setOnMouseClicked(event -> startEditingKey(KeyCode.SPACE));
        grid.add(spaceKeyLabel, 1, 4);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);

        stage.initModality(Modality.APPLICATION_MODAL);
        scene.setOnKeyPressed(this::handleKeyPressed);
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

        Label duplicateKeyLabel = new Label();
        duplicateKeyLabel.setStyle("-fx-text-fill: red;");
        grid.add(duplicateKeyLabel, 0, 2, 2, 1);

        Button saveButton = new Button("Save");
        saveButton.setOnAction(event -> {
            KeyCode newKeyCode = newKeyValueLabel.getText().isEmpty() ? keyCode : KeyCode.valueOf(newKeyValueLabel.getText());
            if (isKeyCodeDuplicated(newKeyCode, keyCode)) {
                duplicateKeyLabel.setText("The selected key is already in use. Please choose a different key.");
            } else {
                updateKeySettings(currentlyEditingKeyCode, newKeyCode);
                updateKeyLabelText(currentlyEditingKeyCode, newKeyCode);
                currentlyEditingKeyCode = null;
                stage.close();
            }
        });
        grid.add(saveButton, 0, 3, 2, 1);

        Scene scene = new Scene(grid, 300, 200);
        stage.setScene(scene);

        scene.setOnKeyPressed(event -> {
            KeyCode newKeyCode = event.getCode();
            newKeyValueLabel.setText(newKeyCode.getName());
            duplicateKeyLabel.setText("");
        });
        stage.showAndWait();
    }

    private boolean isKeyCodeDuplicated(KeyCode newKeyCode, KeyCode currentKeyCode) {
        return (newKeyCode.equals(KeySettings.getUpKey()) && !currentKeyCode.equals(KeyCode.UP)) ||
                (newKeyCode.equals(KeySettings.getDownKey()) && !currentKeyCode.equals(KeyCode.DOWN)) ||
                (newKeyCode.equals(KeySettings.getLeftKey()) && !currentKeyCode.equals(KeyCode.LEFT)) ||
                (newKeyCode.equals(KeySettings.getRightKey()) && !currentKeyCode.equals(KeyCode.RIGHT)) ||
                (newKeyCode.equals(KeySettings.getSpaceKey()) && !currentKeyCode.equals(KeyCode.SPACE));
    }

    private void showDuplicateKeyAlert() {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Duplicate Key");
        alert.setContentText("The selected key is already in use. Please choose a different key.");
        alert.showAndWait();
    }

    private String getCurrentKeyLabel(KeyCode keyCode) {
        switch (keyCode) {
            case UP:
                return "Up";
            case LEFT:
                return "Left";
            case DOWN:
                return "Down";
            case RIGHT:
                return "Right";
            case SPACE:
                return "Space";
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
        switch (oldKeyCode) {
            case UP:
                upKeyLabel.setText(newKeyCode.getName());
                break;
            case LEFT:
                leftKeyLabel.setText(newKeyCode.getName());
                break;
            case DOWN:
                downKeyLabel.setText(newKeyCode.getName());
                break;
            case RIGHT:
                rightKeyLabel.setText(newKeyCode.getName());
                break;
            case SPACE:
                spaceKeyLabel.setText(newKeyCode.getName());
                break;
            default:
                break;
        }
    }

    public void updateKeySettings(KeyCode oldKeyCode, KeyCode newKeyCode) {
        switch (oldKeyCode) {
            case UP:
                KeySettings.setUpKey(newKeyCode.toString());
                upKeyLabel.setText(KeySettings.getUpKey());
                break;
            case LEFT:
                KeySettings.setLeftKey(newKeyCode.toString());
                leftKeyLabel.setText(KeySettings.getLeftKey());
                break;
            case DOWN:
                KeySettings.setDownKey(newKeyCode.toString());
                downKeyLabel.setText(KeySettings.getDownKey());
                break;
            case RIGHT:
                KeySettings.setRightKey(newKeyCode.toString());
                rightKeyLabel.setText(KeySettings.getRightKey());
                break;
            case SPACE:
                KeySettings.setSpaceKey(newKeyCode.toString());
                spaceKeyLabel.setText(KeySettings.getSpaceKey());
                break;
            default:
                // 기타 키에 대한 처리 추가
                break;
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}