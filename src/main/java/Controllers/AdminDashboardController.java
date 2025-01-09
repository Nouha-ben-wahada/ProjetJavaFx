package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AdminDashboardController {

    @FXML
    void handleGestionProduits(ActionEvent event) throws IOException {
        ouvrirFenetre("/produitinterface.fxml");
    }

    @FXML
    void handleGestionCommandes(ActionEvent event) throws IOException {
        ouvrirFenetre("/CommandeInterface.fxml");
    }

    @FXML
    void handleGestionLivraisons(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/GestionLivraisons.fxml");
    }

    @FXML
    void handleGestionReclamations(ActionEvent event) throws IOException {
        ouvrirFenetre("/AdminRecPage.fxml");
    }

    private void ouvrirFenetre(String fichierFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void handleDeconnexion(ActionEvent actionEvent) throws IOException {
        ouvrirFenetre("/LoginPage.fxml");
    }
}
