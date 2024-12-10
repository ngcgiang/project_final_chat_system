package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import io.github.cdimascio.dotenv.Dotenv;

public class DbConnection {

    private String dbms;
    private String serverName;
    private int portNumber;
    private String dbName;
    private String user;
    private String password;

    public DbConnection() {
        // Load the environment variables from the .env file
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        
        dbms = dotenv.get("DBMS");
        serverName = dotenv.get("SERVER_NAME");
        portNumber = Integer.parseInt(dotenv.get("PORT_NUMBER"));
        dbName = dotenv.get("DB_NAME");
        user = dotenv.get("USER");
        password = dotenv.get("PASSWORD");
    }

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
            e.printStackTrace();
        }

        return conn;
    }
}
