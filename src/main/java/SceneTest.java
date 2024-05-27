import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class SceneTest extends Application {

    @Override
    public void start(Stage primaryStage) {
        // 왼쪽 Group 생성
        Group leftGroup = new Group();
        Button leftButton = new Button("Left Button");
        leftGroup.getChildren().add(leftButton);

        // 오른쪽 Group 생성
        Group rightGroup = new Group();
        Button rightButton = new Button("Right Button");
        rightGroup.getChildren().add(rightButton);

        // HBox를 사용하여 두 Group을 수평으로 배열
        HBox hbox = new HBox(20); // 20은 두 Group 사이의 간격
        hbox.getChildren().addAll(leftGroup, rightGroup);

        // Scene 생성 및 설정
        Scene scene = new Scene(hbox, 400, 200); // 너비 400, 높이 200
        primaryStage.setScene(scene);
        primaryStage.setTitle("Group Layout Example");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
