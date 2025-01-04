package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.control.Label;

import java.io.IOException;

public class AfficherProduitsController {

    @FXML
    private ImageView imageProduit1;
    @FXML
    private ImageView imageProduit2;
    @FXML
    private ImageView imageProduit3;

    @FXML
    private Label labelProduit1;
    @FXML
    private Label labelProduit2;
    @FXML
    private Label labelProduit3;

    @FXML
    private Button btnCreer;

//    public void initialize() {
//        // Charger les images des produits
//        imageProduit1.setImage(new Image(getClass().getResource("/Images/AppleWatchserieS6.jpg").toExternalForm()));  // Chemin relatif vers ressources
//        imageProduit2.setImage(new Image(getClass().getResource("/Images/BrownTissot.jpg").toExternalForm()));
//        imageProduit3.setImage(new Image(getClass().getResource("/Images/FossilClassic.jpg").toExternalForm()));
//
//        // Définir les titres des produits
//        labelProduit1.setText("Apple Watch Serie 6");
//        labelProduit2.setText("Montre Tissot Brown");
//        labelProduit3.setText("Montre Fossil Classic");
//    }

    @FXML
    private void voirDetails1() {
        System.out.println("Détails de la Apple Watch Serie 6");
        // Ajouter le code pour afficher les détails du produit 1, par exemple ouvrir une nouvelle scène.
    }

    @FXML
    private void voirDetails2() {
        System.out.println("Détails de la Montre Tissot Brown");
        // Ajouter le code pour afficher les détails du produit 2
    }

    @FXML
    private void voirDetails3() {
        System.out.println("Détails de la Montre Fossil Classic");
        // Ajouter le code pour afficher les détails du produit 3
    }

    private void switchScene(String fxmlFile) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
            Parent root = loader.load();
            btnCreer.getScene().setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
            // Afficher un message d'erreur à l'utilisateur
        }
    }

    @FXML
    void CreerCompte(ActionEvent event) throws IOException {
        switchScene("/CreateCompte.fxml");
    }

    @FXML
    void Se_connecter(ActionEvent event) throws IOException {
        switchScene("/LoginPage.fxml");
    }
}
