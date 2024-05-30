package Setting;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Config {
    private int windowWidth;
    private int windowHeight;
    private String rightKey;
    private String leftKey;
    private String upKey;
    private String downKey;
    private String spaceKey;
    private char level;
}