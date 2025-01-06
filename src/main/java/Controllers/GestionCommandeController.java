package Controllers;

import Service.ServiceGestionCommande;
import entités.GestionCommande;
import entités.Produit;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.TableView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.MapValueFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

import javafx.beans.property.SimpleObjectProperty;
import javafx.stage.Modality;
import javafx.stage.Stage;


public class GestionCommandeController {

    @FXML
    private TableView<GestionCommande> commandesTable;

    @FXML
    private TextField referenceProduitField;

    @FXML
    private TextField quantiteField;

    @FXML
    private Button ajouterProduitButton;

    @FXML
    private Label montantTotalLabel;

    private ServiceGestionCommande serviceGestionCommande;
    private GestionCommande commandeActuelle;
    private int UserId = 1 ;


    @FXML
    private TableView<Map<String, Object>> tableProduitsCommande;
    @FXML
    private TableColumn<Map<String, Object>, String> colNomProduit;
    @FXML
    private TableColumn<Map<String, Object>, Integer> colQuantiteProduit;
    @FXML
    private TableColumn<Map<String, Object>, Double> colPrixUnitaireProduit;
    @FXML
    private TableColumn<Map<String, Object>, Double> colPrixTotalProduit;

    private final ObservableList<Map<String, Object>> produitsList = FXCollections.observableArrayList();

    private Connection connection;

    @FXML
    public void initialize() {
        serviceGestionCommande = new ServiceGestionCommande();
        commandeActuelle = new GestionCommande(UserId); // Ex: utilisateur avec ID 1

        connection = ConnectionDB.DataBaseConnection.getInstance().getConnection();
        miseAJourUI();
        // Set up the TableColumn with custom callbacks
        colNomProduit.setCellValueFactory(data ->
                new SimpleObjectProperty<>((String) data.getValue().get("nom"))
        );
        colQuantiteProduit.setCellValueFactory(data ->
                new SimpleObjectProperty<>((Integer) data.getValue().get("quantite"))
        );
        colPrixUnitaireProduit.setCellValueFactory(data ->
                new SimpleObjectProperty<>((Double) data.getValue().get("prixUnitaire"))
        );
        colPrixTotalProduit.setCellValueFactory(data ->
                new SimpleObjectProperty<>((Double) data.getValue().get("prixTotal"))
        );

        // Bind the ObservableList to the TableView
        tableProduitsCommande.setItems(produitsList);
        // Add a listener to produitsList to monitor changes
        produitsList.addListener((ListChangeListener<? super Map<String, Object>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println("Added: " + change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    System.out.println("Removed: " + change.getRemoved());
                }
                if (change.wasUpdated()) {
                    System.out.println("Updated: " + produitsList);
                }
            }
        });

        // Optional: Add a listener to monitor changes directly in the TableView
        tableProduitsCommande.getItems().addListener((ListChangeListener<? super Map<String, Object>>) change -> {
            while (change.next()) {
                if (change.wasAdded()) {
                    System.out.println("TableView Added: " + change.getAddedSubList());
                }
                if (change.wasRemoved()) {
                    System.out.println("TableView Removed: " + change.getRemoved());
                }
            }
        });
    }

    @FXML
    public void ajouterProduit() {
        String referenceProduit = referenceProduitField.getText();
        int quantite;
        try {
            quantite = Integer.parseInt(quantiteField.getText());
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Quantité invalide.", Alert.AlertType.ERROR);
            return;
        }

        boolean ajouté = serviceGestionCommande.ajouterProduitDepuisBDD(commandeActuelle, referenceProduit, quantite);
        if (ajouté) {
            miseAJourUI();
            ajouterProduitDansTable(referenceProduit,quantite);
        }
//        } else {
//            afficherAlerte("Erreur", "Impossible d'ajouter le produit.", Alert.AlertType.ERROR);
//        }
    }

    private void miseAJourUI() {
        montantTotalLabel.setText(String.format("Montant Total: %.2f ", commandeActuelle.getMontantTotal()));
        commandeActuelle.afficherCommande(); // Debug
    }

    public static void afficherAlerte(String titre, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(titre);
        alert.setContentText(message);
        alert.showAndWait();
    }


