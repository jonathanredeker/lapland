package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class Create {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        List<String> members = new ArrayList<String>();
        members.add(playerUUID.toString());
        String playerParty = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");

        if (playerParty == null) {
            if (args.length >= 2) {
                String partyName = args[1];
                if (partyName.length() <= 16) {
                    if (lapland.getPartyConfig().getString(partyName) == null) {
                        lapland.getPartyConfig().set(partyName + ".leader", playerUUID.toString());
                        lapland.getPartyConfig().set(partyName + ".members", members);
                        lapland.getPartyConfig().set(partyName + ".experienceCollected", 0);
                        lapland.getPlayerConfig().set(playerUUID.toString() + ".party", partyName);
                        sender.sendMessage(
                                "You have created the party " + ChatColor.AQUA + partyName + ChatColor.RESET + ".");
                    } else {
                        sender.sendMessage("This name is in use by another party. Try something else.");
                    }
                } else {
                    sender.sendMessage("Party names can't be more than 16 characters in length.");
                }
            } else {
                sender.sendMessage("Please enter a party name.");
            }
        } else {
            sender.sendMessage("You are already in a party!");
        }
        lapland.savePartyConfig();
        lapland.savePlayerConfig();
    }

}
