package Service;

import ConnectionDB.DataBaseConnection;
import entités.Client;
import entités.Role;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceLogin {
    private Connection connection = DataBaseConnection.getInstance().getConnection();

    public ServiceLogin() {
        if (connection == null) {
            System.out.println("La connexion à la base de données a échoué !");
        }
    }

    /**
     * Vérifie les informations de connexion.
     *
     * @param username Nom d'utilisateur fourni.
     * @param password Mot de passe fourni.
     * @return true si la connexion réussit, false sinon.
     * @throws SQLException En cas d'erreur SQL.
     */
    public Role login(String username, String password) throws SQLException {
        String query = "SELECT * FROM `user` WHERE user_name = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, username);

            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                String hashedPassword = resultSet.getString("password");
                if (BCrypt.checkpw(password, hashedPassword)) {
                    String roleString = resultSet.getString("role"); // Normalise en majuscules
                    try {
                        return Role.valueOf(roleString); // Conversion en Enum
                    } catch (IllegalArgumentException e) {
                        throw new IllegalArgumentException("Rôle inconnu : " + roleString);
                    }
                }
            }
        }
        return null; // Échec de connexion
    }

}
