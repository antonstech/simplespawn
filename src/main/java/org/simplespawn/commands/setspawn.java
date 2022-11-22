package org.simplespawn.commands;

import org.simplespawn.main;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.text.MessageFormat;
import java.util.logging.Logger;

public class setspawn implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (sender instanceof Player) {

            if (sender.hasPermission("setworldspawn")) {

                // Get the Player Location
                Location location = ((Player) sender).getLocation();
                // Set it as the new one in the Conf and save it
                Plugin plugin = main.getPlugin();
                FileConfiguration config = plugin.getConfig();
                location.setPitch(0);
                location.setPitch(0);
                config.set("spawn-coords", location);
                plugin.saveConfig();

                sender.sendMessage("§aNew Location set :)");
                Logger logger = plugin.getLogger();
                String string = MessageFormat.format("New Spawn-Location set on X {0} Y {1} Z {2}", location.getX(), location.getY(), location.getZ());
                logger.info(string);
            } else {
                sender.sendMessage("§cYou don't have the Permission to do that!");
            }
        } else {
            sender.sendMessage("§cOnly Players can use this Command!");
        }
        return true;
    }
}
