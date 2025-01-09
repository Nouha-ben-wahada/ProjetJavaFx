package Controllers;

import Service.ReclamationService;
import entités.Reclamation;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;

import java.sql.SQLException;
import java.util.Properties;

public class AdminReclamationController {

    @FXML
    private TableView<Reclamation> reclamationTable;
    @FXML
    private TableColumn<Reclamation, Integer> idColumn;
    @FXML
    private TableColumn<Reclamation, String> emailColumn;
    @FXML
    private TableColumn<Reclamation, Integer> commandeIdColumn;
    @FXML
    private TableColumn<Reclamation, String> produitColumn;
    @FXML
    private TableColumn<Reclamation, String> descriptionColumn;
    @FXML
    private TableColumn<Reclamation, String> dateColumn;
    @FXML
    private TextArea responseField;

    private final ObservableList<Reclamation> reclamations = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // Initialize table columns
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idReclamationProperty().asObject());
        emailColumn.setCellValueFactory(cellData -> cellData.getValue().emailProperty());
        commandeIdColumn.setCellValueFactory(cellData -> cellData.getValue().idCommandeProperty().asObject());
        produitColumn.setCellValueFactory(cellData -> cellData.getValue().idProduitProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());
        dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateReclamationProperty().asString());

        // Load reclamations into table
        loadReclamations();
    }

    private void loadReclamations() {
        try {
            reclamations.addAll(new ReclamationService().getAllReclamations());
            reclamationTable.setItems(reclamations);
        } catch (SQLException e) {
            showAlert("Database Error", "Unable to load reclamations: " + e.getMessage(), AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSendResponse() {
        // Get the selected reclamation
        Reclamation selectedReclamation = reclamationTable.getSelectionModel().getSelectedItem();

        if (selectedReclamation == null) {
            showAlert("No Selection", "Please select a reclamation to respond to.", AlertType.WARNING);
            return;
        }

        String responseText = responseField.getText().trim();

        if (responseText.isEmpty()) {
            showAlert("No Response", "Please enter a response before sending.", AlertType.WARNING);
            return;
        }

        try {
            sendResponseEmail(selectedReclamation.getEmail(), responseText);
            showAlert("Success", "Response sent successfully to " + selectedReclamation.getEmail(), AlertType.INFORMATION);
            responseField.clear(); // Clear the response field after sending
        } catch (RuntimeException e) {
            showAlert("Error", "Failed to send response: " + e.getMessage(), AlertType.ERROR);
        }
    }

    private void sendResponseEmail(String to, String adminResponse) {
        final String fromEmail = "boukamchamoatez@gmail.com"; // Your email
        final String password = "tajr tphv bqay epwv";       // App password

        // Email subject and body
        String subject = "Réponse à votre réclamation";
        String body = String.format("""
            Bonjour,

            Merci pour votre réclamation. Voici notre réponse :

            %s

            Si vous avez d'autres questions ou préoccupations, n'hésitez pas à nous contacter.

            Cordialement,
            L'équipe de support.
            """, adminResponse);

        // SMTP properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

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

            Transport.send(message);
            System.out.println("Response email sent successfully to " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending response email: " + e.getMessage());
        }
    }

    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
