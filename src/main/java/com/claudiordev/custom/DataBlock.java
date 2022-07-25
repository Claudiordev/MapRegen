package com.claudiordev.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * Custom Block Data
 */
public class DataBlock {

    private Location location;
    private Material material;
    private byte data;
    private String action = "BREAK"; //"PLACE" or "BREAK"
    private String player; //Player responsible for the action

    private String blockType;

    @SuppressWarnings("deprecation")
    public DataBlock(Block block, String action, String player){
        this.location = block.getLocation();
        this.material = block.getType();
        this.blockType = block.getType().name();
        this.data = block.getData();
        this.action = action;
        this.player = player;
    }

    public Location getLocation(){
        return location;
    }

    /**
     * @return Enum of Material of Block
     */
    public Material getMaterial(){
        return material;
    }

    /**
     * @return ID of the Block
     */
    public byte getData(){
        return data;
    }

    public String getAction() {
        return action;
    }

    public String getPlayer() {
        return player;
    }

}
