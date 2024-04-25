package ScoreBoard;

import User.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class JdbcConnecter {
    private static final Properties props = new Properties();

    static {
        loadProperties();
    }

    private static void loadProperties() {
        try (InputStream input = JdbcConnecter.class.getClassLoader().getResourceAsStream("application.properties")) {
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
    public static String CreateUser(User user) {
        String queryCheck = "SELECT COUNT(*) AS count FROM user WHERE loginId = ? OR nickname = ?";
        String queryInsert = "INSERT INTO user (loginId, nickname, password) VALUES (?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmtCheck = conn.prepareStatement(queryCheck);
             PreparedStatement pstmtInsert = conn.prepareStatement(queryInsert)) {

            // 이미 존재하는 로그인 아이디나 닉네임인지 확인
            pstmtCheck.setString(1, user.getLoginId());
            pstmtCheck.setString(2, user.getNickname());
            ResultSet rs = pstmtCheck.executeQuery();
            rs.next();
            int count = rs.getInt("count");
            rs.close();

            if (count > 0) {
                // 이미 존재하는 로그인 아이디나 닉네임이 있는 경우
                return "로그인아이디 혹은 닉네임이 중복되었습니다.";
            } else {
                // 존재하지 않는 경우, 사용자 데이터 삽입
                pstmtInsert.setString(1, user.getLoginId());
                pstmtInsert.setString(2, user.getNickname());
                pstmtInsert.setString(3, user.getPassword());
                pstmtInsert.executeUpdate();
                System.out.println("Inserted data to user successfully");
                return "Success";
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Failure";
        }
    }

    public static User SuccessLogin(String loginId, String password){
        String query = "SELECT * FROM user WHERE loginId=? AND password=?";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 샘플 데이터 삽입
            pstmt.setString(1, loginId);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String nickname = rs.getString("nickname");
                User user = new User(loginId, nickname, password);
                return user;

            } else {
                return null;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
            return null;
    }
    public static String deleteUserByNickname(String nickname) {
        String query = "DELETE FROM user WHERE nickname = ?";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, nickname);
            pstmt.executeUpdate();

            return "success";

        } catch (SQLException e) {
            e.printStackTrace();
            return "fail";
        }
    }


}