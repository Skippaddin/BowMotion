package dev.skippaddin.bowmotion.commands;

import dev.skippaddin.bowmotion.BowMotion;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class BowMotionCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (sender instanceof Player p) {
            if (args.length == 0) {
                p.sendMessage(ChatColor.RED + "Please provide an argument. Arguments: start, stop, pause");
            } else if (args.length > 1) {
                p.sendMessage(ChatColor.RED + "Please provide only one argument.");
            } else {
                if (args[0].equalsIgnoreCase("start") && p.hasPermission("BowMotion.command.start")) {
                    if (BowMotion.isRunning()) {
                        p.sendMessage(ChatColor.YELLOW + "Game is already running.");
                    } else {
                        BowMotion.setRunning(true);
                        if (BowMotion.getPlugin().getConfig().contains("Timer")) {
                            long timer;
                            Object value = BowMotion.getPlugin().getConfig().get("Timer");
                            if (value instanceof Integer) {
                                timer = ((Integer) value).longValue();
                            } else if (value instanceof Long) {
                                timer = (Long) value;
                            } else {
                                throw new IllegalArgumentException("Value is not a valid number: " + value);
                            }
                            BowMotion.setTimer(timer);
                        }
                        for (Player player : Bukkit.getOnlinePlayers()) {
                            BowMotion.getParticipatingPlayers().add(player.getUniqueId());
                            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                        }
                        BowMotion.setTimerTask(
                                new BukkitRunnable() {

                                    @Override
                                    public void run() {
                                        Long timer = BowMotion.getTimer();

                                        Long hours = timer / 3600;
                                        Long minutes = timer % 3600 / 60;
                                        Long seconds = timer % 60;

                                        String buildTimer = String.format("%02d:%02d:%02d", hours, minutes, seconds);

                                        for (Player player : Bukkit.getOnlinePlayers()) {
                                            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.GOLD + buildTimer));
                                        }
                                        BowMotion.incrementTimer();
                                    }
                                }.runTaskTimer(BowMotion.getPlugin(), 0, 20)
                        );
                    }
                } else if (args[0].equalsIgnoreCase("stop") && p.hasPermission("BowMotion.command.stop")) {
                    if (BowMotion.isRunning()) {
                        BowMotion.getTimerTask().cancel();
                        BowMotion.setRunning(false);
                        BowMotion.getParticipatingPlayers().clear();
                        BowMotion.getPlugin().getConfig().set("Timer", BowMotion.getTimer());
                        BowMotion.getPlugin().saveConfig();
                        p.sendMessage(ChatColor.GREEN + "Challenge has been stopped and the timer got saved.");
                    } else {
                        p.sendMessage(ChatColor.RED + "There is no game running.");
                    }
                } else if (args[0].equalsIgnoreCase("reset") && p.hasPermission("BowMotion.command.reset")) {
                    if (!BowMotion.isRunning()) {
                        BowMotion.resetTimer();
                        p.sendMessage(ChatColor.GREEN + "Timer has been reset.");
                    } else {
                        p.sendMessage(ChatColor.RED + "The game is currently running. Please stop it to reset the timer");
                    }
                }
            }
        }
        return true;
    }
}
