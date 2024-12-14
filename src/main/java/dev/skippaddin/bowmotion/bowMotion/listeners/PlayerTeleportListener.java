package dev.skippaddin.bowmotion.bowMotion.listeners;

import dev.skippaddin.bowmotion.bowMotion.BowMotion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityMountEvent;
import org.bukkit.event.player.PlayerBedEnterEvent;
import org.bukkit.event.player.PlayerBedLeaveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerTeleportListener implements Listener {

    private final HashMap<UUID, Location> playersToTeleport = new HashMap<>();

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent e) {
        if (BowMotion.isRunning()) {
            if (e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END &&
                    e.getTo().getWorld().getEnvironment() == World.Environment.NORMAL) {
                BowMotion.getParticipatingPlayers().remove(e.getPlayer().getUniqueId());
                if (BowMotion.getParticipatingPlayers().isEmpty()) {
                    BowMotion.getTimerTask().cancel();
                    BowMotion.setRunning(false);
                    Long timer = BowMotion.getTimer();

                    Long hours = timer / 3600;
                    Long minutes = timer % 3600 / 60;
                    Long seconds = timer % 60;

                    String buildTimer = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                    for (Player player : Bukkit.getOnlinePlayers()) {
                        player.sendTitle(ChatColor.GOLD + "Congratulations!", "", 10, 20, 10);
                        player.sendMessage(ChatColor.GREEN + "Congrats! You have finished in: " + ChatColor.GOLD + buildTimer);
                    }
                    BowMotion.resetTimer();
                }
            } else if (e.getCause().equals(PlayerTeleportEvent.TeleportCause.ENDER_PEARL)) {
                System.out.println("Triggered");
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onEnterBed(PlayerBedEnterEvent e) {
        if (BowMotion.isRunning()) {
            Player player = e.getPlayer();
            playersToTeleport.put(player.getUniqueId(), player.getLocation());
        }
    }

    @EventHandler
    public void onLeaveBed(PlayerBedLeaveEvent e) {
        if (BowMotion.isRunning()) {
            Player player = e.getPlayer();
            if (playersToTeleport.containsKey(player.getUniqueId())) {
                Bukkit.getScheduler().runTaskLater(BowMotion.getPlugin(), () -> {
                    player.teleport(playersToTeleport.get(player.getUniqueId()));
                    playersToTeleport.remove(player.getUniqueId());
                }, 1L);
            }
        }
    }

    @EventHandler
    public void onPlayerMount(EntityMountEvent e) {
        if (e.getEntity() instanceof Player) {
            e.setCancelled(true);
        }
    }
}
