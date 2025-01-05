package Controllers;

import Service.ServiceLogin;
import entités.Role;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    private ServiceLogin serviceLogin = new ServiceLogin();

    @FXML
    void Connecter(ActionEvent event) {
        if (!validerChamps()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis correctement.", Alert.AlertType.ERROR);
            return;
        }

        String username = txtUsername.getText();
        String password = txtPassword.getText();

        try {
            Role userRole = serviceLogin.login(username, password); // Obtenir le rôle après connexion

            if (userRole != null) {
                afficherAlerte("Connexion réussie", "Bienvenue, " + username + "!", Alert.AlertType.INFORMATION);

                // Redirection basée sur le rôle
                redirectByRole(userRole);
            } else {
                afficherAlerte("Erreur de connexion", "Nom d'utilisateur ou mot de passe incorrect.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Une erreur est survenue lors de la connexion : " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (IOException e) {
            afficherAlerte("Erreur", "Une erreur est survenue lors de la redirection.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void Annuler(ActionEvent event) {
        txtUsername.clear();
        txtPassword.clear();
    }

    @FXML
    void retourAccueil(ActionEvent event) throws IOException {
        switchScene("/HomePage.fxml");
    }

    private void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        txtUsername.getScene().setRoot(root);
    }

    private boolean validerChamps() {
        return !(txtUsername.getText().isEmpty() || txtPassword.getText().isEmpty());
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Redirige l'utilisateur vers une interface différente selon son rôle.
     */
    private void redirectByRole(Role role) throws IOException {
        if (role == Role.Client) {
            switchScene("/IhmClient.fxml"); // Interface client
        } else if (role == Role.Administrateur) {
            switchScene("/IhmAdmin.fxml"); // Interface administrateur
        } else {
            afficherAlerte("Erreur", "Rôle inconnu. Impossible de rediriger.", Alert.AlertType.ERROR);
        }
    }
}
