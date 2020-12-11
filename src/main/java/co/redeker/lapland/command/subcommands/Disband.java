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

public class Disband {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String partyName = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");
        String partyLeader = lapland.getPartyConfig().getString(partyName + ".leader");
        List<String> members = new ArrayList<String>();

        if (partyName != null) {
            if (partyLeader.equalsIgnoreCase(playerUUID.toString())) {
                if (args.length >= 2) {
                    String decision = args[1];
                    if (decision.equalsIgnoreCase("yes")) {
                        members = lapland.getPartyConfig().getStringList(partyName + ".members");
                        lapland.getPartyConfig().set(partyName, null);
                        for (String memberUUID : members) {
                            Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                            if (member != null) {
                                member.sendMessage(ChatColor.RED + "The party has been disbanded.");
                            }
                            if (lapland.getPlayerConfig().get(memberUUID) != null) {
                                lapland.getPlayerConfig().set(memberUUID + ".party", null);
                            }
                        }
                    } else {
                        sender.sendMessage("The party was not disbanded.");
                    }
                } else if (args.length >= 1) {
                    if (lapland.getPartyConfig().getString(partyName) != null) {
                        // send clickable text to sender
                        sender.sendMessage("Are you sure you want to disband the party?");
                        player.spigot()
                                .sendMessage(new ComponentBuilder("\nClick: ").append("[CONFIRM] ")
                                        .color(ChatColor.GREEN)
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party disband yes"))
                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                new Text("Confirms the party disbandment.")))
                                        .append("[CANCEL]")
                                        .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/party disband no"))
                                        .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                                                new Text("Cancels the party disbandment.")))
                                        .color(ChatColor.RED).create());
                    } else {
                        sender.sendMessage("This party does not exist.");
                    }
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
