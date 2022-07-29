package com.claudiordev.commands;

import com.claudiordev.Main;
import com.claudiordev.actions.RegenBlocks;
import com.claudiordev.config.Configuration;
import com.claudiordev.config.MessageFile;
import com.claudiordev.utils.ColorCodes;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * /blockregen in-game chat command handler
 */
public class MapRegen implements CommandExecutor {

    public MapRegen() throws IOException {
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] strings) {
        if (commandSender.hasPermission("mapregen.command")) {
            if (label.equals("mapregen") && strings.length <= 0) {
                helpMessage(commandSender);
            } else {
                switch (strings[0]) {
                    case "regenerate":
                        if (!RegenBlocks.isRunning()) {
                            commandSender.sendMessage(ColorCodes.executeReplace(MessageFile.getMessage("Cmd-regenerate")));
                            regenThread(commandSender);
                        } else {
                            commandSender.sendMessage(ColorCodes.executeReplace(MessageFile.getMessage("Cmd-already-regenerating")));
                        }
                        break;

                    case "count":
                        try {
                            ResultSet rs = Main.getData().retrieve("SELECT count(id) from blocks_data;");

                            int blocks = rs.getInt("count(id)");
                            commandSender.sendMessage(ColorCodes.executeReplace(MessageFile.getMessage("Cmd-count").replace("{0}",String.valueOf(blocks))));
                            rs.close();
                        } catch (SQLException e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        helpMessage(commandSender);
                        break;

                    case "import":
                        commandSender.sendMessage(ColorCodes.executeReplace("&7"+ " Importing of data started..."));
                        break;
                }

            }
        } else {
            commandSender.sendMessage(ColorCodes.executeReplace(MessageFile.getMessage("No-permission")));
        }
        return true;
    }

    void helpMessage(CommandSender commandSender) {
        for (String message: Configuration.getHelpMessage()) {
            message = ColorCodes.executeReplace(message);
            commandSender.sendMessage(message);
        }
    }

    /**
     * Execute regeneration in a new Thread
     */
    void regenThread(CommandSender commandSender) {
        new BukkitRunnable() {
            @Override
            public void run() {
                try {
                    ResultSet rs = Main.getData().retrieve("SELECT *,max(action_date) as max_action_date FROM blocks_data group by id;");

                    RegenBlocks.getInstance(rs,commandSender).run();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.runTaskAsynchronously(Main.getPlugin());
    }
}
