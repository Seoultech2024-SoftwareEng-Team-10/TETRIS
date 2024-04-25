package ScoreBoard;

import java.time.LocalDate;

public class ScoreRecord {
    private String nickname;
    private int score;
    private String mode;
    private String level;
    private int linesCount; // 제거한 라인 수
    private LocalDate date; // 기록 날짜

    // 기본 생성자
    public ScoreRecord(String nickname, int score, String mode,  String level ,int linesCount, LocalDate date) {
        this.nickname = nickname;
        this.score = score;
        this.mode = mode;
        this.level = level;
        this.linesCount = linesCount;
        this.date = date;
    }

    public String getNickname() {
        return nickname;
    }

    public int getScore() {
        return score;
    }

    public String getMode() {
        return mode;
    }

    public String getLevel() {
        return level;
    }

    public int getLinesCount() {
        return linesCount;
    }

    public LocalDate getDate() {
        return date;
    }
}
