package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class Kick {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String partyName = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");
        String partyLeader = lapland.getPartyConfig().getString(partyName + ".leader");
        List<String> members = new ArrayList<String>();
        if (partyName != null) {
            if (partyLeader.equals(playerUUID.toString())) {
                if (args.length >= 2) {
                    String nameToKick = args[1];
                    if (!nameToKick.equalsIgnoreCase(player.getName())) {
                        OfflinePlayer playerToKick = null;
                        for (OfflinePlayer offlinePlayer : Bukkit.getOfflinePlayers()) {
                            if (offlinePlayer.getName().equalsIgnoreCase(nameToKick)) {
                                playerToKick = offlinePlayer;
                                break;
                            }
                        }
                        if (playerToKick != null) {
                            String playerToKickPartyName = lapland.getPlayerConfig()
                                    .getString(playerToKick.getUniqueId().toString() + ".party");
                            if (playerToKickPartyName.equalsIgnoreCase(partyName)) {
                                members = lapland.getPartyConfig().getStringList(partyName + ".members");
                                members.remove(playerToKick.getUniqueId().toString());
                                lapland.getPartyConfig().set(partyName + ".members", members);
                                lapland.getPlayerConfig().set(playerToKick.getUniqueId().toString() + ".party", null);
                                for (String memberUUID : members) {
                                    Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                                    if (member != null) {
                                        member.sendMessage(ChatColor.AQUA + nameToKick + ChatColor.RED
                                                + " has been kicked from the party.");
                                    }
                                }
                                Player playerToKickOnline = Bukkit.getPlayer(playerToKick.getUniqueId());
                                if (playerToKickOnline != null) {
                                    playerToKickOnline
                                            .sendMessage(ChatColor.RED + "You have been kicked from the party.");
                                }
                            } else {
                                sender.sendMessage("That player isn't in your party.");
                            }
                        } else {
                            sender.sendMessage("That player doesn't exist.");
                        }
                    } else {
                        sender.sendMessage("You can't kick yourself!");
                    }
                } else {
                    sender.sendMessage("Please enter a name.");
                }
            } else {
                sender.sendMessage("You are not the party's leader.");
            }
        } else {
            sender.sendMessage("You are not in a party.");
        }
        lapland.savePartyConfig();
        lapland.savePlayerConfig();
    }

}
