package ScoreBoard;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class ScoreBoardWindow {
    private Stage stage;
    private ScoreBoard scoreBoard;
    private int currentPage = 1;
    private ComboBox<String> modeComboBox;

    public ScoreBoardWindow(ScoreBoard scoreBoard) {
        this.scoreBoard = scoreBoard;
        stage = new Stage();
        stage.setTitle("Scoreboard");

        TableView<ScoreRecord> table = new TableView<>();
        ObservableList<ScoreRecord> data = FXCollections.observableArrayList();

        // 초기 데이터 로딩
        List<ScoreRecord> initialData = JdbcConnecter.fetchData(currentPage);
        data.setAll(initialData);

        // ComboBox 설정
        modeComboBox = new ComboBox<>();
        modeComboBox.getItems().addAll("All Modes", "Normal Mode", "Item Mode"); // 모드 옵션 추가
        modeComboBox.setValue("All Modes"); // 기본값 설정

        // 모드 선택 이벤트 핸들러
        modeComboBox.setOnAction(e -> {
            String selectedMode = modeComboBox.getValue();
            switch (selectedMode) {
                case "All Modes":
                    data.setAll(JdbcConnecter.fetchData(currentPage));
                    break;
                case "Normal Mode":
                    data.setAll(JdbcConnecter.fetchDataByMode(0, currentPage));
                    break;
                case "Item Mode":
                    data.setAll(JdbcConnecter.fetchDataByMode(1, currentPage));
                    break;
            }
            table.setItems(data);
        });

        // 컬럼 설정
        TableColumn<ScoreRecord, String> nicknameCol = new TableColumn<>("Nickname");
        nicknameCol.setCellValueFactory(new PropertyValueFactory<>("nickname"));
        TableColumn<ScoreRecord, Integer> scoreCol = new TableColumn<>("Score");
        scoreCol.setCellValueFactory(new PropertyValueFactory<>("score"));
        TableColumn<ScoreRecord, Integer> modeCol = new TableColumn<>("Mode");
        modeCol.setCellValueFactory(new PropertyValueFactory<>("mode"));
        TableColumn<ScoreRecord, Integer> levelCol = new TableColumn<>("Level");
        levelCol.setCellValueFactory(new PropertyValueFactory<>("level"));
        TableColumn<ScoreRecord, Integer> linesCountCol = new TableColumn<>("Lines Count");
        linesCountCol.setCellValueFactory(new PropertyValueFactory<>("linesCount"));
        TableColumn<ScoreRecord, String> dateCol = new TableColumn<>("Date");
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));

        table.getColumns().addAll(nicknameCol, scoreCol, modeCol, levelCol, linesCountCol, dateCol);
        table.setItems(data);

        // 페이지 네비게이션 버튼
        Button prevButton = new Button("< Previous");
        Button nextButton = new Button("Next >");
        prevButton.setOnAction(e -> changePage(table, -1));
        nextButton.setOnAction(e -> changePage(table, 1));

        HBox navigationBox = new HBox(10, prevButton, nextButton);
        navigationBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(13); // 컴포넌트 간의 간격
        root.setAlignment(Pos.CENTER); // VBox 내용을 중앙 정렬
        root.getChildren().addAll(new Label("Scoreboard"), modeComboBox, table, navigationBox);

        Scene scene = new Scene(root, 500, 400); // Adjusted window size
        stage.setScene(scene);
    }

    private void changePage(TableView<ScoreRecord> table, int delta) {
        int newPage = currentPage + delta;
        List<ScoreRecord> newRecords;
        if ("All Modes".equals(modeComboBox.getValue())) {
            newRecords = JdbcConnecter.fetchData(newPage);
        } else if ("Normal Mode".equals(modeComboBox.getValue())) {
            newRecords = JdbcConnecter.fetchDataByMode(0, newPage);
        } else {
            newRecords = JdbcConnecter.fetchDataByMode(1, newPage);
        }

        if (!newRecords.isEmpty()) {
            currentPage = newPage;
            ObservableList<ScoreRecord> newData = FXCollections.observableArrayList(newRecords);
            table.setItems(newData);
        }
    }

    public void show() {
        stage.show();
    }
}
