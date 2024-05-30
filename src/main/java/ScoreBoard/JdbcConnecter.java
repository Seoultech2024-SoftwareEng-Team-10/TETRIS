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
    public static final Properties props = new Properties();

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

    public static void insertData(String loginIdParam, String nicknameParam, int scoreParam, int modeParam, int levelParam, int linesCountParam, long now) {
        String query = "INSERT INTO scoreboard (nickname, score, mode, level, lines_count, date, loginId, time_stamp) VALUES (?, ?, ?, ?, ?, ?,?,?)";
        System.out.println(now);
        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // 샘플 데이터 삽입
            pstmt.setString(1, nicknameParam);
            pstmt.setInt(2, scoreParam);
            pstmt.setInt(3, modeParam);
            pstmt.setInt(4, levelParam);
            pstmt.setInt(5, linesCountParam);
            pstmt.setDate(6, Date.valueOf(LocalDate.now()));
            pstmt.setString(7,loginIdParam);
            pstmt.setLong(8, now);
            pstmt.executeUpdate();


        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    public static List<ScoreRecord> fetchData(int page) {
        List<ScoreRecord> records = new ArrayList<>();
        int pageSize = 10; // 한 페이지당 데이터 수
        int offset = (page - 1) * pageSize; // SQL 쿼리에서 사용할 OFFSET 계산

        String query = "SELECT nickname, score, mode, level, lines_count, date, time_stamp FROM scoreboard ORDER BY score DESC LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    int score = rs.getInt("score");
                    int level = rs.getInt("level");
                    int mode = rs.getInt("mode");
                    int linesCount = rs.getInt("lines_count");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    long timeStamp = rs.getLong("time_stamp");
                    String enLevel = "";
                    String enMode = "";
                    if (level == 69){
                        enLevel = "easy";
                    }
                    else if(level==78){
                        enLevel = "normal";
                    }
                    else if(level ==72){
                        enLevel = "hard";
                    }
                    else{
                        enLevel = Integer.toString(level);
                    }
                    if(mode == 0){
                        enMode = "NormalMode";
                    }
                    else{
                        enMode = "ItemMode";
                    }
                    records.add(new ScoreRecord(nickname, score, enMode, enLevel, linesCount, date, timeStamp));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }
    public static List<ScoreRecord> fetchDataByMode(int modeParam, int page) {
        List<ScoreRecord> records = new ArrayList<>();
        int pageSize = 10; // 한 페이지당 데이터 수
        int offset = (page - 1) * pageSize; // SQL 쿼리에서 사용할 OFFSET 계산

        // mode를 필터링 조건으로 추가
        String query = "SELECT nickname, score, mode, level, lines_count, date FROM scoreboard WHERE mode = ? ORDER BY score DESC LIMIT ? OFFSET ?";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setInt(1, modeParam);
            pstmt.setInt(2, pageSize);
            pstmt.setInt(3, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    int score = rs.getInt("score");
                    int level = rs.getInt("level");
                    int mode = rs.getInt("mode");
                    int linesCount = rs.getInt("lines_count");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    Long timeStamp = rs.getLong("time_stamp");
                    String enLevel = "";
                    String enMode = "";
                    if (level == 69){
                        enLevel = "easy";
                    }
                    else if(level==78){
                        enLevel = "normal";
                    }
                    else if(level ==72){
                        enLevel = "hard";
                    }
                    else{
                        enLevel = Integer.toString(level);
                    }
                    if(mode == 0){
                        enMode ="NormalMode";
                    }
                    else{
                        enMode = "ItemMode";
                    }

                    records.add(new ScoreRecord(nickname, score, enMode,enLevel, linesCount, date,timeStamp));
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
        String query = "DELETE FROM scoreboard WHERE loginId = ?";

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

    public static int fetchPageOfUser(String name, long dateNow) {
        int pageSize = 10; // 한 페이지당 데이터 수
        String query = "SELECT nickname, time_stamp FROM scoreboard ORDER BY score DESC"; // 모든 데이터 가져오기
        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            try (ResultSet rs = pstmt.executeQuery()) {
                int position = 0; // 현재 사용자의 위치
                while (rs.next()) {
                    position++; // 모든 레코드에 대해 위치 증가
                    // 사용자의 이름과 타임스탬프가 일치하는지 확인
                    if (rs.getString("nickname").equals(name) && rs.getLong("time_stamp") == dateNow) {
                        System.out.println("Position of user: " + position);
                        return (position - 1) / pageSize + 1; // 페이지 번호 계산
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

}