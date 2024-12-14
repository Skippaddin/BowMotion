package dev.skippaddin.bowmotion.bowMotion.listeners;

import dev.skippaddin.bowmotion.bowMotion.BowMotion;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;



public class PlayerMoveListener implements Listener {

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e) {

        if (BowMotion.isRunning()) {
            Player player = e.getPlayer();

            if (BowMotion.getTeleportedPlayers().contains(player.getUniqueId())) {
//                Location toCopy = e.getTo().clone();
//                toCopy.setY(toCopy.getY() - 1);
//                Block blockBelow = toCopy.getBlock();
//
//                if ((blockBelow.getType().isSolid() || blockBelow.isLiquid()) && e.getTo().getY() >= e.getFrom().getY()) {
//                    BowMotion.getTeleportedPlayers().remove(uuid);
//                } else {
//                    return;
//                }

                Location playerLocation = player.getLocation();

                // Get player's bounding dimensions
                double playerX = playerLocation.getX();
                double playerY = playerLocation.getY();
                double playerZ = playerLocation.getZ();

                // Player width (0.6 blocks), adjust for edge cases
                double halfWidth = 0.1;

                // Check corners of player's bounding box
                Location[] locationsToCheck = new Location[] {
                        new Location(player.getWorld(), playerX - halfWidth, Math.floor(playerY) - 1.0, playerZ - halfWidth),
                        new Location(player.getWorld(), playerX + halfWidth, Math.floor(playerY) - 1.0, playerZ - halfWidth),
                        new Location(player.getWorld(), playerX - halfWidth, Math.floor(playerY) - 1.0, playerZ + halfWidth),
                        new Location(player.getWorld(), playerX + halfWidth, Math.floor(playerY) - 1.0, playerZ + halfWidth)
                };

                boolean solidBlockUnderneath = false;
                for (Location loc : locationsToCheck) {
                    Block block = loc.getBlock();
                    if (block.isLiquid() || block.getType().isSolid()) {
                        solidBlockUnderneath = true;
                        break;
                    }
                }

                if (solidBlockUnderneath) {
                    BowMotion.getTeleportedPlayers().remove(player.getUniqueId());
                }
                return;
            }

            Location from = e.getFrom();
            Location to = e.getTo();

            if (from.getX() != to.getX() || from.getZ() != to.getZ()) {
                from.setYaw(to.getYaw());
                from.setPitch(to.getPitch());
                from.setY(to.getY());
                e.setTo(from);
            }
        }
    }

}
