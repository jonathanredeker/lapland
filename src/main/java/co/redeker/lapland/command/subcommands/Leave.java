package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class Leave {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String partyName = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");
        String partyLeader = lapland.getPartyConfig().getString(partyName + ".leader");
        List<String> members = new ArrayList<String>();

        // Check if the player is in a party
        if (partyName != null) {
            members = lapland.getPartyConfig().getStringList(partyName + ".members");
            members.remove(playerUUID.toString());
            lapland.getPartyConfig().set(partyName + ".members", members);
            lapland.getPlayerConfig().set(playerUUID + ".party", null);
            sender.sendMessage("You have left the party.");

            // If there are not members remaining, disband the party.
            if (!members.isEmpty()) {
                // Notify all online members about the player's departure from the party.
                for (String memberUUID : members) {
                    Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                    if (member != null) {
                        member.sendMessage(ChatColor.RED + player.getName() + ChatColor.RESET + " has left the party.");
                    }
                }
                // If the player was leader, then set new party leader to a different member.
                if (partyLeader.equalsIgnoreCase(playerUUID.toString())) {
                    // Get first member in party. (default pick)
                    String newLeaderUUID = members.get(0);
                    Player newLeader = null;
                    Boolean onlineReplacementFound = false;
                    // If there is an online member available, pick them over the default.
                    for (String member : members) {
                        newLeader = Bukkit.getPlayer(UUID.fromString(member));
                        if (newLeader != null) {
                            newLeaderUUID = newLeader.getUniqueId().toString();
                            onlineReplacementFound = true;
                            break;
                        }
                    }
                    lapland.getPartyConfig().set(partyName + ".leader", newLeaderUUID);
                    if (onlineReplacementFound) {
                        // Notify all online members of the leadership change.
                        for (String memberUUID : members) {
                            Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                            if (member != null) {
                                member.sendMessage(ChatColor.AQUA + newLeader.getName() + " is the new party leader.");
                            }
                        }
                    }
                }
            } else {
                lapland.getPartyConfig().set(partyName, null);
                sender.sendMessage(ChatColor.RED + "The party has been disbanded.");
            }
        } else {
            sender.sendMessage("You are not in a party!");
        }

        lapland.savePartyConfig();
        lapland.savePlayerConfig();
    }

}