//    public void updateTableUI(String referenceProduitToRemove) {
//        // Clear the existing TableView data
//        produitsList.clear();
//
//        // Iterate through the products in the current order (Map<Produit, Integer>)
//        for (Map.Entry<Produit, Integer> entry : commandeActuelle.getProduits().entrySet()) {
//            Produit produit = entry.getKey();  // Get the product
//            int quantite = entry.getValue();   // Get the quantity
//
//            // If the reference matches the one to remove, skip adding this product to the TableView
//            if (produit.getReference().equals(referenceProduitToRemove)) {
//                // Remove the product from the Map if it matches the reference
//                commandeActuelle.getProduits().remove(produit);
//                break; // Exit loop since we modified the map
//            }
//
//            // Prepare data for the TableView row (nom, prixUnitaire, quantite, prixTotal)
//            Map<String, Object> produitMap = new HashMap<>();
//            produitMap.put("nom", produit.getNom());
//            produitMap.put("prixUnitaire", produit.getPrix());
//            produitMap.put("quantite", quantite);
//            produitMap.put("prixTotal", produit.getPrix() * quantite);
//
//            // Add the product data to the list that backs the TableView
//            produitsList.add(produitMap);
//        }
//    }

    public boolean supprimerProduitDepuisCommande(String referenceProduit) {
        Produit produitToRemove = null; // Placeholder for the product to remove

        // Iterate over the entries in the Map (Produit, Quantity)
        for (Map.Entry<Produit, Integer> entry : commandeActuelle.getProduits().entrySet()) {
            Produit produit = entry.getKey(); // Get the product
            if (Objects.equals(produit.getReference(), referenceProduit)) {
                produitToRemove = produit; // Mark this product for removal
                break; // Exit the loop once the product is found
            }
        }
        // Remove the product if it was found
        if (produitToRemove != null) {
            commandeActuelle.getProduits().remove(produitToRemove); // Remove the product
            commandeActuelle.afficherCommande(); // Debug: Display the updated commande
            return true; // Indicate successful removal
        }

        // If no product matched the reference
        System.out.println("Produit non trouvé dans la commande");
        //commandeActuelle.afficherCommande(); // Debug: Display the unchanged commande
        return false; // Indicate failure to remove
    }


