package Animation.ScoreBoard;

import java.util.ArrayList;
import java.util.List;

public class ScoreBoard {
    private List<ScoreRecord> records;

    // 기본 생성자는 비어있는 리스트로 초기화
    public ScoreBoard() {
        this.records = new ArrayList<>();
    }

    // 초기 레코드 리스트를 사용하여 ScoreBoard를 초기화하는 생성자
    public ScoreBoard(List<ScoreRecord> initialRecords) {
        this.records = new ArrayList<>(initialRecords);
    }

    // 스코어 레코드를 추가하는 메서드
    public void addRecord(ScoreRecord record) {
        records.add(record);
    }

    // 저장된 모든 스코어 레코드를 반환하는 메서드
    public List<ScoreRecord> getRecords() {
        return records;
    }

    // 스코어보드를 문자열로 표현하는 메서드
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("ScoreBoard:\n");
        for (ScoreRecord record : records) {
            sb.append(record.toString()).append("\n");
        }
        return sb.toString();
    }

    // 여기에 기타 스코어보드 관련 메서드들을 추가할 수 있음
}
