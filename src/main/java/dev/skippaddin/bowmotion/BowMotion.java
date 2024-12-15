package dev.skippaddin.bowmotion;

import dev.skippaddin.bowmotion.commands.BowMotionCommand;
import dev.skippaddin.bowmotion.commands.BowMotionTabCompleter;
import dev.skippaddin.bowmotion.listeners.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitTask;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.UUID;

public final class BowMotion extends JavaPlugin {

    private static final HashSet<UUID> participatingPlayers = new HashSet<>();

    private static boolean running = false;

    private final static HashSet<UUID> teleportedPlayers = new HashSet<>();

    private static BukkitTask timerTask;

    private static Plugin essentials;

    public static Method getSafeDestination;

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

        setupEssentials();

        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
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

    private void setupEssentials() {
        if (Bukkit.getPluginManager().isPluginEnabled("Essentials")) {
            essentials = Bukkit.getPluginManager().getPlugin("Essentials");
            try {
                Class<?> locationUtilClass = Class.forName("com.earth2me.essentials.utils.LocationUtil");
                Class<?> essentialsInterface = Class.forName("com.earth2me.essentials.IEssentials");
                getSafeDestination = locationUtilClass.getMethod("getSafeDestination", essentialsInterface, Location.class);
                getLogger().info("Essentials detected. Using safe teleports");
            } catch (Exception e) {
                essentials = null;
                getLogger().info("Could not load Essentials. Might be the wrong version. Falling back to unsafe teleports. Exception: " + e);
            }
        } else {
            getLogger().info("Essentials is missing. Falling back to unsafe teleports.");
        }
    }
}
