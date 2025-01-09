package Controllers;

import entités.Produit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;

public class PanierController {

    @FXML
    private TableView<Produit> tablePanier;
    @FXML
    private TableColumn<Produit, String> colNomProduit;
    @FXML
    private TableColumn<Produit, Double> colPrix;
    @FXML
    private TableColumn<Produit, Integer> colQuantite;
    @FXML
    private TableColumn<Produit, Double> colTotal;

    private ObservableList<Produit> panier = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Configure les colonnes
        colNomProduit.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colQuantite.setCellValueFactory(new PropertyValueFactory<>("quantite"));
        colTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

        // Ajout des données fictives pour tester
        // panier.add(new Produit(1,"Produit A","femme",20.0,"", 2));
//        panier.add(new Produit("Produit B", 15.0, 1));
//        panier.add(new Produit("Produit C", 30.0, 3));

        tablePanier.setItems(panier);
    }

    @FXML
    void handleViderPanier() {
        panier.clear();
        tablePanier.refresh();
        afficherAlerte("Panier", "Votre panier a été vidé.", Alert.AlertType.INFORMATION);
    }

    @FXML
    void handleSupprimerProduit() {
        Produit produitSelectionne = tablePanier.getSelectionModel().getSelectedItem();
        if (produitSelectionne != null) {
            panier.remove(produitSelectionne);
            tablePanier.refresh();
        } else {
            afficherAlerte("Erreur", "Veuillez sélectionner un produit à supprimer.", Alert.AlertType.ERROR);
        }
    }

    @FXML
    void handlePasserCommande() throws IOException{
        if (panier.isEmpty()) {
            afficherAlerte("Erreur", "Votre panier est vide. Ajoutez des produits avant de commander.", Alert.AlertType.ERROR);
            return;
        }
        // Logique pour passer une commande
        afficherAlerte("Succès", "Votre commande a été passée avec succès.", Alert.AlertType.INFORMATION);
        panier.clear();
        tablePanier.refresh();
        ouvrirFenetre("/commande_view.fxml");
    }

    private void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void ouvrirFenetre(String fichierFXML) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fichierFXML));
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }
}
