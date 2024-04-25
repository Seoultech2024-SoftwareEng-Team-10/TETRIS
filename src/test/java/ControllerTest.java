import Setting.SizeConstants;
import Tetris.Controller;
import Tetris.Form;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class ControllerTest {
    private Form form;
    private static final int[][] MESH = new int[SizeConstants.XMAX / SizeConstants.SIZE][SizeConstants.YMAX / SizeConstants.SIZE];

    @BeforeEach
    public void setup() {
        form = new Form(new Text(SizeConstants.SIZE, 0, "O"), new Text(2 * SizeConstants.SIZE, 0, "O"),
                new Text(3 * SizeConstants.SIZE, 0, "O"), new Text(4 * SizeConstants.SIZE, 0, "O"), "testForm");
        // 초기 MESH 설정
        for (int[] row : MESH) {
            Arrays.fill(row, 0);
        }
    }

    @Test
    public void testMoveRight() {
        // 가장 오른쪽 위치에 있지 않은 경우
        Controller.MoveRight(form);
        assertEquals(2 * SizeConstants.SIZE, form.a.getX());
        // 경계 검사
        form.a.setX(SizeConstants.XMAX - SizeConstants.SIZE);
        form.b.setX(SizeConstants.XMAX - SizeConstants.SIZE);
        form.c.setX(SizeConstants.XMAX - SizeConstants.SIZE);
        form.d.setX(SizeConstants.XMAX - SizeConstants.SIZE);
        Controller.MoveRight(form);
        //이동했을때의 위치가 바뀐지 확인ㄷ
        assertEquals(SizeConstants.XMAX - SizeConstants.SIZE, form.a.getX());
    }

    @Test
    public void testMoveLeft() {
        // 왼쪽으로 이동 가능한 경우
        Controller.MoveLeft(form);
        assertEquals(0, form.a.getX());
        // 왼쪽 경계 검사
        form.a.setX(0);
        form.b.setX(0);
        form.c.setX(0);
        form.d.setX(0);
        Controller.MoveLeft(form);
        assertEquals(0, form.a.getX());
    }

    @Test
    public void testWaitingTextMake() {
        boolean colorBlindMode = true; // 색맹 모드 활성화
        char difficultyLevel = 'E'; // 난이도 설정
        Form resultForm = Controller.waitingTextMake(colorBlindMode, difficultyLevel);
        assertNotNull(resultForm);
        //watitingTextMake 위치검사
        assertTrue(resultForm.a.getX() > SizeConstants.XMAX);
        assertTrue(resultForm.a.getY() > 0);
    }
}
