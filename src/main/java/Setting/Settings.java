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
    private int windowWidth;
    private int windowHeight;
    private String rightKey;
    private String leftKey;
    private String upKey;
    private String downKey;
    private String spaceKey;
    private char level;
    private KeySettingsWindow keySettingsWindow;

    public Settings() throws IOException {
        try{
            InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/key.yml"));
            Yaml yaml = new Yaml(new Constructor(Config.class));
            Config config = yaml.load(inputStream);
            this.windowWidth = config.getWindowWidth();
            this.windowHeight = config.getWindowHeight();
            this.rightKey = config.getRightKey();
            this.leftKey = config.getLeftKey();
            this.upKey = config.getUpKey();
            this.downKey =config.getDownKey();
            this.spaceKey = config.getSpaceKey();
            this.level = config.getLevel();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public Settings(String upKey, String downKey, String rightKey, String leftKey) throws IOException {
        try{
            InputStream inputStream = Files.newInputStream(Paths.get("src/main/resources/key.yml"));
            Yaml yaml = new Yaml(new Constructor(Config.class));
            Config config = yaml.load(inputStream);
            this.windowWidth = config.getWindowWidth();
            this.windowHeight = config.getWindowHeight();
            this.rightKey = rightKey;
            this.leftKey = leftKey;
            this.upKey = upKey;
            this.downKey =downKey;
            this.spaceKey = config.getSpaceKey();
            this.level = config.getLevel();
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void updateAndSaveKey(String keyType, String newValue) {
        try {
            switch (keyType.toLowerCase()) {
                case "right":
                    this.rightKey = newValue;
                    break;
                case "left":
                    this.leftKey = newValue;
                    break;
                case "up":
                    this.upKey = newValue;
                    break;
                case "down":
                    this.downKey = newValue;
                    break;
                case "space":
                    this.spaceKey = newValue;
                    break;
                case "windowwidth":
                    this.windowWidth = Integer.parseInt(newValue);
                    break;
                case "windowheight":
                    this.windowHeight = Integer.parseInt(newValue);
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
            data.put("rightKey", this.rightKey);
            data.put("leftKey", this.leftKey);
            data.put("upKey", this.upKey);
            data.put("downKey", this.downKey);
            data.put("spaceKey", this.spaceKey);
            data.put("level", String.valueOf(this.level));
            yaml.dump(data, writer);
        } catch (IOException e) {
            System.out.println("Failed to save settings: " + e.getMessage());
        }
    }

    public void printSettings() {
        System.out.println("Current Key Bindings:");
        System.out.println("Right Key: " + this.rightKey);
        System.out.println("Left Key: " + this.leftKey);
        System.out.println("Up Key: " + this.upKey);
        System.out.println("Down Key: " + this.downKey);
        System.out.println("Space Key: " + this.spaceKey);
        System.out.println("width: "+this.windowWidth);
        System.out.println("height: "+this.windowHeight);
    }
}