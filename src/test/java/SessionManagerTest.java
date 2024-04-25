import User.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import User.SessionManager;


public class SessionManagerTest {
    @BeforeEach
    public void setUp() {
        SessionManager.setCurrentUser(null);
    }



    @Test
    public void testSetCurrentUser() {
        User expectedUser = new User("username", "nickname", "password");
        SessionManager.setCurrentUser(expectedUser);
        assertSame(expectedUser, SessionManager.getCurrentUser(),"The set user should be the same as the current user" );
    }

    @Test
    public void testGetCurrentUser() {
        User expectedUser = new User("username2", "nickname2", "password2");
        SessionManager.setCurrentUser(expectedUser);
        User actualUser = SessionManager.getCurrentUser();
        assertSame( expectedUser, actualUser,"The retrieved user should be the same as the set user");
    }
    @Test
    public void testUserInitialization() {
        assertNull(SessionManager.getCurrentUser(),"Initially, current user should be null");
        User newUser = new User("newuser", "newnickname", "newpassword");
        SessionManager.setCurrentUser(newUser);
        assertNotNull( SessionManager.getCurrentUser(),"After setting, current user should not be null");
        assertEquals( newUser, SessionManager.getCurrentUser(),"Check correct user is set");
    }


}
