package co.redeker.lapland.command.subcommands;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.ChatPaginator;
import org.bukkit.util.ChatPaginator.ChatPage;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class Information {

    public static void execute(final Lapland lapland, final CommandSender sender, final String[] args) {
        Player player = (Player) sender;
        UUID playerUUID = player.getUniqueId();
        String partyName = lapland.getPlayerConfig().getString(playerUUID.toString() + ".party");
        if (partyName != null) {
            List<String> members = lapland.getPartyConfig().getStringList(partyName + ".members");
            List<String> membersOnline = new ArrayList<String>();
            List<String> membersOffline = new ArrayList<String>(members);
            for (String memberUUID : members) {
                Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                if (member != null) {
                    membersOnline.add(memberUUID);
                }
            }
            membersOffline.removeAll(membersOnline);
            String partyLeader = lapland.getPartyConfig().getString(partyName + ".leader");
            String partyLeaderName = lapland.getPlayerConfig().getString(partyLeader + ".name");
            int memberCount = members.size();
            int membersOnlineCount = membersOnline.size();
            int capacity = lapland.getConfig().getInt("maxPlayers");
            int page = 1;

            if (args.length >= 2) {
                try {
                    page = Integer.parseInt(args[1]);
                    if (page <= 0) {
                        page = 1;
                    }
                } catch (NumberFormatException e) {
                    sender.sendMessage("Please enter a whole number.");
                    return;
                }
            }

            StringBuilder header = new StringBuilder();
            header.append(ChatColor.GOLD + "Party Information" + ChatColor.RESET);
            header.append("\n");
            header.append(ChatColor.YELLOW + "Name: " + ChatColor.RESET);
            header.append(partyName);
            header.append("\n");
            header.append(ChatColor.YELLOW + "Leader: " + ChatColor.RESET);
            header.append(partyLeaderName);
            header.append("\n");
            header.append(ChatColor.YELLOW + "Capacity: " + ChatColor.RESET);
            header.append(memberCount);
            header.append("/");
            header.append(capacity);
            header.append("\n");
            header.append(ChatColor.GOLD + "Members – ");
            header.append(membersOnlineCount);
            header.append(" Online");

            StringBuilder body = new StringBuilder();
            String leaderStatus = ChatColor.DARK_GRAY + " ● " + ChatColor.RESET;
            if (membersOnline.contains(partyLeader)) {
                leaderStatus = ChatColor.GREEN + " ● " + ChatColor.RESET;
                membersOnline.remove(partyLeader);
            } else {
                membersOffline.remove(partyLeader);
            }
            body.append(leaderStatus + partyLeaderName);
            body.append("\n");
            for (String memberUUID : membersOnline) {
                String memberName = lapland.getPlayerConfig().getString(memberUUID + ".name");
                if (memberName != null) {
                    body.append(ChatColor.GREEN + " ● " + ChatColor.RESET);
                    body.append(memberName);
                    body.append("\n");
                }
            }
            for (String memberUUID : membersOffline) {
                String memberName = lapland.getPlayerConfig().getString(memberUUID + ".name");
                if (memberName != null) {
                    body.append(ChatColor.DARK_GRAY + " ● " + ChatColor.RESET);
                    body.append(memberName);
                    body.append("\n");
                }
            }

            ChatPage chatPage = ChatPaginator.paginate(body.toString(), page, 55, 4);
            sender.sendMessage(header.toString());
            for (String line : chatPage.getLines()) {
                sender.sendMessage(line);
            }

            ChatColor footerButtonLeftColour = ChatColor.YELLOW;
            ChatColor footerButtonRightColour = ChatColor.YELLOW;
            String footerButtonLeftHoverText = "Go to page " + Integer.toString(chatPage.getPageNumber() - 1) + ".";
            String footerButtonRightHoverText = "Go to page " + Integer.toString(chatPage.getPageNumber() + 1) + ".";
            String footerButtonLeftCommand = "/party info " + Integer.toString(chatPage.getPageNumber() - 1);
            String footerButtonRightCommand = "/party info " + Integer.toString(chatPage.getPageNumber() + 1);
            if (chatPage.getPageNumber() == 1) {
                footerButtonLeftColour = ChatColor.DARK_GRAY;
                footerButtonLeftHoverText = "";
                footerButtonLeftCommand = "";
            }
            if (chatPage.getPageNumber() == chatPage.getTotalPages()) {
                footerButtonRightColour = ChatColor.DARK_GRAY;
                footerButtonRightHoverText = "";
                footerButtonRightCommand = "";
                for (int i = 4 - (chatPage.getLines().length % 4); i > 0; i--) {
                    sender.sendMessage(" ");
                }
            }

            BaseComponent[] footer = new ComponentBuilder("Click: ").append("<<<").color(footerButtonLeftColour)
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, footerButtonLeftCommand))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(footerButtonLeftHoverText)))
                    .append(" Page " + Integer.toString(chatPage.getPageNumber()) + "/" + chatPage.getTotalPages()
                            + " ")
                    .reset().color(ChatColor.GOLD).append(">>>")
                    .event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, footerButtonRightCommand))
                    .event(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(footerButtonRightHoverText)))
                    .color(footerButtonRightColour).create();
            sender.spigot().sendMessage(footer);

        } else {
            sender.sendMessage("You are not in a party.");
        }
    }

}
