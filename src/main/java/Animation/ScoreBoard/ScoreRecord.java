package Animation.ScoreBoard;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ScoreRecord {
    private String nickname;
    private int score;
    private String mode;
    private String level;
    private int linesCount; // 제거한 라인 수
    private LocalDate date; // 기록 날짜
    private long timeStamp; //ms

    // 기본 생성자
    public ScoreRecord(String nickname, int score, String mode,  String level ,int linesCount, LocalDate date, long timeStamp) {
        this.nickname = nickname;
        this.score = score;
        this.mode = mode;
        this.level = level;
        this.linesCount = linesCount;
        this.date = date;
        this.timeStamp = timeStamp;
    }
}
