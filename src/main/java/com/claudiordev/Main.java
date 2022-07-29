package com.claudiordev;

import com.claudiordev.actions.BlockProcessor;
import com.claudiordev.actions.HandleBlocks;
import com.claudiordev.actions.Scheduler;
import com.claudiordev.commands.MapRegen;
import com.claudiordev.config.Configuration;
import com.claudiordev.config.MessageFile;
import com.claudiordev.utils.ColorCodes;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import data.sql.MySQL;
import data.sql.SQLLite;
import data.sql.general.AbstractSQL;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.SQLException;

public class Main extends JavaPlugin {

    private static Main plugin;
    private static PluginManager pm;
    private static AbstractSQL data;
    private static WorldGuardPlugin worldGuardPlugin;

    public static AbstractSQL getData() {
        return data;
    }

    public static WorldGuardPlugin getWorldGuardPlugin() {
        if (worldGuardPlugin == null) {
            worldGuardPlugin = WorldGuardPlugin.inst();
        }
        return worldGuardPlugin;
    }


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
            plugin.getCommand("mapregen").setExecutor(new MapRegen());
        } catch (IOException | NullPointerException e) {
            e.printStackTrace();
            Main.getPlugin().getLogger().info("Error on loading plugin, please contact the supplier.");
        }

        /** Create main folder **/
        if (!Files.exists(Paths.get("plugins/MapRegen"))) {
            try {
                Files.createDirectory(Paths.get("plugins/MapRegen"));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //Load configuration
        loadConfigurations();

        //Load messages file (messages.yml)
        loadMessages();

        new ColorCodes(); //Instanciate first time ColorCodes to be able to access via static referenciation

        switch (Configuration.getDataType()) {
            //SQLLite
            case 1:
                data = new SQLLite("jdbc:sqlite:plugins/MapRegen/db/Data.db",Main.getPlugin().getDataFolder().toString() + "\\db");
                ((SQLLite) data).createdir();
                break;

            case 2:
                data = new MySQL("jdbc:mysql://localhost/",Configuration.getMySQLUsername(),Configuration.getMySQLPassword(),Configuration.getMySQLDatabase());
                break;
        }

        /** Data Connection MySQL or SQLite as choosen in the configuration file (config.yml) **/
        try {
            String query = "";
                    if (Configuration.getDataType() == 1) { //SQLLite
                        query = "CREATE TABLE IF NOT EXISTS blocks_data (id INTEGER PRIMARY KEY AUTOINCREMENT,\n";
                    } else {
                        query = "CREATE TABLE IF NOT EXISTS blocks_data (id INTEGER PRIMARY KEY AUTO_INCREMENT,\n";
                    }
                    query += "player_name varchar(64) NOT NULL,\n" +
                    "block_type varchar(64) NOT NULL,\n" +
                    "block_data varchar(64) NOT NULL DEFAULT 0,\n" +
                    "block_x int(11) NOT NULL DEFAULT 0,\n" +
                    "block_y int(11) NOT NULL DEFAULT 0,\n" +
                    "block_z int(11) NOT NULL DEFAULT 0,\n" +
                    "action varchar(15) NOT NULL DEFAULT 'BREAK',\n" +
                    "action_date timestamp DEFAULT CURRENT_TIMESTAMP);";

            if (!data.connect().isClosed()) { //The connection creates the Data.db file
                Main.getPlugin().getLogger().info("Database Connection detected");
                data.query(query);
            } else {
                Main.getPlugin().getLogger().info("Error on connecting to Database, check the config files and the database and try again!");
                plugin.setEnabled(false);
            }
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info("Error on connecting to Database, check the config files and the database and try again!");
        }

        //Activates Block Processor, handles all the blocks to be processed into the database
        BlockProcessor.getInstance();

        dependencies();

        //Scheduler activation
        if (Configuration.isScheduleRegeneration()) {
            Scheduler.getInstance().runTaskAsynchronously(Main.getPlugin());
        }

        getLogger().info("Map Regen Plugin Loaded");
    }

    @Override
    public void onDisable() {
    }

    /**
     * @return plugin Main class object
     */
    public static Main getPlugin() {
        return plugin;
    }

    /**
     * Check for plugins dependencies,
     * usage on load of server/plugin.
     */
    private void dependencies() {

        if (this.getServer().getPluginManager().getPlugin("WorldGuard") == null) {
            getLogger().severe("* WorldGuard or WorldEdit is not installed or enabled. *");
            getLogger().severe("* This function will be disabled*"); //TODO Specify functions that will not be available
        } else {
            worldGuardPlugin = getWorldGuardPlugin();
        }
    }

    /**
     * Initiate config value and load it's values onto variables defined on Configuration Class
     */
    private void loadConfigurations() {
        saveDefaultConfig();
        Configuration.load();
    }

    /**
     * Load a "messages.yml" file
     */
    private void loadMessages() {
        MessageFile messagesFile = new MessageFile(getDataFolder(),"messages.yml");
        messagesFile.loadMessages();
    }
}
