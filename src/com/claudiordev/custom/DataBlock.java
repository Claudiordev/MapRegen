package com.claudiordev.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DataBlock {

    private Location location;
    private Material material;
    private byte data;
    private String action = "BREAK"; //"PLACE" or "BREAK"
    private String player; //Player responsible for the action

    @SuppressWarnings("deprecation")
    public DataBlock(Block block, String action, String player){
        this.location = block.getLocation();
        this.material = block.getType();
        this.data = block.getData();
        this.action = action;
        this.player = player;
    }

    public Location getLocation(){
        return location;
    }

    public Material getMaterial(){
        return material;
    }

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
