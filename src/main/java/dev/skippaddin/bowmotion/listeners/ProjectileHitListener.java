package dev.skippaddin.bowmotion.listeners;

import dev.skippaddin.bowmotion.BowMotion;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    @EventHandler
    public void onProjectileHit(ProjectileHitEvent e) {
        if (BowMotion.isRunning() && e.getEntity() instanceof Arrow arrow && arrow.getShooter() instanceof Player p) {
            Location arrowLocation = arrow.getLocation().clone();
            Location playerLocation = p.getLocation();
            arrowLocation.setYaw(playerLocation.getYaw());
            arrowLocation.setPitch(playerLocation.getPitch());
            arrow.remove();
            BowMotion.getTeleportedPlayers().add(p.getUniqueId());

            if (BowMotion.getEssentials() == null) {
                teleportPlayer(p, arrowLocation);
            } else {
                try {
                    Location safeLocation = (Location) BowMotion.getSafeDestination.invoke(null, BowMotion.getEssentials(), arrowLocation);
                    safeLocation.setY(arrowLocation.getY());
                    teleportPlayer(p, arrowLocation);
                } catch (Exception exception) {
                    BowMotion.getPlugin().getLogger().info("Could not use safe teleport. Falling back to unsafe teleport");
                    teleportPlayer(p, arrowLocation);
                }
            }
        }
    }

    private void teleportPlayer(Player player, Location location) {
        player.teleport(location);
        player.playSound(player, Sound.ENTITY_PLAYER_TELEPORT, 1f, 1f);
    }
}
