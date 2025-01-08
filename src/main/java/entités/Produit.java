package entités;

import javafx.beans.property.*;

public class Produit {
    private StringProperty reference;
    private StringProperty nom;
    private StringProperty description;
    private DoubleProperty prix;
    private IntegerProperty stock;
    private StringProperty categorie;
    private StringProperty sousCategorie;
    // Constructor for Produit class
    public Produit(String reference, String nom, String description, double prix, int stock,
                   String categorie, String sousCategorie) {
        this.reference = new SimpleStringProperty(reference);
        this.nom = new SimpleStringProperty(nom);
        this.description = new SimpleStringProperty(description);
        this.prix = new SimpleDoubleProperty(prix);
        this.stock = new SimpleIntegerProperty(stock);
        this.categorie = new SimpleStringProperty(categorie);
        this.sousCategorie = new SimpleStringProperty(sousCategorie);
    }

    // Getters and setters for Produit class fields
    public StringProperty referenceProperty() {
        return reference;
    }

    public String getReference() {
        return reference.get();
    }

    public void setReference(String reference) {
        this.reference.set(reference);
    }

    public StringProperty nomProperty() {
        return nom;
    }

    public String getNom() {
        return nom.get();
    }

    public void setNom(String nom) {
        this.nom.set(nom);
    }

    public StringProperty descriptionProperty() {
        return description;
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
    }

    public DoubleProperty prixProperty() {
        return prix;
    }

    public double getPrix() {
        return prix.get();
    }

    public void setPrix(double prix) {
        this.prix.set(prix);
    }

    public IntegerProperty stockProperty() {
        return stock;
    }

    public int getStock() {
        return stock.get();
    }

    public void setStock(int stock) {
        this.stock.set(stock);
    }

    public StringProperty categorieProperty() {
        return categorie;
    }

    public String getCategorie() {
        return categorie.get();
    }

    public void setCategorie(String categorie) {
        this.categorie.set(categorie);
    }

    public StringProperty sousCategorieProperty() {
        return sousCategorie;
    }

    public String getSousCategorie() {
        return sousCategorie.get();
    }

    public void setSousCategorie(String sousCategorie) {
        this.sousCategorie.set(sousCategorie);
    }
}
