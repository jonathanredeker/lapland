package co.redeker.lapland.listener;

import java.util.UUID;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import co.redeker.lapland.Lapland;

public class PlayerJoinListener implements Listener {
    
    private Lapland lapland;

    public PlayerJoinListener(final Lapland lapland) {
        this.lapland = lapland;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        
        if (lapland.getPlayerConfig().get(uuid.toString()) == null) {    
            lapland.getPlayerConfig().set(uuid.toString() + ".party", null);
            lapland.getPlayerConfig().set(uuid.toString() + ".notifyExperienceGain", true);
            lapland.savePlayerConfig();
        }
        
    }

}
