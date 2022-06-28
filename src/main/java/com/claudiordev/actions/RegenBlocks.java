package com.claudiordev.actions;

import com.claudiordev.Main;
import com.claudiordev.config.Configuration;
import com.claudiordev.utils.ColorCodes;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;


import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Action that regenerates the blocks after usage of regeneration command
 */
public class RegenBlocks {
    ResultSet rs;
    CommandSender commandSender;
    static boolean running;

    public RegenBlocks(ResultSet rs,CommandSender commandSender) {
        this.rs = rs;
        this.commandSender = commandSender;
        run();
    }

    public void run(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String max_action_date = "";

                    while (rs.next()) {
                        if (!running) running = true;
                        //TODO Add World to Database, and retrieve it, using UUID and not the name
                        //TODO Check entities of types animals etc, in radius of 3 blocks, and if present add the block to be restored back to Database
                        final String block_type = rs.getString("block_type");
                        final String block_data = rs.getString("block_data");
                        final int block_x = rs.getInt("block_x");
                        final int block_y = rs.getInt("block_y");
                        final int block_z = rs.getInt("block_z");
                        final Location location = new Location(Bukkit.getWorld("world"), block_x, block_y, block_z);
                        final String action = rs.getString("action");
                        max_action_date = rs.getString("max_action_date");

                        /** Regen Runnable **/
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                Block block_regen = location.getWorld().getBlockAt(location);
                                switch (action) {
                                    case "BREAK":
                                        block_regen.setType(Material.getMaterial(block_type));
                                        block_regen.setData(Byte.parseByte(block_data));
                                        break;
                                    case "PLACE":
                                        block_regen.setType(Material.getMaterial("AIR"));
                                        block_regen.setData(Byte.parseByte("0"));
                                        break;
                                }
                            }
                        }.runTask(Main.getPlugin());

                        /** Sound Effect **/
                        int soundRadius = Configuration.getSoundRadius();
                        for (Entity e: location.getWorld().getNearbyEntities(location, soundRadius,soundRadius,soundRadius)) {
                            if (e instanceof Player) {
                                Player player = ((Player) e).getPlayer();
                                player.playSound(player.getLocation(), Sound.DIG_STONE,50,1);
                            }
                        }

                        Thread.sleep(Configuration.getRegenDelay());
                    }

                    if (running) running = false; //Declare running of regeneration as false
                    //Delete all data after regen
                    Main.getData().query("DELETE from blocks_data WHERE action_date <= '"+ max_action_date +"'");
                    commandSender.sendMessage(ColorCodes.executeReplace("&7Map regenerated"));

                } catch (SQLException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();

    }

    public static boolean isRunning() {
        return running;
    }
}
