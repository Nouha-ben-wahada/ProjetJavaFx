package Controllers;

import Service.ReclamationService;
import entités.Reclamation;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Properties;

public class ReclamationController {

    private final ReclamationService reclamationService = new ReclamationService();

    // FXML References
    @FXML private TextField emailField; // Email field
    @FXML private TextField idCommandeField;
    @FXML private TextField idProduitField;
    @FXML private TextArea descriptionField;
    @FXML private TextField photoPathField;
    @FXML private DatePicker dateReclamationField;

    @FXML
    public void addReclamation(ActionEvent event) {
        try {
            // Retrieve user inputs
            String email = emailField.getText();
            int idCommande = Integer.parseInt(idCommandeField.getText());
            String idProduit = idProduitField.getText();
            String description = descriptionField.getText();
            String photoPath = photoPathField.getText();

            // Get the selected date and validate
            LocalDate selectedDate = dateReclamationField.getValue();
            LocalDate currentDate = LocalDate.now();

// Replace future date with the current date
            if (selectedDate == null || selectedDate.isAfter(currentDate)) {
                selectedDate = currentDate;
            }

// Convert LocalDate to LocalDateTime
            LocalDateTime selectedDateTime = selectedDate.atStartOfDay();

// Create a new reclamation object
            Reclamation reclamation = new Reclamation(0, email, idCommande, idProduit, description, photoPath, selectedDateTime);

            // Save reclamation
            reclamationService.addReclamation(reclamation);

            // Send email notification
            sendEmail(email, idCommande, description);

            // Notify user
            showAlert("Success", "Reclamation added and email sent successfully!", AlertType.INFORMATION);

            // Reset input fields
            resetFields();
        } catch (NumberFormatException e) {
            // Handle invalid number format for idCommande
            showAlert("Error", "Invalid command ID. Please enter a valid number.", AlertType.ERROR);
        } catch (Exception e) {
            // Handle other exceptions
            showAlert("Error", "Failed to add reclamation or send email: " + e.getMessage(), AlertType.ERROR);
        }
    }

    // Helper method to show alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Helper method to reset fields after actions
    private void resetFields() {
        emailField.clear();
        idCommandeField.clear();
        idProduitField.clear();
        descriptionField.clear();
        photoPathField.clear();
        dateReclamationField.setValue(null);
    }

    // Send email to user
    private void sendEmail(String to, int commandeId, String description) {
        final String fromEmail = "boukamchamoatez@gmail.com"; // Your email
        final String password = "tajr tphv bqay epwv";   // App password

        // Email subject and body
        String subject = "Confirmation de votre réclamation";
        String body = String.format("""
                Bonjour,

                Nous avons bien reçu votre réclamation concernant la commande #%d. Voici les détails :
                Description : %s

                Un membre de notre équipe vous contactera sous peu pour résoudre ce problème.

                Merci,
                L'équipe de support.
                """, commandeId, description);

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
            System.out.println("Email sent successfully to " + to);
        } catch (MessagingException e) {
            throw new RuntimeException("Error while sending email: " + e.getMessage());
        }
    }
}
