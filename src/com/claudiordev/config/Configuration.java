package com.claudiordev.config;

import java.util.List;

import static com.claudiordev.Main.getPlugin;

public class Configuration {
    static int dataType,soundRadius,regenDelay,scheduleRegenerationTime;
    static String mySQLUsername;
    static String mySQLPassword;
    static String mySQLDatabase;
    static boolean leavesDecay,burnedBlocks,burningBlocks,scheduleRegeneration,worldGuardSchedule,placedBlocks;
    static List<String> helpMessage,worldGuardRegions;

    public Configuration() {}

    /**
     * Load all the Configuration File parameters into their respective variables of the Class
     */
    public static void load() {
        dataType = getPlugin().getConfig().getInt("Data");
        mySQLUsername = getPlugin().getConfig().getString("Username");
        mySQLPassword = getPlugin().getConfig().getString("Password");
        mySQLDatabase = getPlugin().getConfig().getString("Database");
        soundRadius = getPlugin().getConfig().getInt("Sound_radius");
        regenDelay = getPlugin().getConfig().getInt("Regen_delay");
        leavesDecay = getPlugin().getConfig().getBoolean("Leaves_decay");
        helpMessage = getPlugin().getConfig().getStringList("Help_message");
        burnedBlocks = getPlugin().getConfig().getBoolean("Burned_blocks");
        burningBlocks = getPlugin().getConfig().getBoolean("Burn_regeneration"); //TODO, To implement
        scheduleRegeneration = getPlugin().getConfig().getBoolean("Schedule_regeneration");
        scheduleRegenerationTime = getPlugin().getConfig().getInt("Schedule_regeneration_minutes");
        worldGuardSchedule = getPlugin().getConfig().getBoolean("World_guard_schedule");
        worldGuardRegions = getPlugin().getConfig().getStringList("World_guard_regions");
        placedBlocks = getPlugin().getConfig().getBoolean("Placed_blocks");
    }

    public static int getDataType() {
        return dataType;
    }

    public static String getMySQLUsername() {
        return mySQLUsername;
    }

    public static String getMySQLDatabase() {
        return mySQLDatabase;
    }

    public static String getMySQLPassword() {return mySQLPassword; }

    public static int getSoundRadius() {
        return soundRadius;
    }

    public static int getRegenDelay() {
        return regenDelay;
    }

    public static boolean isLeavesDecay() {
        return leavesDecay;
    }

    public static List<String> getHelpMessage() {
        return helpMessage;
    }

    public static boolean isBurnedBlocks() {
        return burnedBlocks;
    }

    public static boolean isScheduleRegeneration() {
        return scheduleRegeneration;
    }

    public static int getScheduleRegenerationTime() {
        return scheduleRegenerationTime;
    }

    public static boolean isWorldGuardSchedule() {
        return worldGuardSchedule;
    }

    public static boolean isPlacedBlocks() {
        return placedBlocks;
    }

}
