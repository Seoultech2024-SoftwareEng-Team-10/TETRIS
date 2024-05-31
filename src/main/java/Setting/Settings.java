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
    private static final String CONFIG_PATH = System.getProperty("user.home") + File.separator + "myAppSettings.yml";

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
        File configFile = new File(CONFIG_PATH);

        if (configFile.exists()) {
            // If the external file exists, load settings from the external file
            loadSettingsFromFile(configFile, yaml);
        } else {
            // If the external file does not exist, load default settings from resource and create the file.
            try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("key.yml")) {
                if (inputStream == null) {
                    throw new FileNotFoundException("key.yml not found in resources");
                }
                Config config = yaml.load(inputStream);
                saveToYaml(config);  // Save default settings to the external file
                loadSettingsFromFile(configFile, yaml);  // Load the newly created external file
            }
        }
    }

    private void loadSettingsFromFile(File file, Yaml yaml) throws FileNotFoundException {
        try (InputStream inputStream = new FileInputStream(file)) {
            Config config = yaml.load(inputStream);
            assignSettings(config);
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to load settings from external file: " + e.getMessage());
        }
    }

    private void assignSettings(Config config) {
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
            saveToYaml(loadCurrentConfig());  // Save updated settings to the external file
        } catch (NumberFormatException | IOException e) {
            System.out.println("Error parsing number or saving file: " + e.getMessage());
        }
    }

    private void saveToYaml(Config config) throws IOException {
        Yaml yaml = new Yaml();
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(CONFIG_PATH))) {
            yaml.dump(config, writer);
        }
    }

    private Config loadCurrentConfig() {
        Config config = new Config();
        config.setIsColorBlindModeOn(this.isColorBlindModeOn);
        config.setWindowWidth(this.windowWidth);
        config.setWindowHeight(this.windowHeight);
        config.setP1rightKey(this.p1rightKey);
        config.setP1leftKey(this.p1leftKey);
        config.setP1upKey(this.p1upKey);
        config.setP1downKey(this.p1downKey);
        config.setSpaceKey(this.spaceKey);
        config.setP2rightKey(this.p2rightKey);
        config.setP2leftKey(this.p2leftKey);
        config.setP2upKey(this.p2upKey);
        config.setP2downKey (this.p2downKey);
        config.setShiftKey(this.shiftKey);
        config.setLevel(this.level);
        return config;
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
        System.out.println("width: " + this.windowWidth);
        System.out.println("height: " + this.windowHeight);
        System.out.println("Level: " + this.level);
    }
}