//    public boolean supprimerProduitDepuisCommande(String referenceProduit) {
//        // Iterate over the entries in the Map (Produit, Quantity)
//        for (Map.Entry<Produit, Integer> entry : commandeActuelle.getProduits().entrySet()) {
//            Produit produit = entry.getKey();  // Get the product
//            if (produit.getReference().equals(referenceProduit)) {
//                // Remove the product from the map
//                commandeActuelle.getProduits().remove(produit);
//                commandeActuelle.afficherCommande(); // Debug
//                return true;
//            }
//        }
//        commandeActuelle.afficherCommande(); // Debug
//        return false;  // Product not found
//    }


    @FXML
    public void supprimerProduitCommande() {
        String referenceProduit = referenceProduitField.getText();  // Get the reference from input

        if (referenceProduit.isEmpty()) {
            afficherAlerte("Erreur", "La référence du produit est vide.", Alert.AlertType.ERROR);
            return;
        }

        boolean removed = supprimerProduitDepuisCommande(referenceProduit);
        if (removed) {
            // Update the UI after the product is removed from the order
            //updateTableUI();
            afficherAlerte("INFORMATION", "Produit supprimer de commande", Alert.AlertType.INFORMATION);
            commandeActuelle.recalculerMontantTotal();
            miseAJourUI();
            //updateTableUI(referenceProduit);
        } else {
            afficherAlerte("Erreur", "Produit non trouvé dans la commande.", Alert.AlertType.ERROR);
        }
    }


    @FXML
    private TextField txtReferenceProduitUpdate;

    @FXML
    private TextField txtQuantiteUpdate;
    public void updateTableUI() {
        // Clear the existing data in produitsList
        produitsList.clear();

        // Iterate through the products in commandeActuelle
        commandeActuelle.getProduits().forEach((produit, quantite) -> {
            // Create a map for each product with all necessary details
            Map<String, Object> produitMap = new HashMap<>();
            //produitMap.put("reference", produit.getReference());
            produitMap.put("nom", produit.getNom()); // Ensure the name is correctly added
            produitMap.put("prix", produit.getPrix()); // Unit price
            produitMap.put("quantite", quantite); // Quantity in the current order
            produitMap.put("prixTotal", produit.getPrix() * quantite); // Total price for this product

            // Add the map to produitsList
            produitsList.add(produitMap);
            produitsList.forEach(row -> System.out.println("Row: " + row));

        });


    }


    @FXML
    public void modifierQuantiteCommande() {
        // Retrieve values from the TextFields
        String referenceProduit = txtReferenceProduitUpdate.getText();
        String quantiteText = txtQuantiteUpdate.getText();
        int nouvelleQuantite;

        // Validate quantity input
        try {
            nouvelleQuantite = Integer.parseInt(quantiteText);
        } catch (NumberFormatException e) {
            afficherAlerte("Erreur", "Quantité invalide. Veuillez entrer un nombre entier.", Alert.AlertType.ERROR);
            return;
        }

        // Call the function to update the quantity
        boolean succes = modifierQuantiteProduit(referenceProduit, nouvelleQuantite);

        // Provide feedback to the user
        if (succes) {
            afficherAlerte("Succès", "Quantité mise à jour avec succès.", Alert.AlertType.INFORMATION);
            updateTableUI(); // Refresh the table view
            commandeActuelle.recalculerMontantTotal();
            miseAJourUI();
        } else {
            afficherAlerte("Erreur", "Produit non trouvé ou erreur de mise à jour.", Alert.AlertType.ERROR);
        }
    }

    public boolean modifierQuantiteProduit(String referenceProduit, int nouvelleQuantite) {
        // Validate the new quantity
        if (nouvelleQuantite < 1) {
            System.out.println("Quantité invalide. Elle doit être supérieure à 0.");
            return false;
        }

        // Find the product in the map
        for (Map.Entry<Produit, Integer> entry : commandeActuelle.getProduits().entrySet()) {
            Produit produit = entry.getKey(); // Get the product
            if (produit.getReference().equals(referenceProduit)) {
                // Update the quantity
                commandeActuelle.getProduits().put(produit, nouvelleQuantite);

                // Debug: Display the updated commande
                commandeActuelle.afficherCommande();
                return true; // Indicate successful update
            }
        }

        // If the product is not found
        System.out.println("Produit non trouvé dans la commande");
        return false; // Indicate failure to update
    }




    public void validerCommande(ActionEvent actionEvent) throws IOException {

        if (this.commandeActuelle.getProduits().isEmpty()) {
            afficherAlerte("Erreur", "La commande est vide", Alert.AlertType.ERROR);
        } else {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/PaymentDialog.fxml"));
            Stage stage = new Stage();
            stage.setTitle("Choisir Mode de Paiement");
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene((Parent) loader.load()));
            stage.showAndWait();
        }
        serviceGestionCommande.sauvegarderCommandeDansBDD(commandeActuelle);
        // Afficher la commande
        commandeActuelle.afficherCommande();
        System.out.println("Montant total : " + commandeActuelle.getMontantTotal());
        afficherAlerte("CONFIRMATION", "Commande " + commandeActuelle.getIdCommande() +" ajouté avec montant totale : "+ commandeActuelle.getMontantTotal() , Alert.AlertType.INFORMATION);
        initialize();
        clearTable();
    }

    public void modifierEtatCommande(ActionEvent actionEvent) {
    }

    public void clearTable() {
        produitsList.clear();  // Clears the ObservableList, which automatically updates the TableView
    }





// Assume this is initialized elsewhere


    /**
     * Adds a product to the table based on the provided reference and quantity.
     *
     * @param reference the reference of the product
     * @param quantite  the quantity of the product
     */
    public void ajouterProduitDansTable(String reference, int quantite) {
        String query = "SELECT nom, prix FROM produit WHERE reference = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, reference);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    // Retrieve product data
                    String nom = resultSet.getString("nom");
                    double prixUnitaire = resultSet.getDouble("prix");
                    double prixTotal = prixUnitaire * quantite;

                    // Create a map to represent a single row of data
                    Map<String, Object> produitData = new HashMap<>();
                    produitData.put("nom", nom);
                    produitData.put("quantite", quantite);
                    produitData.put("prixUnitaire", prixUnitaire);
                    produitData.put("prixTotal", prixTotal);

                    // Add the row to the observable list
                    produitsList.add(produitData);
                } else {
                    System.err.println("Produit introuvable avec la référence : " + reference);
                }
            }
        } catch (SQLException e) {
            System.err.println("Erreur lors de la récupération du produit : " + e.getMessage());
        }
    }


    public void supprimerCommande(ActionEvent actionEvent) {
    }
}

