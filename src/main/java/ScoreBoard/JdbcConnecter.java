package ScoreBoard;

import User.User;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.*;

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

    public static void insertData(String loginIdParam, String nicknameParam, int scoreParam, int modeParam, int levelParam, int linesCountParam) {
        String query = "INSERT INTO scoreboard (nickname, score, mode, level, lines_count, date, loginId) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            // Set the timeout for the query execution (in seconds)
            pstmt.setQueryTimeout(30);  // Set a 30-second timeout. Adjust as necessary.

            // Insert sample data
            pstmt.setString(1, nicknameParam);
            pstmt.setInt(2, scoreParam);
            pstmt.setInt(3, modeParam);
            pstmt.setInt(4, levelParam);
            pstmt.setInt(5, linesCountParam);
            pstmt.setDate(6, Date.valueOf(LocalDate.now()));
            pstmt.setString(7, loginIdParam);
            pstmt.executeUpdate();

            System.out.println("insert data to scoreboard successfully");

        } catch (SQLTimeoutException e) {
            System.err.println("The operation timed out. Please try again later.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("A database error occurred.");
            e.printStackTrace();
        }
    }



    public static List<ScoreRecord> fetchData(int page) {
        List<ScoreRecord> records = new ArrayList<>();
        int pageSize = 20; // 한 페이지당 데이터 수
        int offset = (page - 1) * pageSize; // SQL 쿼리에서 사용할 OFFSET 계산

        String query = "SELECT nickname, score, mode, level, lines_count, date FROM scoreboard ORDER BY score DESC LIMIT ? OFFSET ?";

        // 연결 타임아웃 설정
        int connectionTimeoutSeconds = 10;
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Future<Connection> future = executor.submit(() -> {
            return DriverManager.getConnection(props.getProperty("database.url"), props.getProperty("database.user"), props.getProperty("database.password"));
        });

        try (Connection conn = future.get(connectionTimeoutSeconds, TimeUnit.SECONDS);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setQueryTimeout(10); // 쿼리 실행 타임아웃 설정 (10초)
            pstmt.setInt(1, pageSize);
            pstmt.setInt(2, offset);

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    String nickname = rs.getString("nickname");
                    int score = rs.getInt("score");
                    int mode = rs.getInt("mode");
                    int level = rs.getInt("level");
                    int linesCount = rs.getInt("lines_count");
                    LocalDate date = rs.getDate("date").toLocalDate();
                    String enLevel = "", enMode = "";
                    if (level == 69) {
                        enLevel = "easy";
                    } else if (level == 78) {
                        enLevel = "normal";
                    } else if (level == 72) {
                        enLevel = "hard";
                    } else {
                        enLevel = Integer.toString(level);
                    }
                    if (mode == 0) {
                        enMode = "NormalMode";
                    } else {
                        enMode = "ItemMode";
                    }

                    records.add(new ScoreRecord(nickname, score, enMode, enLevel, linesCount, date));
                }
            }
        } catch (TimeoutException e) {
            System.err.println("JDBC 연결 timeout");
            records.add(new ScoreRecord("Timeout", 0, "N/A", "N/A", 0, LocalDate.now()));
        } catch (SQLException e) {
            System.err.println("DB query error");
            e.printStackTrace();
        } catch (InterruptedException | ExecutionException e) {
            System.err.println("An error occurred while trying to connect to the database.");
            e.printStackTrace();
        } finally {
            executor.shutdown();
        }

        return records;
    }

    public static List<ScoreRecord> fetchDataByMode(int modeParam, int page) {
        List<ScoreRecord> records = new ArrayList<>();
        int pageSize = 20; // 한 페이지당 데이터 수
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

                    records.add(new ScoreRecord(nickname, score, enMode,enLevel, linesCount, date));
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


}