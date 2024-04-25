import ScoreBoard.JdbcConnecter;
import User.User;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import java.sql.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class JdbcConnecterTest {

    private JdbcConnecter jdbcConnecter;

    @BeforeAll
    public void setup() {
        jdbcConnecter = new JdbcConnecter();
    }

    @Test
    public void testInsertData() {
        assertDoesNotThrow(() -> JdbcConnecter.insertData("testUser", 1000, 0, 1, 10),
                "Should not throw an exception when inserting data");
    }

    @Test
    public void testFetchData() {
        assertDoesNotThrow(() -> JdbcConnecter.fetchData(1),
                "Should not throw an exception when fetching data");
    }

    @Test
    public void testCreateUser() {
        User newUser = new User("loginId", "nickname", "password");
        String result = JdbcConnecter.CreateUser(newUser);
        assertEquals("Success", result, "User should be created successfully");
        // 이미 존재하는 사용자로 시도하는 경우
        result = JdbcConnecter.CreateUser(newUser);
        assertEquals("로그인아이디 혹은 닉네임이 중복되었습니다.", result, "Should return an error for duplicate loginId or nickname");
    }

    @Test
    public void testSuccessLoginSuccessful() {
        // 성공적인 로그인 테스트
        User loggedInUser = JdbcConnecter.SuccessLogin("loginId", "password");
        assertNotNull(loggedInUser, "Logged in user should not be null for valid credentials");
    }

    @Test
    public void testSuccessLoginFailure() {
        // 실패하는 로그인 테스트
        User loggedInUser = JdbcConnecter.SuccessLogin("wrongLoginId", "wrongPassword");
        assertNull(loggedInUser, "Should return null for invalid credentials");
    }

    @Test
    public void testDeleteUserByNickname() {
        String result = JdbcConnecter.deleteUserByNickname("testUser");
        assertEquals("success", result, "User should be deleted successfully");
    }

    @AfterEach
    public void cleanUpEach() {
        // 각 테스트 후 데이터 정리 로직
        deleteTestData();
    }

    @AfterAll
    public void tearDown() {
        // 모든 테스트 후 최종 데이터 정리
        deleteUserByLoginId("loginId"); // 예시 로그인 아이디
    }

    private void deleteTestData() {
        String deleteQuery = "DELETE FROM scoreboard WHERE nickname = 'testUser'";

        try (Connection conn = DriverManager.getConnection(
                JdbcConnecter.props.getProperty("database.url"),
                JdbcConnecter.props.getProperty("database.user"),
                JdbcConnecter.props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteUserByLoginId(String loginId) {
        String deleteQuery = "DELETE FROM user WHERE loginId = ?";

        try (Connection conn = DriverManager.getConnection(
                JdbcConnecter.props.getProperty("database.url"),
                JdbcConnecter.props.getProperty("database.user"),
                JdbcConnecter.props.getProperty("database.password"));
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, loginId);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
