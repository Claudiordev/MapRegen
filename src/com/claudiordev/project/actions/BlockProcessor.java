package com.claudiordev.project.actions;

import com.claudiordev.project.Main;
import com.claudiordev.project.custom.DataBlock;
import com.claudiordev.project.data.MySQL;
import org.bukkit.block.BlockState;

import java.sql.SQLException;
import java.util.ArrayList;

public class BlockProcessor {

    static final ArrayList<DataBlock> blocks = new ArrayList<>();
    private Thread processThread;
    MySQL mySQL = new MySQL();

    public BlockProcessor() {
        process();
    }

    public void process() {
        processThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    synchronized (blocks) {
                        while (true) {
                            if (blocks.size() > 0) {
                                StringBuilder a = new StringBuilder();
                                a.append("INSERT INTO blocks_data (player_name, block_type, block_data, block_x, block_y, block_z, action) VALUES ");

                                for(int i = 0; i < blocks.size(); i++) {
                                    DataBlock block = blocks.get(i);
                                    String block_type = block.getMaterial().name();
                                    String block_data = String.valueOf(block.getData());
                                    String block_x = String.valueOf(block.getLocation().getX());
                                    String block_y = String.valueOf(block.getLocation().getY());
                                    String block_z = String.valueOf(block.getLocation().getZ());
                                    String action = block.getAction();

                                    //Main.getPlugin().getLogger().info("Processing, Block: " + block_type);
                                    a.append("('"+ "-" +"','"+ block_type + "','"+ block_data +"','"+ block_x +"','"+ block_y +"','"+ block_z +"', '"+ action +"'),");
                                }

                                a.setCharAt(a.lastIndexOf(","),';');
                                mySQL.manageData(a.toString());
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
        });

        processThread.start();
    }

    public void add(DataBlock dataBlock) {
        blocks.add(dataBlock);
    }
}
