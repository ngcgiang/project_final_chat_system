package config;

import java.sql.*;
import java.util.Properties;

public class DbConnection {
    String dbms = "mysql";
    String serverName = "localhost";
    int portNumber = 3306;
    String dbName = "chat_system";
    String user = "root";
    //String password = "nguyenhuytan2004";
    String password = "ngcjang1803";

    public Connection getConnection() {
        Connection conn = null;
        Properties connectionProps = new Properties();
        connectionProps.put("user", user);
        connectionProps.put("password", password);

        try {
            String connString = "jdbc:" + dbms + "://" + serverName +
                    ":" + portNumber + "/";
            conn = DriverManager.getConnection(connString, connectionProps);
            conn.setCatalog(dbName);
        } catch (SQLException e) {
            conn = null;
        }

        return conn;
    }
}