package dev.skippaddin.bowmotion.bowMotion;

import dev.skippaddin.bowmotion.bowMotion.commands.BowMotionCommand;
import dev.skippaddin.bowmotion.bowMotion.commands.BowMotionTabCompleter;
import dev.skippaddin.bowmotion.bowMotion.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashSet;
import java.util.UUID;

public final class BowMotion extends JavaPlugin {

    private static final HashSet<UUID> participatingPlayers = new HashSet<>();

    private static boolean starting = false;

    private static boolean running = false;

    private final static HashSet<UUID> teleportedPlayers = new HashSet<>();

    private static BukkitTask timerTask;

    private static Plugin essentials;

    public static void setTimer(Long timer) {
        BowMotion.timer = timer;
    }

    private static Long timer = 0L;

    private static BowMotion plugin;


    public static boolean isRunning() {
        return running;
    }

    public static void setRunning(boolean newRunning) {
        running = newRunning;
    }

    public static HashSet<UUID> getTeleportedPlayers() {
        return teleportedPlayers;
    }

    public static boolean isStarting() {
        return starting;
    }

    public static void setStarting(boolean starting) {
        BowMotion.starting = starting;
    }

    public static BukkitTask getTimerTask() {
        return timerTask;
    }

    public static void setTimerTask(BukkitTask timerTask) {
        BowMotion.timerTask = timerTask;
    }

    public static Long getTimer() {
        return timer;
    }

    public static void incrementTimer() {
        timer++;
    }

    public static void resetTimer() {
        timer = 0L;
        saveTimer();
    }

    public static BowMotion getPlugin() {
        return plugin;
    }

    public static HashSet<UUID> getParticipatingPlayers() {
        return participatingPlayers;
    }

    public static void saveTimer() {
        plugin.getConfig().set("Timer", timer);
        plugin.saveConfig();
    }

    public static Plugin getEssentials() {
        return essentials;
    }

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin = this;

        getServer().getPluginManager().registerEvents(new PlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new ProjectileHitListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerMoveListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerTeleportListener(), this);
        getServer().getPluginManager().registerEvents(new PlayerQuitListener(), this);
        getCommand("BowMotion").setExecutor(new BowMotionCommand());
        getCommand("BowMotion").setTabCompleter(new BowMotionTabCompleter());
    }

    @Override
    public void onDisable() {
        if (running) {
            timerTask.cancel();
        }
        saveTimer();
    }

    public void setupEssentials() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials") && essentials == null) {
            essentials = (Plugin) Bukkit.getPluginManager().getPlugin("Essentials");
        } else {
            getServer().getLogger().info("Essentials is missing. Falling back to unsafe teleports.");
        }
    }
}
