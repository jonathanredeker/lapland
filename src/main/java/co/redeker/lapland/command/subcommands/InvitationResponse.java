package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class InvitationResponse {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        List<String> invitations = new ArrayList<String>();
        List<String> members = new ArrayList<String>();
        if (args.length >= 3) {
            String decision = args[1];
            String partyName = args[2];
            invitations = lapland.getPartyConfig().getStringList(partyName + ".invitations");

            if (lapland.getPartyConfig().getString(partyName) != null) {
                if (invitations.contains(playerUUID.toString())) {
                    if (lapland.getPlayerConfig().getString(player.getUniqueId().toString() + ".party") == null) {
                        invitations.remove(playerUUID.toString());
                        lapland.getPartyConfig().set(partyName + ".invitations", invitations);
                        if (decision.equalsIgnoreCase("accept")) {
                            lapland.getPlayerConfig().set(playerUUID.toString() + ".party", partyName);
                            members = lapland.getPartyConfig().getStringList(partyName + ".members");
                            for (String memberUUID : members) {
                                Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                                if (member != null) {
                                    member.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.RESET
                                            + " has joined the party.");
                                }
                            }
                            members.add(playerUUID.toString());
                            lapland.getPartyConfig().set(partyName + ".members", members);
                            sender.sendMessage(
                                    "You have joined the party " + ChatColor.RED + partyName + ChatColor.RESET + ".");
                        } else {
                            String leaderUUID = lapland.getPartyConfig().getString(partyName + ".leader");
                            Player leader = Bukkit.getPlayer(UUID.fromString(leaderUUID));
                            if (leader != null) {
                                leader.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.RED
                                        + " has declined your invitation.");
                            }
                            sender.sendMessage("You have declined the party invitation.");
                        }
                    } else {
                        sender.sendMessage("You're already in a party.");
                    }
                } else {
                    sender.sendMessage("You weren't invited to this party.");
                }
            } else {
                sender.sendMessage("That party doesn't exist.");
            }
            lapland.savePartyConfig();
            lapland.savePlayerConfig();
        }

    }

}
