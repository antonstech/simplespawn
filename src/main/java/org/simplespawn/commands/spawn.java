package org.simplespawn.commands;

import org.bukkit.Bukkit;
import org.simplespawn.main;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;


public class spawn implements CommandExecutor {

    // Copied from the lovely DestinyOfYeet <3
    static HashMap<Player, Integer> spawnMap = new HashMap<>();


    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin plugin = main.getPlugin();
        FileConfiguration config = plugin.getConfig();

        if (sender instanceof Player) {

            if (check_if_only_overworld()) {
                World.Environment playerworldenv = ((Player) sender).getWorld().getEnvironment();
                String playerworldtype = playerworldenv.name();

                if (playerworldtype.equals("NORMAL") || playerworldtype.equals("CUSTOM")) {
                    teleport(sender, cmd, label, args);
                } else {
                    sender.sendMessage("§cYou are not in the Overworld!");
                }
            } else {
                teleport(sender, cmd, label, args);
            }
        } else {
            sender.sendMessage("§cOnly Players can use this Command!");
        }


        return true;

    }

    public boolean check_if_only_overworld() {
        Plugin plugin = main.getPlugin();
        FileConfiguration config = plugin.getConfig();
        return config.getBoolean("only-allow-overworld");
    }

    public void teleport(CommandSender sender, Command cmd, String label, String[] args) {
        Plugin plugin = main.getPlugin();
        FileConfiguration config = plugin.getConfig();
        Player player = (Player) sender;

        double playerX = player.getLocation().getX();
        double playerZ = player.getLocation().getZ();

        int taskID;
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(main.getPlugin(), new Runnable() {
            int countdown = plugin.getConfig().getInt("countdown");

            @Override
            public void run() {
                final Location spawn_location = config.getLocation("spawn-coords");
                if (spawn_location == null) {
                    sender.sendMessage("§cThe Location has been removed from the Config");
                } else {
                    if (countdown <= 0) {
                        (player).teleport(spawn_location);
                        Bukkit.getScheduler().cancelTask(spawnMap.get(player));
                    } else {
                        if (playerX != player.getLocation().getX() || playerZ != player.getLocation().getZ()) {
                            Bukkit.getScheduler().cancelTask(spawnMap.get(player));
                            player.sendMessage("§cYou have moved! Teleport cancelled!");
                        } else {
                            player.sendMessage("§aYou will be Teleported in " + "§c" + countdown + " §aseconds!");
                            countdown--;
                        }
                    }
                }


            }


        }, 0, 20);
        spawnMap.put((Player) sender, taskID);
    }
}
