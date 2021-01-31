package com.claudiordev.project.data;

import com.claudiordev.project.Main;
import data.AbstractData;

import java.sql.*;

public class MySQL extends AbstractData {

    public MySQL(){
        //Default Constructor
    }

    public MySQL(String url, String username, String password) {
        this.url = url;
        this.username = username;
        this.password = password;
    }

    /**
     * Create a table within the Database;
     */
    public void createTable(String tableName) {
        classCall();

        String sql = "CREATE TABLE IF NOT EXISTS " + tableName +" (id INTEGER PRIMARY KEY AUTO_INCREMENT,\n" +
                "player_name varchar(64) NOT NULL,\n" +
                "block_type varchar(64) NOT NULL,\n" +
                "block_data varchar(64) NOT NULL DEFAULT 0,\n" +
                "block_x int(11) NOT NULL DEFAULT 0,\n" +
                "block_y int(11) NOT NULL DEFAULT 0,\n" +
                "block_z int(11) NOT NULL DEFAULT 0,\n" +
                "action_date timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP);";

        this.tablename = tableName;

        try (Connection conn = DriverManager.getConnection(url,username,password);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }
    }

    /**
     * @param query String query used to select the data;
     * @return A resultset of data with a Select Query
     */
    public ResultSet retrieveAllData(String query) {
        try {
            classCall();

            Connection conn = DriverManager.getConnection(url,username,password);
            Statement statement = conn.createStatement();
            return statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return  null;
    }

    public boolean insert(String... args) {
        classCall();

        String query = "INSERT INTO blocks_data (player_name,block_type,block_data,block_x,block_y,block_z,action) " +
                "values ('"+ args[0] +"','"+ args[1]+ "','"+ args[2] +"','"+ args[3] +"','"+ args[4] +"','"+ args[5] +"', '"+ args[6] +"');";

        //Main.getPlugin().getLogger().info("Inserted into SQLLite: " + query);

        try (Connection conn = DriverManager.getConnection(url,username,password)) {
            Statement stmt = conn.createStatement();
            if(stmt.execute(query)) return true;

            //Closes conn automatically after Try/Catch;
        } catch (SQLException e) {
            Main.getPlugin().getLogger().info(e.getMessage());
        }

        return false;
    }

    /** Call the Driver for JDBC SQLLite **/
    public void classCall() {
        //Set the Driver class for JDBC MySQL
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
