package com.claudiordev.project.data;

import com.claudiordev.project.Main;
import data.AbstractData;
import org.apache.commons.io.FileUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;

public class SQLLite extends AbstractData {

    public static File file = new File(Main.getPlugin().getDataFolder().toString() + "\\db\\Data.db");

    public SQLLite(){
        //Default Constructor
    }

    public SQLLite(String url) throws IOException {
        this.url = url;
        createdir();
    }

    /**
     * Create Directory for the SQLLite Database;
     */
    public void createdir() throws IOException {
        if (!file.exists()) {
            file.createNewFile();
        }
    }

    /**
     * Create a table within the Database;
     */
    public void createTable(String tableName) {
        classCall();

        String sql = "CREATE TABLE IF NOT EXISTS blocks_data (id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "player_name varchar(64) NOT NULL,\n" +
                "block_type varchar(64) NOT NULL,\n" +
                "block_data varchar(64) NOT NULL DEFAULT 0,\n" +
                "block_x int(11) NOT NULL DEFAULT 0,\n" +
                "block_y int(11) NOT NULL DEFAULT 0,\n" +
                "block_z int(11) NOT NULL DEFAULT 0,\n" +
                "action_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);";

        this.tablename = tableName;

        try (Connection conn = DriverManager.getConnection(url);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }
    }

    public boolean insert(String... args) {
        classCall();

        String query = "INSERT INTO blocks_data (player_name,block_type,block_data,block_x,block_y,block_z) " +
                "values ('"+ args[0] +"','"+ args[1]+ "','"+ args[2] +"','"+ args[3] +"','"+ args[4] +"','"+ args[5] +"');";

        //Main.getPlugin().getLogger().info("Inserted into SQLLite: " + query);

        try (Connection conn = DriverManager.getConnection(url)) {
            Statement stmt = conn.createStatement();
            if(stmt.execute(query)) return true;

            //Closes conn automatically after Try/Catch;
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }

        return false;
    }

    /**
     * @param query String query used to select the data;
     * @return A resultset of data with a Select Query
     */
    public ResultSet retrieveAllData(String query) {
        try {
            classCall();

            Connection conn = DriverManager.getConnection(url);
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }

    /** Call the Driver for JDBC SQLLite **/
    public void classCall() {
        //Set the Driver class for JDBC MySQL
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
