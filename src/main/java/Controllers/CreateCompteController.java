package Controllers;

import java.io.IOException;
import java.sql.SQLException;

import entités.Role;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import entités.User;
import Service.ServiceUser;
import org.mindrot.jbcrypt.BCrypt;  // Import BCrypt

public class CreateCompteController {

    @FXML
    private TextField txtadresse;

    @FXML
    private TextField txtemail;

    @FXML
    private TextField txtnom;

    @FXML
    private TextField txtpassword;

    @FXML
    private TextField txtprenom;

    @FXML
    private TextField txttel;

    @FXML
    private TextField txtuser;

    @FXML
    private ComboBox<Role> comboRole;

    private ServiceUser serviceUser = new ServiceUser();

    @FXML
    public void initialize() {
        comboRole.setItems(FXCollections.observableArrayList(Role.values()));
    }

    @FXML
    void annuler(ActionEvent event) {
        // Réinitialise tous les champs
        txtadresse.clear();
        txtemail.clear();
        txtnom.clear();
        txtpassword.clear();
        txtprenom.clear();
        txttel.clear();
        txtuser.clear();
        comboRole.setValue(null);
    }

    @FXML
    void creer(ActionEvent event) {
        // Validation des entrées utilisateur
        if (!validerChamps()) {
            afficherAlerte("Erreur", "Tous les champs doivent être remplis correctement.", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Hashing the password before creating the User object
            String hashedPassword = BCrypt.hashpw(txtpassword.getText(), BCrypt.gensalt());

            // Création de l'objet User avec mot de passe haché
            User user = new User(
                    txtnom.getText(),
                    txtprenom.getText(),
                    txttel.getText(),
                    txtemail.getText(),
                    txtadresse.getText(),
                    txtuser.getText(),
                    hashedPassword,  // Use hashed password
                    comboRole.getValue()
            );

            // Ajout de l'utilisateur via le service
            serviceUser.ajouter(user);
            afficherAlerte("Succès", "Compte créé avec succès.", Alert.AlertType.INFORMATION);

            // Réinitialise les champs après succès
            annuler(event);
            switchScene("/LoginPage.fxml");
        } catch (SQLException e) {
            afficherAlerte("Erreur SQL", "Une erreur est survenue lors de la création du compte : " + e.getMessage(), Alert.AlertType.ERROR);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur de format", "Le numéro de téléphone doit être un entier valide.", Alert.AlertType.ERROR);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean validerChamps() {
        return !(txtnom.getText().isEmpty() ||
                txtprenom.getText().isEmpty() ||
                txttel.getText().isEmpty() || !txttel.getText().matches("\\d{8}") ||
                txtemail.getText().isEmpty() || !txtemail.getText().matches("^[\\w.%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$") ||
                txtadresse.getText().isEmpty() ||
                txtuser.getText().isEmpty() ||
                txtpassword.getText().isEmpty() ||
                comboRole.getValue() == null);
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        txtnom.getScene().setRoot(root);
    }

    @FXML
    void retourAccueil(ActionEvent event) throws IOException {
        switchScene("/HomePage.fxml");
    }
}
