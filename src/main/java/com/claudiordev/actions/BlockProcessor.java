package com.claudiordev.actions;

import com.claudiordev.Main;
import com.claudiordev.config.Configuration;
import com.claudiordev.custom.DataBlock;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Handle the storage of blocks events onto the Database
 * (Broken Blocks, Placed Blocks).
 */
public class BlockProcessor {

    static final ArrayList<DataBlock> blocks = new ArrayList<>();
    private static BlockProcessor blockProcessor;

    public BlockProcessor() {
        process();
    }


    /**
     * @return Singleton of BlockProcessor
     */
    public static BlockProcessor getInstance() {
        if (blockProcessor == null) {
            blockProcessor = new BlockProcessor();
        }

        return blockProcessor;
    }

    public void process() {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    synchronized (blocks) {
                        while (true) {
                            if (blocks.size() > 0) {
                                StringBuilder a = new StringBuilder();
                                a.append("INSERT INTO blocks_data (player_name,block_type,block_data,block_x,block_y,block_z,action) ");

                                if (Configuration.getDataType() == 2) a.append("VALUES ");

                                for(DataBlock block: blocks) {
                                    String block_type = block.getMaterial().name();
                                    String block_data = String.valueOf(block.getData());
                                    String block_x = String.valueOf(block.getLocation().getX());
                                    String block_y = String.valueOf(block.getLocation().getY());
                                    String block_z = String.valueOf(block.getLocation().getZ());
                                    String action = block.getAction();

                                    if (Configuration.getDataType() == 1 && blocks.indexOf(block) == 0) { //SQL Lite Index 0
                                        a.append("SELECT '" + block.getPlayer() + "','" + block_type + "','" + block_data + "','" + block_x + "','" + block_y + "','" + block_z + "','" + action + "' ");
                                    } else if (Configuration.getDataType() == 2){ //MySQL
                                        a.append("('" + block.getPlayer() + "','" + block_type + "','" + block_data + "','" + block_x + "','" + block_y + "','" + block_z + "','" + action + "'),");
                                    } else if(Configuration.getDataType() == 1) { //SQL Lite Index > 0
                                        a.append("UNION ALL SELECT '" + block.getPlayer() + "','" + block_type + "','" + block_data + "','" + block_x + "','" + block_y + "','" + block_z + "','" + action + "' ");
                                    }
                                }

                                if (Configuration.getDataType() == 2) { //MySQL
                                    a.setCharAt(a.lastIndexOf(","),';');
                                }

                                Main.getData().query(a.toString());
                                Main.getPlugin().getLogger().info(a.toString());

                                blocks.clear();
                            } else {
                                blocks.wait();
                            }
                        }
                    }
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getPlugin());
    }

    public void add(DataBlock dataBlock) {
        if (!Configuration.getBlockExceptions().contains(dataBlock.getMaterial().toString())) blocks.add(dataBlock);
    }
}
