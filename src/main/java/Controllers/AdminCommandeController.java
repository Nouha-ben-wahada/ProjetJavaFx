package Controllers;

import ConnectionDB.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.*;
import java.util.*;

import javafx.beans.property.SimpleStringProperty;

public class AdminCommandeController {

    @FXML
    private TableView<Map<String, Object>> commandesTable;
    @FXML
    private TableColumn<Map<String, Object>, String> colIdCommande;
    @FXML
    private TableColumn<Map<String, Object>, String> colMontantTotal;
    @FXML
    private TableColumn<Map<String, Object>, String> colEtatCommande;
    @FXML
    private Button btnAjouterCommande;
    @FXML
    private Button btnModifierEtat;
    @FXML
    private Button btnSupprimerCommande;

    private final ObservableList<Map<String, Object>> commandesList = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Set custom cell value factories for each column
        colIdCommande.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                (String) data.getValue().get("idCommande")));
        colMontantTotal.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.valueOf(data.getValue().get("montantTotal"))));
        colEtatCommande.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                (String) data.getValue().get("etatCommande")));

        // Bind the ObservableList to the TableView
        commandesTable.setItems(commandesList);

        // Add dummy data for testing
        loadCommandesFromDatabase();

    }


    @FXML
    private void validerCommande(ActionEvent event) {
        // Get the selected commande from the table
        Map<String, Object> selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Aucune commande sélectionnée.", Alert.AlertType.ERROR);
            return;
        }

        // Get the ID of the selected commande
        String commandeId = (String) selectedCommande.get("idCommande");

        // Update the state in the database
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get database connection
            conn = DataBaseConnection.getInstance().getConnection();

            // Prepare SQL update statement
            String sql = "UPDATE commande SET etat_commande = ? WHERE id_Commande = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, "Validée");
            stmt.setString(2, commandeId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Update the local data model
                selectedCommande.put("etatCommande", "Validée");
                commandesTable.refresh();

                // Show success message
                showAlert("Succès", "Commande validée avec succès.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la validation de la commande.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la validation de la commande.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void modifierEtatCommande(ActionEvent event) {
        // Get the selected commande from the table
        Map<String, Object> selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Aucune commande sélectionnée.", Alert.AlertType.ERROR);
            return;
        }

        // Get the current ID of the commande
        String commandeId = (String) selectedCommande.get("idCommande");

        // Show a choice dialog for the new state
        List<String> etats = Arrays.asList("En attente", "En cours", "Livré", "Validé", "En livraison");
        ChoiceDialog<String> dialog = new ChoiceDialog<>("En attente", etats);
        dialog.setTitle("Modifier État de Commande");
        dialog.setHeaderText("Changer l'état de la commande");
        dialog.setContentText("Veuillez sélectionner un nouvel état:");

        Optional<String> result = dialog.showAndWait();
        if (result.isEmpty()) {
            return; // If no selection, exit
        }

        // Get the chosen state
        String newEtat = result.get();

        // Update the commande in the database
        Connection conn = null;
        PreparedStatement stmt = null;

        try {
            // Get database connection
            conn = DataBaseConnection.getInstance().getConnection();

            // Prepare SQL update statement
            String sql = "UPDATE commande SET etat_commande = ? WHERE id_Commande = ?";
            stmt = conn.prepareStatement(sql);
            stmt.setString(1, newEtat);
            stmt.setString(2, commandeId);

            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                // Update the local data model
                selectedCommande.put("etatCommande", newEtat);
                commandesTable.refresh();

                // Show success message
                showAlert("Succès", "État de la commande modifié avec succès.", Alert.AlertType.INFORMATION);
            } else {
                showAlert("Erreur", "Échec de la modification de l'état de la commande.", Alert.AlertType.ERROR);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la mise à jour de l'état de la commande.", Alert.AlertType.ERROR);
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    @FXML
    private void supprimerCommande(ActionEvent event) {
        // Get the selected commande from the table
        Map<String, Object> selectedCommande = commandesTable.getSelectionModel().getSelectedItem();
        if (selectedCommande == null) {
            showAlert("Erreur", "Aucune commande sélectionnée.", Alert.AlertType.ERROR);
            return;
        }

        // Get the ID of the selected commande and ensure it is an integer
        String idCommandeString = (String) selectedCommande.get("idCommande");
        if (idCommandeString == null) {
            showAlert("Erreur", "ID de commande non valide.", Alert.AlertType.ERROR);
            return;
        }

        int idCommande;
        try {
            idCommande = Integer.parseInt(idCommandeString);
        } catch (NumberFormatException e) {
            showAlert("Erreur", "L'ID de commande n'est pas un nombre valide.", Alert.AlertType.ERROR);
            return;
        }

        // Confirm deletion
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Confirmation");
        confirmationAlert.setHeaderText("Supprimer la commande ?");
        confirmationAlert.setContentText("Voulez-vous vraiment supprimer cette commande ?");
        Optional<ButtonType> result = confirmationAlert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Remove the command from the database
            try {
                Connection connection = DataBaseConnection.getInstance().getConnection();
                String query = "DELETE FROM commande WHERE id_commande = ?";
                PreparedStatement statement = connection.prepareStatement(query);
                statement.setInt(1, idCommande);
                int rowsAffected = statement.executeUpdate();

                if (rowsAffected > 0) {
                    // If successfully deleted from the database, remove from the UI list
                    commandesList.remove(selectedCommande);
                    showAlert("Succès", "Commande supprimée avec succès.", Alert.AlertType.INFORMATION);
                } else {
                    showAlert("Erreur", "Échec de la suppression de la commande dans la base de données.", Alert.AlertType.ERROR);
                }

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
                showAlert("Erreur", "Une erreur s'est produite lors de la suppression de la commande.", Alert.AlertType.ERROR);
            }
        }
    }



    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }


    private void loadCommandesFromDatabase() {
        Connection conn = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Get database connection from the singleton class
            conn = DataBaseConnection.getInstance().getConnection();

            // SQL query to fetch commandes
            String query = "SELECT id_commande, montant_total, etat_commande FROM commande";
            stmt = conn.createStatement();
            rs = stmt.executeQuery(query);

            // Clear the existing list
            commandesList.clear();

            // Process each row in the result set
            while (rs.next()) {
                Map<String, Object> commande = new HashMap<>();
                commande.put("idCommande", rs.getString("id_commande"));
                commande.put("montantTotal", rs.getString("montant_total"));
                commande.put("etatCommande", rs.getString("etat_commande"));

                commandesList.add(commande);
            }

            // Bind the updated list to the TableView
            commandesTable.setItems(commandesList);
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Erreur lors de la récupération des commandes.", Alert.AlertType.ERROR);
        } finally {
            // Clean up resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                // Connection is managed by the singleton; do not close it here
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
