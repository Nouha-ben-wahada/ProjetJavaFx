package Controllers;

import Service.ServiceProduit;
import entités.Produit;
import entités.Vetements;
import entités.Accessoires;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.SQLException;

public class AfficherProduitsController {

    @FXML
    private TableView<Produit> tableProduits;

    @FXML
    private TableColumn<Produit, String> colReference;

    @FXML
    private TableColumn<Produit, String> colNom;

    @FXML
    private TableColumn<Produit, String> colDescription;

    @FXML
    private TableColumn<Produit, Double> colPrix;

    @FXML
    private TableColumn<Produit, Integer> colStock;

    @FXML
    private TableColumn<Produit, String> colCategorie;

    @FXML
    private TableColumn<Produit, String> colSousCategorie;

    @FXML
    private TableColumn<Produit, String> colTaille; // Spécifique aux vêtements

    @FXML
    private TableColumn<Produit, String> colCouleur;

    @FXML
    private TableColumn<Produit, String> colMatiere; // Spécifique aux accessoires

    private ServiceProduit serviceProduit;

    private ObservableList<Produit> produitsList;

    public AfficherProduitsController() {
        serviceProduit = new ServiceProduit(); // Instance du service
        produitsList = FXCollections.observableArrayList();
    }

    @FXML
    public void initialize() {
        // Associer les colonnes aux attributs de Produit
        colReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colDescription.setCellValueFactory(new PropertyValueFactory<>("description"));
        colPrix.setCellValueFactory(new PropertyValueFactory<>("prix"));
        colStock.setCellValueFactory(new PropertyValueFactory<>("stock"));
        colCategorie.setCellValueFactory(new PropertyValueFactory<>("categorie"));
        colSousCategorie.setCellValueFactory(new PropertyValueFactory<>("sousCategorie"));
        colTaille.setCellValueFactory(new PropertyValueFactory<>("taille"));
        colCouleur.setCellValueFactory(new PropertyValueFactory<>("couleur"));
        colMatiere.setCellValueFactory(new PropertyValueFactory<>("matiere"));

        // Charger les produits depuis la base de données
        try {
            produitsList.addAll(serviceProduit.getAll());
        } catch (SQLException e) {
            System.out.println("Erreur lors du chargement des produits : " + e.getMessage());
        }

        tableProduits.setItems(produitsList);
    }


}
