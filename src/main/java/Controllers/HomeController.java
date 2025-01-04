package Controllers;

import java.sql.SQLException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.io.IOException;
import java.sql.SQLException;

public class HomeController {

    @FXML
    private Button btnCreer;

    private void switchScene(String fxmlFile) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Parent root = loader.load();
        btnCreer.getScene().setRoot(root);
    }

    @FXML
    void CreerCompte(ActionEvent event) throws IOException {
        switchScene("/CreateCompte.fxml");
    }

    @FXML
    void Se_connecter(ActionEvent event) throws IOException {
        switchScene("/LoginPage.fxml");
    }

    @FXML
    void AfficherProduits(ActionEvent event) throws IOException {
        switchScene("/AfficherProduits.fxml");
    }
}
