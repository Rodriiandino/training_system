package training.system.core.factory;

import training.system.core.exception.DatabaseConnectionException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory implements AutoCloseable {
    private static final String url = "jdbc:mysql://localhost:3306/training_system?useTimezone=true&serverTimezone=UTC";
    private static final String usuario = "trainingUser";
    private static final String password = "Training123";

    private Connection connection;

    public ConnectionFactory() throws DatabaseConnectionException {
        try {
            connection = DriverManager.getConnection(url, usuario, password);
        } catch (SQLException e) {
            throw new DatabaseConnectionException("Error al conectar a la base de datos", e);
        }
    }

    public Connection getConnection() {
        return connection;
    }

    @Override
    public void close() throws SQLException, DatabaseConnectionException {
        if (connection != null && !connection.isClosed()) {
            try {
                connection.close();
            } catch (SQLException e) {
                throw new DatabaseConnectionException("Error al cerrar la conexión a la base de datos", e);
            }
        }
    }
}