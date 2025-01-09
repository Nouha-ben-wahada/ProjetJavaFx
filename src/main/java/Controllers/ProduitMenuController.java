package Controllers;

import javafx.animation.TranslateTransition;
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
import javafx.scene.control.Label;
import javafx.util.Duration;
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

    @FXML
    private Label labelTitre;  // Injecter directement le Label

    public void initialize() {
        // Créer l'animation de translation pour le titre
        TranslateTransition transition = new TranslateTransition();
        transition.setNode(labelTitre);
        transition.setFromX(0); // Position initiale
        transition.setToX(300); // Déplace le titre à droite
        transition.setCycleCount(TranslateTransition.INDEFINITE); // L'animation boucle indéfiniment
        transition.setAutoReverse(true); // L'animation s'inverse après chaque cycle
        transition.setInterpolator(javafx.animation.Interpolator.LINEAR); // Choisir l'interpolation linéaire
        transition.setRate(0.5); // La vitesse de l'animation
        transition.setDuration(Duration.seconds(3)); // Durée de l'animation

        // Démarrer l'animation
        transition.play();
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
    @FXML
    private ComboBox<String> cmbSousCategorieAdd;

    @FXML
    void ajouterProduit(ActionEvent event) {
        try {
            // Vérification des champs nécessaires
            if (txtNomAdd.getText().isEmpty() || txtDescriptionAdd.getText().isEmpty() ||
                    txtPrixAdd.getText().isEmpty() || txtStockAdd.getText().isEmpty() ||
                    cmbCategorie.getSelectionModel().isEmpty() || cmbSousCategorieAdd.getSelectionModel().isEmpty() ||
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

            // Récupérer la sous-catégorie sélectionnée dans la ComboBox
            String sousCategorie = cmbSousCategorieAdd.getValue();
            Produit produit = null;

            // Créer un produit en fonction de la catégorie et sous-catégorie
            if (sousCategorie.equalsIgnoreCase("Vêtement")) {
                // If it's a 'Vetements' product, ask for more details (taille and couleur)
                String taille = "Taille ici"; // Replace with user input
                String couleur = "Couleur ici"; // Replace with user input

                produit = new Vetements(
                        txtIdAdd.getText(),
                        txtNomAdd.getText(),
                        txtDescriptionAdd.getText(),
                        prix,
                        stock,
                        cmbCategorie.getValue(),
                        sousCategorie,
                        taille,
                        couleur
                );
            } else if (sousCategorie.equalsIgnoreCase("Accessoire")) {
                // If it's an 'Accessoires' product, ask for more details (couleur and matière)
                String couleur = "Couleur ici"; // Replace with user input
                String matiere = "Matière ici"; // Replace with user input

                produit = new Accessoires(
                        txtIdAdd.getText(),
                        txtNomAdd.getText(),
                        txtDescriptionAdd.getText(),
                        prix,
                        stock,
                        cmbCategorie.getValue(),
                        sousCategorie,
                        couleur,
                        matiere
                );
            } else {
                showAlert(AlertType.WARNING, "Sous-catégorie inconnue. Veuillez choisir 'Vêtement' ou 'Accessoire'.");
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

    @FXML
    private VBox vboxVetementFields;
    @FXML
    private VBox vboxAccessoireFields;

    @FXML
    private void updateFieldsBySousCategorie() {
        String selectedSousCategorie = cmbSousCategorieAdd.getValue();

        if (selectedSousCategorie == null) {
            // Si aucune sous-catégorie n'est sélectionnée, masquer les champs
            vboxVetementFields.setVisible(false);
            vboxVetementFields.setManaged(false);
            vboxAccessoireFields.setVisible(false);
            vboxAccessoireFields.setManaged(false);
            return;
        }

        switch (selectedSousCategorie) {
            case "Vêtement":
                vboxVetementFields.setVisible(true);
                vboxVetementFields.setManaged(true);
                vboxAccessoireFields.setVisible(false);
                vboxAccessoireFields.setManaged(false);
                break;

            case "Accessoire":
                vboxVetementFields.setVisible(false);
                vboxVetementFields.setManaged(false);
                vboxAccessoireFields.setVisible(true);
                vboxAccessoireFields.setManaged(true);
                break;

            default:
                vboxVetementFields.setVisible(false);
                vboxVetementFields.setManaged(false);
                vboxAccessoireFields.setVisible(false);
                vboxAccessoireFields.setManaged(false);
                break;
        }
    }


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
