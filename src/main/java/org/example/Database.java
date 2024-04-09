package org.example;

import org.flywaydb.core.Flyway;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    public static Connection connection;
    private static final Database INSTANCE = new Database();

    private Database() {
        String url = PropertyReader.getConnectionUrlForPostgres();
        String user = PropertyReader.getUserForPostgres();
        String password = PropertyReader.getPasswordForPostgres();
        try {
            connection = DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            System.out.println(String.format("SQL exception. Can not create connection. Reason: %s", e.getMessage()));
            throw new RuntimeException("Can not create connection.");
        }
    }

    public static Database getInstance(){
        return INSTANCE;
    }

    public static Connection getConnection(){
        return connection;
    }

    public void closeConnection() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void flywayMigration(String connectionUrl, String username, String password){
        Flyway flyway = Flyway.configure().dataSource(connectionUrl, username, password).load();
        flyway.migrate();
    }
}
