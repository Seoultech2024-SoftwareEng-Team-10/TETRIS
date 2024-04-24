package ScoreBoard;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class ScoreboardConnector {
    private static final Properties props = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = ScoreboardConnector.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (input == null) {
                throw new RuntimeException("application.properties 를 찾을 수 없습니다.");
            }
            props.load(input);
        } catch (IOException ex) {
            throw new RuntimeException("application.properties 를 로드하는데 실패했습니다.", ex);
        }
    }

    public static void insertData(String nicknameParam, int scoreParam, String timeParam, int linesCountParam) {
        String query = "INSERT INTO scoreboard (nickname, score, time, lines_count, date) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 샘플 데이터 삽입
            pstmt.setString(1, nicknameParam);
            pstmt.setInt(2, scoreParam);
            pstmt.setString(3, "00:00:00");
            pstmt.setInt(4, linesCountParam);
            pstmt.setDate(5, Date.valueOf(LocalDate.now()));
            pstmt.executeUpdate();

            System.out.println("insert data to scoreboard successfully");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static List<ScoreRecord> fetchData(int page) {
        List<ScoreRecord> records = new ArrayList<>();
        int pageSize = 20; // 한 페이지당 데이터 수
        int offset = (page - 1) * pageSize; // SQL 쿼리에서 사용할 OFFSET 계산

        String query = "SELECT nickname, score, time, lines_count, date FROM scoreboard ORDER BY score DESC LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    int score = rs.getInt("score");
                    String time = rs.getString("time");
                    int linesCount = rs.getInt("lines_count");
                    LocalDate date = rs.getDate("date").toLocalDate();

                    records.add(new ScoreRecord(nickname, score, time, linesCount, date));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return records;
    }

}