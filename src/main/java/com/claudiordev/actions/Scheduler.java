package com.claudiordev.actions;

import com.claudiordev.Main;
import com.claudiordev.config.Configuration;
import com.claudiordev.utils.ColorCodes;
import org.bukkit.scheduler.BukkitRunnable;
import time.Converters;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Scheduler extends BukkitRunnable {

    int timer = 0; //Seconds
    private static Scheduler scheduler;

    /**
     * @return Singleton of Scheduler
     */
    public static Scheduler getInstance() {
        if (scheduler == null) {
            scheduler = new Scheduler();
        }
        return scheduler;
    }

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(1000);
                timer++;

                Main.getPlugin().getLogger().info("Scheduler is running, timer: " + timer);

                if (Converters.secondToMinute(timer) >= Configuration.getScheduleRegenerationTime() && !RegenBlocks.isRunning()) {
                    ResultSet rs = Main.getData().retrieve("SELECT *,max(action_date) as max_action_date FROM blocks_data group by id;");
                    RegenBlocks.getInstance(rs,Main.getPlugin().getServer().getConsoleSender());

                    timer = 0;
                } else if(Converters.secondToMinute(timer) >= Configuration.getScheduleRegenerationTime()  && RegenBlocks.isRunning()){
                    Main.getPlugin().getServer().getLogger().info(ColorCodes.executeReplace("&7Map already being regenerated, skipping this turn..."));

                    timer = 0;
                }
                //Reset timer and break scheduler

            } catch (InterruptedException | SQLException e) {
                this.cancel();
                Main.getPlugin().getLogger().severe("Error on Scheduler Thread, please restart the server as soon as possible and report this error on SpigotMC.org!");
            }

        }
    }

}
