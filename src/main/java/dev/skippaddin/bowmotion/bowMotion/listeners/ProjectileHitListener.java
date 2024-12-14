package dev.skippaddin.bowmotion.bowMotion.listeners;

import dev.skippaddin.bowmotion.bowMotion.BowMotion;
import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ProjectileHitListener implements Listener {

    private java.lang.reflect.Method getSafeDestination;

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
                p.teleport(arrowLocation);
            } else {
                try {
                    if (getSafeDestination == null) {
                        Class<?> locationUtilClass = Class.forName("com.earth2me.essentials.utils.LocationUtil");
                        Class<?> essentialsInterface = Class.forName("com.earth2me.essentials.IEssentials");
                        getSafeDestination = locationUtilClass.getMethod("getSafeDestination", essentialsInterface, Location.class);
                    }
                    Location safeLocation = (Location) getSafeDestination.invoke(null, BowMotion.getEssentials(), arrowLocation);
                    safeLocation.setY(arrowLocation.getY());
                    p.teleport(safeLocation);
//                    Location safeLocation = LocationUtil.getSafeDestination(BowMotion.getEssentials(), arrowLocation);
//                    safeLocation.setY(arrowLocation.getY());
//                    p.teleport(safeLocation);
                } catch (Exception ex) {
                    System.out.println(ex);
                    p.teleport(arrowLocation);
                }
            }
        }
    }
}
