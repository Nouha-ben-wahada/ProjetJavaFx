package ConnectionDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DataBaseConnection {
    private static DataBaseConnection instance;
    private Connection connection;

    // Constantes pour la configuration
    private static final String URL = "jdbc:mysql://localhost:3306/projetjava";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private DataBaseConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = createConnection();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Erreur d'initialisation de la connexion", e);
        }
    }

    public static synchronized DataBaseConnection getInstance() {
        if (instance == null) {
            instance = new DataBaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = createConnection();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erreur de connexion à la base de données", e);
        }
        return connection;
    }

    private Connection createConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }
}
