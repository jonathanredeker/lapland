package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Invite {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String partyName = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");
        List<String> invitations = new ArrayList<String>();

        if (partyName != null) {
            String partyLeader = lapland.getPartyConfig().getString(partyName + ".leader");
            if (partyLeader.equalsIgnoreCase(playerUUID.toString())) {
                if (args.length >= 2) {
                    String inviteeName = args[1];
                    Player invitee = Bukkit.getPlayer(inviteeName);
                    if (invitee != null) {
                        if (!invitee.getUniqueId().toString().equalsIgnoreCase(playerUUID.toString())) {
                            List<String> members = lapland.getPartyConfig().getStringList(partyName + ".members");
                            int partyMaxSize = lapland.getConfig().getInt("maxPlayers");
                            if (members.size() < partyMaxSize) {
                                if (lapland.getPlayerConfig()
                                        .getString(invitee.getUniqueId().toString() + ".party") == null) {
                                    invitations = lapland.getPartyConfig().getStringList(partyName + ".invitations");
                                    // Check if invitation to party already exists to elimate potential for
                                    // redundant entries.
                                    if (!invitations.contains(invitee.getUniqueId().toString())) {
                                        invitations.add(invitee.getUniqueId().toString());
                                        lapland.getPartyConfig().set(partyName + ".invitations", invitations);
                                    }
                                    // Prompt invitee with invitation.
                                    invitee.sendMessage(ChatColor.AQUA + player.getName() + ChatColor.RESET
                                            + " has invited you to join the party " + ChatColor.RED + partyName
                                            + ChatColor.RESET + ".");
                                    invitee.spigot()
                                            .sendMessage(new ComponentBuilder("\nClick: ").append("[ACCEPT] ")
                                                    .color(ChatColor.GREEN)
                                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                            "/party inviteresponse accept " + partyName))
                                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                            new Text("Accept the party invitation and join.")))
                                                    .append("[DECLINE]")
                                                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND,
                                                            "/party inviteresponse decline " + partyName))
                                                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                            new Text("Decline the party invitation.")))
                                                    .color(ChatColor.RED).create());
                                    sender.sendMessage(ChatColor.GREEN + "An invitation has been sent to "
                                            + ChatColor.AQUA + inviteeName + ChatColor.GREEN + ".");
                                } else {
                                    sender.sendMessage(
                                            ChatColor.AQUA + inviteeName + ChatColor.RESET + " is already in a party!");
                                }
                            } else {
                                sender.sendMessage("You can't have more than " + Integer.toString(partyMaxSize)
                                        + " players in a party.");
                            }
                        } else {
                            sender.sendMessage("You can't invite yourself to the party!");
                        }
                    } else {
                        sender.sendMessage("Could not find " + ChatColor.AQUA + inviteeName + ChatColor.RESET + ".");
                    }
                } else {
                    sender.sendMessage("Please enter a player name.");
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
