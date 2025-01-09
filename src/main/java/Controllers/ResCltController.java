package Controllers;

import ConnectionDB.DataBaseConnection;
import Service.ServiceDeService;
import Service.ServiceReservationList;
import Service.ServiceUser;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import entités.Reservation22;
import entités.Service;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.mail.*;
import javax.mail.internet.*;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.regex.Pattern;

public class ResCltController {

    private ServiceReservationList serviceReservation = new ServiceReservationList();

    @FXML
    private VBox servicesVBox;

    @FXML
    private VBox ReservationVBox;

    @FXML
    private TextField clientIdField, clientNameField, clientPrenameField, clientEmailField, clientPhoneField;

    @FXML
    private DatePicker reservationDatePicker;

    @FXML
    private ComboBox<String> reservationTimeComboBox;

    @FXML
    private Button addReservationButton;

    @FXML
    private Label feedbackLabel;

    private ServiceDeService service;

    @FXML
    private ImageView imageview;
    // Initialiser les heures disponibles pour la réservation
    /*public void initialize() {
        reservationTimeComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00");
    }*/


    // Initialiser le contrôleur avec la connexion à la base de données
    public void initialize() throws SQLException {
        Connection connection = DataBaseConnection.getInstance().getConnection();
        service = new ServiceDeService(connection);
        loadServices();
        reservationTimeComboBox.getItems().addAll("08:00", "09:00", "10:00", "11:00", "14:00", "15:00", "16:00", "17:00");
    }

    // Charger toutes les réservations et les afficher
    public void loadServices() throws SQLException {
        List<Service> services = service.getAll();
        for (Service service : services) {
            Button serviceButton = new Button(service.getCategorie() + "\n");
            serviceButton.setOnMouseClicked((MouseEvent event) -> onServiceSelected(service));
            servicesVBox.getChildren().add(serviceButton);
        }
    }

    private Service selectedService;

    // Afficher les détails de la réservation choisie et permettre au client de confirmer
    private void onServiceSelected(Service service) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Réservation sélectionnée");
        alert.setHeaderText("Service n° "+ service.getIdService() + " : \n" + service.getCategorie() + " \n Montant Total : " + service.getPrix() + "D");
        alert.setContentText("Durée: " + service.getDuration() + " heures\nDéscription: " + service.getDescription());
        alert.showAndWait();

