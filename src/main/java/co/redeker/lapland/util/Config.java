package co.redeker.lapland.util;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import co.redeker.lapland.Lapland;

final public class Config {

    private static YamlConfiguration partyConfig;
    private static YamlConfiguration playerConfig;
    private static File partyConfigFile;
    private static File playerConfigFile;

    public static FileConfiguration createPartyConfig(Lapland lapland) {
        partyConfigFile = new File(lapland.getDataFolder(), "party.yml");
        if (!partyConfigFile.exists()) {
            partyConfigFile.getParentFile().mkdirs();
            lapland.saveResource("party.yml", false);
        }

        partyConfig = new YamlConfiguration();
        try {
            partyConfig.load(partyConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return partyConfig;
    }

    public static FileConfiguration createPlayerConfig(Lapland lapland) {
        playerConfigFile = new File(lapland.getDataFolder(), "player.yml");
        if (!playerConfigFile.exists()) {
            playerConfigFile.getParentFile().mkdirs();
            lapland.saveResource("player.yml", false);
        }

        playerConfig = new YamlConfiguration();
        try {
            playerConfig.load(playerConfigFile);
        } catch (IOException | InvalidConfigurationException e) {
            e.printStackTrace();
        }
        return playerConfig;
    }

    public static File getPartyConfigFile() {
        return partyConfigFile;
    }

    public static File getPlayerConfigFile() {
        return playerConfigFile;
    }

    public static FileConfiguration getPartyConfig() {
        return partyConfig;
    }

    public static FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

}
