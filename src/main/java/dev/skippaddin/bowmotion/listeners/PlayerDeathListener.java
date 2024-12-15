package dev.skippaddin.bowmotion.listeners;

import dev.skippaddin.bowmotion.BowMotion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class PlayerDeathListener implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        if (BowMotion.isRunning()) {
            BowMotion.getTimerTask().cancel();
            BowMotion.setRunning(false);
            BowMotion.getParticipatingPlayers().clear();
            BowMotion.resetTimer();

            for (Player player : Bukkit.getOnlinePlayers()) {
                player.sendTitle(ChatColor.RED + "Game Over", "", 10, 20, 10);
                player.sendMessage(ChatColor.RED + "Game Over...");
            }

        }
    }

}
