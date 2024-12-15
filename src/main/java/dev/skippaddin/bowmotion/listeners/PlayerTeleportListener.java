package dev.skippaddin.bowmotion.listeners;

import dev.skippaddin.bowmotion.BowMotion;
import dev.skippaddin.bowmotion.utility.FinishGameUtility;
import org.bukkit.Bukkit;
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
                    FinishGameUtility.finishGame();
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