        // Affiche l'ID du service (facultatif, pour vérification)
        System.out.println("ID du service sélectionné : " + service.getIdService());
        this.selectedService = service;
        //return service.getIdService();
        // Permet au client d'entrer ses informations personnelles
        //confirmButton.setDisable(false); // Le bouton Confirmer devient actif
    }

    // Obtenir la réservation sélectionnée par l'utilisateur (dans un cas réel, vous auriez une variable pour garder la réservation actuelle)
    private Service getSelectedService() {
        // Retournez la réservation sélectionnée
        return selectedService;
    }

    // Ajouter une réservation
    @FXML
    private void onAddReservation(ActionEvent event) {
        try {
            // Récupérer les données saisies
            Service selectedService = getSelectedService();
            ServiceUser su = new ServiceUser();
            //int clientId = Integer.parseInt(clientIdField.getText());
            String clientUserName = clientNameField.getText();
            int clientId = su.getIdByUserName(clientUserName);
            int idService = selectedService.getIdService();
            //String clientName = clientNameField.getText();
            //String clientPrename = clientPrenameField.getText();
            String clientEmail = clientEmailField.getText();
            String clientPhone = clientPhoneField.getText();
            LocalDate reservationDate = reservationDatePicker.getValue();
            LocalTime reservationTime = LocalTime.parse(reservationTimeComboBox.getValue());
            String categorie = selectedService.getCategorie().toString().toUpperCase();
            int montantTotal = selectedService.getPrix();
            // Valider les données
            if (!validateEmail(clientEmail)) {
                showFeedback("Veuillez entrer un e-mail valide.", true);
                return;
            }

            if (!validatePhone(clientPhone)) {
                showFeedback("Veuillez entrer un numéro de téléphone valide (8 chiffres).", true);
                return;
            }

            if (reservationDate == null || reservationTime == null) {
                showFeedback("Veuillez sélectionner une date et une heure.", true);
                return;
            }

            if (selectedService == null) {
                showFeedback("Veuillez sélectionner une service.", true);
            }

            // Créer une nouvelle réservation
            Reservation22 reservation = new Reservation22(
                    0, // L'ID sera généré automatiquement
                    clientId,
                    clientUserName,
                    //clientPrename,
                    idService,
                    //getSelectedService().getIdService(),
                    //getSelectedService().getCategorie(),
                    categorie,
                    reservationDate,
                    reservationTime,
                    montantTotal,
                    //getSelectedService().getPrix(),
                    //selectedService.getPrix(),
                    "CONFIRMEE"

            );
            System.out.println(clientId);
            // Ajouter la réservation via le service
            serviceReservation.ajouter(reservation);

            // Générer un PDF de confirmation
            //generatePdfConfirmation(reservation);

            // Envoyer un email de confirmation
            sendEmailConfirmation(clientEmail, reservation);

            showFeedback("Réservation effectuée avec succès.", false);
            //loadReservations(); // Recharger la liste des réservations
            // Reservation successful, ask user to download PDF
            if (askUserToDownloadPDF()) {
                generatePdfConfirmation(reservation);
            }

        } catch (NumberFormatException e) {
            showFeedback("Veuillez entrer un ID client valide.", true);
        } catch (Exception e) {
            showFeedback("Une erreur est survenue : Veuillez remplir tous les champs" + e.getMessage(), true);
        }
    }

    // Charger et afficher les réservations
    public void loadReservations() {
        ReservationVBox.getChildren().clear();
        List<Reservation22> reservations = serviceReservation.getAll();

        if (reservations.isEmpty()) {
            ReservationVBox.getChildren().add(new Label("Aucune réservation disponible."));
        } else {
            for (Reservation22 reservation : reservations) {
                Label reservationLabel = new Label(reservation.toString());
                ReservationVBox.getChildren().add(reservationLabel);
            }
        }
    }

    /*private void loadReservations() {
        ReservationVBox.getChildren().clear();
        List<Reservation22> reservations = serviceReservation.getAll();

        if (reservations.isEmpty()) {
            ReservationVBox.getChildren().add(new Label("Aucune réservation disponible."));
        } else {
            for (Reservation22 reservation : reservations) {
                HBox reservationBox = new HBox(10);
                reservationBox.setPadding(new Insets(10));
                Label reservationLabel = new Label(reservation.toString());
                Button pdfButton = new Button("Télécharger PDF");
                pdfButton.setOnAction(event -> generatePdfConfirmation(reservation));
                reservationBox.getChildren().addAll(reservationLabel, pdfButton);
                ReservationVBox.getChildren().add(reservationBox);
            }
        }
    }*/

    private boolean askUserToDownloadPDF() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation de réservation");
        alert.setHeaderText("Votre réservation a été effectuée avec succès.");
        alert.setContentText("Souhaitez-vous télécharger un PDF de confirmation ?");
        ButtonType downloadButton = new ButtonType("Télécharger PDF");
        ButtonType noButton = new ButtonType("Non");
        alert.getButtonTypes().setAll(downloadButton, noButton);

        Optional<ButtonType> result = alert.showAndWait();
        return result.get() == downloadButton;
    }

    // Afficher un message de retour (succès ou erreur)
    private void showFeedback(String message, boolean isError) {
        feedbackLabel.setText(message);
        feedbackLabel.setStyle(isError ? "-fx-text-fill: red;" : "-fx-text-fill: green;");
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Information");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    // Valider l'e-mail
    private boolean validateEmail(String email) {
        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.matches(emailRegex, email);
    }

    // Valider le numéro de téléphone
    private boolean validatePhone(String phone) {
        String phoneRegex = "^[0-9]{8}$";
        return Pattern.matches(phoneRegex, phone);
    }

    /*private void generatePdfConfirmation(Reservation22 reservation) {
        Document document = new Document();
        try {
            PdfWriter.getInstance(document, new FileOutputStream("Confirmation_Reservation.pdf"));
            document.open();
            document.add(new Paragraph("Confirmation de Réservation"));
            document.add(new Paragraph("Reservation ID : " + reservation.getIdReservation()));
            document.add(new Paragraph("Client ID: " + reservation.getId()));
            document.add(new Paragraph("Nom: " + reservation.getnomclient()));
            document.add(new Paragraph("Prénom: " + reservation.getPrenomclient()));
            document.add(new Paragraph("Date: " + reservation.getReservationDate()));
            document.add(new Paragraph("Heure: " + reservation.getReservationTime()));
            document.add(new Paragraph("Statut: " + reservation.getStatus()));
        } catch (DocumentException | java.io.IOException e) {
            e.printStackTrace();
        } finally {
            document.close();
        }
    }*/

    private void generatePdfConfirmation(Reservation22 reservation) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Enregistrer le PDF de confirmation");
        fileChooser.setInitialFileName("Confirmation_Reservation.pdf");
        File selectedFile = fileChooser.showSaveDialog(null);

        if (selectedFile != null) {
            try {
                Document document = new Document();
                PdfWriter.getInstance(document, new FileOutputStream(selectedFile));
                document.open();

                document.add(new Paragraph("*********************Confirmation de Réservation******************"));
                document.add(new Paragraph("Reservation ID : " + reservation.getIdReservation()));
                document.add(new Paragraph("Client ID: " + reservation.getId()));
                document.add(new Paragraph("UserName: " + reservation.getUsernameclient()));
                document.add(new Paragraph("Date: " + reservation.getReservationDate()));
                document.add(new Paragraph("Heure: " + reservation.getReservationTime()));
                document.add(new Paragraph("Statut: " + reservation.getStatus()));
                document.close();
                System.out.println("PDF de confirmation enregistré avec succès.");
            } catch (DocumentException | IOException e) {
                e.printStackTrace();
                showError("Une erreur est survenue lors de la génération du PDF.");
            }
        }
    }


    private void sendEmailConfirmation(String clientEmail, Reservation22 reservation) {
        String host = "smtp.gmail.com"; // Remplacez par votre serveur SMTP
        String from = "nouhawahada@gmail.com";
        String password = "kofe obab czxq jmkz";

        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", "587");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(clientEmail));
            message.setSubject("Détails de votre Réservation");
            message.setText("Cher(e) Client(e) : " + reservation.getUsernameclient() + "\n\n Nous vous informons que votre réservation avec l'ID " + reservation.getIdReservation() + " a été confirmée.\n\nVoici les détails de votre réservation  :\n\n Votre reservation sere :  le " + reservation.getReservationDate() + " à " + reservation.getReservationTime() + "\n\n Service réservé : " + reservation.getCategorie() + " avec le prix " + reservation.getMontantTotal() + ".\n\n Merci pour votre confiance. \n\n Cordialement");

            Transport.send(message);
            System.out.println("Email envoyé avec succès.");
            // Popup d'information
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Confirmation Email");
            alert.setHeaderText(null);
            alert.setContentText("Un email de confirmation a été envoyé à " + clientEmail + ".");
            alert.showAndWait();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    /*@FXML
    private void onActionConfirmerReservation(ActionEvent event) {
        try {
            // Récupérer les informations nécessaires
            Service selectedService = getSelectedService(); // Cette méthode récupère le service sélectionné
            if (selectedService == null) {
                showError("Veuillez sélectionner un service.");
                return;
            }
            // Récupérer les informations nécessaires
            //Service selectedService = getSelectedService();  // Cette méthode récupère le service sélectionné
            Integer idReservation = getNextIdReservation();
            //Integer id = 11;
            //Integer id = Integer.parseInt(clientIdField.getText());
            //System.out.println("ID Client : " + id);
            String nomClient = clientNameField.getText();
            String prenomClient = clientPrenameField.getText();
            //Integer idService = selectedService.getIdService();
            //String categorie = String.valueOf(selectedService.getCategorie());
            LocalDate reservationDate = getSelectedDate(); // La méthode pour obtenir la date sélectionnée
            System.out.println("Date de Réservation : " + reservationDate);
            LocalTime reservationTime = getSelectedTime();  // La méthode pour obtenir l'heure sélectionnée
            System.out.println("Time de Réservation : " + reservationTime);
            //Integer Prix = selectedService.getPrix();
            String status = Reservation22.Status.CONFIRMEE.name();
            // Assurez-vous que le service et l'utilisateur sont bien sélectionnés
            //if (selectedService != null) {
            //int id = currentUser.getId();  // L'ID du client connecté

            // Appeler la méthode pour confirmer la réservation
            //confirmerReservation( idReservation, 11, nomClient, prenomClient, idService, categorie, reservationDate, reservationTime ,Prix, status);
            //} else {
            // Afficher une erreur si le service ou l'utilisateur n'est pas correctement sélectionné
            //   showError("Veuillez sélectionner un service et vous assurer que vous êtes connecté.");
            //}
            // Créer une nouvelle réservation
            Reservation22 reservation = new Reservation22(
                    idReservation,
                    11,
                    nomClient,
                    prenomClient,
                    1,
                    "MAQUILLAGE",
                    reservationDate,
                    reservationTime,
                    100,
                    "CONFIRMEE"
            );
            System.out.println("ID Client : " + reservation.getId());
            // Appeler le service pour ajouter la réservation
            ServiceReservation22 serviceReservation = new ServiceReservation22();
            serviceReservation.ajouter(reservation);

            // Afficher un message de succès
            showSuccessMessage();

        } catch (NumberFormatException e) {
            showError("ID client ou autre valeur numérique invalide.");
        } catch (SQLException e) {
            showError("Une erreur s'est produite lors de l'ajout de la réservation : " + e.getMessage());
        }
    }*/

    /*private LocalDate getSelectedDate() {
        // Ensure the date is not null (i.e., user has selected a date)
        if (reservationDatePicker.getValue() != null) {
            return reservationDatePicker.getValue();
        } else {
            // Handle the case where no date is selected
            showError("Veuillez sélectionner une date.");
            return null; // Return null or handle appropriately
        }

    }

    // Cette méthode récupère l'heure sélectionnée dans le ComboBox
    public LocalTime getSelectedTime() {
        String selectedTime = reservationTimeComboBox.getValue(); // Récupère la valeur sélectionnée dans le ComboBox
        if (selectedTime != null) {
            return LocalTime.parse(selectedTime);  // Convertit la chaîne en LocalTime
        } else {
            showAlert("Erreur", "Aucun Time sélectionné.");
        }
        return null;
    }*/

    /*private void showAlert(String dateSélectionnée, String s) {
    }*/

    // Afficher un message de succès
    private void showSuccessMessage() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Succès");
        alert.setHeaderText("Réservation confirmée");
        alert.setContentText("Un email de confirmation a été envoyé.");
        alert.showAndWait();
    }
}
