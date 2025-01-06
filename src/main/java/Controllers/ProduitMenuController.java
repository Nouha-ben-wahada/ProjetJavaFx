package Controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import entités.Produit;
import entités.Vetements;
import entités.Accessoires;
import Service.ServiceProduit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class ProduitMenuController {

    @FXML
    private ComboBox<String> cmbCategorie;  // ComboBox pour la sélection de catégorie

    // Sections de la VBox
    @FXML
    private VBox addProduit;
    @FXML
    private VBox deleteProduit;
    @FXML
    private VBox updateProduit;
    @FXML
    private VBox searchProduit;

    // Champs de texte pour les entrées
    @FXML
    private TextField txtIdAdd, txtNomAdd, txtDescriptionAdd, txtPrixAdd, txtStockAdd, txtSousCategorieAdd;
    @FXML
    private TextField txtIdDelete;
    @FXML
    private TextField txtIdUpdate, txtNomUpdate, txtDescriptionUpdate, txtPrixUpdate, txtStockUpdate, txtCategorieUpdate, txtSousCategorieUpdate;
    @FXML
    private TextField txtNomSearch;

    private ServiceProduit serviceProduit = new ServiceProduit();

    // Initialisation du ComboBox avec les catégories
    public void initialize() {
        ObservableList<String> categories = FXCollections.observableArrayList("Homme", "Femme", "Enfant");
        cmbCategorie.setItems(categories);  // Définir les éléments du ComboBox
    }

    // Afficher la section Ajouter Produit
    @FXML
    void afficherAjouter(ActionEvent event) {
        addProduit.setVisible(true);
        deleteProduit.setVisible(false);
        updateProduit.setVisible(false);
        searchProduit.setVisible(false);
    }

    // Afficher la section Supprimer Produit
    @FXML
    void afficherSupprimer(ActionEvent event) {
        addProduit.setVisible(false);
        deleteProduit.setVisible(true);
        updateProduit.setVisible(false);
        searchProduit.setVisible(false);
    }

    // Afficher la section Mettre à jour Produit
    @FXML
    void afficherMettreAJour(ActionEvent event) {
        addProduit.setVisible(false);
        deleteProduit.setVisible(false);
        updateProduit.setVisible(true);
        searchProduit.setVisible(false);
    }

    // Afficher la section Rechercher Produit
    @FXML
    void afficherRechercher(ActionEvent event) {
        addProduit.setVisible(false);
        deleteProduit.setVisible(false);
        updateProduit.setVisible(false);
        searchProduit.setVisible(true);
    }

    // Annuler l'action et réinitialiser les vues
    @FXML
    void annuler(ActionEvent event) {
        addProduit.setVisible(false);
        deleteProduit.setVisible(false);
        updateProduit.setVisible(false);
        searchProduit.setVisible(false);
    }

    // Ajouter un produit
    @FXML
    void ajouterProduit(ActionEvent event) {
        try {
            // Vérification des champs nécessaires
            if (txtNomAdd.getText().isEmpty() || txtDescriptionAdd.getText().isEmpty() ||
                    txtPrixAdd.getText().isEmpty() || txtStockAdd.getText().isEmpty() ||
                    cmbCategorie.getSelectionModel().isEmpty() || txtSousCategorieAdd.getText().isEmpty() ||
                    txtIdAdd.getText().isEmpty()) {
                showAlert(AlertType.WARNING, "Veuillez remplir tous les champs.");
                return;
            }

            // Validation des champs numériques
            double prix;
            int stock;
            try {
                prix = Double.parseDouble(txtPrixAdd.getText());
                stock = Integer.parseInt(txtStockAdd.getText());
            } catch (NumberFormatException e) {
                showAlert(AlertType.WARNING, "Veuillez entrer des valeurs numériques valides pour le prix et le stock.");
                return;
            }

            // Créer un produit en fonction de la catégorie et sous-catégorie
            Produit produit;

            if (txtSousCategorieAdd.getText().equalsIgnoreCase("Vetement")) {
                produit = new Vetements(
                        txtIdAdd.getText(),
                        txtNomAdd.getText(),
                        txtDescriptionAdd.getText(),
                        prix,
                        stock,
                        cmbCategorie.getValue(),
                        txtSousCategorieAdd.getText(),
                        "Taille ici",
                        "Couleur ici"
                );
            } else if (txtSousCategorieAdd.getText().equalsIgnoreCase("Accessoire")) {
                produit = new Accessoires(
                        txtIdAdd.getText(),
                        txtNomAdd.getText(),
                        txtDescriptionAdd.getText(),
                        prix,
                        stock,
                        cmbCategorie.getValue(),
                        txtSousCategorieAdd.getText(),
                        "Couleur ici",
                        "Matière ici"
                );
            } else {
                showAlert(AlertType.WARNING, "Sous-catégorie inconnue. Veuillez choisir 'Vetement' ou 'Accessoire'.");
                return;
            }

            // Ajouter le produit à la base de données
            serviceProduit.ajouter(produit);
            showAlert(AlertType.INFORMATION, "Produit ajouté avec succès!");
            resetFields();

        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur lors de l'ajout du produit: " + e.getMessage());
        }
    }


    // Supprimer un produit
    // Supprimer un produit
    // Supprimer un produit
    // Supprimer un produit
    @FXML
    void supprimerProduit(ActionEvent event) {
        try {
            String referenceProduit = txtIdDelete.getText();  // Utiliser "référence" au lieu de "ID"
            if (referenceProduit.isEmpty()) {
                showAlert(AlertType.WARNING, "Veuillez entrer la référence du produit à supprimer.");
                return;
            }

            // Utilisation de la méthode supprimer() avec la référence
            serviceProduit.supprimer(referenceProduit);
            showAlert(AlertType.INFORMATION, "Produit supprimé avec succès !");
            txtIdDelete.clear();
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur lors de la suppression du produit: " + e.getMessage());
        }
    }




    // Mettre à jour un produit
    @FXML
    void mettreAJourProduit(ActionEvent event) {
        try {
            String produitId = txtIdUpdate.getText();
            if (produitId.isEmpty()) {
                showAlert(AlertType.WARNING, "Veuillez entrer l'ID du produit à mettre à jour.");
                return;
            }

            Produit produit = serviceProduit.getById(produitId); // Rechercher le produit par ID
            if (produit != null) {
                // Mettre à jour les informations du produit
                produit.setNom(txtNomUpdate.getText());
                produit.setDescription(txtDescriptionUpdate.getText());
                produit.setPrix(Double.parseDouble(txtPrixUpdate.getText()));
                produit.setStock(Integer.parseInt(txtStockUpdate.getText()));

                serviceProduit.update(produit);
                showAlert(AlertType.INFORMATION, "Produit mis à jour avec succès!");
            } else {
                showAlert(AlertType.WARNING, "Produit non trouvé.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur lors de la mise à jour du produit: " + e.getMessage());
        }
    }

    // Rechercher un produit
    @FXML
    void rechercherProduit(ActionEvent event) {
        try {
            String produitNom = txtNomSearch.getText();
            if (produitNom.isEmpty()) {
                showAlert(AlertType.WARNING, "Veuillez entrer un nom de produit à rechercher.");
                return;
            }

            Produit produit = serviceProduit.getById(produitNom); // Utilisation de getById pour rechercher par référence
            if (produit != null) {
                showAlert(AlertType.INFORMATION, "Produit trouvé: " + produit.getNom());
            } else {
                showAlert(AlertType.WARNING, "Aucun produit trouvé avec ce nom.");
            }
        } catch (SQLException e) {
            showAlert(AlertType.ERROR, "Erreur lors de la recherche du produit: " + e.getMessage());
        }
    }

    // Méthode pour afficher une alerte
    private void showAlert(AlertType alertType, String message) {
        Alert alert = new Alert(alertType);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Réinitialiser les champs après ajout
    private void resetFields() {
        txtIdAdd.clear();
        txtNomAdd.clear();
        txtDescriptionAdd.clear();
        txtPrixAdd.clear();
        txtStockAdd.clear();
        cmbCategorie.getSelectionModel().clearSelection();
        txtSousCategorieAdd.clear();
    }
}
