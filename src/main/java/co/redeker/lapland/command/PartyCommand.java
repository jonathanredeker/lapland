package co.redeker.lapland.command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import co.redeker.lapland.Lapland;
import co.redeker.lapland.command.subcommands.Create;
import co.redeker.lapland.command.subcommands.Disband;
import co.redeker.lapland.command.subcommands.InvitationResponse;
import co.redeker.lapland.command.subcommands.Invite;
import co.redeker.lapland.command.subcommands.Kick;
import co.redeker.lapland.command.subcommands.Leave;

final public class PartyCommand implements TabExecutor {

    private Lapland lapland;

    public PartyCommand(final Lapland lapland) {
        this.lapland = lapland;
    }

    @Override
    public boolean onCommand(final CommandSender sender, final Command command, final String label,
            final String[] args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;
            if (player.hasPermission("lapland.party")) {
                if (args.length > 0) {
                    switch (args[0]) {
                        case "help":
                            break;
                        case "info":
                            break;
                        case "create":
                            Create.execute(lapland, sender, args);
                            break;
                        case "invite":
                            Invite.execute(lapland, sender, args);
                            break;
                        case "inviteresponse":
                            InvitationResponse.execute(lapland, sender, args);
                            break;
                        case "kick":
                            Kick.execute(lapland, sender, args);
                            break;
                        case "leave":
                            Leave.execute(lapland, sender, args);
                            break;
                        case "disband":
                            Disband.execute(lapland, sender, args);
                            break;
                        case "leader":
                            break;
                        default:
                            // Execute info
                    }
                } else {
                    // Execute help
                }
            } else {
                sender.sendMessage("Insufficient priveleges.");
            }
        } else {
            sender.sendMessage("This command may only be executed by a player.");
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            String[] argumentList = { "create", "invite", "leave", "disband", "kick" };
            return Arrays.asList(argumentList);
        } else if (args.length == 2) {
            List<String> argumentList = new ArrayList<String>();
            List<Player> players = new ArrayList<Player>(Bukkit.getOnlinePlayers());
            switch (args[0]) {
                case "create":
                    argumentList.add("<name>");
                    break;
                case "kick": // To-do: Only suggest players that are in your party.
                case "invite":
                    for (Player player : players) {
                        if (player.getName() != sender.getName()) {
                            argumentList.add(player.getName());
                        }
                    }
                    break;
            }
            return argumentList;
        }
        return null;
    }

}
