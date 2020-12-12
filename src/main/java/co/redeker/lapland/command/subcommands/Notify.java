package co.redeker.lapland.command.subcommands;

import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class Notify {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        boolean notifyExperienceGain = lapland.getPlayerConfig()
                .getBoolean(playerUUID.toString() + ".notifyExperienceGain");
        if (args.length >= 1) {
            notifyExperienceGain = !notifyExperienceGain;
            lapland.getPlayerConfig().set(playerUUID.toString() + ".notifyExperienceGain", notifyExperienceGain);
            String action;
            if (notifyExperienceGain) {
                action = ChatColor.GREEN + "enabled";
            } else {
                action = ChatColor.RED + "disabled";
            }
            sender.sendMessage("You have " + action + ChatColor.RESET + " experience gain notifications.");
        }
        lapland.savePlayerConfig();
    }
}
