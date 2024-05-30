//import ScoreBoard.JdbcConnecter;
//import User.User;
//import ScoreBoard.ScoreRecord;
//import org.junit.jupiter.api.Test;
//
//import java.sql.*;
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertNotNull;
//
//public class JdbcConnecterTest {
//
//
//    @Test
//    void testInsertData() throws SQLException {
//        JdbcConnecter.insertData("login123", "Nick123", 1000, 1, 10, 20, System.currentTimeMillis());
//    }
//    @Test
//    void testFetchData() throws SQLException {
//        List<ScoreRecord> result = JdbcConnecter.fetchData(1);
//        assertNotNull(result, "The result list should not be null");
//    }
//
//    @Test
//    void testFetchDataByMode() throws SQLException {
//        List<ScoreRecord> result = JdbcConnecter.fetchDataByMode(1, 1);
//    }
//    @Test
//    void testCreateUser() throws SQLException {
//        String result = JdbcConnecter.CreateUser(new User("login123", "Nick123", "password"));
//        assertNotNull(result, "The result string should not be null");
//    }
//
//    @Test
//    void testSuccessLogin() throws SQLException {
//        User result = JdbcConnecter.SuccessLogin("login123", "password");
//        assertNotNull(result, "The user should not be null");
//    }
//
//    @Test
//    void testDeleteUserByNickname() throws SQLException {
//        String result = JdbcConnecter.deleteUserByNickname("Nick123");
//        // 결과 문자열 검증
//    }
//    @Test
//    void testFetchPageOfUser() throws SQLException {
//        int result = JdbcConnecter.fetchPageOfUser("Nick123", System.currentTimeMillis());
//
//    }
//
//
//
//}
