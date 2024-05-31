package Setting;


import lombok.Getter;
import lombok.Setter;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map;

@Getter
@Setter
public class Settings {
    private int isColorBlindModeOn;
    private int windowWidth;
    private int windowHeight;
    private String p1rightKey;
    private String p1leftKey;
    private String p1upKey;
    private String p1downKey;
    private String spaceKey;
    private String p2rightKey;
    private String p2leftKey;
    private String p2upKey;
    private String p2downKey;
    private String shiftKey;
    private char level;
    private KeySettingsWindow keySettingsWindow;

    public Settings() throws IOException {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("key.yml")) {
            if (inputStream == null) {
                throw new FileNotFoundException("key.yml not found in resources");
            }
            Config config = yaml.load(inputStream);
            this.isColorBlindModeOn = config.getIsColorBlindModeOn();
            this.windowWidth = config.getWindowWidth();
            this.windowHeight = config.getWindowHeight();
            this.p1rightKey = config.getP1rightKey();
            this.p1leftKey = config.getP1leftKey();
            this.p1upKey = config.getP1upKey();
            this.p1downKey = config.getP1downKey();
            this.spaceKey = config.getSpaceKey();
            this.p2rightKey = config.getP2rightKey();
            this.p2leftKey = config.getP2leftKey();
            this.p2upKey = config.getP2upKey();
            this.p2downKey = config.getP2downKey();
            this.shiftKey = config.getShiftKey();
            this.level = config.getLevel();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void updateAndSaveKey(String keyType, String newValue) {
        try {
            switch (keyType.toLowerCase()) {
                case "iscolorblindmodeon":
                    this.isColorBlindModeOn = Integer.parseInt(newValue);
                    break;
                case "right":
                    this.p1rightKey = newValue;
                    break;
                case "left":
                    this.p1leftKey = newValue;
                    break;
                case "up":
                    this.p1upKey = newValue;
                    break;
                case "down":
                    this.p1downKey = newValue;
                    break;
                case "space":
                    this.spaceKey = newValue;
                    break;
                case "d":
                    this.p2rightKey = newValue;
                    break;
                case "a":
                    this.p2leftKey = newValue;
                    break;
                case "w":
                    this.p2upKey = newValue;
                    break;
                case "s":
                    this.p2downKey = newValue;
                    break;
                case "shift":
                    this.shiftKey = newValue;
                    break;
                case "windowwidth":
                    this.windowWidth = Integer.parseInt(newValue);
                    break;
                case "windowheight":
                    this.windowHeight = Integer.parseInt(newValue);
                    break;
                case "level":
                    this.level = newValue.charAt(0);
                    break;
                default:
                    System.out.println("Unknown key type or screen setting: " + keyType);
                    return;
            }
            saveToYaml();
        } catch (NumberFormatException e) {
            System.out.println("Error parsing number for screen settings: " + e.getMessage());
        }
    }

    // 설정을 YAML 파일에 저장하는 메서드
    private void saveToYaml() {
        String userHome = System.getProperty("user.home");  // 사용자의 홈 디렉토리 경로를 얻음
        File configFile = new File(userHome, "myAppSettings.yml");  // 설정 파일명을 myAppSettings.yml로 정의

        Map<String, Object> data = new LinkedHashMap<>();  // 설정 데이터를 저장할 맵
        data.put("isColorBlindModeOn", this.isColorBlindModeOn);
        data.put("windowWidth", this.windowWidth);
        data.put("windowHeight", this.windowHeight);
        data.put("p1rightKey", this.p1rightKey);
        data.put("p1leftKey", this.p1leftKey);
        data.put("p1upKey", this.p1upKey);
        data.put("p1downKey", this.p1downKey);
        data.put("spaceKey", this.spaceKey);
        data.put("p2rightKey", this.p2rightKey);
        data.put("p2leftKey", this.p2leftKey);
        data.put("p2upKey", this.p2upKey);
        data.put("p2downKey", this.p2downKey);
        data.put("shiftKey", this.shiftKey);
        data.put("level", String.valueOf(this.level));

        Yaml yaml = new Yaml();
        try (BufferedWriter writer = Files.newBufferedWriter(configFile.toPath())) {
            yaml.dump(data, writer);  // 맵 데이터를 YAML 형식으로 변환하여 파일에 쓴다
        } catch (IOException e) {
            System.out.println("Failed to save settings: " + e.getMessage());  // 파일 저장 중 오류 발생 시 로그 출력
        }
    }


    public void printSettings() {
        System.out.println("Current Key Bindings:");
        System.out.println("isColorBlindModeOn: " + this.isColorBlindModeOn);
        System.out.println("P1 Right Key: " + this.p1rightKey);
        System.out.println("P1 Left Key: " + this.p1leftKey);
        System.out.println("P1 Up Key: " + this.p1upKey);
        System.out.println("P1 Down Key: " + this.p1downKey);
        System.out.println("Space Key: " + this.spaceKey);
        System.out.println("P2 Right Key: " + this.p2rightKey);
        System.out.println("P2 Left Key: " + this.p2leftKey);
        System.out.println("P2 Up Key: " + this.p2upKey);
        System.out.println("P2 Down Key: " + this.p2downKey);
        System.out.println("Shift Key: " + this.shiftKey);
        System.out.println("width: "+this.windowWidth);
        System.out.println("height: "+this.windowHeight);
        System.out.println("Level: "+this.level);
    }
}