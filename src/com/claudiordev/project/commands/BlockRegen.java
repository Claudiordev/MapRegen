package com.claudiordev.project.commands;

import com.claudiordev.project.actions.RegenBlocks;
import com.claudiordev.project.data.MySQL;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.io.IOException;
import java.sql.ResultSet;

public class BlockRegen implements CommandExecutor {

    MySQL mysql = new MySQL();

    public BlockRegen() throws IOException {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (label.equals("regen") && strings.length <= 0) {
            commandSender.sendMessage("Executing regeneration of blocks...");

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ResultSet rs = mysql.retrieveAllData("SELECT *,max(action_date) as max_action_date FROM blocks_data group by id;");

                        //Execute RegenBlocks action
                        new RegenBlocks(rs);

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        } else {
            commandSender.sendMessage("No arguments available");
        }
        return true;
    }
}
