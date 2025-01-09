package entités;

import javafx.beans.property.*;

import java.time.LocalDateTime;

public class Reclamation {

    private final IntegerProperty idReclamation;
    private final StringProperty email;
    private final IntegerProperty idCommande;
    private final StringProperty idProduit;
    private final StringProperty description;
    private final StringProperty photoPath;
    private final ObjectProperty<LocalDateTime> dateReclamation;

    // Constructor
    public Reclamation(int idReclamation, String email, int idCommande, String idProduit, String description, String photoPath, LocalDateTime dateReclamation) {
        this.idReclamation = new SimpleIntegerProperty(idReclamation);
        this.email = new SimpleStringProperty(email);
        this.idCommande = new SimpleIntegerProperty(idCommande);
        this.idProduit = new SimpleStringProperty(idProduit);
        this.description = new SimpleStringProperty(description);
        this.photoPath = new SimpleStringProperty(photoPath);
        this.dateReclamation = new SimpleObjectProperty<>(dateReclamation);
    }

    // Getters and Property Methods
    public int getIdReclamation() {
        return idReclamation.get();
    }

    public IntegerProperty idReclamationProperty() {
        return idReclamation;
    }

    public void setIdReclamation(int idReclamation) {
        this.idReclamation.set(idReclamation);
    }

    public String getEmail() {
        return email.get();
    }

    public StringProperty emailProperty() {
        return email;
    }

    public void setEmail(String email) {
        this.email.set(email);
    }

    public int getIdCommande() {
        return idCommande.get();
    }

    public IntegerProperty idCommandeProperty() {
        return idCommande;
    }

    public void setIdCommande(int idCommande) {
        this.idCommande.set(idCommande);
    }

    public String getIdProduit() {
        return idProduit.get();
    }

    public StringProperty idProduitProperty() {
        return idProduit;
    }

    public void setIdProduit(String idProduit) {
        this.idProduit.set(idProduit);
    }

    public String getDescription() {
        return description.get();
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public String getPhotoPath() {
        return photoPath.get();
    }

    public StringProperty photoPathProperty() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath.set(photoPath);
    }

    public LocalDateTime getDateReclamation() {
        return dateReclamation.get();
    }

    public ObjectProperty<LocalDateTime> dateReclamationProperty() {
        return dateReclamation;
    }

    public void setDateReclamation(LocalDateTime dateReclamation) {
        this.dateReclamation.set(dateReclamation);
    }
}
