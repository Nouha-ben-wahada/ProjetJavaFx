package Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

public class PaymentDialogController {

    @FXML
    private ComboBox<String> paymentMethodComboBox;

    // Handle payment confirmation
    public void confirmerPaiement() {
        String selectedPaymentMethod = paymentMethodComboBox.getSelectionModel().getSelectedItem();

        if (selectedPaymentMethod != null) {
            showAlert("Paiement Confirmé", "Vous avez choisi: " + selectedPaymentMethod, AlertType.INFORMATION);
        } else {
            showAlert("Erreur", "Veuillez sélectionner un mode de paiement", AlertType.ERROR);
        }
    }

    // Handle payment cancellation
    public void annulerPaiement() {
        showAlert("Annulation", "Paiement annulé", AlertType.INFORMATION);
    }

    // Method to show alerts
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
