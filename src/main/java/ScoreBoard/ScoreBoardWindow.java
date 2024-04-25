package ScoreBoard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

import static Setting.SizeConstants.*;

public class ScoreBoardWindow {
    private Stage stage;
    private ScoreBoard scoreBoard;
    private int currentPage = 1;

    public ScoreBoardWindow(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        stage = new Stage();
        stage.setTitle("기록");

        TableView<ScoreRecord> table = new TableView<>();
        ObservableList<ScoreRecord> data = FXCollections.observableArrayList(scoreBoard.getRecords());

        // 컬럼 설정
        TableColumn<ScoreRecord, String> nicknameCol = new TableColumn<>("닉네임");
        nicknameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn<ScoreRecord, Integer> scoreCol = new TableColumn<>("점수");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<ScoreRecord, String> timeCol = new TableColumn<>("시간");
        timeCol.setCellValueFactory(new PropertyValueFactory<>("time"));
        TableColumn<ScoreRecord, Integer> linesCountCol = new TableColumn<>("라인 수");
        linesCountCol.setCellValueFactory(new PropertyValueFactory<>("linesCount"));
        TableColumn<ScoreRecord, String> dateCol = new TableColumn<>("날짜");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(nicknameCol, scoreCol, timeCol, linesCountCol, dateCol);
        table.setItems(data);

        // 페이지 네비게이션 버튼
        Button prevButton = new Button("< 이전");
        Button nextButton = new Button("다음 >");
        prevButton.setOnAction(e -> changePage(table, -1));
        nextButton.setOnAction(e -> changePage(table, 1));

        HBox navigationBox = new HBox(10, prevButton, nextButton);
        navigationBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(10); // 컴포넌트 간의 간격
        root.setAlignment(Pos.CENTER); // VBox 내용을 중앙 정렬
        root.getChildren().addAll(new Label("스코어 보드"), table, navigationBox);

        Scene scene = new Scene(root, XMAX + 150, YMAX - SIZE);
        stage.setScene(scene);
    }

    private void changePage(TableView<ScoreRecord> table, int delta) {
        System.out.println(delta);
        currentPage += delta;
        if (currentPage <= 0){
            currentPage = 1;
        }
        List<ScoreRecord> newRecords = JdbcConnecter.fetchData(currentPage);
        if (newRecords.isEmpty()) {
            currentPage -= delta; // 데이터가 없으면 페이지 번호 롤백
        } else {
            ObservableList<ScoreRecord> newData = FXCollections.observableArrayList(newRecords);
            table.setItems(newData);
        }
    }

    public void show() {
        stage.show();
    }
}
