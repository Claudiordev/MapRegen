package com.claudiordev.actions;

import com.claudiordev.Main;
import com.claudiordev.config.Configuration;
import com.claudiordev.utils.ColorCodes;
import time.Converters;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Automatic Regeneration Scheduler.
 * If enabled on configurations, runs the regeneration of the map automatically.
 */
public class Scheduler{
    int timer = 0;
    private static Scheduler scheduler;

    public Scheduler() {
        run();
    }

    public static Scheduler getInstance() {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }

        return scheduler;
    }

    public void run(){
        new Thread(() -> {
            while(true) {
                try {
                    Thread.sleep(1000);
                    timer++;

                    //Execute scheduler after x time and if not already running
                    if (Converters.secondToMinute(timer) >= Configuration.getScheduleRegenerationTime() && !RegenBlocks.isRunning()) {
                        ResultSet rs = Main.getData().retrieve("SELECT *,max(action_date) as max_action_date FROM blocks_data group by id;");

                        new RegenBlocks(rs,Main.getPlugin().getServer().getConsoleSender());

                        timer = 0;
                    } else if (Converters.secondToMinute(timer) >= Configuration.getScheduleRegenerationTime()  && RegenBlocks.isRunning()){
                        Main.getPlugin().getServer().getLogger().info(ColorCodes.executeReplace("&7Map already being regenerated onto scheduler, skipping this turn..."));
                    }
                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
