package com.claudiordev.project;

import com.claudiordev.project.actions.BlockProcessor;
import com.claudiordev.project.actions.HandleBlocks;
import com.claudiordev.project.commands.BlockRegen;
import com.claudiordev.project.data.MySQL;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static PluginManager pm;

    /** Main Method **/
    public static void main(String[] args) {
    }

    /** Method Launched on Load of Server **/
    @Override
    public void onEnable() {
        plugin = this;

        //Get the Plugin Manager
        pm = getServer().getPluginManager();

        //Register Class that handles events
        pm.registerEvents(new HandleBlocks(),this);

        try {
            //"/regen" command instantiation
            this.getCommand("regen").setExecutor(new BlockRegen());
        } catch (IOException e) {
            e.printStackTrace();
        }

        dependencies();

        /** Create main folder **/
        if (!Files.exists(Paths.get("plugins/Kingdoms"))) {
            try {
                Files.createDirectory(Paths.get("plugins/Kingdoms"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        MySQL mySQLData = new MySQL("jdbc:mysql://localhost/kingdoms","root", "");
        if (mySQLData.connect("com.mysql.jdbc.Driver")) {
            mySQLData.createTable("blocks_data");
            Main.getPlugin().getLogger().info("MySQL Connection detected");
        } else {
            Main.getPlugin().getLogger().info("Error on connecting to Database, check the config files and the database and try again!");
            plugin.setEnabled(false);
        }

        //Activates Block Processor
        new BlockProcessor();

        getLogger().info("BlockRegen Plugin Loaded");
    }

    @Override
    public void onDisable() {
    }

    /**
     * @return plugin object
     */
    public static Main getPlugin() {
        return plugin;
    }

    /**
     * Check for plugins dependencies,
     * usage on load of server/plugin.
     */
    private void dependencies() {
        if (!Bukkit.getPluginManager().isPluginEnabled("WorldGuard") || !Bukkit.getPluginManager().isPluginEnabled("WorldEdit") ) {
            getLogger().severe("* WorldGuard or WorldEdit is not installed or enabled. *");
            getLogger().severe("* This function will be disabled*"); //TODO Specify functions that will not be available
        }
    }
}
