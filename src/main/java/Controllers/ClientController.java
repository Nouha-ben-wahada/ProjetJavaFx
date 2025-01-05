package Controllers;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ClientController {

    public void handleConsulterProduits(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/ConsulterProduits.fxml");
    }

    public void handleRechercherProduit(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/RechercherProduit.fxml");
    }

    public void handleGererPanier(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/GererPanier.fxml");
    }

    public void handlePasserCommande(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/PasserCommande.fxml");
    }

    public void handleConsulterCommandes(ActionEvent event) throws IOException {
        ouvrirFenetre("/Views/ConsulterCommandes.fxml");
    }

    public void handleDeconnexion(ActionEvent event) throws IOException {
        ouvrirFenetre("/LoginPage.fxml");
    }

    private void ouvrirFenetre(String fichierFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
