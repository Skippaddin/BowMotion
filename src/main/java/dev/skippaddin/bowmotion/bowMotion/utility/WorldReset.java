package dev.skippaddin.bowmotion.bowMotion.utility;

import org.bukkit.*;
import org.bukkit.entity.Player;

import java.io.File;

//Not using currently. Might be later, but seems unnecessary.
public final class WorldReset {

    private WorldReset() {}

    private static final String[] worldsToReset = {"world", "world_nether", "world_the_end"};

    private static boolean resetting = false;

    public static boolean isResetting() {
        return resetting;
    }

    public static void resetWorlds() {
        resetting = true;

        for (Player player : Bukkit.getOnlinePlayers()) {
            player.kickPlayer(ChatColor.RED + "Game Over. Someone has died :(");
        }

        for (String worldName : worldsToReset) {
            World world = Bukkit.getWorld(worldName);
            if (world != null) {
                Bukkit.unloadWorld(world, false);
            }
            File worldFolder = new File(worldName);
            deleteFolder(worldFolder);
        }

        createWorld("world", World.Environment.NORMAL, WorldType.NORMAL);
        createWorld("world_nether", World.Environment.NETHER, WorldType.NORMAL);
        createWorld("world_the_end", World.Environment.THE_END, WorldType.NORMAL);

        resetting = false;

    }

    private static void deleteFolder(File folder) {
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                deleteFolder(file);
            }
        }
        folder.delete();
    }

    private static void createWorld(String name, World.Environment environment, WorldType worldType) {
        WorldCreator creator = new WorldCreator(name);
        creator.environment(environment);
        creator.type(worldType);
        Bukkit.createWorld(creator);
    }
}
