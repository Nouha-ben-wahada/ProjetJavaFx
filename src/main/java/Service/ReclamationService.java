package Service;

import ConnectionDB.DataBaseConnection;
import entités.Reclamation;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ReclamationService {

    private final Connection connection = DataBaseConnection.getInstance().getConnection();

    // Constructor
    public ReclamationService() {
        try {
            if (connection == null || connection.isClosed()) {
                throw new RuntimeException("Database connection is not available.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error verifying database connection: " + e.getMessage());
        }
    }

    // Add a reclamation
    public void addReclamation(Reclamation reclamation) throws SQLException {
        String query = "INSERT INTO Reclamation (email, idCommande, idProduit, description, photoPath, dateReclamation) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, reclamation.getEmail()); // Set email
            statement.setInt(2, reclamation.getIdCommande());
            statement.setString(3, reclamation.getIdProduit());
            statement.setString(4, reclamation.getDescription());
            statement.setString(5, reclamation.getPhotoPath());
            statement.setTimestamp(6, Timestamp.valueOf(reclamation.getDateReclamation()));

            statement.executeUpdate();

            // Retrieve the generated idReclamation
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    reclamation.setIdReclamation(generatedKeys.getInt(1));
                }
            }
        }
    }

    // Update a reclamation
    public void updateReclamation(Reclamation reclamation) throws SQLException {
        String query = "UPDATE Reclamation SET email = ?, idCommande = ?, idProduit = ?, description = ?, photoPath = ?, dateReclamation = ? WHERE idReclamation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, reclamation.getEmail()); // Update email
            statement.setInt(2, reclamation.getIdCommande());
            statement.setString(3, reclamation.getIdProduit());
            statement.setString(4, reclamation.getDescription());
            statement.setString(5, reclamation.getPhotoPath());
            statement.setTimestamp(6, Timestamp.valueOf(reclamation.getDateReclamation()));
            statement.setInt(7, reclamation.getIdReclamation());
            statement.executeUpdate();
        }
    }

    // Delete a reclamation by ID
    public void deleteReclamation(int idReclamation) throws SQLException {
        String query = "DELETE FROM Reclamation WHERE idReclamation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idReclamation);
            statement.executeUpdate();
        }
    }

    // Get a reclamation by ID
    public Reclamation getReclamationById(int idReclamation) throws SQLException {
        String query = "SELECT * FROM Reclamation WHERE idReclamation = ?";
        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, idReclamation);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return mapResultSetToReclamation(resultSet);
                }
            }
        }
        return null;
    }

    // Get all reclamations
    public List<Reclamation> getAllReclamations() throws SQLException {
        List<Reclamation> reclamations = new ArrayList<>();
        String query = "SELECT * FROM Reclamation";
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                reclamations.add(mapResultSetToReclamation(resultSet));
            }
        }
        return reclamations;
    }

    // Helper method to map ResultSet to Reclamation object
    private Reclamation mapResultSetToReclamation(ResultSet resultSet) throws SQLException {
        return new Reclamation(
                resultSet.getInt("idReclamation"),
                resultSet.getString("email"), // Map email
                resultSet.getInt("idCommande"),
                resultSet.getString("idProduit"),
                resultSet.getString("description"),
                resultSet.getString("photoPath"),
                resultSet.getTimestamp("dateReclamation").toLocalDateTime()
        );
    }
}
