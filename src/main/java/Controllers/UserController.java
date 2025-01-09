package Controllers;

import Service.ServiceProduitUser;
import Service.ServiceUser;
import entités.Produit;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.event.ActionEvent;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    @FXML private TextField txtNomSearchUser;
    @FXML private ComboBox<String> cmbCategorieSearchUser;
    @FXML private ComboBox<String> cmbSousCategorieSearchUser;
    @FXML private TableView<Produit> tableResults;
    @FXML private TableColumn<Produit, String> colNomProduit;
    @FXML private TableColumn<Produit, String> colCategorieProduit;
    @FXML private TableColumn<Produit, String> colSousCategorieProduit;
    @FXML private TableColumn<Produit, Double> colPrixProduit;

    private ServiceProduitUser serviceProduitUser;

    public UserController() {
        this.serviceProduitUser = new ServiceProduitUser();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        colNomProduit.setCellValueFactory(cellData -> cellData.getValue().nomProperty());
        colCategorieProduit.setCellValueFactory(cellData -> cellData.getValue().categorieProperty());
        colSousCategorieProduit.setCellValueFactory(cellData -> cellData.getValue().sousCategorieProperty());
        colPrixProduit.setCellValueFactory(cellData -> cellData.getValue().prixProperty().asObject());

        cmbCategorieSearchUser.getItems().addAll("Homme", "Femme", "Enfant");
        cmbSousCategorieSearchUser.getItems().addAll("Vêtement", "Accessoire");

        String centerStyle = "-fx-alignment: CENTER;";
        colNomProduit.setStyle(centerStyle);
        colCategorieProduit.setStyle(centerStyle);
        colSousCategorieProduit.setStyle(centerStyle);
        colPrixProduit.setStyle(centerStyle);
    }

    @FXML
    public void rechercherProduitUser(ActionEvent event) {
        try {
            String nomProduit = txtNomSearchUser.getText();
            String categorie = cmbCategorieSearchUser.getValue();
            String sousCategorie = cmbSousCategorieSearchUser.getValue();

            if ((nomProduit == null || nomProduit.trim().isEmpty()) &&
                    (categorie == null || categorie.isEmpty()) &&
                    (sousCategorie == null || sousCategorie.isEmpty())) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Avertissement");
                alert.setHeaderText("Critères de recherche manquants");
                alert.setContentText("Veuillez fournir au moins un critère de recherche.");
                alert.showAndWait();
                return;
            }

            ObservableList<Produit> results;
            if ("Vêtement".equalsIgnoreCase(sousCategorie)) {
                // Rechercher uniquement dans la table vêtements
                results = serviceProduitUser.getProduitsFromVetements(categorie, nomProduit);
            } else if ("Accessoire".equalsIgnoreCase(sousCategorie)) {
                // Rechercher uniquement dans la table accessoires
                results = serviceProduitUser.getProduitsFromAccessoires(categorie, nomProduit);
            } else {
                // Rechercher dans toutes les tables (fallback)
                results = serviceProduitUser.getProduitsFiltrés(categorie, sousCategorie, nomProduit);
            }

            if (results.isEmpty()) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Aucun résultat");
                alert.setHeaderText(null);
                alert.setContentText("Aucun produit ne correspond aux critères de recherche.");
                alert.showAndWait();
            } else {
                tableResults.setItems(results);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erreur");
            alert.setHeaderText("Erreur de recherche");
            alert.setContentText("Une erreur est survenue: " + e.getMessage());
            alert.showAndWait();
        }
    }


}
