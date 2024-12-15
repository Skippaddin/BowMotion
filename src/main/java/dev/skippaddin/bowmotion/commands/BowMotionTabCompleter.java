package dev.skippaddin.bowmotion.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class BowMotionTabCompleter implements TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String s, String[] args) {
        if (command.getName().equalsIgnoreCase("BowMotion") && sender instanceof Player p) {
            List<String> argList = new ArrayList<>();
            if (args.length == 1) {
                if (p.hasPermission("BowMotion.command.start")) {
                    argList.add("start");
                }
                if (p.hasPermission("BowMotion.command.stop")) {
                    argList.add("stop");
                }
                if (p.hasPermission("BowMotion.command.reset")) {
                    argList.add("reset");
                }
            }
            return argList;
        }
        return null;
    }

}
