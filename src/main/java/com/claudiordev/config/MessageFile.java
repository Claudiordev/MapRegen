package com.claudiordev.config;

import java.io.File;

public class MessageFile extends AbstractFile {
    //Constructor matching super constructor
    public MessageFile(File dataFolder, String fileName) {
        super(dataFolder, fileName);
    }

    /**
     * Initiate the load of the Default values and save them into the respective file;
     */
    public void loadMessages() {
        this.add("No-permission", "&cYou do not have permission for this!");
        this.add("Cmd-regenerate", "&7Executing regeneration...");
        this.add("Cmd-already-regenerating", "&7A regeneration is already on going...");
        this.add("Cmd-count", "&7 {0} blocks to be regenerated");
        this.add("Data-Argument-Missing", "&cError, argument missing, please type the destiny (MySQL or SQLLite)");
        this.add("Data-Wrong-Argument", "&cError, wrong argument, please type the destiny (MySQL or SQLLite)");
        this.add("Data-SQLLite-Import-Success", "&aImported data to SQLLite with success!");
        this.add("Data-SQLLite-Import-Start", "&7Starting to import MySQL data into SQLLite Database...");
        this.add("Data-MySQL-Import-Success", "&aImported data to MySQL with success!");
        this.add("Data-MySQL-Import-Start", "&7Starting to import SQLLite data into MySQL Database...");
        this.add("Data-MySQL-Connect-Error", "&cError connection to the Database, please check the config.yml ands logs, and try again!");
        this.save();
    }

    /**
     * @param path relative to the message, e.g "No-permission"
     * @return the String value of the message
     */
    public static String getMessage(String path) {
        return MessageFile.getFileConfiguration().getString(path);
    }
}
