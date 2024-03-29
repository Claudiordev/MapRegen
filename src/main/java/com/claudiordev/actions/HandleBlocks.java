package com.claudiordev.actions;

import com.claudiordev.config.Configuration;
import com.claudiordev.custom.DataBlock;
import org.bukkit.block.Block;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Class that handles events of breaking and placing blocks,
 * to be restored automatically
 */
public class HandleBlocks implements Listener {


    /**
     * Get the info of a broken block, Coordinates, Type, Data and Player who did the action
     * Deprecated Method (getData()), since it's an old version of spigot API (1.8)
     * @param event of breaking a block
     */
    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        synchronized (BlockProcessor.blocks) {
            DataBlock dataBlock = new DataBlock(event.getBlock(),"BREAK",event.getPlayer().getDisplayName());

            BlockProcessor.getInstance().add(dataBlock);

            BlockProcessor.blocks.notifyAll();
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (Configuration.isPlacedBlocks()) {
            synchronized (BlockProcessor.blocks) {
                BlockProcessor.getInstance().add(new DataBlock(event.getBlock(), "PLACE", event.getPlayer().getDisplayName()));

                BlockProcessor.blocks.notifyAll();
            }
        }
    }

    @EventHandler
    public void onBlockExplode(final EntityExplodeEvent event) {
        //Custom Data Object List
        List<DataBlock> blocks = new ArrayList<>();

        //Event Blocks Default Object List
        
        for(Block i: event.blockList()) {
            DataBlock dataBlock = new DataBlock(i,"BREAK","SERVER");
            if (!Configuration.getBlockExceptions().contains(dataBlock.getMaterial().toString())) blocks.add(dataBlock);
        }

        synchronized (BlockProcessor.blocks) {
            BlockProcessor.blocks.addAll(blocks);

            //Wakes up a single thread that is waiting on this object's monitor aka Thread
            BlockProcessor.blocks.notifyAll();


        }
    }

    @EventHandler
    public void onBlockPistonMove(BlockPistonExtendEvent event) {
        //Replica from BlockPlace Event within BlockPiston moved
        //this.onBlockPlace((BlockPlaceEvent) e);
        event.getBlock();
    }

    /**
     * Decay of leaves after tree being cut
     * @param event LeavesDecayEvent
     */
    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        if (Configuration.isLeavesDecay()) {
            synchronized (BlockProcessor.blocks) {
                BlockProcessor.getInstance().add(new DataBlock(event.getBlock(), "BREAK", "SERVER"));

                //Wakes up a single thread that is waiting on this object's monitor aka Thread
                BlockProcessor.blocks.notifyAll();
            }
        }
    }

    @EventHandler
    public void onBlockBurnt(BlockBurnEvent event) {
        if (Configuration.isBurnedBlocks()) {
            synchronized (BlockProcessor.blocks) {
                BlockProcessor.getInstance().add(new DataBlock(event.getBlock(), "BREAK", "SERVER"));

                BlockProcessor.blocks.notifyAll();
            }
        }
    }
}

//TODO, Add expections for example, Chests on being broken, not to be regenerated
