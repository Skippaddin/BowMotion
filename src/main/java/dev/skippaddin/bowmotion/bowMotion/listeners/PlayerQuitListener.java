package dev.skippaddin.bowmotion.bowMotion.listeners;

import dev.skippaddin.bowmotion.bowMotion.BowMotion;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if ((BowMotion.isRunning() || BowMotion.isStarting()) && Bukkit.getOnlinePlayers().size() == 1) {
            BowMotion.getTimerTask().cancel();
            BowMotion.setRunning(false);
            BowMotion.getParticipatingPlayers().clear();

            BowMotion.saveTimer();
        }
    }
}
