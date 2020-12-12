package co.redeker.lapland.listener;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.ExperienceOrb;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityTargetEvent;

import co.redeker.lapland.Lapland;
import net.md_5.bungee.api.ChatColor;

public class EntityTargetListener implements Listener {

    private Lapland lapland;

    public EntityTargetListener(final Lapland lapland) {
        this.lapland = lapland;
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent event) {
        Entity entity = event.getEntity();
        Entity target = event.getTarget();

        // If target is player
        if (target instanceof Player) {
            Player player = (Player) target;

            // Handle dropped experience orbs
            String partyName = lapland.getPlayerConfig().getString(player.getUniqueId().toString() + ".party");
            if (partyName != null) {
                if (entity instanceof ExperienceOrb) {
                    ExperienceOrb experienceOrb = (ExperienceOrb) entity;
                    List<String> members = lapland.getPartyConfig().getStringList(partyName + ".members");
                    List<String> membersToRemove = new ArrayList<String>();
                    members.remove(player.getUniqueId().toString());
                    for (String memberUUID : members) {
                        Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                        if (member != null && !member.isDead()) {
                            double maxDistance = lapland.getConfig().getDouble("maxDistance") * 16;
                            boolean dimensionalSharing = lapland.getConfig().getBoolean("dimensionalSharing");
                            double distance;
                            try {
                                distance = player.getLocation().distance(member.getLocation());
                            } catch (IllegalArgumentException e) { // Differing worlds
                                distance = Double.POSITIVE_INFINITY;
                            }
                            // If dimensionalSharing disabled AND if outside range or dead,
                            // then remove them from the experience share event.
                            if (!dimensionalSharing && distance > maxDistance && maxDistance != 0) {
                                membersToRemove.add(memberUUID);
                            }
                        } else {
                            membersToRemove.add(memberUUID);
                        }
                    }
                    members.removeAll(membersToRemove); // To avoid concurrency issues with enhanced loop.
                    if (!members.isEmpty() && !player.isDead()) {
                        int remainingPartySize = members.size();
                        // ExperienceOrb experienceOrb = (ExperienceOrb)entity;
                        double experienceKept = lapland.getConfig().getDouble("experienceKept");
                        if (experienceKept < 0) {
                            experienceKept = 0;
                        } else if (experienceKept > 100) {
                            experienceKept = 100;
                        }
                        experienceKept /= 100;
                        double partyExperience = (1.0 - experienceKept) * experienceOrb.getExperience();
                        double memberExperience = partyExperience / remainingPartySize;
                        double playerExperience = Math.ceil(experienceKept * experienceOrb.getExperience());
                        // If player picks up 1 experience point, allot that to them.
                        if (playerExperience == 0) {
                            memberExperience = 0;
                            playerExperience = 1;
                        }
                        // player.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You have gained
                        // " + Integer.toString((int)playerExperience) + " experience.");
                        int experienceCollected = (int) (playerExperience + (memberExperience * remainingPartySize));
                        lapland.getPartyConfig().set(partyName + ".experienceCollected", experienceCollected);
                        experienceOrb.setExperience((int) playerExperience);
                        if ((int) memberExperience > 0) {
                            for (String memberUUID : members) {
                                Player member = Bukkit.getPlayer(UUID.fromString(memberUUID));
                                if (member != null) {
                                    if (lapland.getPlayerConfig().getBoolean(memberUUID + ".notifyExperienceGain")) {
                                        member.sendMessage(ChatColor.GRAY + "" + ChatColor.ITALIC + "You have gained "
                                                + Integer.toString((int) memberExperience) + " experience from "
                                                + player.getName() + ".");
                                    }
                                    member.giveExp((int) memberExperience);
                                }
                            }
                        }
                    }
                    // event.setCancelled(true);
                    // event.setTarget(null);
                }
            }
        }
    }

}
