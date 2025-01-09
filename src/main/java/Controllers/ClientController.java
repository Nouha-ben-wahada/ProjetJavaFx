package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Label;

public class ClientController {

    public void setLblClientName(String lblClientName) {
        this.lblClientName.setText(lblClientName);
    }

    @FXML
    private Label lblClientName;

    @FXML
    public void handleConsulterProduits(ActionEvent event) throws IOException {
        ouvrirFenetre("/AfficherProduits.fxml");
    }
    @FXML
    public void handleRechercherProduit(ActionEvent event) throws IOException {
        ouvrirFenetre("/userinterface.fxml");
    }
    @FXML
    void handleReclamer(ActionEvent event) throws IOException {
        ouvrirFenetre("/ReclamationPage.fxml");
    }
    @FXML
    public void handleConsulterCommandes(ActionEvent event) throws IOException {
        ouvrirFenetre("/gestion_commande.fxml");
    }
    @FXML
    public void handleDeconnexion(ActionEvent event) throws IOException {
        ouvrirFenetre("/LoginPage.fxml");
    }

    @FXML
    void handleReserverService(ActionEvent event) throws IOException {
        ouvrirFenetre("/ResCltView.fxml"); // Assurez-vous d'avoir un fichier FXML pour la réservation
    }
    private void ouvrirFenetre(String fichierFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
