import static org.junit.jupiter.api.Assertions.*;

import Tetris.BlockColor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javafx.scene.paint.Color;

class BlockColorTest {

    @BeforeEach
    void setUp() {
        BlockColor.setColorBlindMode(false);  // 기본적으로 색맹 모드 비활성화
    }

    @Test
    void testGetColorNormalMode() {
        assertEquals(Color.SLATEGRAY, BlockColor.getColor("j"), "Check color for 'j' in normal mode");
        assertEquals(Color.DARKGOLDENROD, BlockColor.getColor("l"), "Check color for 'l' in normal mode");
        assertEquals(Color.INDIANRED, BlockColor.getColor("o"), "Check color for 'o' in normal mode");
        assertEquals(Color.FORESTGREEN, BlockColor.getColor("s"), "Check color for 's' in normal mode");
        assertEquals(Color.CADETBLUE, BlockColor.getColor("t"), "Check color for 't' in normal mode");
        assertEquals(Color.HOTPINK, BlockColor.getColor("z"), "Check color for 'z' in normal mode");
        assertEquals(Color.SANDYBROWN, BlockColor.getColor("i"), "Check color for 'i' in normal mode");
        assertNull(BlockColor.getColor("unknown"), "Check color for undefined block");
    }

    @Test
    void testGetColorColorBlindMode() {
        BlockColor.setColorBlindMode(true);  // 색맹 모드 활성화
        assertEquals(Color.RED, BlockColor.getColor("j"), "Check color for 'j' in color blind mode");
        assertEquals(Color.GREEN, BlockColor.getColor("l"), "Check color for 'l' in color blind mode");
        assertEquals(Color.BLUE, BlockColor.getColor("o"), "Check color for 'o' in color blind mode");
        assertEquals(Color.ORANGE, BlockColor.getColor("s"), "Check color for 's' in color blind mode");
        assertEquals(Color.PURPLE, BlockColor.getColor("t"), "Check color for 't' in color blind mode");
        assertEquals(Color.YELLOW, BlockColor.getColor("z"), "Check color for 'z' in color blind mode");
        assertEquals(Color.WHITE, BlockColor.getColor("i"), "Check color for 'i' in color blind mode");
    }

    @Test
    void testSetColorBlindMode() {
        BlockColor.setColorBlindMode(true);
        assertTrue(BlockColor.colorBlindMode, "Color blind mode should be true after setting to true");
        BlockColor.setColorBlindMode(false);
        assertFalse(BlockColor.colorBlindMode, "Color blind mode should be false after setting to false");
    }
}
