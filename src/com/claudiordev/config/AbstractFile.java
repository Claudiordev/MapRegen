package com.claudiordev.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

abstract public class AbstractFile {
    //Data Folder Path, "getDataFolder(); AND Java Native File
    private File nativeFile;
    private static FileConfiguration fileConfiguration;

    //Default constructor
    public AbstractFile(File dataFolder, String fileName) {
        this.nativeFile = new File(dataFolder, fileName); //Path of the File and FileName, "/BlockRegen", "messages.yml"

        //If File dosen't exist, create new File
        if (!nativeFile.exists()) {
            try {
                nativeFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.fileConfiguration = YamlConfiguration.loadConfiguration(nativeFile);
    }

    /**
     * Add a value to the File
     * @param path String path of the value
     * @param value Value in the Path in Question
     */
    public void add(String path, String value){
        fileConfiguration.set(path,value);
    }

    /**
     * Save file
     */
    public void save() {
        try {
            fileConfiguration.save(nativeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Used to call the FileConfiguration object
     * @return FileConfiguration Object
     */
    public static FileConfiguration getFileConfiguration() {
        return fileConfiguration;
    }

}