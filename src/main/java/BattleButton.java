import Setting.SizeConstants;
import Tetris.Controller;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.IOException;

public class BattleButton {
    public static void show(SizeConstants sizeConstants, Controller controller) {
        Stage stage = new Stage();
        stage.setTitle("2player");

        Button normalModeButton = new Button("일반모드");
        normalModeButton.setStyle("-fx-background-color: #add8e6; -fx-text-fill: #ffffff; -fx-font-size: 16px;");
        Button itemModeButton = new Button("아이템모드");
        itemModeButton.setStyle("-fx-background-color: #6495ed; -fx-text-fill: #ffffff; -fx-font-size: 16px;");
        Button timerModeButton = new Button("타이머모드");
        timerModeButton.setStyle("-fx-background-color: #6a5acd; -fx-text-fill: #ffffff; -fx-font-size: 16px;");

        VBox root = new VBox(20, normalModeButton, itemModeButton, timerModeButton); // 버튼 간격 20
        root.setAlignment(Pos.CENTER); // 가운데 정렬
        root.setPadding(new Insets(20)); // 안쪽 여백 20

        Scene scene = new Scene(root, 400, 300); // 창 크기 400x300

        normalModeButton.setOnAction(event -> {
            try {
                Stage gameStage = new Stage();
                BattleApplication battleApp = new BattleApplication(sizeConstants, controller);
                // HelloApplication의 start 메소드 호출
                battleApp.start(gameStage);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        stage.setScene(scene);
        stage.show();
    }
}
