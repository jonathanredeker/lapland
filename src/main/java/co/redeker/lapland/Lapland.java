package co.redeker.lapland;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import co.redeker.lapland.command.PartyCommand;
import co.redeker.lapland.listener.EntityTargetListener;
import co.redeker.lapland.listener.PlayerJoinListener;
import co.redeker.lapland.util.Config;


public class Lapland extends JavaPlugin {

    private FileConfiguration partyConfig;
    private FileConfiguration playerConfig;
    private File partyConfigFile;
    private File playerConfigFile;

    @Override
    public void onEnable() {
        getConfig().options().copyDefaults(true);
        saveConfig();
        partyConfig = Config.createPartyConfig(this);
        partyConfigFile = Config.getPartyConfigFile();
        playerConfig = Config.createPlayerConfig(this);
        playerConfigFile = Config.getPlayerConfigFile();

        getServer().getPluginManager().registerEvents(new EntityTargetListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(this), this);

        getCommand("party").setExecutor(new PartyCommand(this));
        getCommand("pt").setExecutor(new PartyCommand(this));
        getCommand("party").setTabCompleter(new PartyCommand(this));
        getCommand("pt").setTabCompleter(new PartyCommand(this));

    }

    @Override
    public void onDisable() {
        savePlayerConfig();
        savePartyConfig();
        saveConfig();
    }

    public FileConfiguration getPartyConfig() {
        return partyConfig;
    }

    public FileConfiguration getPlayerConfig() {
        return playerConfig;
    }

    public void savePartyConfig() {
        try {
            partyConfig.save(partyConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + partyConfigFile, ex);
        }
    }

    public void savePlayerConfig() {
        try {
            playerConfig.save(playerConfigFile);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Could not save config to " + playerConfigFile, ex);
        }
    }

}