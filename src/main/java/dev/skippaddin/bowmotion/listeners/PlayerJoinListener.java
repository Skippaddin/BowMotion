package dev.skippaddin.bowmotion.listeners;

import dev.skippaddin.bowmotion.BowMotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        if (BowMotion.isRunning()) {
            BowMotion.getParticipatingPlayers().add(e.getPlayer().getUniqueId());
        }
    }
}
