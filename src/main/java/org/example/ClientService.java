package org.example;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClientService {
    private Connection connection;

    public ClientService(Connection connection) {
        this.connection = connection;
        initializeDatabase();
    }

    private void initializeDatabase() {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS clients (id SERIAL PRIMARY KEY, name VARCHAR(255))");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public long create(String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String insertQuery = "INSERT INTO clients (name) VALUES (?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setString(1, name);
            preparedStatement.executeUpdate();
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                throw new SQLException("Creating client failed, no ID obtained.");
            }
        }
    }

    public String getById(long id) throws SQLException {
        String selectQuery = "SELECT name FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectQuery)) {
            preparedStatement.setLong(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getString("name");
            } else {
                throw new IllegalArgumentException("Client with id " + id + " not found");
            }
        }
    }

    public void setName(long id, String name) throws SQLException {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be null or empty");
        }

        String updateQuery = "UPDATE clients SET name = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateQuery)) {
            preparedStatement.setString(1, name);
            preparedStatement.setLong(2, id);
            int updatedRows = preparedStatement.executeUpdate();
            if (updatedRows == 0) {
                throw new IllegalArgumentException("Client with id " + id + " not found");
            }
        }
    }

    public void deleteById(long id) throws SQLException {
        String deleteQuery = "DELETE FROM clients WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(deleteQuery)) {
            preparedStatement.setLong(1, id);
            int deletedRows = preparedStatement.executeUpdate();
            if (deletedRows == 0) {
                throw new IllegalArgumentException("Client with id " + id + " not found");
            }
        }
    }

    public List<Client> listAll() throws SQLException {
        List<Client> clients = new ArrayList<>();
        String selectQuery = "SELECT * FROM clients";
        try (Statement statement = connection.createStatement()) {
            ResultSet resultSet = statement.executeQuery(selectQuery);
            while (resultSet.next()) {
                long id = resultSet.getLong("id");
                String name = resultSet.getString("name");
                clients.add(new Client(id, name));
            }
        }
        return clients;
    }
}