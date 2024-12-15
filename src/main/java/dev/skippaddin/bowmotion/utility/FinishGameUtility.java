package dev.skippaddin.bowmotion.utility;

import dev.skippaddin.bowmotion.BowMotion;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class FinishGameUtility {

    private FinishGameUtility() {}

    public static void finishGame() {
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
}
