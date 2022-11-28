package org.simplespawn;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import org.simplespawn.commands.*;
import org.simplespawn.updatechecker.updatechecker;

import java.text.MessageFormat;
import java.util.logging.Logger;

import org.simplespawn.metrics.Metrics;


public final class main extends JavaPlugin {

    private static Plugin plugin;

    public static Plugin getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {

        plugin = this;
        Logger logger = plugin.getLogger();
        logger.info("SimpleSpawn is loading...");
        new updatechecker(this).get_latest_version(version -> {
            if (this.getDescription().getVersion().equals(version)) {
                logger.info("Running on the newest Version :)");
            } else {
                logger.warning("There is a new Version of SimpleSpawn available :)");
                String string = MessageFormat.format("Currently running on {0}, {1} is available now", this.getDescription().getVersion(), version);
                logger.info(string);
            }
        });

        // bStats Metrics
        Metrics metrics = new Metrics(this, 16910);


        // Registering the Commands
        this.getCommand("spawn").setExecutor(new spawn());
        this.getCommand("setspawn").setExecutor(new setspawn());

        // Getting the world spawn
        World world = (World) Bukkit.getServer().getWorld("world");
        Location spawnpoint = world.getSpawnLocation();

        // Config Stuff
        FileConfiguration config = plugin.getConfig();
        config.options().header("""
                Hey, if you have any Questions or Feedback, you can write me on Discord: antonsl#2805
                countdown - Countdown till you get teleported
                (cooldown - How long you have to wait before using /spawn again) not implemented yet!
                spawn-coords - The Coordinates of the Spawn
                only-allow-overworld - If this is enabled, you can only use /spawn in the Overworld, otherwise just set it to false""");
        config.addDefault("countdown", 5);
        config.addDefault("cooldown", 20);
        config.addDefault("spawn-coords", spawnpoint);
        config.addDefault("only-allow-overworld", true);
        config.options().copyDefaults(true);
        config.options().parseComments();
        saveConfig();


        logger.info("SimpleSpawn has loaded successfully :)");
    }

    @Override
    public void onDisable() {
        Logger logger = plugin.getLogger();
        logger.info("SimpleSpawn has successfully shut down!");
    }
}
