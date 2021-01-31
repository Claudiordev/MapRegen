package com.claudiordev.project.custom;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

public class DataBlock {

    private Location location;
    private Material material;
    private byte data;
    private String action = "BREAK"; //"PLACE" or "BREAK"

    @SuppressWarnings("deprecation")
    public DataBlock(Block block, String action){
        this.location = block.getLocation();
        this.material = block.getType();
        this.data = block.getData();
        this.action = action;
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
}
