package com.claudiordev.project.actions;

import com.claudiordev.project.Main;
import com.claudiordev.project.commands.BlockRegen;
import com.claudiordev.project.custom.DataBlock;
import com.claudiordev.project.data.MySQL;
import com.claudiordev.project.data.SQLLite;
import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.Explosion;
import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.entity.ExplosionPrimeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.RegisteredListener;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.BlockIterator;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;

/**
 * Class that handles the events of breaking and placing blocks,
 * to be restored automatically
 */
public class HandleBlocks implements Listener {

    MySQL sqlLite = new MySQL();

    /**
     * Get the info of a broken block, Coordinates, Type and Data
     * Deprecated Method (getData()), since it's an old version of API (1.8)
     * @param event of breaking a block
     */
    @SuppressWarnings("deprecation")
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        //TODO, Check wheter the broken block is a tree facing up, having leaves nearby and add them to the database
        Player player = event.getPlayer();
        Block block = event.getBlock();
        final String player_name = player.getDisplayName();
        final String block_type = block.getType().name();
        final String block_data = String.valueOf(block.getData());
        final String block_x = String.valueOf(block.getX());
        final String block_y = String.valueOf(block.getY());
        final String block_z = String.valueOf(block.getZ());
        final String action = "BREAK";

        new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                sqlLite.insert(player_name, block_type, block_data, block_x, block_y, block_z, action);
            }
        }).start();

        Main.getPlugin().getLogger().info("Active Threads: " + Thread.activeCount());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Player player = event.getPlayer();
        Block block = event.getBlock();
        final String player_name = player.getDisplayName();
        final String block_type = block.getType().name();
        final String block_data = String.valueOf(block.getData());
        final String block_x = String.valueOf(block.getX());
        final String block_y = String.valueOf(block.getY());
        final String block_z = String.valueOf(block.getZ());
        final String action = "PLACE";

        new Thread(new BukkitRunnable() {
            @Override
            public void run() {
                sqlLite.insert(player_name, block_type, block_data, block_x, block_y, block_z, action);
            }
        }).start();
    }

    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent event) {
        //Custom Data Object List
        List<DataBlock> blocks = new ArrayList<>();

        //Event Blocks Default Object List
        for (int i = 0; i < event.blockList().size(); i++) {
            blocks.add(new DataBlock(event.blockList().get(i),"BREAK"));
        }

        synchronized (BlockProcessor.blocks) {
            BlockProcessor.blocks.addAll(blocks);

            //Wakes up a single thread that is waiting on this object's monitor aka Thread
            BlockProcessor.blocks.notifyAll();
        }
    }

    @EventHandler
    public void onBlockPistonMove(BlockPistonExtendEvent event) {

    }
}

//TODO, Add expections for example, Chests on being broken, not to be regenerated
