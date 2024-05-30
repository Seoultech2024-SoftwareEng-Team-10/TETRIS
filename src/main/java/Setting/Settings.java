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
    //private boolean isColorBlindModeOn;
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
        try{
            InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/key.yml"));
            Yaml yaml = new Yaml(new Constructor(Config.class));
            Config config = yaml.load(inputStream);
            //this.isColorBlindModeOn = config.isColorBlindModeOn();
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
                //case "iscolorblindmodeon":
                    //this.isColorBlindModeOn = Boolean.parseBoolean(newValue);
                    //break;
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
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get("src/main/resources/key.yml"))) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = new LinkedHashMap<>();
            //data.put("isColorBlindModeOn", this.isColorBlindModeOn);
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
            yaml.dump(data, writer);
        } catch (IOException e) {
            System.out.println("Failed to save settings: " + e.getMessage());
        }
    }

    public void printSettings() {
        System.out.println("Current Key Bindings:");
        //System.out.println("isColorBlindModeOn: " + this.isColorBlindModeOn);
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