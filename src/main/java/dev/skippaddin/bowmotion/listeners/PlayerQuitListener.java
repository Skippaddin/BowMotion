package dev.skippaddin.bowmotion.listeners;

import dev.skippaddin.bowmotion.BowMotion;
import dev.skippaddin.bowmotion.utility.FinishGameUtility;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        if ((BowMotion.isRunning())) {
            BowMotion.getParticipatingPlayers().remove(e.getPlayer().getUniqueId());
            if (Bukkit.getOnlinePlayers().size() == 1) {
                BowMotion.getTimerTask().cancel();
                BowMotion.setRunning(false);
                BowMotion.saveTimer();
            } else if (BowMotion.getParticipatingPlayers().isEmpty()) {
                FinishGameUtility.finishGame();
            }
        }
    }
}
