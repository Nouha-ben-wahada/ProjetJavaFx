package Controllers;

import ConnectionDB.DataBaseConnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.Properties;

public class GestionlivraisonAdminController {

    @FXML
    private TableView<Map<String, Object>> tableCommandes;

    @FXML
    private final ObservableList<Map<String, Object>> commandesList = FXCollections.observableArrayList();
    @FXML
    private TableColumn<Map<String, Object>, String> colIdCommande;
    @FXML
    private TableColumn<Map<String, Object>, String> colIdUser;
    @FXML
    private TableColumn<Map<String, Object>, String> colMontantTotal;
    @FXML
    private TableColumn<Map<String, Object>, String> colEtatCommande;
    @FXML
    private ComboBox<String> comboLivreur; // Or the appropriate type
    @FXML
    public void initialize(){
        // Bind TableColumn to map keys
        colIdCommande.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                (String) data.getValue().get("idCommande")));
        colIdUser.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.valueOf(data.getValue().get("userId"))));
        colMontantTotal.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                String.valueOf(data.getValue().get("montantTotal"))));
        colEtatCommande.setCellValueFactory(data -> javafx.beans.binding.Bindings.createStringBinding(() ->
                (String) data.getValue().get("etatCommande")));

        // Set the data model for the TableView
        tableCommandes.setItems(commandesList);

        loadLivreurs();
        loadCommandesNeedingDelivery();

    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }




    private void loadCommandesNeedingDelivery() {
        commandesList.clear(); // Assuming commandesList is an ObservableList<Map<String, Object>>
        try {
            Connection connection = DataBaseConnection.getInstance().getConnection();
            String query = "SELECT id_commande, id_user, montant_total, etat_commande FROM commande " +
                    "WHERE commande.etat_commande = 'Validée' AND commande.mode_paiement = 'Livraison'";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Map<String, Object> commande = new HashMap<>();
                commande.put("idCommande", resultSet.getString("id_commande"));
                commande.put("userId", resultSet.getString("id_user"));
                commande.put("montantTotal", resultSet.getDouble("montant_total"));
                commande.put("etatCommande", resultSet.getString("etat_commande"));
                commandesList.add(commande);
            }

            tableCommandes.setItems(commandesList);
            //debug
            //commandesList.forEach(row -> System.out.println("Row: " + row));

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des commandes.", Alert.AlertType.ERROR);
        }
    }

    private void loadLivreurs() {
        try {
            Connection connection = DataBaseConnection.getInstance().getConnection();
            String query = "SELECT nom, numero FROM livreur WHERE disponible = 1"; // Only available delivery persons
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            ObservableList<String> livreurList = FXCollections.observableArrayList();
            while (resultSet.next()) {
                String livreurInfo = "Nom: " + resultSet.getString("nom") + " | Numéro: " + resultSet.getInt("numero");
                livreurList.add(livreurInfo);
            }

            if (livreurList.isEmpty()) {
                // No available livreurs
                comboLivreur.setItems(FXCollections.observableArrayList(new String[]{"Aucun livreur disponible"}));
                comboLivreur.setDisable(true); // Disable the ComboBox
            } else {
                // Populate ComboBox with available livreurs
                comboLivreur.setItems(livreurList);
                comboLivreur.setDisable(false); // Enable the ComboBox
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors du chargement des livreurs.", Alert.AlertType.ERROR);
        }
    }


//    @FXML
//    private void assignLivreur() {
//        Map<String, Object> selectedCommande = tableCommandes.getSelectionModel().getSelectedItem();
//        String selectedLivreur = comboLivreur.getSelectionModel().getSelectedItem().toString();
//
//        if (selectedCommande == null || selectedLivreur == null) {
//            showAlert("Erreur", "Veuillez sélectionner une commande et un livreur.", Alert.AlertType.ERROR);
//            return;
//        }
//        System.out.println("Selected Livreur: " + selectedLivreur);
//        System.out.println("Selected Commande ID: " + selectedCommande.get("idCommande"));
//
//        try {
//            Connection connection = DataBaseConnection.getInstance().getConnection();
//
//            // Begin transaction
//            connection.setAutoCommit(false);
//
//            // Update the commande table
//            String updateCommandeQuery = "UPDATE commande SET id_livreur = (SELECT id FROM livreur WHERE nom = ?), etat_commande = 'En livraison' WHERE id_commande = ?";
//            PreparedStatement statementCommande = connection.prepareStatement(updateCommandeQuery);
//            statementCommande.setString(1, selectedLivreur);
//            statementCommande.setInt(2, Integer.parseInt(selectedCommande.get("idCommande").toString()));
//            int rowsAffectedCommande = statementCommande.executeUpdate();
//
//            // Update the livreur table
//            String updateLivreurQuery = "UPDATE livreur SET disponible = 0 WHERE nom = ?";
//            PreparedStatement statementLivreur = connection.prepareStatement(updateLivreurQuery);
//            statementLivreur.setString(1, selectedLivreur);
//            int rowsAffectedLivreur = statementLivreur.executeUpdate();
//
//            System.out.println("Rows affected in commande: " + rowsAffectedCommande);
//            System.out.println("Rows affected in livreur: " + rowsAffectedLivreur);
//
//            // Commit transaction if both updates succeed
//            if (rowsAffectedCommande > 0 && rowsAffectedLivreur > 0) {
//                connection.commit();
//                showAlert("Succès", "Livreur assigné à la commande avec succès.", Alert.AlertType.INFORMATION);
//            } else {
//                connection.rollback();
//                showAlert("Erreur", "L'attribution du livreur a échoué.", Alert.AlertType.ERROR);
//            }
//
//            // Close statements
//            statementCommande.close();
//            statementLivreur.close();
//
//            // Optional: Refresh the table data
//            loadCommandesNeedingDelivery();
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showAlert("Erreur", "Une erreur s'est produite lors de l'attribution du livreur.", Alert.AlertType.ERROR);
//        }
//    }
    private void assignLivreur() {
        Map<String, Object> selectedCommande = tableCommandes.getSelectionModel().getSelectedItem();
        String selectedLivreur = comboLivreur.getSelectionModel().getSelectedItem().toString();

        if (selectedCommande == null || selectedLivreur == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande et un livreur.", Alert.AlertType.ERROR);
            return;
        }

        // Parse the "nom" from the selectedLivreur string
        String livreurName = parseLivreurName(selectedLivreur);

        try {
            Connection connection = DataBaseConnection.getInstance().getConnection();

            // Begin transaction
            connection.setAutoCommit(false);

            // Update the commande table
            String updateCommandeQuery = "UPDATE commande SET id_livreur = (SELECT id FROM livreur WHERE nom = ?), etat_commande = 'En livraison' WHERE id_commande = ?";
            PreparedStatement statementCommande = connection.prepareStatement(updateCommandeQuery);
            statementCommande.setString(1, livreurName);
            statementCommande.setInt(2, Integer.parseInt(selectedCommande.get("idCommande").toString()));
            int rowsAffectedCommande = statementCommande.executeUpdate();

            // Update the livreur table
            String updateLivreurQuery = "UPDATE livreur SET disponible = 0 WHERE nom = ?";
            PreparedStatement statementLivreur = connection.prepareStatement(updateLivreurQuery);
            statementLivreur.setString(1, livreurName);
            int rowsAffectedLivreur = statementLivreur.executeUpdate();

            // Commit transaction if both updates succeed
            if (rowsAffectedCommande > 0 && rowsAffectedLivreur > 0) {
                connection.commit();
                showAlert("Succès", "Livreur assigné à la commande avec succès.", Alert.AlertType.INFORMATION);
            } else {
                connection.rollback();
                showAlert("Erreur", "L'attribution du livreur a échoué.", Alert.AlertType.ERROR);
            }

            // Close statements
            statementCommande.close();
            statementLivreur.close();

            // Optional: Refresh the table data
            loadCommandesNeedingDelivery();
            loadLivreurs();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de l'attribution du livreur.", Alert.AlertType.ERROR);
        }
    }

    // Utility function to parse livreur name
    private String parseLivreurName(String selectedLivreur) {
        if (selectedLivreur == null || !selectedLivreur.contains("Nom: ") || !selectedLivreur.contains("| Numéro: ")) {
            return null; // Return null if the format is invalid
        }

        // Extract the livreur's name from the formatted string
        String[] parts = selectedLivreur.split("\\|");
        if (parts.length > 0) {
            return parts[0].replace("Nom: ", "").trim(); // Remove the "Nom: " prefix and trim any extra spaces
        }

        return null; // Return null if parsing fails
    }




    private void notifyClient(int userId, String message) {
        try {
            Connection connection = DataBaseConnection.getInstance().getConnection();
            String query = "SELECT emailClient FROM client WHERE idClient = ?";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                String email = resultSet.getString("emailClient");
                sendEmail(email, "Mise à jour de votre commande", message);
            } else {
                showAlert("Erreur", "Client introuvable.", Alert.AlertType.ERROR);
            }

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert("Erreur", "Une erreur s'est produite lors de la notification du client.", Alert.AlertType.ERROR);
        }
    }



    private void sendEmail(String to, String subject, String body) {
        // Email configuration
        final String fromEmail = "boukamchamoatez@gmail.com"; // Replace with your email
        final String password = "tajr tphv bqay epwv";    // Replace with your email password

        // SMTP server configuration
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com"); // Trust Gmail SMTP server
        //properties.put("mail.debug", "true");


        Session session = Session.getInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(fromEmail, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(fromEmail));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject);
            message.setText(body);

            // Send the email
            Transport.send(message);

            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("Failed to send email to " + to);
        }
    }


    @FXML
    private void assignLivreurAndNotify() {
        Map<String, Object> selectedCommande = tableCommandes.getSelectionModel().getSelectedItem();
        String selectedLivreur = comboLivreur.getSelectionModel().getSelectedItem().toString();

        if (selectedCommande == null || selectedLivreur == null) {
            showAlert("Erreur", "Veuillez sélectionner une commande et un livreur.", Alert.AlertType.ERROR);
            return;
        }

        int userId = Integer.parseInt(selectedCommande.get("userId").toString());
        assignLivreur(); // Assign the livreur

        notifyClient(userId, "Votre commande a été assignée à un livreur.");
    }

}
